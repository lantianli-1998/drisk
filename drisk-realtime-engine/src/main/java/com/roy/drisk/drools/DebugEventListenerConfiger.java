package com.roy.drisk.drools;

import org.drools.core.event.DebugAgendaEventListener;
import org.drools.core.event.DebugProcessEventListener;
import org.drools.core.event.DebugRuleRuntimeEventListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc 用于为KieSession设置
 */
@Component
@Lazy
public class DebugEventListenerConfiger {
    private static final Logger LOGGER = LoggerFactory.getLogger(DebugEventListenerConfiger.class);
    private static final ConcurrentHashMap<String, Config> configuration = new ConcurrentHashMap<>();
    private static final Map<DebugListenerType, EventListener> debugListeners = new HashMap<>();

    public enum DebugListenerType {
        RULERUNTIME, AGENDA, PROCESS
    }

    @PostConstruct
    private void initDebugListeners() {
        debugListeners.put(DebugListenerType.RULERUNTIME, new DebugRuleRuntimeEventListener());
        debugListeners.put(DebugListenerType.AGENDA, new DebugAgendaEventListener());
        debugListeners.put(DebugListenerType.PROCESS, new DebugProcessEventListener());
    }

    /**
     * 为<code>KieSession</code>设置debugger
     *
     * @param ruleName   规则名称
     * @param kieSession 需要设置的KieSession
     */
    public static void setSessionDebugger(String ruleName, KieSession kieSession) {
        if (!configuration.containsKey(ruleName))
            return;
        Config config = configuration.get(ruleName);
        if (config.has(DebugListenerType.RULERUNTIME)) {
            kieSession.addEventListener((DebugRuleRuntimeEventListener)
                    debugListeners.get(DebugListenerType.RULERUNTIME));
        }
        if (config.has(DebugListenerType.AGENDA)) {
            kieSession.addEventListener(((DebugAgendaEventListener)
                    debugListeners.get(DebugListenerType.AGENDA)));
        }
        if (config.has(DebugListenerType.PROCESS)) {
            kieSession.addEventListener((DebugProcessEventListener)
                    debugListeners.get(DebugListenerType.PROCESS));
        }
    }

    /**
     * 为<code>StatelessKieSession</code>设置debugger
     *
     * @param ruleName   规则名称
     * @param kieSession 需要设置的StatelessKieSession
     */
    public static void setSessionDebugger(String ruleName, StatelessKieSession kieSession) {
        if (!configuration.containsKey(ruleName))
            return;
        Config config = configuration.get(ruleName);
        if (config.has(DebugListenerType.RULERUNTIME))
            kieSession.addEventListener((DebugRuleRuntimeEventListener)
                    debugListeners.get(DebugListenerType.RULERUNTIME));
        if (config.has(DebugListenerType.AGENDA))
            kieSession.addEventListener(((DebugAgendaEventListener)
                    debugListeners.get(DebugListenerType.AGENDA)));
        if (config.has(DebugListenerType.PROCESS))
            kieSession.addEventListener((DebugProcessEventListener)
                    debugListeners.get(DebugListenerType.PROCESS));
    }

    /**
     * 为ruleName设置type类型的debugger
     *
     * @param ruleName 规则名称
     * @param type     debugger类型
     */
    public void enableDebugger(String ruleName, String type) {
        enableDebugger(ruleName, DebugListenerType.valueOf(type.toUpperCase()));
    }

    /**
     * 为ruleName设置type类型的debugger
     *
     * @param ruleName 规则名称
     * @param type     debugger类型
     */
    public void enableDebugger(String ruleName, DebugListenerType type) {
        Config config = configuration.getOrDefault(ruleName, new Config());
        config.enable(type);
        configuration.putIfAbsent(ruleName, config);
        LOGGER.info("Enable debugger {} for rule {}", type, ruleName);
    }

    /**
     * 为ruleName设置所有类型的debugger
     *
     * @param ruleName 规则名称
     */
    public void enableAllDebugger(String ruleName) {
        Config config = configuration.getOrDefault(ruleName, new Config());
        for (DebugListenerType type : DebugListenerType.values()) {
            config.enable(type);
        }
        configuration.putIfAbsent(ruleName, config);
        LOGGER.info("Enable all debuggers for rule {}", ruleName);
    }

    /**
     * 为ruleName解除设置type类型的debugger
     *
     * @param ruleName 规则名称
     * @param type     debugger类型
     */
    public void disableDebugger(String ruleName, String type) {
        disableDebugger(ruleName, DebugListenerType.valueOf(type.toUpperCase()));
    }

    /**
     * 为ruleName解除设置type类型的debugger
     *
     * @param ruleName 规则名称
     * @param type     debugger类型
     */
    public void disableDebugger(String ruleName, DebugListenerType type) {
        Config config = configuration.get(ruleName);
        if (config != null) {
            config.disable(type);
            LOGGER.info("Disable debugger {} for rule {}", type, ruleName);
        }
    }

    /**
     * 为ruleName解除设置所有类型的debugger
     *
     * @param ruleName 规则名称
     */
    public void disableAllDebugger(String ruleName) {
        Config config = configuration.get(ruleName);
        if (config != null) {
            for (DebugListenerType type : DebugListenerType.values()) {
                config.disable(type);
            }
            LOGGER.info("Disable all debuggers for rule {}", ruleName);
        }
    }

    /**
     * 取得各规则的Debugger配置
     *
     * @return debugger配置，Key为ruleName，Value为配置类
     */
    public Map<String, Config> getConfiguration() {
        return Collections.unmodifiableMap(configuration);
    }

    private static final class Config {
        private Set<DebugListenerType> types = new HashSet<>();

        void enable(DebugListenerType type) {
            types.add(type);
        }

        void disable(DebugListenerType type) {
            types.remove(type);
        }

        public boolean has(DebugListenerType type) {
            return types.contains(type);
        }

        @Override
        public String toString() {
            return types.toString();
        }
    }
}
