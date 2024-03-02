package com.roy.drisk.engine.handler;

import com.roy.drisk.exception.MessageCodeConstants;
import com.roy.drisk.message.ContextMessage;
import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author roy
 * @date 2021/10/27
 * @desc 默认的输入输出消息的编解码器，目前Engine只支持固定使用此编解码器。
 */
@Component
public class DefaultContextMessageCodec implements ContextMessageCodec {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultContextMessageCodec.class);

    @Override
    public ContextMessage decode(RequestMessage requestMessage) {
        LOGGER.debug("Engine Received: {}", requestMessage);
        ContextMessage contextMessage = new ContextMessage(requestMessage.getData());
        initContextMessage(requestMessage, contextMessage);
        return contextMessage;
    }

    private static void initContextMessage(RequestMessage requestMessage, ContextMessage contextMessage) {
        contextMessage.setStartTime(System.currentTimeMillis());
        contextMessage.setBaseName(requestMessage.getBaseName());
        contextMessage.setSessionName(requestMessage.getSessionName());
        contextMessage.setRequestId(requestMessage.getRequestId());
        contextMessage.setClientId(requestMessage.getClientId());
        contextMessage.setClientIp(requestMessage.getClientIp());
        contextMessage.setUserNo(requestMessage.getUserNo());
        contextMessage.setMobileNo(requestMessage.getMobileNo());
        contextMessage.setMessageCode(MessageCodeConstants.SYSTEM_SUCC); //默认状态是成功。
    }

    @Override
    public ResponseMessage encode(ContextMessage contextMessage) {
        ResponseMessage responseMessage = buildResponseMessage(contextMessage);
        LOGGER.debug("Engine Send: {}", responseMessage);
        return responseMessage;
    }

    private static ResponseMessage buildResponseMessage(ContextMessage contextMessage) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setBaseName(contextMessage.getBaseName());
        responseMessage.setSessionName(contextMessage.getSessionName());
        responseMessage.setRequestId(contextMessage.getRequestId());
        responseMessage.setClientId(contextMessage.getClientId());
        responseMessage.setClientIp(contextMessage.getClientIp());
        responseMessage.setUserNo(contextMessage.getUserNo());
        responseMessage.setMobileNo(contextMessage.getMobileNo());
        responseMessage.setDuration(contextMessage.getDuration());
        responseMessage.setResultCode(contextMessage.getResultCode());
        responseMessage.setMessageCode(contextMessage.getMessageCode());
        responseMessage.setErrorMessage(contextMessage.getErrorMessage());
        responseMessage.setData(contextMessage.getRspData());
        return responseMessage;
    }
}
