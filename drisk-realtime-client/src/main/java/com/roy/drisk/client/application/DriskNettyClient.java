package com.roy.drisk.client.application;

import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;

import java.io.Closeable;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc 往实时风控发送报文检查的客户端
 */
public interface DriskNettyClient extends Closeable {

    ResponseMessage send(final RequestMessage message, final int waitMilliSeconds) throws Throwable;

    ResponseMessage send(final RequestMessage message) throws Throwable;
}
