package com.roy.drisk.rules.riskchk;

import com.roy.drisk.services.GeneralServiceFactory;
import com.roy.drisk.message.ContextMessage;
import com.roy.drisk.services.blackMbl.BlackMblService;
import com.roy.drisk.services.login.FailedLoginMblService;
import com.roy.drisk.services.limit.LimitService;
import com.roy.drisk.services.ParamService;

import java.math.BigDecimal;

/**
 * @author lantianli
 * @date 2021/11/9
 * @desc
 */
public class BaseChkPredeal {

    protected void addDayLimit(ContextMessage message){
        final String mblNo = message.walker().req("BSINF.MBL_NO");
        final String txDt = message.walker().req("BSINF.TX_DT");
        final String txAmt = message.walker().req("BSINF.TX_AMT");
        final LimitService service = GeneralServiceFactory.getService(LimitService.class);
        final BigDecimal oldDayLimit = service.queryMblDayLimit(mblNo, txDt);
        final BigDecimal currentSum = oldDayLimit.add(new BigDecimal(txAmt));
        //当前日交易累计额
        message.addTempItem("CURRENT_MBL_DAY_SUMAMT", currentSum);

        final ParamService paramService = GeneralServiceFactory.getService(ParamService.class);
        final BigDecimal mblDayLimit = paramService.getMblDayLimit();
        //设定的日限额
        message.addTempItem("MBL_DAY_AMT_LIMIT",mblDayLimit);
    }

    protected void addDayLimitBlack(ContextMessage message){
        final String mblNo = message.walker().req("BSINF.MBL_NO");
        final String txDt = message.walker().req("BSINF.TX_DT");
        final BlackMblService blackMblService = GeneralServiceFactory.getService(BlackMblService.class);
        final boolean isMblInDayLimitBlack = blackMblService.isMblInDayLimtBlack(mblNo);

        message.addTempItem("IS_MBL_IN_DAYLIMIT_BLACK",isMblInDayLimitBlack);
    }

    protected void addMblBlackInfo(ContextMessage message) {
        final String mblNo = message.walker().req("BSINF.MBL_NO");
        final BlackMblService blackMblService = GeneralServiceFactory.getService(BlackMblService.class);
        if(blackMblService.isMblInBlack(mblNo)){
            message.addTempItem("MBL_IN_BLACK",true);
        }else{
            message.addTempItem("MBL_IN_BLACK",false);
        }
    }

    protected void addMblLoginFailedInfo(ContextMessage message) {
        final String mblNo = message.walker().req("BSINF.MBL_NO");
        final FailedLoginMblService failedLoginMblService = GeneralServiceFactory.getService(FailedLoginMblService.class);
        if(failedLoginMblService.isMblLoginFailed(mblNo)){
            message.addTempItem("MBL_LOGIN_FAILED",true);
        }else{
            message.addTempItem("MBL_LOGIN_FAILED",false);
        }
    }
}
