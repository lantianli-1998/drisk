package connector;

import com.roy.drisk.connector.hbase.HBaseUtil;
import com.roy.drisk.connector.hbase.StringResultsExtractor;
import com.roy.drisk.connector.hbase.StringRowMapper;
import com.roy.drisk.connector.service.DriskConnectorFactory;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Scan;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public class HBaseTest {

    @Before
    public void before(){
        System.setProperty("drisk.env","sit");
    }

    @Test
    public void testHBaseConnection() throws IOException {
        TableName[] names = DriskConnectorFactory.getHBaseConnection().getAdmin().listTableNames();
        for (TableName name : names) {
            System.out.println(name.toString());
        }
    }

    @Test
    public void testHBaseGet() {
        putTestData();
        HBaseUtil util = new HBaseUtil(DriskConnectorFactory.getHBaseConnection());
        Map<String, Map<String, String>> res =
                util.get("PUNISHMENTJNL", "000000000001_839387965875808", new StringRowMapper());
        Assert.assertEquals("01|02|04", res.get("P").get("AllowTransType"));
    }

    @Test
    public void testHBaseFind() {
        putTestData();
        HBaseUtil util = new HBaseUtil(DriskConnectorFactory.getHBaseConnection());
        Scan scan = new Scan();
        scan.addColumn("P".getBytes(), "AllowTransType".getBytes());
        scan.setStartRow("000000000001_839387965875808".getBytes());
        scan.setStopRow("000000000001_839387965875808".getBytes());
        Map<String, Map<String, Map<String, String>>> res =
                util.find("PUNISHMENTJNL", scan, new StringResultsExtractor());
        Assert.assertEquals("01|02|04", res.get("000000000001_839387965875808").get("P").get("AllowTransType"));
    }

    @Test
    public void testHBasePut() {
        putTestData();
        testHBaseGet();
    }

    private void putTestData() {
        HBaseUtil util = new HBaseUtil(DriskConnectorFactory.getHBaseConnection());
        Map<String, Map<String, String>> families = new HashMap<>();
        Map<String, String> qualifier = new HashMap<>();
        qualifier.put("AllowTransType", "01|02|04");
        families.put("P", qualifier);
        util.put("PUNISHMENTJNL", "000000000001_839387965875808", families);
    }

    @After
    public void after() {
        DriskConnectorFactory.close();
    }
}
