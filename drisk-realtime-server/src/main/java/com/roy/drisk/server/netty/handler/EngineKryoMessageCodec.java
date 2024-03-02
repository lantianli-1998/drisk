package com.roy.drisk.server.netty.handler;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;
import io.netty.channel.ChannelHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 * 解析消息报文体，支持Kryo格式
 */
@ChannelHandler.Sharable
@Component
@ConditionalOnClass(name = "com.esotericsoftware.kryo.Kryo")
public class EngineKryoMessageCodec extends AbstractEngineMessageCodec {
    private KryoPool pool;

    public EngineKryoMessageCodec() {
        KryoFactory factory = () -> {
            Kryo kryo = new Kryo();
            kryo.register(RequestMessage.class);
            kryo.register(ResponseMessage.class);
            return kryo;
        };
        this.pool = new KryoPool.Builder(factory).softReferences().build();
    }

    @Override
    protected RequestMessage doDecode(byte[] bytes) throws Exception {
        Kryo kryo = pool.borrow();
        try (Input input = new Input(new ByteArrayInputStream(bytes))) {
            return kryo.readObject(input, RequestMessage.class);
        } finally {
            pool.release(kryo);
        }
    }

    @Override
    protected byte[] doEncode(ResponseMessage msg) throws Exception {
        Kryo kryo = pool.borrow();
        try (Output output = new Output(new ByteArrayOutputStream())) {
            kryo.writeObject(output, msg);
            return output.toBytes();
        } finally {
            pool.release(kryo);
        }
    }
}
