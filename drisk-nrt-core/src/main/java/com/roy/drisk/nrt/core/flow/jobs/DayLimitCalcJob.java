package com.roy.drisk.nrt.core.flow.jobs;

import com.roy.drisk.services.GeneralServiceFactory;
import com.roy.drisk.nrt.core.TxLog;
import com.roy.drisk.nrt.core.flow.anno.ChkTxLog;
import com.roy.drisk.services.limit.LimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lantianli
 * @date 2021/11/8
 * @desc 月度限额累计任务
 */
public class DayLimitCalcJob implements FlowJob{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //针对 交易=支付，结果=成功 的交易进行日消费额度累计，用于实时风控进行日限额判断。
    @Override
    @ChkTxLog(txTyp = "ZF",txRst = "0",order = 100)
    public boolean process(TxLog txLog) {
        logger.info("MonthLimitCalcJob start process");
        if(txLog.MBL_NO==null || "".equals(txLog.MBL_NO)){
            return false;
        }
        final LimitService limitService = GeneralServiceFactory.getService(LimitService.class);
        final Boolean res = limitService.updateMblDayLimitInRedis(txLog.MBL_NO, txLog.TX_DT, txLog.TX_AMT);
        return res;
    }
}
