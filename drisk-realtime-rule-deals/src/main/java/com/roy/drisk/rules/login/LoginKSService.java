package com.roy.drisk.rules.login;

import com.roy.drisk.context.EngineContextIter;
import com.roy.drisk.message.ContextMessage;
import com.roy.drisk.rule.BeforeProcessRule;
import com.roy.drisk.rules.message.DroolsMessageCodes;
import com.roy.drisk.rules.riskchk.BaseChkPredeal;
import com.roy.drisk.rules.utils.MessageValidator;

/**
 * @author lantianli
 * @date 2021/11/5
 * @desc
 */
public class LoginKSService extends BaseChkPredeal implements BeforeProcessRule {
    @Override
    public void before(EngineContextIter engineContext) {
        ContextMessage message = engineContext.message();
        MessageValidator validator = new MessageValidator(message, true);
        validator.reqNotBlank("BSINF.MBL_NO", DroolsMessageCodes.MBL_NO_REQUIRE);
        //添加黑名单检查结果
        addMblBlackInfo(message);
        //添加连续登陆检查结果
        addMblLoginFailedInfo(message);
    }
}
