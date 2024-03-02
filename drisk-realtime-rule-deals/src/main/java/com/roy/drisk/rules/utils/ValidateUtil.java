package com.roy.drisk.rules.utils;

import com.roy.drisk.exception.EngineExitException;
import com.roy.drisk.message.ContextMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author lantianli
 * @date 2021/11/8
 * @desc 消息字段检查工具类
 */
public class ValidateUtil {
    private static boolean checkFailed(ContextMessage message, String errorMessage,
                                       String messageCode, boolean throwException) {
        message.setErrorMessage(errorMessage);
        message.setMessageCode(messageCode);
        if (throwException)
            throw new EngineExitException(errorMessage);
        return false;
    }

    public static boolean shouldTrue(boolean value, ContextMessage message, String errorMessage,
                                     String messageCode, boolean throwException) {
        if (!value) {
            checkFailed(message, errorMessage, messageCode, throwException);
        }
        return true;
    }

    public static boolean notNull(Object value, ContextMessage message, String errorMessage,
                                  String messageCode, boolean throwException) {
        if (value == null) {
            checkFailed(message, errorMessage, messageCode, throwException);
        }
        return true;
    }

    public static boolean notBlank(String value, ContextMessage message, String errorMessage,
                                   String messageCode, boolean throwException) {
        if (StringUtils.isBlank(value)) {
            checkFailed(message, errorMessage, messageCode, throwException);
        }
        return true;
    }

    public static boolean anyNotBlank(String[] values, ContextMessage message, String errorMessage,
                                      String messageCode, boolean throwException) {
        boolean allBlank = true;
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) {
                allBlank = false;
                break;
            }
        }
        if (allBlank) {
            checkFailed(message, errorMessage, messageCode, throwException);
        }
        return true;
    }

    public static boolean allNotBlank(String[] values, ContextMessage message, String errorMessage,
                                      String messageCode, boolean throwException) {
        if (StringUtils.isAnyBlank(values)) {
            checkFailed(message, errorMessage, messageCode, throwException);
        }
        return true;
    }

    public static boolean in(String value, ContextMessage message, String errorMessage,
                             String messageCode, boolean throwException, String[] expected) {
        if (expected == null || expected.length == 0) {
            return true;
        }
        if (StringUtils.isBlank(value) || !Arrays.asList(expected).contains(value)) {
            checkFailed(message, errorMessage, messageCode, throwException);
        }
        return true;
    }

    public static boolean eq(String value, ContextMessage message, String errorMessage,
                             String messageCode, boolean throwException, String expected) {
        return in(value, message, errorMessage, messageCode, throwException, new String[]{expected});
    }
}
