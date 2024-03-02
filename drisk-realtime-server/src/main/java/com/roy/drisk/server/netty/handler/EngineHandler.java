package com.roy.drisk.server.netty.handler;

import com.roy.drisk.engine.service.Engine;
import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 * <code>EngineHandler</code>调用规则引擎<code>Engine</code>处理业务逻辑，
 * 并返回响应。对于非KeepAlived链接，返回后断开连接，对于读写超时也会自动断开链接。
 */
@ChannelHandler.Sharable
@Component
@Lazy
public class EngineHandler extends SimpleChannelInboundHandler<RequestMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineHandler.class);
    private Engine engine;

    @Autowired
    public EngineHandler(@Qualifier("rulesEngine") Engine engine) {
        this.engine = engine;
    }

    private static void setRemoteAddress(ChannelHandlerContext ctx, RequestMessage msg) {
        SocketAddress address = ctx.channel().remoteAddress();
        if (address instanceof InetSocketAddress) {
            msg.setClientIp(((InetSocketAddress) address).getAddress().getHostAddress());
        }
    }

    private static void logAttrInfo(String tag, ChannelHandlerContext ctx, String requestId) {
        LOGGER.debug("EngineHandler {}: version[{}] format[{}] requestId[{}]",
                tag, ctx.channel().attr(HandlerConstants.VERSION_ATTR),
                ctx.channel().attr(HandlerConstants.FORMAT_ATTR), requestId);
    }

    private static boolean isKeepAlive(Channel ch) {
        if (!ch.isOpen())
            return false;
        return ch.config().getOption(ChannelOption.SO_KEEPALIVE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage msg) throws Exception {
        setRemoteAddress(ctx, msg);
        logAttrInfo("received", ctx, msg.getRequestId());
        Future<ResponseMessage> responseFuture = ctx.executor().next().submit(() -> engine.process(msg));
        responseFuture.addListener((FutureListener<ResponseMessage>) future -> {
            ResponseMessage rspMsg = future.get();
            logAttrInfo("send", ctx, rspMsg.getRequestId());
            ChannelFuture writeFuture = ctx.writeAndFlush(rspMsg);
            if (!isKeepAlive(ctx.channel())) {
                writeFuture.addListener((ChannelFutureListener) closeFuture -> {
                    LOGGER.debug("Closing not keepAlived connection {}", closeFuture.channel());
                    closeFuture.channel().close();
                });
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("EngineHandlerError", cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
                LOGGER.debug("Reader {} idles too long, closing context {}", ctx.channel().remoteAddress(), ctx);
                ctx.close();
            }
        }
    }
}
