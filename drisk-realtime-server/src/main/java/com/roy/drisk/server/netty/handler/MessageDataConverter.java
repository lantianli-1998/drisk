package com.roy.drisk.server.netty.handler;

import com.roy.drisk.message.MessageFormat;
import com.roy.drisk.message.MessageProtocol;
import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 * TCP传输的二进制协议与Message对象转换辅助类
 */
@Component
public class MessageDataConverter {
    @Autowired
    private EngineJsonMessageCodec jsonCodec;
    @Autowired(required = false)
    private EngineKryoMessageCodec kryoCodec;

    public RequestMessage generateRequest(MessageProtocol protocol) throws Exception {
        switch (protocol.getFormat()) {
            case JSON:
                return jsonCodec.doDecode(protocol.getMsgData());
            case KRYO:
                return kryoCodec.doDecode(protocol.getMsgData());
            default:
                throw new IllegalArgumentException("Format not supported: " + protocol.getFormat());
        }
    }

    public byte[] generateResponse(int version, MessageFormat format, ResponseMessage msg) throws Exception {
        switch (format) {
            case JSON:
                return combineData(version, format, jsonCodec.doEncode(msg));
            case KRYO:
                return combineData(version, format, kryoCodec.doEncode(msg));
            default:
                throw new IllegalArgumentException("Format not supported: " + format);
        }
    }

    private byte[] combineData(int version, MessageFormat format, byte[] msgData) {
        byte[] data = new byte[msgData.length + 2];
        data[0] = (byte) version;
        data[1] = (byte) format.getValue();
        System.arraycopy(msgData, 0, data, 2, msgData.length);
        return data;
    }
}
