package com.roy.drisk.server.netty.server;

import com.roy.drisk.server.netty.config.MessageSettings;
import com.roy.drisk.server.netty.config.TransportSettings;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * @author roy
 * @date 2021/10/27
 * @desc 启动Netty的TCP端口监听
 */
@Component
public class NettyTCPServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyTCPServer.class);
    private TransportSettings transportSettings;
    private MessageSettings messageSettings;
    private ServerBootstrap bootstrap;
    @Autowired
    private NettyServerChannelInitializer channelInitializer;
    @Autowired
    private EventExecutorGroup engineHandlerExecutor;

    @Autowired
    public NettyTCPServer(TransportSettings transportSettings, MessageSettings messageSettings) {
        this.transportSettings = transportSettings;
        this.messageSettings = messageSettings;
    }

    private void createServerBootstrap() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(transportSettings.getBossThreads());
        EventLoopGroup workerGroup = new NioEventLoopGroup(transportSettings.getWorkerThreads());
        Class<? extends ServerChannel> channelClazz = NioServerSocketChannel.class;
        if (transportSettings.isNativeEpoll()) {
            channelClazz = EpollServerSocketChannel.class;
        }
        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(channelClazz)
                .childHandler(channelInitializer)
                .option(ChannelOption.SO_BACKLOG, transportSettings.getSoBacklog())
                .option(ChannelOption.SO_REUSEADDR, transportSettings.isSoReuseAddress())
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, transportSettings.isTcpNoDelay())
                .childOption(ChannelOption.SO_KEEPALIVE, transportSettings.isSoKeepAlive())
                .childOption(ChannelOption.SO_SNDBUF, transportSettings.getSoSendBufferSize())
                .childOption(ChannelOption.SO_RCVBUF, transportSettings.getSoReceiveBufferSize())
                .childOption(ChannelOption.SO_REUSEADDR, transportSettings.isSoReuseAddress());
    }

    public void start() throws Throwable {
        LOGGER.info("Initiating NettyTCPServer with {}", transportSettings);
        LOGGER.info("Initiating NettyTCPServer channel with {}", messageSettings);
        createServerBootstrap();
        InetSocketAddress address =
                new InetSocketAddress(transportSettings.getBindAddress(), transportSettings.getPort());
        LOGGER.info("Starting NettyTCPServer at {}...", address);
        ChannelFuture future = bootstrap.bind(address).sync();
        LOGGER.info("NettyTCPServer started.");
        future.channel().closeFuture().sync();
        LOGGER.info("NettyTCPServer channel closed.");
    }

    @PreDestroy
    public void stop() {
        LOGGER.info("Stopping NettyTCPServer...");
        engineHandlerExecutor.shutdownGracefully();
        if (bootstrap != null) {
            bootstrap.childGroup().shutdownGracefully();
            bootstrap.group().shutdownGracefully();
        }
        LOGGER.info("NettyTCPServer stopped.");
    }
}
