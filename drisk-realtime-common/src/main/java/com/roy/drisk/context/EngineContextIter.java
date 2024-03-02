package com.roy.drisk.context;

import com.roy.drisk.message.ContextMessage;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public interface EngineContextIter {
    ContextMessage message();

    <E> void sendEvent(E event);
}
