package com.roy.drisk.client.domain.netty.handler;

import com.roy.drisk.message.MessageFormat;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc 解析消息头消息格式代码(1字节)
 */
@ChannelHandler.Sharable
public class MessageFormatCodec extends MessageToMessageCodec<ByteBuf, ByteBuf> {
    private MessageFormat format;

    public MessageFormatCodec(MessageFormat format) {
        this.format = format;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 1) {
            return;
        }
        int formatValue = in.readByte();
        MessageFormat format = MessageFormat.byValue(formatValue);
        ctx.channel().attr(HandlerConstants.FORMAT_ATTR).set(format);
        out.add(in.retainedSlice());
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int length = msg.readableBytes();
        if (length < 0) {
            throw new IllegalArgumentException(
                    "Adjusted frame length (" + length + ") is less than zero");
        }
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeByte((byte) format.getValue());
        buf.writeBytes(msg, msg.readerIndex(), msg.readableBytes());
        out.add(buf);
    }
}
