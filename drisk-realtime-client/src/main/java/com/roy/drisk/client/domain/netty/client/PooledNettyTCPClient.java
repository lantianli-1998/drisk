package com.roy.drisk.client.domain.netty.client;

import com.roy.drisk.client.contract.ClientSettings;
import com.roy.drisk.client.domain.netty.handler.ClientChannelPoolHandler;
import com.roy.drisk.client.domain.netty.handler.MessageSender;
import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;
import io.netty.channel.Channel;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelHealthChecker;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc Netty连接池客户端
 */
public class PooledNettyTCPClient extends AbstractNettyTCPClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(PooledNettyTCPClient.class);
    private ChannelPoolMap<InetSocketAddress, FixedChannelPool> poolMap;

    public PooledNettyTCPClient(final ClientSettings settings) {
        super(settings);
        createChannelPoolMap();
    }

    private void createChannelPoolMap() {
        this.poolMap = new AbstractChannelPoolMap<InetSocketAddress, FixedChannelPool>() {
            @Override
            protected FixedChannelPool newPool(InetSocketAddress key) {
                return new FixedChannelPool(getBootstrap().remoteAddress(key),
                        new ClientChannelPoolHandler(getChannelInitializer()),
                        ChannelHealthChecker.ACTIVE, FixedChannelPool.AcquireTimeoutAction.FAIL,
                        getSettings().getTransportPoolAcquireTimeoutMillis(),
                        getSettings().getTransportPoolMaxConnections(),
                        getSettings().getTransportPoolMaxPendingAcquires());
            }
        };
    }

    @Override
    public ResponseMessage doSend(final InetSocketAddress address, final RequestMessage message,
                                  final int waitMilliSeconds)
            throws Throwable {
        FixedChannelPool pool = poolMap.get(address);
        Future<Channel> channelFuture = pool.acquire().sync();
        if (channelFuture.isSuccess()) {
            Channel ch = channelFuture.getNow();
            try {
                MessageSender sender = getMessageSender(ch);
                return sender.sendMessage(message, waitMilliSeconds);
            } finally {
                pool.release(ch);
            }
        } else {
            LOGGER.error("Connect server " + address + " error ", channelFuture.cause());
            throw channelFuture.cause();
        }
    }

    @Override
    public void close() {
        LOGGER.debug("Closing ChannelPoolMap...");
        for (InetSocketAddress address : getSettings().getTransportServers()) {
            if (poolMap.contains(address)) {
                poolMap.get(address).close();
            }
        }
        super.close();
    }
}
