package com.roy.drisk.server.netty.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;
import io.netty.channel.ChannelHandler;
import org.springframework.stereotype.Component;

/**
 * @author roy
 * @date 2021/11/8
 * @desc 解析消息报问题
 */
@ChannelHandler.Sharable
@Component
public class EngineJsonMessageCodec extends AbstractEngineMessageCodec {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected RequestMessage doDecode(byte[] bytes) throws Exception {
        return mapper.readValue(bytes, RequestMessage.class);
    }

    @Override
    protected byte[] doEncode(ResponseMessage msg) throws Exception {
        return mapper.writeValueAsBytes(msg);
    }
}
