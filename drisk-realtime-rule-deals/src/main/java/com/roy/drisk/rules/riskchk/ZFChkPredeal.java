package com.roy.drisk.rules.riskchk;

import com.roy.drisk.context.EngineContextIter;
import com.roy.drisk.message.ContextMessage;

import java.math.BigDecimal;

/**
 * @author roy
 * @date 2021/11/9
 * @desc 针对支付交易的前处理类
 */
public class ZFChkPredeal extends BaseChkPredeal implements RiskChkPredeals{
    @Override
    public int pre(EngineContextIter engineContextIter) {
        final ContextMessage message = engineContextIter.message();
        //需要添加日限额累计数据。
        addDayLimit(message);
        //添加日限额黑名单信息
        addDayLimitBlack(message);

//        message.addTempItem("CURRENT_MBL_DAY_SUMAMT",new BigDecimal("200.00"));
//        message.addTempItem("MBL_DAY_LIMIT",new BigDecimal("100.00"));
        return 0;
    }
}
