package com.roy.drisk.rules.riskchk;

import com.roy.drisk.context.EngineContextIter;
import com.roy.drisk.exception.EngineExitException;
import com.roy.drisk.message.ContextMessage;
import com.roy.drisk.rule.BeforeProcessRule;
import com.roy.drisk.rules.message.DroolsConstants;
import com.roy.drisk.rules.message.DroolsMessageCodes;
import com.roy.drisk.rules.utils.MessageValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author roy
 * @date 2021/11/5
 * @desc
 */
public class RiskChkKSService implements BeforeProcessRule {

    private Map<String, RiskChkPredeals> predeals;

    public RiskChkKSService() {
        predeals = new HashMap<>();
        predeals.put(DroolsConstants.TX_TYP_ZF, new ZFChkPredeal());
    }

    @Override
    public void before(EngineContextIter engineContext) {
        ContextMessage message = engineContext.message();
        //公共必输项检测
        MessageValidator validator = new MessageValidator(message, true);
        validator.reqNotBlank("BSINF.MBL_NO", DroolsMessageCodes.MBL_NO_REQUIRE);
        validator.reqNotBlank("BSINF.TX_TYP", DroolsMessageCodes.TX_TYP_REQUIRE);
        validator.reqNotBlank("BSINF.BUS_CNL", DroolsMessageCodes.BUS_CNL_REQUIRE);
        validator.reqNotBlank("BSINF.TX_AMT", DroolsMessageCodes.TX_AMT_REQUIRE);
        validator.reqNotBlank("BSINF.TX_DT", DroolsMessageCodes.TX_DT_REQUIRE);
        //手机号黑名单检测


        //调用对应交易的前处理方
        final String txtyp = message.walker().req("BSINF.TX_TYP");
        final int result = predeals.get(txtyp).pre(engineContext);
        if (result != 0) {
            throw new EngineExitException();
        }
    }
}
