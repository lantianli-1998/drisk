package com.roy.drisk.drools;

import com.roy.drisk.engine.context.EngineContext;
import com.roy.drisk.engine.util.RuleUtil;
import com.roy.drisk.rule.AfterProcessRule;
import com.roy.drisk.rule.BeforeProcessRule;
import com.roy.drisk.rule.InjectableBeansRule;
import org.kie.api.definition.KiePackage;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 * Drools规则服务管理类，用于管理依附于规则的Service的生命周期。
 * <code>DroolsRuleServiceManager</code>初始化时，根据baseName及sessionName
 * 从<code>KieContainer</code>中规则对应的package中加载命名规则为sessionName + "Service"
 * 的类，初始化完成后对于实现了{@link EngineContextAwareRule}接口的类会注入{@link EngineContext}，
 * 规则调用前，对于实现了{@link InjectableBeansRule}接口的类会调用其方法取得可注入的Beans注入规则引擎，
 * 之后，对于实现了{@link BeforeProcessRule}接口的类会调用其前处理方法，最后在规则调用后，
 * 对于实现了{@link AfterProcessRule}接口的类会调用其后处理方法。
 */
public class DroolsRuleServiceManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DroolsRuleServiceManager.class);

    private Map<String, Object> services;

    public DroolsRuleServiceManager(KieContainer kieContainer, String baseName, String sessionName) {
        this.services = loadServices(kieContainer, baseName, sessionName);
    }

    private static Map<String, Object> loadServices(KieContainer kieContainer, String baseName, String sessionName) {
        ClassLoader classLoader = kieContainer.getClassLoader();
        LOGGER.trace("KieContainer classloader is {}", classLoader);
        Map<String, Object> services = new HashMap<>();
        for (KiePackage kiePackage : kieContainer.getKieBase(baseName).getKiePackages()) {
            LOGGER.trace("Scanning services in {}", kiePackage.getName());
            try {
                Class<?> clazz = Class.forName(serviceClassName(kiePackage.getName(), sessionName), true, classLoader);
                Object service = clazz.newInstance();
                services.put(RuleUtil.serviceName(clazz), service);
                LOGGER.trace("Load Rules Service: {} by {}", service, service.getClass().getClassLoader());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                LOGGER.trace("Service not found in {}, reason: {}", kiePackage, e.getMessage());
            }
        }
        if (services.isEmpty()) {
            LOGGER.info("No services for {}:{}", baseName, sessionName);
        }
        return services;
    }

    private static String serviceClassName(String packageName, String sessionName) {
        return packageName + "." + sessionName + "Service";
    }

    /**
     * 取得规则对应的服务
     *
     * @return 服务的集合
     */
    public Map<String, Object> getServices() {
        return services;
    }

    /**
     * 向规则服务注入<code>EngineContext</code>
     * 2018年5月21日 这一步给注释掉了。 
     * @param engineContext EngineContext
     */
    public void injectContext(EngineContext engineContext) {
        services.values().stream().filter(service ->
                service instanceof EngineContextAwareRule)
                .forEach(service -> ((EngineContextAwareRule) service).setEngineContext(engineContext));
    }

    /**
     * 调用规则服务前处理
     */
    public void invokeBeforeProcessServices(EngineContext engineContext) {
        services.values().stream().filter(service ->
                service instanceof BeforeProcessRule)
                .forEach(service -> {
                    LOGGER.debug("Before service: {}", service);
                    ((BeforeProcessRule) service).before(engineContext);
                });
    }

    /**
     * 取得可注入的Beans集合
     *
     * @return 待注入Beans集合
     */
    public Map<String, Object> getInjectableBeans(EngineContext engineContext) {
        Map<String, Object> beans = new HashMap<>();
        services.values().stream().filter(service ->
                service instanceof InjectableBeansRule)
                .forEach(service -> {
                    Map<String, Object> serviceBeans = ((InjectableBeansRule) service).getBeans(engineContext);
                    if (serviceBeans != null && !serviceBeans.isEmpty())
                        beans.putAll(serviceBeans);
                });
        return beans;
    }

    /**
     * 调用规则服务后处理
     */
    public void invokeAfterProcessServices(EngineContext engineContext) {
        services.values().stream().filter(service ->
                service instanceof AfterProcessRule)
                .forEach(service -> {
                    LOGGER.debug("After service: {}", service);
                    ((AfterProcessRule) service).after(engineContext);
                });
    }
}
