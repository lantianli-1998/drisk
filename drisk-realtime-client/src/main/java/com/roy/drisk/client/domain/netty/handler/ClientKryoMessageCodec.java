package com.roy.drisk.client.domain.netty.handler;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;
import io.netty.channel.ChannelHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author roy
 * @date 2021/10/26
 * @desc 解析消息报文体，支持Kryo格式
 */

@ChannelHandler.Sharable
public class ClientKryoMessageCodec extends AbstractClientMessageCodec {
    private KryoPool pool;

    public ClientKryoMessageCodec() {
        KryoFactory factory = new KryoFactory() {
            @Override
            public Kryo create() {
                Kryo kryo = new Kryo();
                kryo.register(RequestMessage.class);
                kryo.register(ResponseMessage.class);
                return kryo;
            }
        };
        this.pool = new KryoPool.Builder(factory).softReferences().build();
    }

    @Override
    protected ResponseMessage doDecode(byte[] bytes) throws Exception {
        Kryo kryo = pool.borrow();
        Input input = null;
        try {
            input = new Input(new ByteArrayInputStream(bytes));
            return kryo.readObject(input, ResponseMessage.class);
        } finally {
            if (input != null) {
                input.close();
            }
            pool.release(kryo);
        }
    }

    @Override
    protected byte[] doEncode(RequestMessage msg) throws Exception {
        Kryo kryo = pool.borrow();
        Output output = null;
        try {
            output = new Output(new ByteArrayOutputStream());
            kryo.writeObject(output, msg);
            return output.toBytes();
        } finally {
            if (output != null) {
                output.close();
            }
            pool.release(kryo);
        }
    }
}
