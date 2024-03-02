package com.roy.drisk.exception;

/**
 * @author roy
 * @date 2021/10/27
 * @desc 业务方法退出异常，此异常表示业务方法需要退出，Engine需要按业务正常处理
 */
public class EngineExitException extends EngineException {
    private static final String MESSAGE_CODE = MessageCodeConstants.ENGINE_EXIT;

    public EngineExitException() {
        super(MESSAGE_CODE);
    }

    public EngineExitException(String message) {
        super(MESSAGE_CODE, message);
    }

    public EngineExitException(String message, Throwable cause) {
        super(MESSAGE_CODE, message, cause);
    }

    public EngineExitException(Throwable cause) {
        super(MESSAGE_CODE, cause);
    }

    public EngineExitException(String message, Throwable cause,
                               boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE_CODE, message, cause, enableSuppression, writableStackTrace);
    }
}
