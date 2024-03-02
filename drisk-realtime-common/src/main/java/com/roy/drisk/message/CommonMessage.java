package com.roy.drisk.message;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc 公共消息头
 */
public abstract class CommonMessage {
    private String baseName;
    private String sessionName;
    private String requestId;
    private String clientId;
    private String clientIp;
    private String userNo;
    private String mobileNo;

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

    @Override
    public String toString() {
        return "CommonMessage{" +
                "baseName='" + baseName + '\'' +
                ", sessionName='" + sessionName + '\'' +
                ", requestId='" + requestId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", userNo='" + userNo + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                '}';
    }
}
