package com.roy.drisk.drools;

import com.roy.drisk.drools.session.StatefulSessionProcessor;
import com.roy.drisk.drools.session.StatelessSessionProcessor;
import com.roy.drisk.engine.context.EngineContext;
import com.roy.drisk.engine.service.AbstractEngine;
import com.roy.drisk.engine.util.RuleUtil;
import com.roy.drisk.exception.KieSessionInvalidException;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc Drools规则引擎
 */
@Component
@Lazy
public class DroolsEngine extends AbstractEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(DroolsEngine.class);
    private KieInfrastructure kieInfrastructure;
    @Autowired
    private StatefulSessionProcessor statefulSessionProcessor;
    @Autowired
    private StatelessSessionProcessor statelessSessionProcessor;

    @Autowired
    public DroolsEngine(KieInfrastructure kieInfrastructure) {
        this.kieInfrastructure = kieInfrastructure;
    }

    @Override
    protected void doProcess(EngineContext context) {
        String baseName = context.message().getBaseName();
        String sessionName = context.message().getSessionName();
        KieContainer kieContainer = kieInfrastructure.getSessionByName(baseName, sessionName);
        LOGGER.trace("Got KieContainer: {}", kieContainer);
        DroolsRuleServiceManager serviceManager = new DroolsRuleServiceManager(kieContainer, baseName, sessionName);
        LOGGER.trace("Got DroolsRuleServiceManager: {}", serviceManager);
//        serviceManager.injectContext(context);
        serviceManager.invokeBeforeProcessServices(context);
        String ruleName = RuleUtil.buildRuleName(baseName, sessionName);
        StatelessKieSession statelessKieSession = null;
        KieSession statefulKieSession = null;
        try {
            statelessKieSession = kieContainer.newStatelessKieSession(sessionName);
            LOGGER.trace("Create stateless {} with {}", statelessKieSession, ruleName);
        } catch (Exception e) {
            statefulKieSession = kieContainer.newKieSession(sessionName);
            LOGGER.trace("Create stateful {} with {}", statefulKieSession, ruleName);
        }
        if (statefulKieSession == null && statelessKieSession != null) {
            DebugEventListenerConfiger.setSessionDebugger(ruleName, statelessKieSession);
            statelessSessionProcessor.process(statelessKieSession, context, serviceManager);
        } else if (statefulKieSession != null && statelessKieSession == null) {
            DebugEventListenerConfiger.setSessionDebugger(ruleName, statefulKieSession);
            statefulSessionProcessor.process(statefulKieSession, context, serviceManager);
        } else {
            throw new KieSessionInvalidException("KieSession " + baseName + ":" + sessionName + " not found.");
        }
        serviceManager.invokeAfterProcessServices(context);
    }
}
