package com.roy.drisk.commonservice.exception;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc Service业务异常
 */
public class ServiceBusinessException extends ServiceException {
    public ServiceBusinessException(String messageCode) {
        super(messageCode);
    }

    public ServiceBusinessException(String messageCode, String message) {
        super(messageCode, message);
    }

    public ServiceBusinessException(String messageCode, String message, Throwable cause) {
        super(messageCode, message, cause);
    }

    public ServiceBusinessException(String messageCode, Throwable cause) {
        super(messageCode, cause);
    }

    public ServiceBusinessException(String messageCode, String message, Throwable cause,
                                    boolean enableSuppression, boolean writableStackTrace) {
        super(messageCode, message, cause, enableSuppression, writableStackTrace);
    }
}
