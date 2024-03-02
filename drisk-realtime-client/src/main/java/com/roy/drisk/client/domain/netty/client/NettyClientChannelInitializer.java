package com.roy.drisk.client.domain.netty.client;

import com.roy.drisk.client.contract.ClientSettings;
import com.roy.drisk.client.domain.netty.handler.ByteArrayClientHandler;
import com.roy.drisk.client.domain.netty.handler.HandlerConstants;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc
 */
public class NettyClientChannelInitializer {
    private ClientSettings settings;
    private ChannelHandler lengthPrepender;
//    private ChannelHandler messageVersionCodec;
//    private ChannelHandler messageFormatCodec;
//    private ChannelHandler clientMessageCodec;

    public NettyClientChannelInitializer(ClientSettings settings) {
        this.settings = settings;
        initHandlers();
    }

    private void initHandlers() {
        this.lengthPrepender = new LengthFieldPrepender(settings.getMessagePreLength());
//        this.messageVersionCodec = new MessageVersionCodec(settings.getMessageVersion());
//        this.messageFormatCodec = new MessageFormatCodec(settings.getMessageFormat());
//        switch (settings.getMessageFormat()) {
//            case JSON:
//                this.clientMessageCodec = new ClientJsonMessageCodec();
//                break;
//            case KRYO:
//                this.clientMessageCodec = new ClientKryoMessageCodec();
//                break;
//        }
    }

    public void initChannel(Channel ch) {
        int preLength = settings.getMessagePreLength();
        ch.pipeline().addLast("lengthDecoder",
                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, preLength, 0, preLength));
        ch.pipeline().addLast("lengthPrepender", lengthPrepender);
        ch.pipeline().addLast("bytesDecoder", new ByteArrayDecoder());
        ch.pipeline().addLast("bytesEncoder", new ByteArrayEncoder());
//        ch.pipeline().addLast("messageVersionCodec", messageVersionCodec);
//        ch.pipeline().addLast("messageFormatCodec", messageFormatCodec);
//        ch.pipeline().addLast("clientMessageCodec", clientMessageCodec);
//        ch.pipeline().addLast(HandlerConstants.CLIENT_HANDLER, new ClientHandler());
        ch.pipeline().addLast(HandlerConstants.CLIENT_HANDLER, new ByteArrayClientHandler(settings));
    }
}
