package com.roy.drisk.server.application;

import com.roy.drisk.drools.KieInfrastructure;
import com.roy.drisk.drools.KieScannerManager;
import com.roy.drisk.server.application.util.DriskConstants;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

@Controller
@RequestMapping("/DroolsScan")
public class DroolsScanController {

	private Logger logger = LoggerFactory.getLogger(DroolsScanController.class);
	@Autowired
    private KieScannerManager kieScannerManager;
	@Autowired
	private KieInfrastructure kieInfrastructure;

	private static final String VERSIONRULE_BASE_NAME = "VersionKB";
	private static final String VERSIONRULE_SESSION_NAME = "VersionKS";
	//更新drls包。返回包中的最新版本号
	@RequestMapping(value="/updateDrlVersion",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String scanDrools(String code) {
		if(DriskConstants.accessKey.equals(code)) {
			//触发一次drools扫描。会更新maven中的最新版本rules包。
			kieScannerManager.scanNow();
			logger.info("successed to update rule jar package");
			//跑一次获取版本的规则，读取rules包中的版本号。
			KieContainer container = kieInfrastructure.getSessionByName(VERSIONRULE_BASE_NAME, VERSIONRULE_SESSION_NAME);
			KieSession kieSession = container.newKieSession(VERSIONRULE_SESSION_NAME);
			kieSession.fireAllRules();
			Collection<? extends Object> objects = kieSession.getObjects();
			System.out.println(objects.size());
			for(Object drlVersion :objects) {
				if(null != drlVersion) {
					logger.info("Current version : " + drlVersion);
					return "Current version : " + drlVersion;
				}
			}
			return "version rool is not exists.please check the rules jar file";
		}else {
			return "please check the accessKey";
		}
	}
	//获取当前drl文件包版本
	@RequestMapping(value="/getDrlVersion",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String getDrlVersion(String code) {
		if(DriskConstants.accessKey.equals(code)) {
			//跑一次获取版本的规则，读取rules包中的版本号。
			KieContainer container = kieInfrastructure.getSessionByName(VERSIONRULE_BASE_NAME, VERSIONRULE_SESSION_NAME);
			KieSession kieSession = container.newKieSession(VERSIONRULE_SESSION_NAME);
			kieSession.fireAllRules();
			Collection<? extends Object> objects = kieSession.getObjects();
			for(Object drlVersion :objects) {
				if(null != drlVersion) {
					logger.info("Current version : " + drlVersion);
					return "Current version : " + drlVersion;
				}
			}
			return "version rule is not exists.please check the rules jar file";
		}else {
			return "please check the accessKey";
		}
	}
}
