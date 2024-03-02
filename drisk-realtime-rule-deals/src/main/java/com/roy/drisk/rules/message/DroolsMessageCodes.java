package com.roy.drisk.rules.message;

/**
 * @author lantianli
 * @date 2021/11/7
 * @desc
 */
public class DroolsMessageCodes {
    public static final String SUCC_CODE = "RFK00000";// 交易成功
    public static final String MBL_NO_REQUIRE = "RFK10001";//缺少BSINF.MBL_NO
    public static final String TX_DT_REQUIRE = "RFK10002";//缺少BSINF.TX_DT
    public static final String BUS_CNL_REQUIRE = "RFK10003";//缺少BSINF.BUS_CNL
    public static final String TX_TYP_REQUIRE = "RFK10004";//缺少BSINF.TX_TYP
    public static final String TX_AMT_REQUIRE = "RFK10005";//缺少BSINF.TX_DT


    public static final String MBL_IN_BLACK = "RFK2101"; //手机号在外部黑名单中
    public static final String MBL_LOGIN_FAILED = "RFK2103";//手机号连续登陆失败

    public static final String MBL_DAY_LIMIT_EXCEED = "RFK2002"; //手机号日限额超出
    public static final String MBL_IN_TRANSFER_BLACK = "RFK2003"; //手机号触发LJ002规则
}
