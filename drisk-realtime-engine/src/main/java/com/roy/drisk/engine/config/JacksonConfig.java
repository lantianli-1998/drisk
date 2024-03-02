package com.roy.drisk.engine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author roy
 * @date 2021/10/27
 * @desc jackson配置类
 */
@Configuration
public class JacksonConfig {
    /**
     * Json ObjectMapper
     *
     * @return ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
