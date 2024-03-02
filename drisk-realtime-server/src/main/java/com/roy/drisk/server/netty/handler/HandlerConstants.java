package com.roy.drisk.server.netty.handler;

import com.roy.drisk.message.MessageFormat;
import io.netty.util.AttributeKey;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 * Netty handler常量
 */
public class HandlerConstants {
    public static final AttributeKey<Integer> VERSION_ATTR = AttributeKey.newInstance("MESSAGE_VERSION");
    public static final AttributeKey<MessageFormat> FORMAT_ATTR = AttributeKey.newInstance("MESSAGE_FORMAT");

    public static final String ENGINE_MESSAGE_CODEC = "engineMessageCodec";
}