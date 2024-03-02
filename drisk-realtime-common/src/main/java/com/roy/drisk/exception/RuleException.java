package com.roy.drisk.exception;

/**
 * @author roy
 * @date 2021/10/27
 * @desc 规则公共异常
 */
public class RuleException extends EngineException {
    public RuleException(String messageCode) {
        super(messageCode);
    }

    public RuleException(String messageCode, String message) {
        super(messageCode, message);
    }

    public RuleException(String messageCode, String message, Throwable cause) {
        super(messageCode, message, cause);
    }

    public RuleException(String messageCode, Throwable cause) {
        super(messageCode, cause);
    }

    public RuleException(String messageCode, String message, Throwable cause,
                         boolean enableSuppression, boolean writableStackTrace) {
        super(messageCode, message, cause, enableSuppression, writableStackTrace);
    }
}
