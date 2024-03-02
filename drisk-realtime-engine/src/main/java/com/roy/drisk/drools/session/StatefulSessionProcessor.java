package com.roy.drisk.drools.session;

import com.roy.drisk.drools.DroolsRuleServiceManager;
import com.roy.drisk.drools.agenda.RuleInactivator;
import com.roy.drisk.engine.context.EngineContext;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author roy
 * @date 2021/10/27
 * @desc StateFul KieSession处理器
 */
@Component
@Lazy
public class StatefulSessionProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatefulSessionProcessor.class);
    @Autowired
    private RuleInactivator ruleInactivator;

    public void process(KieSession kieSession, EngineContext context, DroolsRuleServiceManager serviceManager) {
        try {
            doProcess(kieSession, context, serviceManager);
        } finally {
            kieSession.dispose();
        }
    }

    private void doProcess(KieSession kieSession, EngineContext context, DroolsRuleServiceManager serviceManager) {
        // 这里要注意，global对象需要在drl文件中声明了，这里才能插入。如果没有先声明，这里插入会抛出RuntimeException异常。
        // global主要是用来声明一些所有规则都需要用到的对象。
//        kieSession.setGlobal("service", context.service());
//        serviceManager.getServices().forEach(kieSession::setGlobal);
        kieSession.insert(context.message());
        Map<String, Object> beans = serviceManager.getInjectableBeans(context);
        beans.values().forEach(kieSession::insert);
        LOGGER.trace("Fire rules {} with message {}", kieSession, context.message());
        kieSession.fireAllRules(ruleInactivator);
    }
}
