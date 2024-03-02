package com.roy.drisk.commonservice.exception;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc service 异常
 */
public class ServiceException extends RuntimeException {
    private String messageCode = "";

    public ServiceException(String messageCode) {
        this.messageCode = messageCode;
    }

    public ServiceException(String messageCode, String message) {
        super(message);
        this.messageCode = messageCode;
    }

    public ServiceException(String messageCode, String message, Throwable cause) {
        super(message, cause);
        this.messageCode = messageCode;
    }

    public ServiceException(String messageCode, Throwable cause) {
        super(cause);
        this.messageCode = messageCode;
    }

    public ServiceException(String messageCode, String message, Throwable cause,
                            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.messageCode = messageCode;
    }

    public String getMessageCode() {
        return messageCode;
    }
}
