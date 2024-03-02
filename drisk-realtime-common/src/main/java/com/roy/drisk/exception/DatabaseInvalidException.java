package com.roy.drisk.exception;


/**
 * @author lantianli
 * @date 2023/10/27
 * @desc 数据源不可用
 */
public class DatabaseInvalidException extends EngineException {
    private static final String MESSAGE_CODE = MessageCodeConstants.DATABASE_INVALID;

    public DatabaseInvalidException() {
        super(MESSAGE_CODE);
    }

    public DatabaseInvalidException(String message) {
        super(MESSAGE_CODE, message);
    }

    public DatabaseInvalidException(String message, Throwable cause) {
        super(MESSAGE_CODE, message, cause);
    }

    public DatabaseInvalidException(Throwable cause) {
        super(MESSAGE_CODE, cause);
    }

    public DatabaseInvalidException(String message, Throwable cause,
                                    boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE_CODE, message, cause, enableSuppression, writableStackTrace);
    }
}
