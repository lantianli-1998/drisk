package com.roy.drisk.rule;

import com.roy.drisk.context.EngineContextIter;

import java.util.Map;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 * 规则服务是否向规则引擎注入Bean，实现此接口的规则服务，
 * 由Engine在前处理后，调用规则前向规则引擎注入Bean。
 */
public interface InjectableBeansRule {
    /**
     * 取得待注入的Bean
     *
     * @return 待注入的BeanMap，Key为Bean name，Value为Bean object
     */
    Map<String, Object> getBeans(EngineContextIter context);
}
