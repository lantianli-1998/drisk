package com.roy.drisk.drlLab;

import java.util.Properties;

import com.roy.drisk.commonservice.config.EnvVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = "com.roy.drisk")
@SpringBootApplication
public class DRiskDrlLab {

	private static Logger logger = LoggerFactory.getLogger(DRiskDrlLab.class);

	private static void initSystemProperties() {
        Properties properties = System.getProperties();
//        properties.setProperty("drisk.env","sit");
        properties.put("spring.profiles.active", EnvVariable.name());
//        logger.info("System properties:");
//        properties.stringPropertyNames()
//                .forEach(key -> logger.info(key+" ==> "+System.getProperty(key)));
    }

    public static void main(String[] args) {

        initSystemProperties();
        logger.info("Starting Drisk DrlLab Server...");
        SpringApplication.run(DRiskDrlLab.class, args);
        logger.info("Drisk DrlLab Server started!");
    }
}
