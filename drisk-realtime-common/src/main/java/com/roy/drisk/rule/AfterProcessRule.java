package com.roy.drisk.rule;

import com.roy.drisk.context.EngineContextIter;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 * 规则服务是否需要在规则运行前进行后处理，实现此接口的规则服务，
 * 由Engine在运行规则结束后调用{@link #after()}进行后处理
 */
public interface AfterProcessRule {
    /**
     * 后处理方法
     */
//    void after(ContextMessage message);
    void after(EngineContextIter engine);
}
