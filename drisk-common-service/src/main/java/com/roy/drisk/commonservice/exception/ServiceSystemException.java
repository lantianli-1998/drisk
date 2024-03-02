package com.roy.drisk.commonservice.exception;

/**
 * Service系统异常
 *
 * @author qin_chen
 */

/**
 * @author roy
 * @date 2021/10/27
 * @desc service系统异常
 */
public class ServiceSystemException extends ServiceException {
    public ServiceSystemException(String messageCode) {
        super(messageCode);
    }

    public ServiceSystemException(String messageCode, String message) {
        super(messageCode, message);
    }

    public ServiceSystemException(String messageCode, String message, Throwable cause) {
        super(messageCode, message, cause);
    }

    public ServiceSystemException(String messageCode, Throwable cause) {
        super(messageCode, cause);
    }

    public ServiceSystemException(String messageCode, String message, Throwable cause,
                                  boolean enableSuppression, boolean writableStackTrace) {
        super(messageCode, message, cause, enableSuppression, writableStackTrace);
    }
}
