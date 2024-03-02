package com.roy.drisk.message;

import java.io.Serializable;
import java.util.Map;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc 响应信息
 */
public class ResponseMessage extends CommonMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private long duration;
    private int resultCode;
    private String messageCode;
    private String errorMessage;
    private Map<String, Object> data;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "duration=" + duration +
                ", resultCode=" + resultCode +
                ", messageCode='" + messageCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", data=" + data +
                "} " + super.toString();
    }
}
