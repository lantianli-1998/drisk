package com.roy.drisk.context;

import com.roy.drisk.message.ContextMessage;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public interface EngineContextIter {
    ContextMessage message();

    <E> void sendEvent(E event);
}
