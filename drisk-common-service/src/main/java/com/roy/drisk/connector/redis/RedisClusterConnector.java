package com.roy.drisk.connector.redis;

import com.roy.drisk.connector.config.ConnectorProperties;
import com.roy.drisk.connector.redis.batch.RedisClusterBatchClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.util.JedisClusterCRC16;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc 集群模式Redis连接器，用于创建及管理Redis集群客户端。
 */
public class RedisClusterConnector implements BaseRedisConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClusterConnector.class);
    private Properties properties;
    private JedisCluster jedisCluster;
    private RedisClusterClient client;
    private RedisClusterBatchClient batchClient;
    private TreeMap<Long, String> slotHostMap;
    private AtomicBoolean closed = new AtomicBoolean(false);

    public RedisClusterConnector(Properties properties) {
        this.properties = ConnectorProperties.filterProperties(properties, RedisConstants.PROP_KEY);
        LOGGER.info("Create RedisClusterConnector with {}", this.properties);
        this.jedisCluster = initCluster();
        this.slotHostMap = initSlotHostMap();
        this.client = JedisClientProxy.clusterProxy(new RedisClusterClient(this.jedisCluster),
                Integer.parseInt(properties.getProperty(RedisConstants.SLOWLOGMILLIS)), this.jedisCluster);
        this.batchClient = JedisClientProxy.clusterProxy(new RedisClusterBatchClient(this.jedisCluster),
                Integer.parseInt(properties.getProperty(RedisConstants.SLOWLOGMILLIS)), this.jedisCluster);
    }
    
    public Jedis getJedisByKey(String key){
    	int slot = JedisClusterCRC16.getSlot(key);
    	Map.Entry<Long, String> entry = slotHostMap.lowerEntry(Long.valueOf(slot));
    	return jedisCluster.getClusterNodes().get(entry.getValue()).getResource();
    }
    
    private TreeMap<Long, String> initSlotHostMap() {
    	Map<String, JedisPool> nodeMap = this.jedisCluster.getClusterNodes();
        String anyHostAndPortStr = nodeMap.keySet().iterator().next();
    	
        TreeMap<Long, String> tree = new TreeMap<Long, String>();
        String parts[] = anyHostAndPortStr.split(":");
        HostAndPort anyHostAndPort = new HostAndPort(parts[0], Integer.parseInt(parts[1]));
        try{
            Jedis jedis = new Jedis(anyHostAndPort.getHost(), anyHostAndPort.getPort());
            List<Object> list = jedis.clusterSlots();
            for (Object object : list) {
                List<Object> line = (List<Object>) object;
                List<Object> master = (List<Object>) line.get(2);
                String hostAndPort = new String((byte[]) master.get(0)) + ":" + master.get(1);
                tree.put((Long) line.get(0), hostAndPort);
                tree.put((Long) line.get(1), hostAndPort);
            }
            jedis.close();
        }catch(Exception e){
            
        }
        return tree;
    }

    private JedisCluster initCluster() {
        return new JedisCluster(HostAndPortUtil.parseHostsAndPorts(properties.getProperty(RedisConstants.HOST_PORT)),
                Integer.parseInt(properties.getProperty(RedisConstants.TIMEOUT)),
                Integer.parseInt(properties.getProperty(RedisConstants.TIMEOUT)),
                Integer.parseInt(properties.getProperty(RedisConstants.MAX_REDIRECTS)),
                properties.getProperty(RedisConstants.PASSWORD),
                JedisPoolConfigUtil.newPoolConfig(properties));
    }

    @Override
    public RedisClusterClient getClient() {
        return client;
    }
    
    @Override
    public RedisClusterBatchClient getBatchClient() {
        return batchClient;
    }

	public Map<Long, String> getSlotHostMap() {
		return slotHostMap;
	}
	
    @Override
    public boolean isClosed() {
        return closed.get();
    }

    @Override
    public void close() throws IOException {
        if (closed.compareAndSet(false, true)) {
            LOGGER.info("RedisClusterConnector is closing...");
            jedisCluster.close();
            LOGGER.info("RedisClusterConnector closed.");
        }
    }
}
