package com.roy.drisk.server.netty.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author roy
 * @date 2021/10/27
 * @desc Transport配置类，由spring从properties中加载以transport为前缀的相关配置。
 */
@Component
@ConfigurationProperties(prefix = "transport")
public class TransportSettings {
    /**
     * 分发请求使用的IO线程数
     */
    private int bossThreads;
    /**
     * 处理具体IO读写的线程数
     */
    private int workerThreads;
    /**
     * 处理handler业务逻辑使用的线程数
     */
    private int handlerThreads;
    /**
     * 是否使用native epoll（Linux推荐开启）
     */
    private boolean nativeEpoll;
    /**
     * 绑定IP
     */
    private String bindAddress;
    /**
     * 服务端口
     */
    private int port;
    private boolean tcpNoDelay;
    private boolean soKeepAlive;
    private boolean soReuseAddress;
    private int soBacklog;
    private int soSendBufferSize;
    private int soReceiveBufferSize;
    /**
     * IO读取操作的空闲时间，时间段内无数据读取操作则由服务端主动断开链接
     */
    private int readerIdleTimeSeconds;
    /**
     * 规则引擎实现名称
     */
    private String supportedEngine;

    public int getBossThreads() {
        return bossThreads;
    }

    public void setBossThreads(int bossThreads) {
        this.bossThreads = bossThreads;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public int getHandlerThreads() {
        return handlerThreads;
    }

    public void setHandlerThreads(int handlerThreads) {
        this.handlerThreads = handlerThreads;
    }

    public boolean isNativeEpoll() {
        boolean isLinux = System.getProperty("os.name").toLowerCase().startsWith("linux");
        return isLinux && nativeEpoll;
    }

    public void setNativeEpoll(boolean nativeEpoll) {
        this.nativeEpoll = nativeEpoll;
    }

    public String getBindAddress() {
        return bindAddress;
    }

    public void setBindAddress(String bindAddress) {
        this.bindAddress = bindAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public boolean isSoKeepAlive() {
        return soKeepAlive;
    }

    public void setSoKeepAlive(boolean soKeepAlive) {
        this.soKeepAlive = soKeepAlive;
    }

    public boolean isSoReuseAddress() {
        return soReuseAddress;
    }

    public void setSoReuseAddress(boolean soReuseAddress) {
        this.soReuseAddress = soReuseAddress;
    }

    public int getSoBacklog() {
        return soBacklog;
    }

    public void setSoBacklog(int soBacklog) {
        this.soBacklog = soBacklog;
    }

    public int getSoSendBufferSize() {
        return soSendBufferSize;
    }

    public void setSoSendBufferSize(int soSendBufferSize) {
        this.soSendBufferSize = soSendBufferSize;
    }

    public int getSoReceiveBufferSize() {
        return soReceiveBufferSize;
    }

    public void setSoReceiveBufferSize(int soReceiveBufferSize) {
        this.soReceiveBufferSize = soReceiveBufferSize;
    }

    public int getReaderIdleTimeSeconds() {
        return readerIdleTimeSeconds;
    }

    public void setReaderIdleTimeSeconds(int readerIdleTimeSeconds) {
        this.readerIdleTimeSeconds = readerIdleTimeSeconds;
    }

    public String getSupportedEngine() {
        return supportedEngine;
    }

    public void setSupportedEngine(String supportedEngine) {
        this.supportedEngine = supportedEngine;
    }

    @Override
    public String toString() {
        return "TransportSettings{" +
                "bossThreads=" + bossThreads +
                ", workerThreads=" + workerThreads +
                ", handlerThreads=" + handlerThreads +
                ", nativeEpoll=" + nativeEpoll +
                ", port=" + port +
                ", tcpNoDelay=" + tcpNoDelay +
                ", soKeepAlive=" + soKeepAlive +
                ", soReuseAddress=" + soReuseAddress +
                ", soBacklog=" + soBacklog +
                ", soSendBufferSize=" + soSendBufferSize +
                ", soReceiveBufferSize=" + soReceiveBufferSize +
                ", readerIdleTimeSeconds=" + readerIdleTimeSeconds +
                ", supportedEngine='" + supportedEngine + '\'' +
                '}';
    }
}
