package com.roy.drisk.client.cli;

/**
 * User: QC
 * Date: 2016-07-15
 * Time: 09:12
 */
public class RunParam {
    private Type type;
    private String topic = "test";
    private String message = "RiskRule";
    private TCPMode tcpMode = TCPMode.TIME;
    private int threadsNum = Runtime.getRuntime().availableProcessors() * 2;
    private int seconds = 60;

    public RunParam(String type) {
        this.type = Type.valueOf(type.toUpperCase());
    }

    public Type getType() {
        return type;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TCPMode getTcpMode() {
        return tcpMode;
    }

    public void setTcpMode(String tcpMode) {
        this.tcpMode = TCPMode.valueOf(tcpMode.toUpperCase());
    }

    public int getThreadsNum() {
        return threadsNum;
    }

    public void setThreadsNum(String threadsNum) {
        this.threadsNum = Integer.parseInt(threadsNum);
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = Integer.parseInt(seconds);
    }

    public enum Type {
        MESSAGE, POOL, SIMPLE
    }

    public enum TCPMode {
        ONCE, TIME
    }

    @Override
    public String toString() {
        return "RunParam{" +
                "type=" + type +
                ", topic='" + topic + '\'' +
                ", message='" + message + '\'' +
                ", tcpMode=" + tcpMode +
                ", threadsNum=" + threadsNum +
                ", seconds=" + seconds +
                '}';
    }
}
