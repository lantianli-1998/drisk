package com.roy.drisk.engine.service;

import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc 规则引擎
 */
public interface Engine {
    ResponseMessage process(RequestMessage reqMsg);
}
