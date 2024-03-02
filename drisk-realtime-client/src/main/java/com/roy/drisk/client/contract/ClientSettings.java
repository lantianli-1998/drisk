package com.roy.drisk.client.contract;

import com.roy.drisk.client.infrastructure.ServerAddressUtil;
import com.roy.drisk.message.MessageFormat;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author roy
 * @date 2021/10/26
 * @desc 客户端配置类
 */
public class ClientSettings {
    private int transportBossThreads;
    private boolean transportNativeEpoll;
    private List<InetSocketAddress> transportServers;
    private int transportConnectTimeoutMillis;
    private boolean transportTcpNoDelay;
    private boolean transportSoKeepAlive;
    private boolean transportSoReuseAddress;
    private int transportSoBacklog;
    private int transportSoSendBufferSize;
    private int transportSoReceiveBufferSize;
    private long transportPoolAcquireTimeoutMillis;
    private int transportPoolMaxConnections;
    private int transportPoolMaxPendingAcquires;
    private int transportFetchWaitMilliSeconds;

    private MessageMockType messageMockType;
    private int messageVersion;
    private MessageFormat messageFormat;
    private int messagePreLength;
    private Charset messageEncoding;

    private String kafkaBootstrapServers;
    private String kafkaAcks;
    private String kafkaBufferMemory;
    private String kafkaRetries;
    private String kafkaBatchSize;
    private String kafkaClientId;
    private String kafkaConnectionsMaxIdleMs;
    private String kafkaLingerMs;
    private String kafkaMaxBlockMs;
    private String kafkaMaxRequestSize;
    private String kafkaReceiveBufferBytes;
    private String kafkaRequestTimeoutMs;
    private String kafkaSendBufferBytes;
    private String kafkaMaxInFlightRequestsPerConnection;
    private String kafkaMetadataMaxAgeMs;
    private String kafkaReconnectBackoffMs;
    private String kafkaRetryBackoffMs;
    private String kafkaDefaultTopic;


    public String getKafkaDefaultTopic() {
        return kafkaDefaultTopic;
    }

    public void setKafkaDefaultTopic(String kafkaDefaultTopic) {
        this.kafkaDefaultTopic = kafkaDefaultTopic;
    }

    public int getTransportBossThreads() {
        return transportBossThreads;
    }

    public void setTransportBossThreads(String transportBossThreads) {
        this.transportBossThreads = Integer.parseInt(transportBossThreads);
    }

    public boolean isTransportNativeEpoll() {
        boolean isLinux = System.getProperty("os.name").toLowerCase().startsWith("linux");
        return isLinux && transportNativeEpoll;
    }

    public void setTransportNativeEpoll(String transportNativeEpoll) {
        this.transportNativeEpoll = Boolean.parseBoolean(transportNativeEpoll);
    }

    public List<InetSocketAddress> getTransportServers() {
        return transportServers;
    }

    public void setTransportServers(String transportServers) {
        this.transportServers = ServerAddressUtil.parseLine(transportServers);
    }

    public int getTransportConnectTimeoutMillis() {
        return transportConnectTimeoutMillis;
    }

    public void setTransportConnectTimeoutMillis(String transportConnectTimeoutMillis) {
        this.transportConnectTimeoutMillis = Integer.parseInt(transportConnectTimeoutMillis);
    }

    public boolean isTransportTcpNoDelay() {
        return transportTcpNoDelay;
    }

    public void setTransportTcpNoDelay(String transportTcpNoDelay) {
        this.transportTcpNoDelay = Boolean.parseBoolean(transportTcpNoDelay);
    }

    public boolean isTransportSoKeepAlive() {
        return transportSoKeepAlive;
    }

    public void setTransportSoKeepAlive(String transportSoKeepAlive) {
        this.transportSoKeepAlive = Boolean.parseBoolean(transportSoKeepAlive);
    }

    public boolean isTransportSoReuseAddress() {
        return transportSoReuseAddress;
    }

    public void setTransportSoReuseAddress(String transportSoReuseAddress) {
        this.transportSoReuseAddress = Boolean.parseBoolean(transportSoReuseAddress);
    }

    public int getTransportSoBacklog() {
        return transportSoBacklog;
    }

    public void setTransportSoBacklog(String transportSoBacklog) {
        this.transportSoBacklog = Integer.parseInt(transportSoBacklog);
    }

    public int getTransportSoSendBufferSize() {
        return transportSoSendBufferSize;
    }

    public void setTransportSoSendBufferSize(String transportSoSendBufferSize) {
        this.transportSoSendBufferSize = Integer.parseInt(transportSoSendBufferSize);
    }

    public int getTransportSoReceiveBufferSize() {
        return transportSoReceiveBufferSize;
    }

    public void setTransportSoReceiveBufferSize(String transportSoReceiveBufferSize) {
        this.transportSoReceiveBufferSize = Integer.parseInt(transportSoReceiveBufferSize);
    }

    public long getTransportPoolAcquireTimeoutMillis() {
        return transportPoolAcquireTimeoutMillis;
    }

    public void setTransportPoolAcquireTimeoutMillis(String transportPoolAcquireTimeoutMillis) {
        this.transportPoolAcquireTimeoutMillis = Long.parseLong(transportPoolAcquireTimeoutMillis);
    }

    public int getTransportPoolMaxConnections() {
        return transportPoolMaxConnections;
    }

    public void setTransportPoolMaxConnections(String transportPoolMaxConnections) {
        this.transportPoolMaxConnections = Integer.parseInt(transportPoolMaxConnections);
    }

    public int getTransportPoolMaxPendingAcquires() {
        return transportPoolMaxPendingAcquires;
    }

    public void setTransportPoolMaxPendingAcquires(String transportPoolMaxPendingAcquires) {
        this.transportPoolMaxPendingAcquires = Integer.parseInt(transportPoolMaxPendingAcquires);
    }

    public int getTransportFetchWaitMilliSeconds() {
        return transportFetchWaitMilliSeconds;
    }

    public void setTransportFetchWaitMilliSeconds(String transportFetchWaitMilliSeconds) {
        this.transportFetchWaitMilliSeconds = Integer.parseInt(transportFetchWaitMilliSeconds);
    }

    public MessageMockType getMessageMockType() {
        return messageMockType;
    }

    public void setMessageMockType(String messageMockType) {
        this.messageMockType = MessageMockType.valueOf(messageMockType.toUpperCase());
    }

    public int getMessageVersion() {
        return messageVersion;
    }

    public void setMessageVersion(String messageVersion) {
        this.messageVersion = Integer.parseInt(messageVersion);
    }

    public MessageFormat getMessageFormat() {
        return messageFormat;
    }

    public void setMessageFormat(String messageFormat) {
        this.messageFormat = MessageFormat.valueOf(messageFormat.toUpperCase());
    }

    public int getMessagePreLength() {
        return messagePreLength;
    }

    public void setMessagePreLength(String messagePreLength) {
        this.messagePreLength = Integer.parseInt(messagePreLength);
    }

    public Charset getMessageEncoding() {
        return messageEncoding;
    }

    public void setMessageEncoding(String messageEncoding) {
        this.messageEncoding = Charset.forName(messageEncoding);
    }

    public String getKafkaBootstrapServers() {
        return kafkaBootstrapServers;
    }

    public void setKafkaBootstrapServers(String kafkaBootstrapServers) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;
    }

    public String getKafkaAcks() {
        return kafkaAcks;
    }

    public void setKafkaAcks(String kafkaAcks) {
        this.kafkaAcks = kafkaAcks;
    }

    public String getKafkaBufferMemory() {
        return kafkaBufferMemory;
    }

    public void setKafkaBufferMemory(String kafkaBufferMemory) {
        this.kafkaBufferMemory = kafkaBufferMemory;
    }

    public String getKafkaRetries() {
        return kafkaRetries;
    }

    public void setKafkaRetries(String kafkaRetries) {
        this.kafkaRetries = kafkaRetries;
    }

    public String getKafkaBatchSize() {
        return kafkaBatchSize;
    }

    public void setKafkaBatchSize(String kafkaBatchSize) {
        this.kafkaBatchSize = kafkaBatchSize;
    }

    public String getKafkaClientId() {
        return kafkaClientId;
    }

    public void setKafkaClientId(String kafkaClientId) {
        this.kafkaClientId = kafkaClientId;
    }

    public String getKafkaConnectionsMaxIdleMs() {
        return kafkaConnectionsMaxIdleMs;
    }

    public void setKafkaConnectionsMaxIdleMs(String kafkaConnectionsMaxIdleMs) {
        this.kafkaConnectionsMaxIdleMs = kafkaConnectionsMaxIdleMs;
    }

    public String getKafkaLingerMs() {
        return kafkaLingerMs;
    }

    public void setKafkaLingerMs(String kafkaLingerMs) {
        this.kafkaLingerMs = kafkaLingerMs;
    }

    public String getKafkaMaxBlockMs() {
        return kafkaMaxBlockMs;
    }

    public void setKafkaMaxBlockMs(String kafkaMaxBlockMs) {
        this.kafkaMaxBlockMs = kafkaMaxBlockMs;
    }

    public String getKafkaMaxRequestSize() {
        return kafkaMaxRequestSize;
    }

    public void setKafkaMaxRequestSize(String kafkaMaxRequestSize) {
        this.kafkaMaxRequestSize = kafkaMaxRequestSize;
    }

    public String getKafkaReceiveBufferBytes() {
        return kafkaReceiveBufferBytes;
    }

    public void setKafkaReceiveBufferBytes(String kafkaReceiveBufferBytes) {
        this.kafkaReceiveBufferBytes = kafkaReceiveBufferBytes;
    }

    public String getKafkaRequestTimeoutMs() {
        return kafkaRequestTimeoutMs;
    }

    public void setKafkaRequestTimeoutMs(String kafkaRequestTimeoutMs) {
        this.kafkaRequestTimeoutMs = kafkaRequestTimeoutMs;
    }

    public String getKafkaSendBufferBytes() {
        return kafkaSendBufferBytes;
    }

    public void setKafkaSendBufferBytes(String kafkaSendBufferBytes) {
        this.kafkaSendBufferBytes = kafkaSendBufferBytes;
    }

    public String getKafkaMaxInFlightRequestsPerConnection() {
        return kafkaMaxInFlightRequestsPerConnection;
    }

    public void setKafkaMaxInFlightRequestsPerConnection(String kafkaMaxInFlightRequestsPerConnection) {
        this.kafkaMaxInFlightRequestsPerConnection = kafkaMaxInFlightRequestsPerConnection;
    }

    public String getKafkaMetadataMaxAgeMs() {
        return kafkaMetadataMaxAgeMs;
    }

    public void setKafkaMetadataMaxAgeMs(String kafkaMetadataMaxAgeMs) {
        this.kafkaMetadataMaxAgeMs = kafkaMetadataMaxAgeMs;
    }

    public String getKafkaReconnectBackoffMs() {
        return kafkaReconnectBackoffMs;
    }

    public void setKafkaReconnectBackoffMs(String kafkaReconnectBackoffMs) {
        this.kafkaReconnectBackoffMs = kafkaReconnectBackoffMs;
    }

    public String getKafkaRetryBackoffMs() {
        return kafkaRetryBackoffMs;
    }

    public void setKafkaRetryBackoffMs(String kafkaRetryBackoffMs) {
        this.kafkaRetryBackoffMs = kafkaRetryBackoffMs;
    }

    @Override
    public String toString() {
        return "ClientSettings{" +
                "transportBossThreads=" + transportBossThreads +
                ", transportNativeEpoll=" + transportNativeEpoll +
                ", transportServers='" + transportServers + '\'' +
                ", transportTcpNoDelay=" + transportTcpNoDelay +
                ", transportSoKeepAlive=" + transportSoKeepAlive +
                ", transportSoReuseAddress=" + transportSoReuseAddress +
                ", transportSoBacklog=" + transportSoBacklog +
                ", transportSoSendBufferSize=" + transportSoSendBufferSize +
                ", transportSoReceiveBufferSize=" + transportSoReceiveBufferSize +
                ", messageMockType=" + messageMockType +
                ", messageVersion=" + messageVersion +
                ", messageFormat=" + messageFormat +
                ", messagePreLength=" + messagePreLength +
                ", messageEncoding=" + messageEncoding +
                ", kafkaBootstrapServers='" + kafkaBootstrapServers + '\'' +
                ", kafkaAcks='" + kafkaAcks + '\'' +
                ", kafkaBufferMemory='" + kafkaBufferMemory + '\'' +
                ", kafkaRetries='" + kafkaRetries + '\'' +
                ", kafkaBatchSize='" + kafkaBatchSize + '\'' +
                ", kafkaClientId='" + kafkaClientId + '\'' +
                ", kafkaConnectionsMaxIdleMs='" + kafkaConnectionsMaxIdleMs + '\'' +
                ", kafkaLingerMs='" + kafkaLingerMs + '\'' +
                ", kafkaMaxBlockMs='" + kafkaMaxBlockMs + '\'' +
                ", kafkaMaxRequestSize='" + kafkaMaxRequestSize + '\'' +
                ", kafkaReceiveBufferBytes='" + kafkaReceiveBufferBytes + '\'' +
                ", kafkaRequestTimeoutMs='" + kafkaRequestTimeoutMs + '\'' +
                ", kafkaSendBufferBytes='" + kafkaSendBufferBytes + '\'' +
                ", kafkaMaxInFlightRequestsPerConnection='" + kafkaMaxInFlightRequestsPerConnection + '\'' +
                ", kafkaMetadataMaxAgeMs='" + kafkaMetadataMaxAgeMs + '\'' +
                ", kafkaReconnectBackoffMs='" + kafkaReconnectBackoffMs + '\'' +
                ", kafkaRetryBackoffMs='" + kafkaRetryBackoffMs + '\'' +
                ", kafkaRetryBackoffMs='" + kafkaRetryBackoffMs + '\'' +
                '}';
    }
}
