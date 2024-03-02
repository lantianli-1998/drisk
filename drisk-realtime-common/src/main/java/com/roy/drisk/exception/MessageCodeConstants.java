package com.roy.drisk.exception;

/**
 * @author roy
 * @date 2021/10/27
 * @desc 系统所有的错误码
 */
public abstract class MessageCodeConstants {
    public static final String SYSTEM_SUCC = "RFK00000";
    public static final String SYSTEM_ERROR = "RFK99999";
    public static final String ENGINE_EXIT = "RFK99000";
    public static final String RULE_NOT_FOUND = "RFK99010";
    public static final String KIE_SESSION_INVALID = "RFK99011";
    public static final String DATABASE_INVALID = "RFK99012";
    public static final String UENV_KEY_INVALID = "RFK99013";
    public static final String UENV_DATA_INVALID = "RFK99014";
    public static final String SERVICE_NOT_FOUND_ERROR = "RFK99100";// 服务不存在
}
