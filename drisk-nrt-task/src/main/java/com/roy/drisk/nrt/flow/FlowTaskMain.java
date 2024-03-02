package com.roy.drisk.nrt.flow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roy.drisk.nrt.core.TxLog;
import com.roy.drisk.nrt.core.flow.services.FlowJobService;
import com.roy.drisk.nrt.core.flow.services.TxLogService;
import com.roy.drisk.nrt.repo.ConfigRepo;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/** 流式计算引擎
 * @author roy
 * @date 2021/11/7
 * @desc 流式累计计算任务入口 -Ddrisk.env=sit
 *  将消息转换成统一的Txlog对象，进行累计后将TxLog转发到kafka，后续可以针对TxLog流进行SQL查询以及CEP处理。
 */
public class FlowTaskMain {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //失败重启策略。尝试重启三次，每次延迟10秒。
//        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
//                3,
//                Time.of(10, TimeUnit.SECONDS)
//        ));

        final String kafkaBootStrapServers = ConfigRepo.getKafkaBootStrapServer();
        final String rtTopic = ConfigRepo.getRtTopic();

        Properties jobProp = new Properties();
        jobProp.setProperty("bootstrap.servers", kafkaBootStrapServers);
        jobProp.setProperty("group.id", ConfigRepo.getFlowJobGroupId());
        final FlinkKafkaConsumer<String> rtsource = new FlinkKafkaConsumer<>(rtTopic, new SimpleStringSchema(), jobProp);
        final DataStreamSource<String> messages = env.addSource(rtsource,"rtsource");

        final ObjectMapper objectMapper = new ObjectMapper();
        //1、string to txlog
        final SingleOutputStreamOperator<TxLog> txLogStream = messages.map(new MapFunction<String, TxLog>() {
            @Override
            public TxLog map(String message) throws Exception {
                final TxLog txLog = TxLogUtil.convertFromMessage(message);
                return txLog;
            }
        });
        txLogStream.print("txLogStream");
        //2、txlog engine
        OutputTag<TxLog> errorTxLog = new OutputTag<TxLog>("errorTxLog"){};
        final SingleOutputStreamOperator<TxLog> succTxLog = txLogStream.process(new ProcessFunction<TxLog, TxLog>() {
            @Override
            public void processElement(TxLog value, Context ctx, Collector<TxLog> out) throws Exception {
                if(StringUtils.isNoneBlank(value.errorMessage)){
                    ctx.output(errorTxLog,value);
                }else{
                    //累计计算
                    Boolean res = FlowJobService.process(value);
                    if(!res){
                        value.errorMessage="FlowJobEngine process failed";
                        ctx.output(errorTxLog,value);
                    }else{
                        out.collect(value);
                    }
                }
            }
        });
        succTxLog.print("succTxLog");
        //3、send to  kafka
        Properties jobSinkProp = new Properties();
        jobSinkProp.setProperty("bootstrap.servers", kafkaBootStrapServers);

        final String flowJobTopic = ConfigRepo.getFlowJobTopic();
        final FlinkKafkaProducer<TxLog> nrtKfkProducer = new FlinkKafkaProducer<>(flowJobTopic
                , (SerializationSchema<TxLog>) element -> {
            System.out.println(element);
            try {
                        return objectMapper.writeValueAsBytes(element);
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                }
                , jobSinkProp);
        //正常消息存入kafka，进行后续分析
        succTxLog.addSink(nrtKfkProducer);
        //错误消息存入HBase，做监控
        succTxLog.getSideOutput(errorTxLog).addSink(new SinkFunction<TxLog>() {
            @Override
            public void invoke(TxLog value, Context context) throws Exception {
//                final TxLogService service = GeneralServiceFactory.getService(TxLogService.class);
                TxLogService.saveTxlog2HBase(value);
            }
        });
        env.execute("DriskFlowTask");
    }

}
