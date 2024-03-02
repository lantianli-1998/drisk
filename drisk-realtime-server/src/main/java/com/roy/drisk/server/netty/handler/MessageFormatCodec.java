package com.roy.drisk.server.netty.handler;

import com.roy.drisk.message.MessageFormat;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 * 解析消息头消息格式代码（1字节）
 */
@ChannelHandler.Sharable
@Component
public class MessageFormatCodec extends MessageToMessageCodec<ByteBuf, ByteBuf> {
    @Autowired
    private EngineJsonMessageCodec engineJsonMessageCodec;
    @Autowired(required = false)
    private EngineKryoMessageCodec engineKryoMessageCodec;

    private void replaceCodec(ChannelHandlerContext ctx, ChannelHandler codecHandler) {
        ctx.channel().pipeline()
                .replace(HandlerConstants.ENGINE_MESSAGE_CODEC,
                        HandlerConstants.ENGINE_MESSAGE_CODEC,
                        codecHandler);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 1) {
            return;
        }
        int formatValue = in.readByte();
        MessageFormat format = MessageFormat.byValue(formatValue);
        ctx.channel().attr(HandlerConstants.FORMAT_ATTR).set(format);
        switch (format) {
            case JSON:
                replaceCodec(ctx, engineJsonMessageCodec);
                break;
            case KRYO:
                replaceCodec(ctx, engineKryoMessageCodec);
                break;
        }
        out.add(in.retainedSlice());
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int length = msg.readableBytes();
        if (length < 0) {
            throw new IllegalArgumentException(
                    "Adjusted frame length (" + length + ") is less than zero");
        }
        MessageFormat format = ctx.channel().attr(HandlerConstants.FORMAT_ATTR).get();
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeByte((byte) format.getValue());
        buf.writeBytes(msg, msg.readerIndex(), msg.readableBytes());
        out.add(buf);
    }
}
