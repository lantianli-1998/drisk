package com.roy.drisk.engine.util;


import com.roy.drisk.connector.redis.cache.CachedParameters;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public class RuleUtil {
    private static final String RISK_EFFECTIVE_SWITCH_KEY = "RiskEffectiveSwitch";
    private static final String RISK_INEFFECTIVE_RSP_KEY = "RiskIneffectiveRsp:";
    private static final String RULE_INACTIVE_SWITCH_KEY = "RuleInactiveSwitch";
    /**
     * 统一构造规则名
     *
     * @param baseName    Kie BaseName
     * @param sessionName Kie SessionName
     * @return 规则名的统一字符串表示
     */
    public static String buildRuleName(String baseName, String sessionName) {
        return baseName + ":" + sessionName;
    }

    /**
     * 统一构造服务名
     *
     * @param clazz 服务class
     * @return 服务名的统一字符串表示
     */
    public static String serviceName(Class clazz) {
        String str = clazz.getSimpleName();
        if (str.length() == 0) {
            return str;
        } else if (str.length() == 1) {
            return str.toLowerCase();
        }
        return String.valueOf(Character.toLowerCase(str.charAt(0))) + str.substring(1);
    }

    /**
     * 规则是否生效
     * @param baseName
     * @param sessionName
     * @return 是否生效
     */
    public static boolean isEffective(String baseName, String sessionName) {
        boolean isEffective = true;
        try {
            Map<String, String> params = CachedParameters.getHash(RISK_EFFECTIVE_SWITCH_KEY);
            if (params != null && !params.isEmpty()) {
                String isEffectiveStr = params.get(baseName + ":" + sessionName);
                isEffective = !"false".equalsIgnoreCase(isEffectiveStr);
            }
        } catch (Exception ignored) {
        }
        return isEffective;
    }

    /**
     * 不生效规则的返回数据
     * @param baseName
     * @param sessionName
     * @return 返回数据
     */
    public static Map<String, String> ineffectiveResponse(String baseName, String sessionName) {
        Map<String, String> rspData = CachedParameters.getHash(RISK_INEFFECTIVE_RSP_KEY
                + baseName + ":" + sessionName);
        if (rspData == null) {
            rspData = new HashMap<>();
        }
        return rspData;
    }

    /**
     * 规则是否生效
     * @param ruleName
     * @return 是否生效
     */
    public static boolean isRuleActive(String ruleName) {
        boolean isActive = true;
        try {
            Set<String> params = CachedParameters.getSet(RULE_INACTIVE_SWITCH_KEY);
            if (params != null && !params.isEmpty()) {
                isActive = !params.contains(ruleName);
            }
        } catch (Exception ignored) {
        }
        return isActive;
    }
}
