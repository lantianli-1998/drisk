package com.roy.drisk.nrt.core.flow.services;

import com.roy.drisk.nrt.core.TxLog;
import com.roy.drisk.nrt.core.flow.anno.ChkTxLog;
import com.roy.drisk.nrt.core.flow.jobs.FlowJob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author lantianli
 * @date 2021/11/8
 * @desc
 */
public class FlowJobMethod {
    private ChkTxLog chkTxLog;
    private Method method;
    private Object flowJob;

    public FlowJobMethod(ChkTxLog chkTxLog, Method method, Object flowJob) {
        this.chkTxLog = chkTxLog;
        this.method = method;
        this.flowJob = flowJob;
    }

    public Boolean invoke(TxLog txlog) throws InvocationTargetException, IllegalAccessException {
        final Object invoke = method.invoke(flowJob,txlog);
        if(invoke instanceof Boolean){
            return (boolean)invoke;
        }
        return null;
    }

    public Object getFlowJob() {
        return flowJob;
    }

    public void setFlowJob(Object flowJob) {
        this.flowJob = flowJob;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public ChkTxLog getChkTxLog() {
        return chkTxLog;
    }

    public void setChkTxLog(ChkTxLog chkTxLog) {
        this.chkTxLog = chkTxLog;
    }
}
