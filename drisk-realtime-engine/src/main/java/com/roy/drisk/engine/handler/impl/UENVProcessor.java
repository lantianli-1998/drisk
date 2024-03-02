package com.roy.drisk.engine.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roy.drisk.connector.redis.CloseableJedisCommands;
import com.roy.drisk.engine.context.EngineContext;
import com.roy.drisk.engine.handler.EngineContextHandlerAdapter;
import com.roy.drisk.exception.EngineException;
import com.roy.drisk.exception.MessageCodeConstants;
import com.roy.drisk.message.ContextMessage;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc <code>UENVProcessor</code>用于从Redis中提取应用保存的UENV区数据。
 */
@Component
@Order(10)
public class UENVProcessor extends EngineContextHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(UENVProcessor.class);
    private static final String UENV_PREFIX = "UENV:";
    private static final int UENV_TTL = 60 * 60 * 24;

    @Override
    public void before(final EngineContext context) {
        ContextMessage message = context.message();
        String key = message.walker().req("GWA.UENV._KEY");
        if (key != null && !"".equals(key)) {
            String redisKey = redisKey(context.message(), key);
            try (CloseableJedisCommands client = context.service().redis()) {
                String res = client.get(redisKey);
                LOGGER.debug("Load UENV [{}]: {}", redisKey, res);
                ObjectMapper objectMapper = context.service().json();
                boolean saveCurrentUENV = true;
                if (res != null && !"".equals(res)) {
                    Map<String, Object> currentUENV = getUENVData(message);
                    if (currentUENV.keySet().stream().filter("_KEY"::equals).collect(Collectors.toSet()).size() == 0) {
                        saveCurrentUENV = false;
                    }
                    setUENVData(message, mergeUENV(currentUENV, deserializeUENV(objectMapper, res)));
                }
                if (saveCurrentUENV) {
                    saveUENV(client, redisKey, serializeUENV(objectMapper, getUENVData(message)));
                }
            } catch (IOException e) {
                throw new EngineException(MessageCodeConstants.UENV_KEY_INVALID, e);
            }
        }
    }

    private String redisKey(ContextMessage message, String key) {
        return UENV_PREFIX + DigestUtils.md5Hex(message.getMobileNo() + key);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getUENVData(ContextMessage message) {
        Map<String, Object> reqData = message.getReqData();
        if (!reqData.containsKey("GWA")) {
            reqData.put("GWA", new HashMap<String, Object>());
        }
        Map<String, Object> gwa = (Map<String, Object>) reqData.get("GWA");
        if (!gwa.containsKey("UENV")) {
            gwa.put("UENV", new HashMap<String, Object>());
        }
        return ((Map<String, Object>) gwa.get("UENV"));
    }

    @SuppressWarnings("unchecked")
    private void setUENVData(ContextMessage message, Map<String, Object> data) {
        Map<String, Object> reqData = message.getReqData();
        Map<String, Object> gwa = (Map<String, Object>) reqData.get("GWA");
        gwa.put("UENV", data);
    }

    private String serializeUENV(ObjectMapper objectMapper, Map<String, Object> currentUENV) {
        try {
            return objectMapper.writeValueAsString(currentUENV);
        } catch (JsonProcessingException e) {
            throw new EngineException(MessageCodeConstants.UENV_DATA_INVALID, e);
        }
    }

    private void saveUENV(CloseableJedisCommands client, String redisKey, String uenvStr) {
        client.setex(redisKey, UENV_TTL, uenvStr);
        LOGGER.debug("Save UENV [{}]: {}", redisKey, uenvStr);
    }

    private Map<String, Object> deserializeUENV(ObjectMapper objectMapper, String res) {
        JavaType javaType = objectMapper.getTypeFactory().
                constructParametricType(Map.class, String.class, Object.class);
        try {
            return objectMapper.readValue(res, javaType);
        } catch (IOException e) {
            throw new EngineException(MessageCodeConstants.UENV_DATA_INVALID, e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> mergeUENV(Map<String, Object> currentUENV, Map<String, Object> lastUENV) {
        currentUENV.forEach((k, v) -> {
            if (lastUENV.containsKey(k)) {
                if (v instanceof Map && lastUENV.get(k) instanceof Map) {
                    mergeUENV((Map<String, Object>) v, (Map<String, Object>) lastUENV.get(k));
                } else {
                    lastUENV.put(k, v);
                }
            } else {
                lastUENV.put(k, v);
            }
        });
        return lastUENV;
    }
}
