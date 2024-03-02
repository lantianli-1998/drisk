package com.roy.drisk.commonservice.exception;

import com.roy.drisk.exception.MessageCodeConstants;

/**
 * @author roy
 * @date 2021/10/27
 * @desc service不存在
 */
public class ServiceNotFoundException extends ServiceSystemException {
    private static final String MESSAGE_CODE = MessageCodeConstants.SERVICE_NOT_FOUND_ERROR;

    public ServiceNotFoundException() {
        super(MESSAGE_CODE);
    }

    public ServiceNotFoundException(String message) {
        super(MESSAGE_CODE, message);
    }

    public ServiceNotFoundException(String message, Throwable cause) {
        super(MESSAGE_CODE, message, cause);
    }

    public ServiceNotFoundException(Throwable cause) {
        super(MESSAGE_CODE, cause);
    }

    public ServiceNotFoundException(String message, Throwable cause,
                                    boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE_CODE, message, cause, enableSuppression, writableStackTrace);
    }
}
