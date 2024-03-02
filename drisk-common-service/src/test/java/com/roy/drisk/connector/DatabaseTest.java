package com.roy.drisk.connector;

import com.roy.drisk.connector.database.DatabaseSession;
import com.roy.drisk.connector.database.DatabaseUtil;
import com.roy.drisk.connector.service.DriskConnectorFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class DatabaseTest {
    @Test
    public void testQuery() throws Exception {
        DatabaseSession session = DriskConnectorFactory.getDatabaseSession();
        HashMap res = session.selectOne("com.roy.drisk.mybatis.mappers.BlackInfoMapper");
        session.close();
        Assert.assertNotNull(res);
        System.out.println(res);
    }

    @Test
    public void testUtilQuery() throws Exception {
        DatabaseUtil util = new DatabaseUtil(DriskConnectorFactory.getDatabaseConnector());
        List<Map<String, Object>> res = util.queryForList("SELECT * FROM oms_order limit 10");
        Assert.assertNotNull(res);
        System.out.println(res);
    }

    @After
    public void after() {
        DriskConnectorFactory.close();
    }
}
