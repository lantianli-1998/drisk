package com.roy.drisk.client;


import com.roy.drisk.client.application.DriskNettyClient;
import com.roy.drisk.client.contract.ClientSettings;
import com.roy.drisk.client.domain.netty.client.PooledNettyTCPClient;
import com.roy.drisk.client.domain.netty.client.SimpleNettyTCPClient;
import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author roy
 * @date 2021/10/31
 * @desc
 */
public class TCPRunner implements Runner {
	private RunParam param;
	private DriskNettyClient tcpClient;

	public TCPRunner(RunParam param, Properties properties) throws IOException {
		this.param = param;
		ClientSettings settings = Settings.create(properties);
		if (param.getType() == RunParam.Type.POOL) {
			tcpClient = new PooledNettyTCPClient(settings);
		} else if (param.getType() == RunParam.Type.SIMPLE) {
			tcpClient = new SimpleNettyTCPClient(settings);
		} else {
			tcpClient = new PooledNettyTCPClient(settings);
		}
	}

	@Override
	public void run() throws Throwable {
		if (param.getTcpMode() == RunParam.TCPMode.ONCE) {
			runOnce();
		} else if (param.getTcpMode() == RunParam.TCPMode.TIME) {
			runInTimeLimits();
		}
	}

	private void runOnce() throws Throwable {
		RequestMessage reqMsg = TCPMessage.newMessage(param.getMessage());
		long start = System.currentTimeMillis();
		ResponseMessage rspMsg = tcpClient.send(reqMsg);
		assert reqMsg.getRequestId().equals(rspMsg.getRequestId());
		System.out.println("Duration: " + (System.currentTimeMillis() - start));
	}

	private void runInTimeLimits() throws Throwable {
		final int threads = param.getThreadsNum();
		final int millis = param.getSeconds() * 1000;
		final AtomicLong succCounts = new AtomicLong();
		final AtomicLong totalDuration = new AtomicLong();
		final AtomicLong failCounts = new AtomicLong();

		final ThreadPoolExecutor workers = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
		final long start = System.currentTimeMillis();
		for (int i = 0; i < threads; i++) {
			try {
				workers.execute(new Runnable() {
					@Override
					public void run() {
						while (millis >= (System.currentTimeMillis() - start)) {
							try {
								RequestMessage reqMsg = TCPMessage.newMessage(param.getMessage());
								ResponseMessage rspMsg = tcpClient.send(reqMsg);
								assert rspMsg != null;
								assert rspMsg.getRequestId().equals(reqMsg.getRequestId());

								totalDuration.addAndGet(rspMsg.getDuration());
								succCounts.incrementAndGet();
							} catch (Throwable throwable) {
								if (!(throwable instanceof InterruptedException)) {
									failCounts.incrementAndGet();
								}
							}
						}
					}
				});
			} catch (RejectedExecutionException ignored) {
			}
		}
		workers.shutdown();
		while (!workers.awaitTermination(millis, TimeUnit.MILLISECONDS)) {
			System.out.println("Workers is shutting down...");
		}
		workers.shutdownNow();
		long counts = succCounts.get();
		long tps = 0;
		long duration = 0;
		if (counts > 0) {
			tps = counts * 1000L / millis;
			duration = totalDuration.get() / counts;
		}
		System.out.println("Count: " + counts + " TotalTime: " + millis + " TPS: " + tps + " AvgDuration: " + duration
				+ " Fail: " + failCounts.get());
	}

	@Override
	public void close() throws Exception {
		if (tcpClient != null) {
			tcpClient.close();
		}
	}
}
