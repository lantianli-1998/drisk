package com.roy.drisk.server.netty.server;

import com.roy.drisk.server.netty.config.MessageSettings;
import com.roy.drisk.server.netty.config.TransportSettings;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc  <code>NettyServerChannelInitializer</code>用于设置Netty channel pipline<br/>
 * <code>IdleStateHandler</code>用于监控读写空闲时间，超时时自动断开链接<br/>
 * <code>LengthFieldBasedFrameDecoder</code>用于解析报文头中的前置长度，组包报文体<br/>
 * <code>LengthFieldPrepender</code>用于为返回报文增加前置长度<br/>
 * <code>ByteArrayDecoder</code>用于将<code>ByteBuf</code>转换为字节数组<br/>
 * <code>ByteArrayEncoder</code>用于将字节数组转换为<code>ByteBuf</code><br/>
 * {@link MessageVersionCodec}用于解析版本号，暂不使用<br/>
 * {@link MessageFormatCodec}用于解析报文格式代码，暂不使用<br/>
 * {@link EngineJsonMessageCodec}用于序列化、反序列化报文，暂不使用<br/>
 * {@link EngineHandler}用于调用Engine处理业务逻辑，暂不使用<br/>
 * {@link ByteArrayEngineHandler}用于处理二进制协议，转换为业务对象并调用Engine处理业务逻辑
 */
@Component
public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private TransportSettings transportSettings;
    private MessageSettings messageSettings;
    private ChannelHandler lengthPrepender;
    //    @Autowired
//    private ChannelHandler messageVersionCodec;
//    @Autowired
//    private ChannelHandler messageFormatCodec;
//    @Autowired
//    @Qualifier("engineJsonMessageCodec")
//    private ChannelHandler engineMessageCodec;
    @Autowired
    @Qualifier("byteArrayEngineHandler")
    private ChannelHandler engineHandler;
    @Autowired
    private EventExecutorGroup engineHandlerExecutor;

    @Autowired
    public NettyServerChannelInitializer(TransportSettings transportSettings, MessageSettings messageSettings) {
        this.transportSettings = transportSettings;
        this.messageSettings = messageSettings;
        initHandlers();
    }

    private void initHandlers() {
        this.lengthPrepender = new LengthFieldPrepender(messageSettings.getPreLength());
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        int preLength = messageSettings.getPreLength();
        ch.pipeline().addLast("idleStateHandler",
                new IdleStateHandler(transportSettings.getReaderIdleTimeSeconds(), 0, 0));
        ch.pipeline().addLast("lengthDecoder",
                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, preLength, 0, preLength));
        ch.pipeline().addLast("lengthPrepender", lengthPrepender);
        ch.pipeline().addLast("bytesDecoder", new ByteArrayDecoder());
        ch.pipeline().addLast("bytesEncoder", new ByteArrayEncoder());
//        ch.pipeline().addLast("messageVersionCodec", messageVersionCodec);
//        ch.pipeline().addLast("messageFormatCodec", messageFormatCodec);
//        ch.pipeline().addLast(HandlerConstants.ENGINE_MESSAGE_CODEC, engineMessageCodec);
        ch.pipeline().addLast(engineHandlerExecutor, "engineHandler", engineHandler);
    }
}
