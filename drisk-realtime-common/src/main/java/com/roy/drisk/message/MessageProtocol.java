package com.roy.drisk.message;

import java.util.Arrays;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc 二进制协议
 */
public class MessageProtocol {
    private int version;
    private MessageFormat format;
    private byte[] msgData;

    public MessageProtocol(byte[] data) {
        if (data == null || data.length <= 3) {
            throw new IllegalArgumentException("Protocol data length not enough");
        }
        initVersionAndFormat(data[0], data[1]);
        this.msgData = Arrays.copyOfRange(data, 2, data.length);
    }

    public MessageProtocol(int version, int format, byte[] msgData) {
        if (msgData == null || msgData.length <= 0) {
            throw new IllegalArgumentException("Protocol msgData length not enough");
        }
        initVersionAndFormat(version, format);
        this.msgData = msgData;
    }

    private void initVersionAndFormat(int version, int format) {
        this.version = version;
        this.format = MessageFormat.byValue(format);
        if (this.format == null) {
            throw new IllegalArgumentException("Protocol format value error: " + format);
        }
    }

    public int getVersion() {
        return version;
    }

    public MessageFormat getFormat() {
        return format;
    }

    public byte[] getMsgData() {
        return msgData;
    }
}
