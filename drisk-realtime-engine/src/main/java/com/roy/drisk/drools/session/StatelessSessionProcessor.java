package com.roy.drisk.drools.session;

import com.roy.drisk.drools.DroolsRuleServiceManager;
import com.roy.drisk.drools.KieInfrastructure;
import com.roy.drisk.drools.agenda.RuleInactivator;
import com.roy.drisk.engine.context.EngineContext;
import com.roy.drisk.message.ContextMessage;
import org.drools.core.command.runtime.rule.FireAllRulesCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.StatelessKieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author roy
 * @date 2021/10/27
 * @desc Stateless KieSession处理器
 */
@Component
@Lazy
public class StatelessSessionProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatelessSessionProcessor.class);
    private KieInfrastructure kieInfrastructure;
    @Autowired
    private RuleInactivator ruleInactivator;

    @Autowired
    public StatelessSessionProcessor(KieInfrastructure kieInfrastructure) {
        this.kieInfrastructure = kieInfrastructure;
    }

    public ContextMessage process(StatelessKieSession kieSession, EngineContext context,
                                  DroolsRuleServiceManager serviceManager) {
        KieCommands kieCommands = kieInfrastructure.getKieServices().getCommands();
        List<Command> cmds = new ArrayList<>();
//        cmds.add(kieCommands.newSetGlobal("service", context.service()));
//        serviceManager.getServices().forEach((name, service) ->
//                cmds.add(kieCommands.newSetGlobal(name, service)));
        cmds.add(kieCommands.newInsert(context.message(), "message", true, null));
        Map<String, Object> beans = serviceManager.getInjectableBeans(context);
        beans.forEach((beanName, bean) -> kieCommands.newInsert(bean, beanName));
        LOGGER.trace("Fire rules {} with message {}", kieSession, context.message());
        cmds.add(new FireAllRulesCommand(ruleInactivator));
        ExecutionResults results = kieSession.execute(kieCommands.newBatchExecution(cmds));
        return (ContextMessage) results.getValue("message");
    }
}
