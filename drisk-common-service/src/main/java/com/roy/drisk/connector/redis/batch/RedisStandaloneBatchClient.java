package com.roy.drisk.connector.redis.batch;

import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public class RedisStandaloneBatchClient extends CloseableJedisBatchCommands {
	private Jedis jedis;

	public RedisStandaloneBatchClient(Jedis jedis) {
		this.jedis = jedis;
	}

	@Override
	public void close() throws IOException {
		jedis.close();
	}

	@Override
	protected Client getClient(String key) {
		return jedis.getClient();
	}

	@Override
	protected Client getClient(byte[] key) {
		return jedis.getClient();
	}
	//TODO 待维护
	public void sync() {
//		if (getPipelinedResponseLength() > 0) {
//			List<Object> unformatted = jedis.getClient().getAll();
//			for (Object o : unformatted) {
//				generateResponse(o);
//			}
//		}
	}
	//TODO 待维护
	public List<Object> syncAndReturnAll() {
//		if (getPipelinedResponseLength() > 0) {
//			List<Object> unformatted = jedis.getClient().getAll();
//			List<Object> formatted = new ArrayList<Object>();
//
//			for (Object o : unformatted) {
//				try {
//					formatted.add(generateResponse(o).get());
//				} catch (JedisDataException e) {
//					formatted.add(e);
//				}
//			}
//			return formatted;
//		} else {
//			return java.util.Collections.<Object> emptyList();
//		}
		return null;
	}
}
