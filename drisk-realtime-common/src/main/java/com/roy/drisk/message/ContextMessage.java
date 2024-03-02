package com.roy.drisk.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc 规则引擎EngineContext使用的消息格式
 */
public class ContextMessage {
    private boolean isTest = false;
    private long startTime;
    private long endTime;
    private String baseName;
    private String sessionName;
    private String requestId;
    private String clientId = "";
    private String clientIp = "";
    private String userNo = "";
    private String mobileNo = "";
    private long duration = -1L;
    private int resultCode = ResultCodeConstants.INIT_ERROR_CODE;
    private String messageCode = "";
    private String errorMessage = "";
    private Map<String, Object> reqData = new HashMap<>();
    private Map<String, Object> rspData = new HashMap<>();
    private Map<String, Object> tempData = new HashMap<>();
    private MessageDataWalker dataWalker;

    public ContextMessage(Map<String, Object> reqData) {
        if (reqData != null) {
            this.reqData = reqData;
        }
        this.dataWalker = new MessageDataWalker(this.reqData, this.rspData, this.tempData);
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

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

    public Map<String, Object> getReqData() {
        return reqData;
    }

    public Object getReqItem(String key) {
        return reqData.get(key);
    }

    public void addReqItem(String key, Object value) {
        reqData.put(key, value);
    }

    public void addReqItems(Map<String, Object> data) {
        reqData.putAll(data);
    }

    public void clearReqData() {
        reqData.clear();
    }

    public Map<String, Object> getRspData() {
        return rspData;
    }

    public Object getRspItem(String key) {
        return rspData.get(key);
    }

    public void addRspItem(String key, Object value) {
        rspData.put(key, value);
    }

    public void addRspItems(Map<String, Object> data) {
        rspData.putAll(data);
    }

    public void clearRspData() {
        rspData.clear();
    }

    public Map<String, Object> getTempData() {
        return tempData;
    }

    public Object getTempItem(String key) {
        return tempData.get(key);
    }

    public void addTempItem(String key, Object value) {
        tempData.put(key, value);
    }

    public void addTempItems(Map<String, Object> data) {
        tempData.putAll(data);
    }

    public void clearTempData() {
        tempData.clear();
    }

    public MessageDataWalker walker() {
        return dataWalker;
    }

    @Override
    public String toString() {
        return "ContextMessage{" +
                "isTest=" + isTest +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", baseName='" + baseName + '\'' +
                ", sessionName='" + sessionName + '\'' +
                ", requestId='" + requestId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", userNo='" + userNo + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", duration=" + duration +
                ", resultCode=" + resultCode +
                ", messageCode='" + messageCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", reqData=" + reqData +
                ", rspData=" + rspData +
                ", tempData=" + tempData +
                '}';
    }
}
