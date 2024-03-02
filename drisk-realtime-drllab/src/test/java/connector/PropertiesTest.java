package connector;

import com.roy.drisk.connector.config.ConnectorProperties;
import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class PropertiesTest {

    @Test
    public void testUserProperties() {
        String userKey = "hbase.zookeeper.quorum";
        String userValue = "testUserProperties";
        Properties userProperties = new Properties();
        userProperties.setProperty(userKey, userValue);
        Properties properties = ConnectorProperties.newProperties(userProperties);
        Assert.assertEquals(properties.getProperty(userKey), userValue);
    }

    @Test
    public void testFilterProperties() {
        String userKey = "hbase1.zookeeper.quorum";
        String userValue = "testUserProperties";
        Properties userProperties = new Properties();
        userProperties.setProperty(userKey, userValue);
        Properties properties = ConnectorProperties.filterProperties(userProperties, "hbase1");
        Assert.assertEquals(properties.getProperty(userKey), userValue);
        Assert.assertNull(properties.getProperty("hbase"));
    }
}
