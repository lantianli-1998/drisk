package com.roy.drisk.engine.handler;

import com.roy.drisk.message.ContextMessage;
import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc Engine输入输出消息的编解码。
 */
public interface ContextMessageCodec {
    /**
     * 解码请求消息生成ContextMessage
     *
     * @param requestMessage 请求消息
     * @return ContextMessage
     */
    ContextMessage decode(RequestMessage requestMessage);

    /**
     * 从ContextMessage中生成响应消息
     *
     * @param contextMessage ContextMessage
     * @return ResponseMessage 响应消息
     */
    ResponseMessage encode(ContextMessage contextMessage);
}
