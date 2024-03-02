package com.roy.drisk.drools.agenda;

import com.roy.drisk.drools.session.StatefulSessionProcessor;
import com.roy.drisk.engine.util.RuleUtil;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
@Component
@Lazy
public class RuleInactivator implements AgendaFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatefulSessionProcessor.class);

    @Override
    public boolean accept(Match match) {
        boolean isActive = RuleUtil.isRuleActive(match.getRule().getName());
        if (!isActive) {
            LOGGER.debug("Rule {} is inactive", match.getRule().getName());
        }
        return isActive;
    }
}
