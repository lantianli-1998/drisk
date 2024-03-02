package com.roy.drisk.nrt.core.flow.jobs;

import com.roy.drisk.nrt.core.TxLog;

/**
 * @author lantianli
 * @date 2021/11/8
 * @desc FlowJob执行接口。对txlog可以进行实时的修改。
 * 返回值为true表示处理成功，将会往下游处理引擎推送消息
 * 返回值为false表示处理失败，将会把日志存入HBase
 */
public interface FlowJob {

    boolean process(TxLog txLog);
}
