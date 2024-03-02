package com.roy.drisk.engine.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.roy.drisk.connector.hbase.HBaseUtil;
import com.roy.drisk.connector.kafka.KafkaMessageProducer;
import com.roy.drisk.connector.service.DriskConnectorFactory;
import com.roy.drisk.engine.config.EngineSettings;
import com.roy.drisk.engine.context.EngineContext;
import com.roy.drisk.engine.handler.EngineContextHandlerAdapter;
import com.roy.drisk.message.ContextMessage;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 * <code>JournalPublisher</code>用于在业务处理完成后向Kafka发送日志流水消息，
 * 此类型的消息将由非实时风控处理。
 */
@Component
@Order(0)
public class JournalPublisher extends EngineContextHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JournalPublisher.class);
    @Autowired
    private EngineSettings settings;

    private String buildJsonMessage(EngineContext context) {
        ContextMessage message = context.message();
        LOGGER.info("checked message: {}",message);
        ObjectMapper objectMapper = context.service().json();
        try {
            //TODO 这里要改。改成只发布感兴趣的内容到kakfa。直接供非实时风控去进行累计。这里只发请求数据以及一个最终的结果好了。
            ObjectNode node = objectMapper.valueToTree(message.getReqData());
            node.put("TABLE_NAME", settings.getJournalTableName());
            node.put("CHECK_TIME", String.valueOf(System.currentTimeMillis()));
            node.put("REQUEST_ID",message.getRequestId());
            node.put("RULE_TRIGGERED",message.walker().temp("RULE_TRIGGERED"));
            node.put("BUSI_CODE",message.getMessageCode());
            //没有处罚任何规则，或者触发了安全规则就认为成功。
            if(StringUtils.isEmpty(message.getMessageCode())
            || StringUtils.endsWith(message.getMessageCode(), "00000")){
            	node.put("TX_RST", "0"); //0为成功
            }else{
            	node.put("TX_RST", "1"); //1为失败
            }
            return objectMapper.writeValueAsString(node);
        } catch (IOException e) {
            LOGGER.error("JournalPublisherException", e);
        }
        return null;
    }
    //将消息发送到kafka，供非实时风控消费处理。
    private void sendMessage(final EngineContext context, final String msg) {
        KafkaMessageProducer<String, String> messageProducer = context.service().kafka();
        try {
            messageProducer.send(settings.getJournalTopic(), null, msg);
            LOGGER.info("Journal msg: {}", msg);
        } catch (Exception e) {
            LOGGER.error("JournalPublisherException", e);
        }
    }

    @Override
    public void after(final EngineContext context) {
        final String msg = buildJsonMessage(context);
        if (msg != null) {
            sendMessage(context, msg);
        }
        saveMsg2HBase(context);
    }
    //将消息整体存入到HBase，供业务进行回溯。
    private void saveMsg2HBase(EngineContext context) {
        final ObjectMapper objectMapper = context.service().json();
        final ContextMessage message = context.message();
        final HBaseUtil hBaseUtil = new HBaseUtil(DriskConnectorFactory.getHBaseConnection());

        try {
            hBaseUtil.put("TXLOG", message.getRequestId(), "info", "record", objectMapper.writeValueAsBytes(message));
        } catch (JsonProcessingException e) {
            LOGGER.error("Journal saveMsg2HBase error" ,e );
        }
    }
}
