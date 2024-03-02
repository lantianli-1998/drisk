package com.roy.drisk.connector.redis.batch;

import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisMovedDataException;
import redis.clients.jedis.exceptions.JedisRedirectionException;
import redis.clients.util.JedisClusterCRC16;
import redis.clients.util.SafeEncoder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc <code>JedisCluster</code>客户端代理类，重写了{@link #close()}方法，统一不同模式下客户端的接口
 */
public class RedisClusterBatchClient extends CloseableJedisBatchCommands {
	private static final Field FIELD_CONNECTION_HANDLER;
	private static final Field FIELD_CACHE;
	static {
		FIELD_CONNECTION_HANDLER = getField(BinaryJedisCluster.class, "connectionHandler");
		FIELD_CACHE = getField(JedisClusterConnectionHandler.class, "cache");
	}

	private JedisSlotBasedConnectionHandler connectionHandler;
	private JedisClusterInfoCache clusterInfoCache;
	private Queue<Client> clients = new LinkedList<Client>(); // 根据顺序存储每个命令对应的Client
	private Map<JedisPool, Jedis> jedisMap = new HashMap<>(); // 用于缓存连接
	private boolean hasDataInBuf = false; // 是否有数据在缓存区

	public RedisClusterBatchClient(JedisCluster jedis) {
		this.connectionHandler = getValue(jedis, FIELD_CONNECTION_HANDLER);
		this.clusterInfoCache = getValue(connectionHandler, FIELD_CACHE);
	}

	/**
	 * 刷新集群信息，当集群信息发生变更时调用
	 * 
	 * @param
	 * @return
	 */
	public void refreshCluster() {
		connectionHandler.renewSlotCache();
	}

	/**
	 * 同步读取所有数据. 与syncAndReturnAll()相比，sync()只是没有对数据做反序列化
	 */
	public void sync() {
		innerSync(null);
	}

	/**
	 * 同步读取所有数据 并按命令顺序返回一个列表
	 * 
	 * @return 按照命令的顺序返回所有的数据
	 */
	public List<Object> syncAndReturnAll() {
		List<Object> responseList = new ArrayList<Object>();

		innerSync(responseList);

		return responseList;
	}

	private void innerSync(List<Object> formatted) {
		HashSet<Client> clientSet = new HashSet<Client>();

		try {
			for (Client client : clients) {
				// 在sync()调用时其实是不需要解析结果数据的，但是如果不调用get方法，发生了JedisMovedDataException这样的错误应用是不知道的，因此需要调用get()来触发错误。
				// 其实如果Response的data属性可以直接获取，可以省掉解析数据的时间，然而它并没有提供对应方法，要获取data属性就得用反射，不想再反射了，所以就这样了
				Object data = generateResponse(client.getOne()).get();
				if (null != formatted) {
					formatted.add(data);
				}
				
				// size相同说明所有的client都已经添加，就不用再调用add方法了
                if (clientSet.size() != jedisMap.size()) {
                    clientSet.add(client);
                }
			}
		} catch (JedisRedirectionException jre) {
			if (jre instanceof JedisMovedDataException) {
				// if MOVED redirection occurred, rebuilds cluster's slot cache,
				// recommended by Redis cluster specification
				refreshCluster();
			}
			throw jre;
		} finally {
			if (clientSet.size() != jedisMap.size()) {
				// 所有还没有执行过的client要保证执行(flush)，防止放回连接池后后面的命令被污染
				for (Jedis jedis : jedisMap.values()) {
					if (clientSet.contains(jedis.getClient())) {
						continue;
					}

					flushCachedData(jedis);
				}
			}

			hasDataInBuf = false;
			try {
				close();
			} catch (IOException e) {
			}
		}
	}
	//TODO 待修复
	private void flushCachedData(Jedis jedis) {
		try {
//			jedis.getClient().flush
		} catch (RuntimeException ex) {
		}
	}

	@Override
	public void close() throws IOException {
		clean();
		clients.clear();

		for (Jedis jedis : jedisMap.values()) {
			if (hasDataInBuf) {
				flushCachedData(jedis);
			}

			jedis.close();
		}

		jedisMap.clear();

		hasDataInBuf = false;
	}

	@Override
	protected Client getClient(String key) {
		byte[] bKey = SafeEncoder.encode(key);
		return getClient(bKey);
	}

	@Override
	protected Client getClient(byte[] key) {
		Jedis jedis = getJedis(JedisClusterCRC16.getSlot(key));
		Client client = jedis.getClient();
		clients.add(client);
		return client;
	}

	private Jedis getJedis(int slot) {
		JedisPool pool = clusterInfoCache.getSlotPool(slot);
		// 根据pool从缓存中获取Jedis
		Jedis jedis = jedisMap.get(pool);
		if (null == jedis) {
			jedis = pool.getResource();
			jedisMap.put(pool, jedis);
		}
		hasDataInBuf = true;
		return jedis;
	}

	private static Field getField(Class<?> cls, String fieldName) {
		try {
			Field field = cls.getDeclaredField(fieldName);
			return field;
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException("cannot find or access field '" + fieldName + "' from " + cls.getName(), e);
		}
	}

	private static <T> T getValue(Object obj, Field field) {
		try {
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			AccessController.doPrivileged(new PrivilegedAction() {
	            @Override
	            public Object run() {
	                modifiersField.setAccessible(true);
	                return null;
	            }
	        });
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL); 
			field.setAccessible(true);
			return (T) field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
}
