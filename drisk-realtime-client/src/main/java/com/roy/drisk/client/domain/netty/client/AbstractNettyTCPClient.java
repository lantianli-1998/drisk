package com.roy.drisk.client.domain.netty.client;

import com.roy.drisk.client.application.DriskNettyClient;
import com.roy.drisk.client.contract.ClientSettings;
import com.roy.drisk.client.contract.MessageMockType;
import com.roy.drisk.client.domain.netty.handler.HandlerConstants;
import com.roy.drisk.client.domain.netty.handler.MessageSender;
import com.roy.drisk.client.domain.netty.loadbalance.RandomServerLoadBalancer;
import com.roy.drisk.client.domain.netty.loadbalance.ServerLoadBalancer;
import com.roy.drisk.client.infrastructure.Stats;
import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;
import com.roy.drisk.message.ResultCodeConstants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc
 */
public abstract class AbstractNettyTCPClient implements DriskNettyClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNettyTCPClient.class);
    private ClientSettings settings;
    private Bootstrap bootstrap;
    private ServerLoadBalancer serverLoadBalancer;
    private NettyClientChannelInitializer channelInitializer;
    private Stats clientStats = new Stats();

    public AbstractNettyTCPClient(final ClientSettings settings) {
        LOGGER.info("Initialize NettyTCPClient with settings: {} ", settings);
        this.settings = settings;
        this.serverLoadBalancer = new RandomServerLoadBalancer(settings.getTransportServers());
        this.channelInitializer = new NettyClientChannelInitializer(settings);
        createClientBootstrap();
        LOGGER.info("NettyTCPClient initialized.");
    }

    private void createClientBootstrap() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(settings.getTransportBossThreads());
        Class<? extends Channel> channelClazz = NioSocketChannel.class;
        if (settings.isTransportNativeEpoll()) {
            channelClazz = EpollSocketChannel.class;
        }
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(bossGroup)
                .channel(channelClazz)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, settings.getTransportConnectTimeoutMillis())
                .option(ChannelOption.SO_REUSEADDR, settings.isTransportSoReuseAddress())
                .option(ChannelOption.TCP_NODELAY, settings.isTransportTcpNoDelay())
                .option(ChannelOption.SO_KEEPALIVE, settings.isTransportSoKeepAlive())
                .option(ChannelOption.SO_SNDBUF, settings.getTransportSoSendBufferSize())
                .option(ChannelOption.SO_RCVBUF, settings.getTransportSoReceiveBufferSize())
                .option(ChannelOption.SO_REUSEADDR, settings.isTransportSoReuseAddress());
    }

    protected ClientSettings getSettings() {
        return settings;
    }

    protected Bootstrap getBootstrap() {
        return bootstrap;
    }

    protected ServerLoadBalancer getServerLoadBalancer() {
        return serverLoadBalancer;
    }

    protected NettyClientChannelInitializer getChannelInitializer() {
        return channelInitializer;
    }

    protected MessageSender getMessageSender(Channel channel) {
        return (MessageSender) channel.pipeline().get(HandlerConstants.CLIENT_HANDLER);
    }

    @Override
    public ResponseMessage send(final RequestMessage message, final int waitMilliSeconds) throws Throwable {
        ResponseMessage responseMessage;
        if (settings.getMessageMockType() == MessageMockType.LOCAL) {
            responseMessage = new ResponseMessage();
        } else {
            responseMessage = sendToServer(message, waitMilliSeconds);
        }
        if (settings.getMessageMockType() != MessageMockType.OFF) {
            responseMessage.setResultCode(ResultCodeConstants.SUCC_CODE);
            responseMessage.setMessageCode("RFK00000");
            responseMessage.setErrorMessage("");
        }
        return responseMessage;
    }

    public ResponseMessage sendToServer(final RequestMessage message, final int waitMilliSeconds) throws Throwable {
        InetSocketAddress address = getServerLoadBalancer().getAddress();
        LOGGER.debug("Send to server {} with {}, waiting for {} milliseconds",
                new String[]{address.toString(), message.toString(), String.valueOf(waitMilliSeconds)});
        long start = System.currentTimeMillis();
        ResponseMessage responseMessage = null;
        try {
            responseMessage = doSend(address, message, waitMilliSeconds);
        } finally {
            clientStats.update(address.toString(), System.currentTimeMillis() - start);
        }
        LOGGER.debug("Receive from server {} with {}", address, responseMessage);
        if (responseMessage == null) {
            throw new IllegalStateException("Response message of requestId "
                    + message.getRequestId() + " is null.");
        }
        if (!message.getRequestId().equals(responseMessage.getRequestId())) {
            throw new IllegalStateException("Message requestId " + message.getRequestId()
                    + " not match with response " + responseMessage.getRequestId());
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage send(final RequestMessage message) throws Throwable {
        return send(message, settings.getTransportFetchWaitMilliSeconds());
    }

    public abstract ResponseMessage doSend(final InetSocketAddress address, final RequestMessage message,
                                           final int waitMilliSeconds) throws Throwable;

    public Stats getStats() {
        return clientStats;
    }

    @Override
    public void close() {
        LOGGER.info("Closing NettyTCPClient...");
        bootstrap.group().shutdownGracefully().syncUninterruptibly();
        LOGGER.info("NettyTCPClient Closed.");
    }
}
