package com.roy.drisk.connector.redis;

import com.roy.drisk.connector.redis.batch.CloseableJedisBatchCommands;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
@SuppressWarnings("unchecked")
public class JedisClientProxy {
	public static <T extends CloseableJedisCommands> T clusterProxy(final T client, final int slowlogMillis,
			final JedisCluster jedisCluster) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(client.getClass());
		enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
			Logger logger = LoggerFactory.getLogger(obj.getClass());
			long start = System.currentTimeMillis();
			Object res = null;
			try {
				res = proxy.invokeSuper(obj, args);
			} finally {
				long elapsed = System.currentTimeMillis() - start;
				if (elapsed > slowlogMillis) {
					logger.info("[REDIS-SLOW]Method: {}, args: {}, res: {}, elapsed: {}", method.getName(), args, res,
							elapsed);
				}
			}
			return res;
		});
		return (T) enhancer.create(new Class[] { JedisCluster.class }, new Object[] { jedisCluster });
	}

	public static <T extends CloseableJedisBatchCommands> T clusterProxy(final T client, final int slowlogMillis,
																		 final JedisCluster jedisCluster) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(client.getClass());
		enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
			Logger logger = LoggerFactory.getLogger(obj.getClass());
			long start = System.currentTimeMillis();
			Object res = null;
			try {
				res = proxy.invokeSuper(obj, args);
			} finally {
				long elapsed = System.currentTimeMillis() - start;
				if (elapsed > slowlogMillis) {
					logger.info("[REDIS-BATCH-SLOW]Method: {}, args: {}, res: {}, elapsed: {}", method.getName(), args, res,
							elapsed);
				}
			}
			return res;
		});
		return (T) enhancer.create(new Class[] { JedisCluster.class }, new Object[] { jedisCluster });
	}
}
