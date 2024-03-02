package com.roy.drisk.connector.redis;

import com.roy.drisk.connector.config.ConnectorProperties;
import com.roy.drisk.connector.redis.batch.CloseableJedisBatchCommands;
import com.roy.drisk.connector.redis.batch.RedisStandaloneBatchClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPool;

import java.util.Properties;
import java.util.Set;

/**
 * @author roy
 * @date 2021/10/27
 * @desc 单机模式Redis连接器，用于创建及管理Redis连接池
 */
public class RedisStandaloneConnector implements BaseRedisConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisStandaloneConnector.class);
    private Properties properties;
    private JedisPool jedisPool;

    public RedisStandaloneConnector(Properties properties) {
        this.properties = ConnectorProperties.filterProperties(properties, RedisConstants.PROP_KEY);
        LOGGER.info("Create RedisStandaloneConnector with {}", this.properties);
        this.jedisPool = initJedisPool();
    }

    private JedisPool initJedisPool() {
        Set<HostAndPort> hostsAndPorts =
                HostAndPortUtil.parseHostsAndPorts(properties.getProperty(RedisConstants.HOST_PORT));
        HostAndPort hostAndPort = hostsAndPorts.iterator().next();
        return new JedisPool(JedisPoolConfigUtil.newPoolConfig(properties),
                hostAndPort.getHost(), hostAndPort.getPort(),
                Integer.parseInt(properties.getProperty(RedisConstants.TIMEOUT)),
                properties.getProperty(RedisConstants.PASSWORD),
                Integer.parseInt(properties.getProperty(RedisConstants.DATABASE)));
    }

    @Override
    public RedisStandaloneClient getClient() {
        return new RedisStandaloneClient(jedisPool.getResource());
    }

	@Override
	public CloseableJedisBatchCommands getBatchClient() {
		return new RedisStandaloneBatchClient(jedisPool.getResource());
	}
	
    @Override
    public boolean isClosed() {
        return jedisPool.isClosed();
    }

    @Override
    public void close() {
        LOGGER.info("RedisStandaloneConnector is closing...");
        jedisPool.close();
        LOGGER.info("RedisStandaloneConnector closed.");
    }
}
