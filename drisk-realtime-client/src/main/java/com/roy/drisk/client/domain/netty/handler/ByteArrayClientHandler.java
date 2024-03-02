package com.roy.drisk.client.domain.netty.handler;

import com.roy.drisk.client.contract.ClientSettings;
import com.roy.drisk.message.MessageProtocol;
import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc
 */
public class ByteArrayClientHandler extends SimpleChannelInboundHandler<byte[]> implements MessageSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(ByteArrayClientHandler.class);
    private static final MessageDataConverter CONVERTER = new MessageDataConverter();
    private ClientSettings settings;
    private volatile Channel channel;
    private final Map<String, BlockingQueue<ResponseMessage>> messages = new HashMap<>();

    public ByteArrayClientHandler(ClientSettings settings) {
        this.settings = settings;
    }

    private ResponseMessage getResponseMessage(String key, int waitMilliSeconds) {
        boolean interrupted = false;
        try {
            for (; ; ) {
                try {
                    return messages.get(key).poll(waitMilliSeconds, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ignore) {
                    LOGGER.warn("Interrupted while polling ResponseMessage", ignore);
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        this.channel = ctx.channel();
    }

    @Override
    public ResponseMessage sendMessage(RequestMessage message, int waitMilliSeconds) throws Exception {
        messages.clear();
        messages.put(message.getRequestId(), new LinkedBlockingQueue<ResponseMessage>());
        byte[] data = CONVERTER.generateRequest(settings.getMessageVersion(), settings.getMessageFormat(), message);
        channel.writeAndFlush(data).sync();
        return getResponseMessage(message.getRequestId(), waitMilliSeconds);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] data) throws Exception {
        MessageProtocol protocol = new MessageProtocol(data);
        ResponseMessage msg = CONVERTER.generateResponse(protocol);
        BlockingQueue<ResponseMessage> queue = messages.get(msg.getRequestId());
        if (queue == null) {
            LOGGER.warn("Dropped received message: {} because requestId {} not in {}",
                    new String[]{msg.toString(), msg.getRequestId(), messages.keySet().toString()});
            return;
        }
        boolean res = queue.offer(msg);
        if (!res) {
            LOGGER.warn("Dropped received message: {} because message queue is full",
                    new String[]{msg.toString(), msg.getRequestId(), messages.keySet().toString()});
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
