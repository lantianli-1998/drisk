package com.roy.drisk.engine.service;

import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;

/**
 * @author roy
 * @date 2021/10/27
 * @desc 规则引擎
 */
public interface Engine {
    ResponseMessage process(RequestMessage reqMsg);
}
