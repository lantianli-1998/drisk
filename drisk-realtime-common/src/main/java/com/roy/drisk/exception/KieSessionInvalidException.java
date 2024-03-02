package com.roy.drisk.exception;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc Kie Session不可用
 */
public class KieSessionInvalidException extends EngineException {
    private static final String MESSAGE_CODE = MessageCodeConstants.KIE_SESSION_INVALID;

    public KieSessionInvalidException() {
        super(MESSAGE_CODE);
    }

    public KieSessionInvalidException(String message) {
        super(MESSAGE_CODE, message);
    }

    public KieSessionInvalidException(String message, Throwable cause) {
        super(MESSAGE_CODE, message, cause);
    }

    public KieSessionInvalidException(Throwable cause) {
        super(MESSAGE_CODE, cause);
    }

    public KieSessionInvalidException(String message, Throwable cause,
                                      boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE_CODE, message, cause, enableSuppression, writableStackTrace);
    }
}
