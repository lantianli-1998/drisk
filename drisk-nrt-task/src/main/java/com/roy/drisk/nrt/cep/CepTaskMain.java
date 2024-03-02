package com.roy.drisk.nrt.cep;

import com.roy.drisk.services.GeneralServiceFactory;
import com.roy.drisk.nrt.core.TxLog;
import com.roy.drisk.nrt.flow.TxLogUtil;
import com.roy.drisk.nrt.repo.ConfigRepo;
import com.roy.drisk.services.login.MblLoginBlack;
import com.roy.drisk.services.login.FailedLoginMblService;
import com.roy.drisk.services.ParamService;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.cep.nfa.aftermatch.AfterMatchSkipStrategy;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import org.codehaus.jackson.map.ObjectMapper;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author lantianli
 * @date 2021/11/7
 * @desc 复杂事件任务入口 实现LG002规则
 * 整个CEP处理流程，实现了这个逻辑后，就可以很容易封装出一个扩展机制。
 * 具体方法：只需要由一个抽象类提供处理过程中的三个方法，
 *  1、提供Pattern定义匹配模式，
 *  2、提供PatternProcessFunction抽取数据，
 *  3、提供ProcessFunction处理结果
 * 接下来就可以由这个抽象类扩展出一系列的实现类来扩展这个流程。一个for循环就搞定了。
 * 扩展的方法跟FlowTask差不多。这里演示起见，就不把这一块功能做得太重了。
 */
public class CepTaskMain {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setAutoWatermarkInterval(1000L);
        //1、从实时风控的处理结果的topic中获取Kafka消息。
        final String kafkaBootStrapServers = ConfigRepo.getKafkaBootStrapServer();
        final String flowJobTopic = ConfigRepo.getFlowJobTopic();

        final Properties jobProp = new Properties();
        jobProp.setProperty("bootstrap.servers", kafkaBootStrapServers);
        jobProp.setProperty("group.id", ConfigRepo.getStreamJobGroupId());
        final FlinkKafkaConsumer<String> rtSource = new FlinkKafkaConsumer<>(flowJobTopic, new SimpleStringSchema(), jobProp);
        final DataStreamSource<String> messageStream = env.addSource(rtSource);
        //测试用。
//        final DataStreamSource<String> messageStream = env.socketTextStream("hadoop01", 7777);

        //2、将消息转换成为TxLog流。
        final SingleOutputStreamOperator<TxLog> txLogStream = messageStream.map((MapFunction<String, TxLog>) message -> {
            final ObjectMapper objectMapper = new ObjectMapper();
            final TxLog txLog = objectMapper.readValue(message, TxLog.class);
            return txLog;
        }).assignTimestampsAndWatermarks(
                WatermarkStrategy.<TxLog>forBoundedOutOfOrderness(Duration.ofSeconds(1))
                .withTimestampAssigner(
                        (SerializableTimestampAssigner<TxLog>) (element, recordTimestamp) ->
                            Long.parseLong(element.CHECK_TIME)
        ));
        txLogStream.print("txLogStream");
        //3、按手机号进行分组
        final KeyedStream<TxLog, String> keyedTxLogStream = txLogStream.keyBy(new KeySelector<TxLog, String>() {
            @Override
            public String getKey(TxLog value) throws Exception {
                return value.MBL_NO;
            }
        });

        final ParamService paramService = GeneralServiceFactory.getService(ParamService.class);
        final int lg002Param1 = paramService.getLG002Param1();//1
        final int lg002Param2 = paramService.getLG002Param2();//5
        final int lg002Param3 = paramService.getLG002Param3();//3
        //4、使用CEP匹配符合条件的用户
        CEP.pattern(keyedTxLogStream,
                Pattern.<TxLog>begin("start", AfterMatchSkipStrategy.noSkip())
                        .where(new SimpleCondition<TxLog>() {
                            @Override
                            public boolean filter(TxLog value) throws Exception {
                                return value.TX_TYP.equals("LG") && value.TX_RST.equals("1");
                            }
                        }).times(lg002Param2).within(Time.days(lg002Param1))
        ).process(new PatternProcessFunction<TxLog, TxLog>() {
            //5、处理匹配到的结果。
            @Override
            public void processMatch(Map<String, List<TxLog>> match, Context ctx, Collector<TxLog> out) throws Exception {
                //这里会拿到连续的 lg002Param2 条记录集合。
                final List<TxLog> records = match.get("start");
                //后续统计信息只需要连续 lg002Param2 条记录中的最后一条。
                //MBL_NO是分组键，这一组记录的MBL_NO肯定都是一样的，取哪条都行。而TX_DT和TX_TM取最后一条。
                if(!records.isEmpty()) {
                    out.collect(records.get(records.size()-1));
                }
            }
        }).process(new ProcessFunction<TxLog, String>() {
            @Override
            public void processElement(TxLog value, Context ctx, Collector<String> out) throws Exception {
                //查看匹配到的结果。 调试用。
//                System.out.println("matched LG002 record: "+value);
                final FailedLoginMblService failedLoginMblService = GeneralServiceFactory.getService(FailedLoginMblService.class);
                failedLoginMblService.addLoginFailedMbl(new MblLoginBlack(value.MBL_NO,value.TX_DT,value.TX_TM,lg002Param3));
            }
        });
        env.execute("DriskCepTask");
    }
}
