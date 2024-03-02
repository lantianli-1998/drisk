package com.roy.drisk.connector.redis.batch;

import redis.clients.jedis.PipelineBase;

import java.io.Closeable;
import java.util.List;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc Redis客户端接口
 */
public abstract class CloseableJedisBatchCommands extends PipelineBase implements Closeable{
	public abstract void sync();
	public abstract List<Object> syncAndReturnAll(); 
}
