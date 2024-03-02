package com.roy.drisk.server.netty.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc Message配置类，由spring从properties中加载以message为前缀的相关配置。
 */
@Component
@ConfigurationProperties(prefix = "message")
public class MessageSettings {
    /**
     * 报文前置长度
     */
    private int preLength;
    /**
     * 报文编码（预留，目前固定为UTF-8，由客户端负责转码）
     */
    private Charset encoding;

    public int getPreLength() {
        return preLength;
    }

    public void setPreLength(int preLength) {
        this.preLength = preLength;
    }

    public Charset getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = Charset.forName(encoding);
    }

    @Override
    public String toString() {
        return "MessageSettings{" +
                "preLength=" + preLength +
                ", encoding=" + encoding +
                '}';
    }
}
