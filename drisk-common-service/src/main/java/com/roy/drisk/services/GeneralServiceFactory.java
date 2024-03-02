package com.roy.drisk.services;

import com.roy.drisk.commonservice.config.ServiceProperties;
import com.roy.drisk.commonservice.exception.ServiceBusinessException;
import com.roy.drisk.commonservice.exception.ServiceNotFoundException;
import com.roy.drisk.commonservice.util.MethodNameUtil;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc 服务工厂类，负责创建服务，向调用者返回服务实例，服务发现机制使用JDK的SPI机制
 */
public class GeneralServiceFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralServiceFactory.class);
    private static final Map<Class<? extends GeneralService>, GeneralService> services = new HashMap<>();
    private static ServiceLoader<GeneralService> servicesLoader;
    private static boolean stats = Boolean.parseBoolean(ServiceProperties.getProperty("service.stats"));
    private static int slowlogMillis = Integer.parseInt(ServiceProperties.getProperty("service.slowlog.millis"));

    private GeneralServiceFactory() {
    }

    static {
        loadServices();
        initServices();
    }

    private static void loadServices() {
        servicesLoader = ServiceLoader.load(GeneralService.class);
        servicesLoader.forEach(service -> {
            Class<? extends GeneralService> serviceClazz = service.getClass();
            if (services.containsKey(serviceClazz)) {
                LOGGER.info("Ignored duplicate service: {}", serviceClazz);
            } else {
                LOGGER.info("Loading service {}", serviceClazz);
                if (stats) {
                    services.put(serviceClazz, proxyService(service));
                } else {
                    services.put(serviceClazz, service);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <T extends GeneralService> T proxyService(final T service) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(service.getClass());
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            long start = System.currentTimeMillis();
            Object res = null;
            String key = MethodNameUtil.methodNameWithClass(method);
            try {
                res = proxy.invokeSuper(obj, args);
            } catch (Exception rme){
                LOGGER.warn("Method:{}, args:{}", key, args);
                throw new ServiceBusinessException(rme.getMessage());
            } finally {
                long elapsed = System.currentTimeMillis() - start;
                if (elapsed > slowlogMillis) {
                    LOGGER.info("[SLOW]Method: {}, args: {}, res: {}, elapsed: {}", key, args, res, elapsed);
                } else {
                    LOGGER.debug("Method: {}, args: {}, res: {}, elapsed: {}", key, args, res, elapsed);
                }
            }
            return res;
        });
        return (T) enhancer.create();
    }

    private static void initServices() {
        services.entrySet().stream()
                .filter(entry -> (entry.getValue() instanceof InitializableService))
                .forEach(entry -> ((InitializableService) entry.getValue()).init());
    }

    public static Map<Class<? extends GeneralService>, GeneralService> getServices() {
        return Collections.unmodifiableMap(services);
    }

    @SuppressWarnings("unchecked")
    public static <T extends GeneralService> T getService(Class<T> clazz) {
        if (services.containsKey(clazz))
            return (T) services.get(clazz);
        else
            throw new ServiceNotFoundException("Service " + clazz + " not found.");
    }
}
