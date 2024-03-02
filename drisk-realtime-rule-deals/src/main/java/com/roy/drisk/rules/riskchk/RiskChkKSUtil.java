package com.roy.drisk.rules.riskchk;

import com.roy.drisk.message.ContextMessage;
import com.roy.drisk.rules.message.DroolsConstants;
import com.roy.drisk.rules.message.DroolsMessageCodes;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * @author lantianli
 * @date 2021/11/7
 * @desc 触发规则后的公共处理方法。
 * RULE_TRIGGERED字段记录所有触发了的规则ID。
 * messageCode字段记录消息整体的处理结果。客户应用就可以根据这个字段来控制自己的业务逻辑。
 *  正常的处理逻辑会需要根据规则优先级做判断。给客户返回最严重的错误。
 *  这里做一个简化的逻辑，只记录messageCode编号最大的那一个错误码。
 */
public class RiskChkKSUtil {

    private static final String TRIGGERED_RULES="RULE_TRIGGERED";
    private static final String RESULT_CODE = "BUSI_CODE";

    public static void ruleresult(ContextMessage msg, String ruleid,String msgCd){
        Map<String, Object> tempData = msg.getTempData();
        //记录所有触发了的规则
        String oldValue = tempData.containsKey(TRIGGERED_RULES)?tempData.get(TRIGGERED_RULES).toString():"";
        msg.addTempItem(TRIGGERED_RULES, oldValue+","+ruleid);
        //简化逻辑：获取编号最大的错误码
        if(isExchangeCode(msgCd,msg.getMessageCode())){
            msg.setMessageCode(msgCd);
        }
    }

    //比较两个MessageCode. RFKxxx
    private static boolean isExchangeCode(String msgCd1, String msgCd2) {
        try{
            final int rfk1 = Integer.parseInt(msgCd1.replace("RFK", ""));
            final int rfk2 = Integer.parseInt(msgCd2.replace("RFK", ""));
            return (rfk1 - rfk2)>0;
        }catch (Exception e){
            return false;
        }


    }
}
