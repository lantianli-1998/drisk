package com.roy.drisk;

import com.roy.drisk.commonservice.config.EnvVariable;
import com.roy.drisk.server.netty.service.DriskServerBootStrap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
@SpringBootApplication
public class DriskServerApplication implements CommandLineRunner {
    private static final Logger logger = Logger.getLogger(DriskServerApplication.class);

    @Autowired
    private DriskServerBootStrap bootstrap;
    public static void main(String[] args) {
        initSystemProperties();
        SpringApplication.run(DriskServerApplication.class);
    }

    private static void initSystemProperties() {
        Properties properties = System.getProperties();
        properties.put("spring.profiles.active", EnvVariable.name());
    }
    @Override
    public void run(String... args) throws Exception {
        try {
            bootstrap.boot();
        } catch (Throwable t) {
            logger.error("Starting Sword Risk Server failed", t);
            System.exit(1);
        }
    }
}
