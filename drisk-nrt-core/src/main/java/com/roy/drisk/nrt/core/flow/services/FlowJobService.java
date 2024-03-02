package com.roy.drisk.nrt.core.flow.services;

import com.roy.drisk.nrt.core.TxLog;
import com.roy.drisk.nrt.core.flow.anno.ChkTxLog;
import com.roy.drisk.nrt.core.flow.jobs.FlowJob;
import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author roy
 * @date 2021/11/8
 * @desc
 */
public class FlowJobService{

    private static Logger logger  = LoggerFactory.getLogger(FlowJobService.class);

    private static List<FlowJobMethod> flowJobList = new ArrayList<>();

    static {
        init();
    }

    public static void init() {
        final Reflections reflections = new Reflections("com.roy.drisk.nrt.core.flow.jobs",new SubTypesScanner());
        for (Class<? extends FlowJob> flowJob : reflections.getSubTypesOf(FlowJob.class)) {
            try {
                final Method method = flowJob.getMethod("process", TxLog.class);
                final ChkTxLog chkTxLog = method.getAnnotation(ChkTxLog.class);
                if(chkTxLog.txTyp().length > 0 && chkTxLog.txRst().equals("ALL")){
                    //chkTxlog的txTyp和txRst不允许都不进行配置。
                    continue;
                }
                Class<?> clazz = method.getDeclaringClass();
                Object obj = getInstance(clazz);
                FlowJobMethod flowJobMethod = new FlowJobMethod(chkTxLog,method,obj);
                flowJobList.add(flowJobMethod);
            } catch (NoSuchMethodException e) {
                continue;
            }
        }
        flowJobList.stream().sorted((job1,job2)-> job1.getChkTxLog().order() - job2.getChkTxLog().order() );
    }

    /**
     * 流计算引擎进行消息处理的方法。
     * @param txLog
     * @return 返回值为true，将会进入后续的计算引擎中。返回值为false，会由flink作为错误消息存入到HBase中，待回查。
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Boolean process(TxLog txLog) throws InvocationTargetException, IllegalAccessException {
        for (FlowJobMethod flowJobMethod : flowJobList) {
            final ChkTxLog chkTxLog = flowJobMethod.getChkTxLog();

            if(isResponseable(chkTxLog,txLog)){
                return flowJobMethod.invoke(txLog);
            }
        }
        return true;
    }

    private static boolean isResponseable(ChkTxLog chkTxLog, TxLog txLog) {
        boolean checkByTxtyp = false;
        boolean checkByTxrst = false;
        final String[] chkTxtyps = chkTxLog.txTyp();
        for (String chkTxtyp : chkTxtyps) {
            if(StringUtils.equals(chkTxtyp,txLog.TX_TYP)){
                checkByTxtyp=true;
                break;
            }
        }

        final String chkTxRst = chkTxLog.txRst();
        if(StringUtils.equals(chkTxRst,"ALL")
        || StringUtils.equals(chkTxRst,txLog.TX_RST)){
            checkByTxrst = true;
        }

        return checkByTxrst && checkByTxtyp;
    }

    private static Map<Class, Object> services = new ConcurrentHashMap<>();

    public static  <T> T getInstance(Class<T> clazz) {
        if(null == services){
            services = new ConcurrentHashMap<>();
        }
        if (!services.containsKey(clazz)) {

            try {
                synchronized (clazz) {
                    if (!services.containsKey(clazz)) services.put(clazz, clazz.newInstance());
                }
            } catch (InstantiationException e) {
                logger.warn("实例化FlowJob执行对象错误, e:{}", e.getMessage());
            } catch (IllegalAccessException e) {
                logger.warn("FlowJob执行对象错误, e:{}", e.getMessage());
            }
        }

        return (T) services.get(clazz);
    }
}
