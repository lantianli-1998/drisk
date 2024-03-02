package com.roy.drisk.server.netty.handler;

import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author roy
 * @date 2021/10/27
 * @desc  解析消息报文体，
 * 请求报文从目标格式反序列化为<code>RequestMessage</code>，同时设置消息来源IP到<code>RequestMessage</code>中，
 * 响应报文从<code>ResponseMessage</code>序列化为目标格式
 */
@Component
public abstract class AbstractEngineMessageCodec extends MessageToMessageCodec<ByteBuf, ResponseMessage> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        RequestMessage requestMessage = doDecode(bytes);
        out.add(requestMessage);
    }

    protected abstract RequestMessage doDecode(byte[] bytes) throws Exception;

    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseMessage msg, List<Object> out) throws Exception {
        byte[] bytes = doEncode(msg);
        ByteBuf byteBuf = ctx.alloc().buffer().writeBytes(bytes);
        out.add(byteBuf);
    }

    protected abstract byte[] doEncode(ResponseMessage msg) throws Exception;
}
