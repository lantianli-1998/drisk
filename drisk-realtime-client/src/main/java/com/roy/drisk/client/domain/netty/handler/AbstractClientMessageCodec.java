package com.roy.drisk.client.domain.netty.handler;

import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * @author roy
 * @date 2021/10/26
 * @desc  解析消息报文体，
 * 请求报文从<code>RequestMessage</code>序列化为目标格式，
 * 响应报文从目标格式反序列化为<code>ResponseMessage</code>
 */
public abstract class AbstractClientMessageCodec extends MessageToMessageCodec<ByteBuf, RequestMessage> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        ResponseMessage responseMessage = doDecode(bytes);
        out.add(responseMessage);
    }

    protected abstract ResponseMessage doDecode(byte[] bytes) throws Exception;

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestMessage msg, List<Object> out) throws Exception {
        byte[] bytes = doEncode(msg);
        ByteBuf byteBuf = ctx.alloc().buffer().writeBytes(bytes);
        out.add(byteBuf);
    }

    protected abstract byte[] doEncode(RequestMessage msg) throws Exception;
}
