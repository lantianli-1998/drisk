package com.roy.drisk.client.domain.netty.handler;

import com.alibaba.fastjson.JSON;
import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;
import io.netty.channel.ChannelHandler;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc
 */
@ChannelHandler.Sharable
public class ClientJsonMessageCodec extends AbstractClientMessageCodec {
    @Override
    protected ResponseMessage doDecode(byte[] bytes) throws Exception {
        return JSON.parseObject(bytes, ResponseMessage.class);
    }

    @Override
    protected byte[] doEncode(RequestMessage msg) throws Exception {
        return JSON.toJSONBytes(msg);
    }
}
