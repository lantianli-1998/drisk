package com.roy.drisk.exception;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc 规则未找到
 */
public class RuleNotFoundException extends EngineException {
    private static final String MESSAGE_CODE = MessageCodeConstants.RULE_NOT_FOUND;

    public RuleNotFoundException() {
        super(MESSAGE_CODE);
    }

    public RuleNotFoundException(String message) {
        super(MESSAGE_CODE, message);
    }

    public RuleNotFoundException(String message, Throwable cause) {
        super(MESSAGE_CODE, message, cause);
    }

    public RuleNotFoundException(Throwable cause) {
        super(MESSAGE_CODE, cause);
    }

    public RuleNotFoundException(String message, Throwable cause,
                                 boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE_CODE, message, cause, enableSuppression, writableStackTrace);
    }
}
