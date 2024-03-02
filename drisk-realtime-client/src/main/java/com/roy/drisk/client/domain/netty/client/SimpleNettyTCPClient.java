package com.roy.drisk.client.domain.netty.client;

import com.roy.drisk.client.contract.ClientSettings;
import com.roy.drisk.client.domain.netty.handler.MessageSender;
import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc 短连接客户端
 */
public class SimpleNettyTCPClient extends AbstractNettyTCPClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNettyTCPClient.class);

    public SimpleNettyTCPClient(final ClientSettings settings) {
        super(settings);
        appendChannelInitializer();
    }

    private void appendChannelInitializer() {
        getBootstrap().handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                getChannelInitializer().initChannel(ch);
            }
        });
    }

    @Override
    public ResponseMessage doSend(final InetSocketAddress address, final RequestMessage message,
                                  final int waitMilliSeconds)
            throws Throwable {
        ChannelFuture channelFuture = getBootstrap().connect(address).sync();
        if (channelFuture.isSuccess()) {
            try {
                MessageSender sender = getMessageSender(channelFuture.channel());
                return sender.sendMessage(message, waitMilliSeconds);
            } finally {
                channelFuture.channel().close().sync();
            }
        } else {
            LOGGER.error("Connect server " + address + " error ", channelFuture.cause());
            throw channelFuture.cause();
        }
    }
}
