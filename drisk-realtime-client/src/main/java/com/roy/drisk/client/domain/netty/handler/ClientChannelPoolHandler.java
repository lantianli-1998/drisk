package com.roy.drisk.client.domain.netty.handler;

import com.roy.drisk.client.domain.netty.client.NettyClientChannelInitializer;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolHandler;

/**
 * @author roy
 * @date 2021/10/26
 * @desc
 */
public class ClientChannelPoolHandler implements ChannelPoolHandler {
    private NettyClientChannelInitializer channelInitializer;

    public ClientChannelPoolHandler(NettyClientChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    @Override
    public void channelReleased(Channel ch) throws Exception {
    }

    @Override
    public void channelAcquired(Channel ch) throws Exception {
    }

    @Override
    public void channelCreated(Channel ch) throws Exception {
        channelInitializer.initChannel(ch);
    }
}
