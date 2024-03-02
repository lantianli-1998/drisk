package com.roy.drisk.exception;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc Engine公共异常
 */
public class EngineException extends RuntimeException {
    private String messageCode = "";

    public EngineException(String messageCode) {
        this.messageCode = messageCode;
    }

    public EngineException(String messageCode, String message) {
        super(message);
        this.messageCode = messageCode;
    }

    public EngineException(String messageCode, String message, Throwable cause) {
        super(message, cause);
        this.messageCode = messageCode;
    }

    public EngineException(String messageCode, Throwable cause) {
        super(cause);
        this.messageCode = messageCode;
    }

    public EngineException(String messageCode, String message, Throwable cause,
                           boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.messageCode = messageCode;
    }

    public String getMessageCode() {
        return messageCode;
    }
}
