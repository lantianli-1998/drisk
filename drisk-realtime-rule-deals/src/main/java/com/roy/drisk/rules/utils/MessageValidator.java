package com.roy.drisk.rules.utils;

import com.roy.drisk.message.ContextMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class MessageValidator {
	private ContextMessage message;
	private boolean throwException;

	public ContextMessage getMessage() {
		return message;
	}

	public void setMessage(ContextMessage message) {
		this.message = message;
	}

	public boolean isThrowException() {
		return throwException;
	}

	public void setThrowException(boolean throwException) {
		this.throwException = throwException;
	}

	public MessageValidator(ContextMessage message) {
		this.message = message;
		this.throwException = true;

	}

	public MessageValidator(ContextMessage message, boolean throwException) {
		this.message = message;
		this.throwException = throwException;
	}

	public boolean reqNotBlank(String path, String errorMessage, String messageCode) {
		String value = message.walker().req(path);
		return ValidateUtil.notBlank(value, message, errorMessage, messageCode, throwException);
	}

	public boolean reqNotBlank(String path, String messageCode) {
		return reqNotBlank(path, "Request field " + path + " is blank", messageCode);
	}

	public boolean reqAnyNotBlank(String[] paths, String errorMessage, String messageCode) {
		String[] values = Arrays.stream(paths).map(path -> message.walker().req(path)).toArray(String[]::new);
		return ValidateUtil.anyNotBlank(values, message, errorMessage, messageCode, throwException);
	}

	public boolean reqAnyNotBlank(String[] paths, String messageCode) {
		return reqAnyNotBlank(paths, "Request fields " + Arrays.asList(paths) + " are all blank", messageCode);
	}

	public boolean reqAllNotBlank(String[] paths, String errorMessage, String messageCode) {
		String[] values = Arrays.stream(paths).map(path -> message.walker().req(path)).toArray(String[]::new);
		return ValidateUtil.allNotBlank(values, message, errorMessage, messageCode, throwException);
	}

	public boolean reqAllNotBlank(String[] paths, String messageCode) {
		return reqAllNotBlank(paths, "Request fields " + Arrays.asList(paths) + " has blank", messageCode);
	}

	public boolean reqIn(String path, String errorMessage, String messageCode, String[] expected) {
		String value = message.walker().req(path);
		return ValidateUtil.in(value, message, errorMessage, messageCode, throwException, expected);
	}

	public boolean reqIn(String path, String messageCode, String[] expected) {
		return reqIn(path, "Request field " + path + " is not valid, expected " + Arrays.asList(expected), messageCode, expected);
	}

	public boolean reqEq(String path, String errorMessage, String messageCode, String expected) {
		return ValidateUtil.eq(path, message, errorMessage, messageCode, throwException, expected);
	}

	public boolean reqEq(String path, String messageCode, String expected) {
		return reqEq(path, "Request field " + path + " is not valid, expected " + expected, messageCode, expected);
	}

	public boolean whenReqEquals(String whenPath, String expected) {
		if (StringUtils.equals(message.walker().req(whenPath), expected)) {
			return true;
		} else {
			return false;
		}
	}
}
