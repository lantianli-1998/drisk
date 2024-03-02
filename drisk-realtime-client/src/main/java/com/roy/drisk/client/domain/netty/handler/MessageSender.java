package com.roy.drisk.client.domain.netty.handler;


import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;

/**
 * @author roy
 * @date 2021/10/26
 * @desc
 */
public interface MessageSender {
    ResponseMessage sendMessage(RequestMessage message, int waitMilliSeconds) throws Exception;
}
