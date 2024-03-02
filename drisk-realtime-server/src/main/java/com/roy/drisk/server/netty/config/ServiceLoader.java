package com.roy.drisk.server.netty.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author roy
 * @date 2021/10/27
 * @desc 用于配置一些启动加载的东东。
 */
@Configuration
public class ServiceLoader {

	private Logger logger = Logger.getLogger(this.getClass());
	//类加载过程中会创建连接并建立缓存。
	@PostConstruct
	private void initConnector() throws ClassNotFoundException {
		logger.info("=============初始化连接=============");
		Class.forName("com.roy.drisk.connector.service.DriskConnectorFactory");
		logger.info("=============初始化services=============");
		Class.forName("com.roy.drisk.services.GeneralServiceFactory");
	}
}
