package com.roy.drisk.engine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roy.drisk.engine.context.EngineContext;
import com.roy.drisk.engine.context.EngineContextFactory;
import com.roy.drisk.engine.handler.ContextMessageCodec;
import com.roy.drisk.engine.handler.EngineContextHandler;
import com.roy.drisk.engine.util.MDCUtil;
import com.roy.drisk.engine.util.RuleUtil;
import com.roy.drisk.exception.EngineException;
import com.roy.drisk.exception.EngineExitException;
import com.roy.drisk.exception.MessageCodeConstants;
import com.roy.drisk.exception.RuleException;
import com.roy.drisk.message.ContextMessage;
import com.roy.drisk.message.RequestMessage;
import com.roy.drisk.message.ResponseMessage;
import com.roy.drisk.message.ResultCodeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc 规则引擎公共抽象
 */
public abstract class AbstractEngine implements Engine {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEngine.class);
    @Autowired
    private ContextMessageCodec contextMessageCodec;
    @Resource
    private List<EngineContextHandler> contextHandlers;
    @Autowired
    private EngineContextFactory contextFactory;
    @Autowired
    private ThreadPoolTaskExecutor engineExecutor;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 规则引擎业务处理方法
     *
     * @param requestMessage 请求消息
     * @return ResponseMessage 响应消息
     */
    public ResponseMessage process(RequestMessage requestMessage) {
        MDCUtil.putMDCKey(requestMessage.getRequestId());
        ContextMessage message = contextMessageCodec.decode(requestMessage);
        EngineContext context = contextFactory.newContext(message);
        boolean isEffective = RuleUtil.isEffective(requestMessage.getBaseName(), requestMessage.getSessionName());
        if (isEffective) {
            commonProcess(context);
        } else {
            LOGGER.info("Risk session {}:{} is ineffective, message: {}",
                    requestMessage.getBaseName(), requestMessage.getSessionName(), context.message());
            ineffectiveProcess(context, requestMessage);
        }
        ResponseMessage responseMessage = contextMessageCodec.encode(context.message());
        MDCUtil.removeMDCKey();
        return responseMessage;
    }

    private void ineffectiveProcess(EngineContext context, RequestMessage requestMessage) {
        ContextMessage message = context.message();
        message.setResultCode(ResultCodeConstants.SUCC_CODE);
        message.setMessageCode(MessageCodeConstants.SYSTEM_SUCC);
        message.setErrorMessage("");
        RuleUtil.ineffectiveResponse(message.getBaseName(), message.getSessionName())
                .forEach((k, v) -> context.message().addRspItem(k, v));
        engineExecutor.execute(() -> {
            MDCUtil.putMDCKey(requestMessage.getRequestId() + "I");
            try {
                ContextMessage newMessage = contextMessageCodec.decode(
                        objectMapper.readValue(objectMapper.writeValueAsString(requestMessage), RequestMessage.class));
                EngineContext newContext = contextFactory.newContext(newMessage);
                newContext.message().setTest(true);
                commonProcess(newContext);
            } catch (Exception e) {
                LOGGER.error("EngineException", e);
            } finally {
                MDCUtil.removeMDCKey();
            }
        });
    }

    private void commonProcess(EngineContext context) {
        try {
            try {
                LOGGER.trace("Engine before start: {}", context.message());
                handlerBeforeProcess(contextHandlers, context);
                LOGGER.trace("Engine process start: {}", context.message());
                doProcess(context);
            } finally {
                finishEngineContext(context);
            }
        } catch (Throwable t) {
            handleException(context, t);
        }
        LOGGER.trace("Engine after start: {}", context.message());
        after(context);
    }

    /**
     * 业务处理后对EngineContext的统一后处理，
     * 目前只设置结束时间和处理时间
     *
     * @param context 待处理的EngineContext
     */
    private void finishEngineContext(EngineContext context) {
        ContextMessage contextMessage = context.message();
        contextMessage.setEndTime(System.currentTimeMillis());
        contextMessage.setDuration(contextMessage.getEndTime() - contextMessage.getStartTime());
        contextMessage.setResultCode(ResultCodeConstants.SUCC_CODE);
    }

    /**
     * 按handlers顺序调用各handlers的前处理
     *
     * @param handlers EngineContextHandler列表
     * @param context  待处理的EngineContext
     */
    private void handlerBeforeProcess(List<EngineContextHandler> handlers, EngineContext context) {
        for (EngineContextHandler handler : handlers) {
            handler.before(context);
        }
    }

    /**
     * 按handlers逆序调用各handlers的后处理
     *
     * @param handlers EngineContextHandler列表
     * @param context  待处理的EngineContext
     */
    private void handlerAfterProcess(List<EngineContextHandler> handlers, EngineContext context) {
        for (int i = handlers.size() - 1; i >= 0; i--) {
            handlers.get(i).after(context);
        }
    }

    /**
     * Engine抛出的异常处理。<br/>
     * 如异常为<code>RuleException</code>，则设置返回码为成功，错误码和错误信息以异常中的为准<br/>
     * 如异常为<code>EngineExitException</code>，则设置返回码为成功，不设置错误码和错误信息，
     * 以异常业务抛出异常前的设置为准<br/>
     * 如异常为<code>EngineException</code>，则设置返回码为系统异常，错误码和错误信息以异常中的为准<br/>
     * 否则则设置返回码为系统异常，错误码和为系统错误，错误信息以异常中的为准<br/>
     *
     * @param context 待处理的EngineContext
     * @param t       待处理异常
     */
    private void handleException(EngineContext context, Throwable t) {
        LOGGER.trace("Engine exit with {}, message: {}", t, context.message());
        ContextMessage contextMessage = context.message();
        if (t instanceof RuleException) {
            RuleException e = (RuleException) t;
            contextMessage.setResultCode(ResultCodeConstants.SUCC_CODE);
            contextMessage.setMessageCode(e.getMessageCode());
            contextMessage.setErrorMessage(e.getMessage());
        } else if (t instanceof EngineExitException) {
            contextMessage.setResultCode(ResultCodeConstants.SUCC_CODE);
        } else if (t instanceof EngineException) {
            EngineException e = (EngineException) t;
            contextMessage.setResultCode(ResultCodeConstants.SYS_ERROR_CODE);
            contextMessage.setMessageCode(e.getMessageCode());
            contextMessage.setErrorMessage(e.getMessage());
        } else {
            contextMessage.setResultCode(ResultCodeConstants.SYS_ERROR_CODE);
            contextMessage.setMessageCode(MessageCodeConstants.SYSTEM_ERROR);
            contextMessage.setErrorMessage(t.getMessage());
            LOGGER.error("EngineException", t);
        }
    }

    protected abstract void doProcess(EngineContext context);

    private void after(EngineContext context) {
        try {
            handlerAfterProcess(contextHandlers, context);
        } catch (Throwable t) {
            handleException(context, t);
        }
    }
}
