package com.roy.drisk.nrt.stream;

import com.roy.drisk.services.GeneralServiceFactory;
import com.roy.drisk.nrt.core.TxLog;
import com.roy.drisk.nrt.repo.ConfigRepo;
import com.roy.drisk.nrt.stream.function.AddDay;
import com.roy.drisk.services.blackMbl.DayLimitBlack;
import com.roy.drisk.services.blackMbl.BlackMblService;
import com.roy.drisk.services.ParamService;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Properties;

/**
 * @author lantianli
 * @date 2021/11/7
 * @desc 累计型规则计算示例
 */
public class StreamTaskMain {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //1、获取flowjob发送到kafka的消息
        final String kafkaBootStrapServers = ConfigRepo.getKafkaBootStrapServer();
        final String flowJobTopic = ConfigRepo.getFlowJobTopic();

        final Properties jobProp = new Properties();
        jobProp.setProperty("bootstrap.servers", kafkaBootStrapServers);
        jobProp.setProperty("group.id", ConfigRepo.getStreamJobGroupId());
        final FlinkKafkaConsumer<String> flowjobresource = new FlinkKafkaConsumer<>(flowJobTopic, new SimpleStringSchema(), jobProp);
        final DataStreamSource<String> messages = env.addSource(flowjobresource);

        //2、将消息转换成为TxLog流
        final SingleOutputStreamOperator<TxLog> txLogStream = messages.map((MapFunction<String, TxLog>) value -> {
            final ObjectMapper objectMapper = new ObjectMapper();
            final TxLog txLog = objectMapper.readValue(value, TxLog.class);
            return txLog;
        });
        txLogStream.print("txLogStream");
        final EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner().build();
        final StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, environmentSettings);
        tableEnv.createTemporaryFunction("add_day", new AddDay());

        tableEnv.createTemporaryView("txLog", txLogStream);

        final ParamService paramService = GeneralServiceFactory.getService(ParamService.class);
//          SQL测试
//        String testSql = "select MBL_NO as mblno,sum(cast(TX_AMT as Double)) as amt,count(REQUEST_ID) as cnt from txLog" +
//                " where TX_DT > add_day(TX_DT,'yyyyMMdd'," + paramService.getLJ002Param1() + ") " +
//                " GROUP BY MBL_NO ";
//        final Table testTable = tableEnv.sqlQuery(testSql);
//        final DataStream<Tuple2<Boolean, Tuple3<String,Double,Long>>> testDS =
//                tableEnv.toRetractStream(testTable, TypeInformation.of(new TypeHint<Tuple3<String,Double,Long>>() {}));
//        testDS.print("testDS");

        String sql = "select MBL_NO as mblno,sum(cast(TX_AMT as Double)) as amt,count(REQUEST_ID) as cnt from txLog" +
                " where TX_DT > add_day(TX_DT,'yyyyMMdd'," + paramService.getLJ002Param1() + ") " +
                " GROUP BY MBL_NO " +
                " HAVING sum(cast(TX_AMT as Double)) < " + paramService.getLJ002Param3() + " AND count(REQUEST_ID) > " + paramService.getLJ002Param2();

        final Table queryTable = tableEnv.sqlQuery(sql);
        final DataStream<Tuple2<Boolean, Tuple3<String, Double, Long>>> tuple2DataStream = tableEnv.toRetractStream(queryTable, TypeInformation.of(new TypeHint<Tuple3<String, Double, Long>>() {
        }));
        final int lj002Param4 = paramService.getLJ002Param4();
        tuple2DataStream.print("tuple2DataStream");
        tuple2DataStream.addSink(new SinkFunction<Tuple2<Boolean, Tuple3<String, Double, Long>>>() {
            @Override
            public void invoke(Tuple2<Boolean, Tuple3<String, Double, Long>> value, Context context) throws Exception {
                String mblNo = value.f1.f0;
                Double amt = value.f1.f1;
                Long cnt = value.f1.f2;
                final DayLimitBlack dayLimitBlack = new DayLimitBlack(mblNo, amt, cnt, lj002Param4);
                final BlackMblService blackMblService = GeneralServiceFactory.getService(BlackMblService.class);
                blackMblService.addMblDayLimitBlack(dayLimitBlack);
            }
        });

        env.execute("DriskStreamTask");
    }
}
