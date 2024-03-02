package com.roy.drisk.rule;

import com.roy.drisk.context.EngineContextIter;

/**
 * @author roy
 * @date 2021/10/27
 * @desc 规则服务是否需要在规则运行前进行前处理，实现此接口的规则服务，
 */
public interface BeforeProcessRule {
    /**
     * 前处理方法
     */
//    void before(ContextMessage message);
    void before(EngineContextIter context);
}
