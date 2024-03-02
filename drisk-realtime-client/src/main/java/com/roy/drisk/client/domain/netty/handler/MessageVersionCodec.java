package com.roy.drisk.client.domain.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * @author roy
 * @date 2021/10/26
 * @desc 解析消息头版本号(1字节)
 */
@ChannelHandler.Sharable
public class MessageVersionCodec extends MessageToMessageCodec<ByteBuf, ByteBuf> {
    private int version;

    public MessageVersionCodec(int version) {
        this.version = version;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 1) {
            return;
        }
        int version = in.readByte();
        ctx.channel().attr(HandlerConstants.VERSION_ATTR).set(version);
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
        buf.writeByte((byte) version);
        buf.writeBytes(msg, msg.readerIndex(), msg.readableBytes());
        out.add(buf);
    }
}
