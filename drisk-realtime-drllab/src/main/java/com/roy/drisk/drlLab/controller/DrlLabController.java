package com.roy.drisk.drlLab.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.roy.drisk.drlLab.service.Dservice;
import com.roy.drisk.drools.KieInfrastructure;
import com.roy.drisk.drools.KieScannerManager;
import com.roy.drisk.message.ContextMessage;
import org.drools.core.command.runtime.rule.FireAllRulesCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping(value="/drlLab")
public class DrlLabController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private KieInfrastructure kieInfrastructure;
	@Autowired
    private KieScannerManager kieScannerManager;
	
	@RequestMapping(value="/getVersion",method= {RequestMethod.POST})
	@ResponseBody
	public String getVersion() {
		String version = "the version rule is not found.";
		logger.info("received request to update the version of rules package");
		//触发一次drools扫描，会去maven库中更新最新的rules包。
		kieScannerManager.scanNow();
		String baseName = "VersionKB";
		String sessionName = "VersionKS";
		KieContainer kieContainer = kieInfrastructure.getSessionByName(baseName, sessionName);
		KieSession kieSession = kieContainer.newKieSession(sessionName);
		kieSession.fireAllRules();
		
		for(Object obj :  kieSession.getObjects()) {
			if(obj instanceof String) {
				version = obj.toString();
				break;
			}
		}
		return version;
	}
	
	@RequestMapping(value="/requestPost",method= {RequestMethod.POST})
	@ResponseBody
	public String requestPost(String drlRequest){

		logger.info("request received: {}",drlRequest);
		if(drlRequest.indexOf('%')>=0) {
			try {
				drlRequest = URLDecoder.decode(drlRequest,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.warn("报文编码失败。请重新检查。drlRequest = {}",drlRequest);
			}
		}
		logger.info("start build contextMessage.........");
		ContextMessage contextMessage = null;
		//把drlRequest字符串组装成ContextMessage
		contextMessage = buildContextMessage(drlRequest);
		logger.info("ContextMessage builded : {}", contextMessage);
		//转换错误，直接返回。
		if(null != contextMessage.getTempItem("ConvertError") && "".equals(contextMessage.getTempItem("ConvertError"))) {
			return JSON.toJSONString(contextMessage);
		}else {
			//用ContextMessage直接跑drl规则。
			String baseName = contextMessage.getBaseName();
			String sessionName = contextMessage.getSessionName();
			try {
				KieContainer kieContainer = kieInfrastructure.getSessionByName(baseName, sessionName);
				StatelessKieSession statelessKieSession = null;
		        KieSession statefulKieSession = null;
		        try {
		            statelessKieSession = kieContainer.newStatelessKieSession(sessionName);
		            logger.info("Create stateless {} with {}", statelessKieSession,baseName+":"+sessionName);
		        } catch (Exception e) {
		            statefulKieSession = kieContainer.newKieSession(sessionName);
		            logger.info("Create stateful {} with {}", statefulKieSession,baseName+":"+sessionName);
		        }
		        if (statefulKieSession == null && statelessKieSession != null) {
		            KieCommands kieCommands = kieInfrastructure.getKieServices().getCommands();
		            List<Command> cmds = new ArrayList<>();
//		            cmds.add(kieCommands.newSetGlobal("service", null));
//		            cmds.add(kieCommands.newSetGlobal("riskChkKSService", null));
		            
		            cmds.add(kieCommands.newInsert(contextMessage, "message", true, null));
		            logger.info("Fire rules {} with message {}", statelessKieSession, contextMessage);
		            cmds.add(new FireAllRulesCommand());
		            ExecutionResults results = statelessKieSession.execute(kieCommands.newBatchExecution(cmds));
		            contextMessage =(ContextMessage) results.getValue("message"); 
		        } else if (statefulKieSession != null && statelessKieSession == null) {
//		            statefulKieSession.setGlobal("service", null);
//		            statefulKieSession.setGlobal("riskChkKSService", null);
//		            statefulKieSession.setGlobal("dservice",new Dservice());
		            statefulKieSession.insert(contextMessage);
		            statefulKieSession.fireAllRules();
		        } else {
		        	contextMessage.addTempItem("runError", "KieSession " + baseName + ":" + sessionName + " run error.");
		        }
			}catch(Exception e) {
				contextMessage.addTempItem("runError", "KieSession " + baseName + ":" + sessionName + " not found.");
			}
			
		}
		return JSON.toJSONString(contextMessage);
	}
	
	//把drlRequest字符串组装成ContextMessage
	private ContextMessage buildContextMessage(String drlRequest) {
		Map<String, Object> reqData = new HashMap<>();
		ContextMessage contextMessage = new ContextMessage(reqData);
		contextMessage.setTest(true);
		String[] attrs = drlRequest.split(";");
		String attr = "";//转换的属性
		try {
			for(int i = 0 ; i < attrs.length;i++) {
				attr=attrs[i];
				String[] attrSplit = attr.trim().split("=");
				String key = attrSplit[0];
				String value = attrSplit[1];
				//构造基本参数
				if("baseName".equals(key)) {
					contextMessage.setBaseName(value);
				}else if("sessionName".equals(key)) {
					contextMessage.setSessionName(value);
				}else if("requestId".equals(key)) {
					contextMessage.setRequestId(value);
				}else if("clientId".equals(key)) {
					contextMessage.setClientId(value);
				}else if("clientIp".equals(key)) {
					contextMessage.setClientIp(value);
				}else if("userNo".equals(key)) {
					contextMessage.setUserNo(value);
				}else if("mobileNo".equals(key)) {
					contextMessage.setMobileNo(value);
				}else { //reqData和tempData，先处理value。map采用json格式，list采用数组格式，字符串用双引号。
					String attrType = key.substring(0,key.indexOf('.'));
					if(("reqData").equals(attrType) || ("tempData").equals(attrType)) {
						Object endValue;
						if(value.startsWith("\"")) {//字符串格式
							endValue = value.replaceAll("\"", "");
						}else if(value.startsWith("[")) {//中括号开头的，字符串数组
							value = value.substring(1,value.length()-1);
							endValue = value.replaceAll("\"", "").replaceAll("\'", "").split(",");
						}else if("true".equals(value)){//Boolean类型
							endValue = true;
						}else if("false".equals(value)){
							endValue = false;
						}else if(value.indexOf(".")>-1){
							endValue = new BigDecimal(value);
						}
						else{//int类型
							endValue = Integer.parseInt(value);
						}
						//value处理完了后，构造reqData
						String[] keyParts = key.replace(attrType+".", "").split("\\.");
						if(("reqData").equals(attrType)) {
							setData(reqData,keyParts,0,endValue);
							contextMessage.addReqItems(reqData);
							//构造tempData
						}else if(("tempData").equals(attrType)) {
							Map<String, Object> tempData = new HashMap<>();
							setData(tempData,keyParts,0,endValue);
							contextMessage.addTempItems(tempData);
						}
					}
				}
			}
		} catch (NumberFormatException e) {
			contextMessage.addTempItem("ConvertError", "属性 "+attr+" 转换错误。");
		}
		return contextMessage;
	}

	private void setData(Map<String, Object> dataArea, String[] keyParts, int idx, Object value) {
        if (keyParts.length == (idx + 1)) {
            dataArea.put(keyParts[idx], value);
            return;
        }
        String key = keyParts[idx];
        if (dataArea.get(key) == null) {
            dataArea.put(key, new HashMap<String, Object>());
        }
        setData((Map<String, Object>) dataArea.get(key), keyParts, idx + 1, value);
    }
}
