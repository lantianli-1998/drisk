package com.roy.drisk.engine.handler.impl;

import com.roy.drisk.connector.http.HttpParam;
import com.roy.drisk.connector.http.HttpSyncClient;
import com.roy.drisk.connector.http.HttpUtil;
import com.roy.drisk.engine.context.EngineContext;
import com.roy.drisk.engine.handler.QueuedAttributeHandler;
import com.roy.drisk.engine.util.MDCUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Queue;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 * <code>HttpNotifier</code>用于处理Http通知事件，
 * 若收到的业务流程中需要在处理完成后发送Http通知，则可以使用{@link EngineContext#sendEvent(Object)}
 * 发送{@link HttpParam}类型的事件。
 */
@Component
@Order(900)
public class HttpNotifier extends QueuedAttributeHandler<HttpParam> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpNotifier.class);
    private static final String HANDLER_HTTPNOTIFY = "Handler:HttpNotify";
    @Autowired
    private ThreadPoolTaskExecutor engineExecutor;

    @Override
    public String getAttributeKey() {
        return HANDLER_HTTPNOTIFY;
    }

    @Override
    protected void doAfterProcess(final EngineContext context, Queue<HttpParam> values) {
        final String requestId = context.message().getRequestId();
        values.forEach(param -> {
            try {
                engineExecutor.execute(() -> {
                    MDCUtil.putMDCKey(requestId);
                    try (CloseableHttpClient client = buildClient(param)) {
                        HttpUtil.sync(new HttpSyncClient(client), param);
                    } catch (Exception e) {
                        LOGGER.error("HttpNotifierException " + param, e);
                    }
                    MDCUtil.removeMDCKey();
                });
            } catch (TaskRejectedException e) {
                LOGGER.error("HttpNotifier rejected: {}, {}", context.message(), param);
            }
        });
    }

    private CloseableHttpClient buildClient(HttpParam param) {
        return HttpClients.custom()
                .setRetryHandler(new StandardHttpRequestRetryHandler(param.getRetry(), false))
                .build();
    }
}
