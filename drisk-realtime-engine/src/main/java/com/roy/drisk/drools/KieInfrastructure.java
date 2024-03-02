package com.roy.drisk.drools;

import com.roy.drisk.engine.config.RulesSettings;
import com.roy.drisk.engine.service.EngineConstants;
import com.roy.drisk.engine.util.FileUtil;
import com.roy.drisk.engine.util.MavenUtil;
import com.roy.drisk.exception.RuleNotFoundException;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.roy.drisk.engine.util.MavenUtil.parseCoordinatesString;


/**
 * @author lantianli
 * @date 2023/10/31
 * @desc
 * Kie相关基础设施管理类，<code>KieInfrastructure</code>根据配置的Maven坐标
 * 为各规则包初始化{@link ReleaseId}、{@link KieContainer}、{@link KieScanner}，
 * 同时构造{@link KieScannerManager}用于扫描规则文件变更。
 */
@Component
@Lazy
public class KieInfrastructure {
    private static final Logger LOGGER = LoggerFactory.getLogger(KieInfrastructure.class);
    public static final String KIE_MAVEN_SETTINGS = "kie.maven.settings.custom";
    private RulesSettings settings;
    private KieServices kieServices;
    private List<ReleaseId> releaseIdList;
    private List<KieContainer> kieContainerList;
    private List<KieScanner> kieScannerList;
    @Autowired
    private KieScannerManager kieScannerManager;

    @Autowired
    public KieInfrastructure(RulesSettings settings) {
        LOGGER.info("Initiating KieInfrastructure with {}", settings);
        this.settings = settings;
        initMavenSettingsFile();
    }

    /**
     * 设置<code>KieScanner</code>需要的系统变量、配置文件
     */
    private void initMavenSettingsFile() {
        String path = System.getProperty(KIE_MAVEN_SETTINGS);
        if (FileUtil.isExistFile(path)) {
            LOGGER.info("KieInfrastructure using customize settings: {}", path);
            return;
        }
        path = FileUtil.getFilePath(EngineConstants.CONFIG_DIR, "settings.xml");
        if (FileUtil.isExistFile(path)) {
            LOGGER.info("KieInfrastructure using customize settings: {}", path);
            System.setProperty(KIE_MAVEN_SETTINGS, path);
        }
    }

    @PostConstruct
    private void init() {
        this.kieServices = KieServices.Factory.get();
        this.releaseIdList = buildReleaseIdList(settings.getCoordinates());
        this.kieContainerList = buildContainerList(this.releaseIdList);
        this.kieScannerList = buildScannerList(this.kieContainerList);
        this.kieScannerManager.initScanner(settings, releaseIdList, kieScannerList);
    }

    private List<ReleaseId> buildReleaseIdList(List<String> coordinates) {
        List<ReleaseId> releaseIdList = new ArrayList<>();
        for (String str : coordinates) {
            MavenUtil.Coordinates c = parseCoordinatesString(str);
            if (c != null) {
                ReleaseId releaseId = kieServices.newReleaseId(c.getGroupId(), c.getArtifactId(), c.getVersion());
                releaseIdList.add(releaseId);
                LOGGER.info("Added ReleaseId: {}", releaseId.toString());
            }
        }
        return releaseIdList;
    }

    private List<KieContainer> buildContainerList(List<ReleaseId> releaseIdList) {
        List<KieContainer> kieContainerList = new ArrayList<>();
        for (ReleaseId releaseId : releaseIdList) {
            KieContainer kieContainer = kieServices.newKieContainer(releaseId, this.getClass().getClassLoader());
            kieContainerList.add(kieContainer);
            LOGGER.info("Added KieContainer: {}", kieContainer.toString());
        }
        return kieContainerList;
    }

    private List<KieScanner> buildScannerList(List<KieContainer> kieContainerList) {
        List<KieScanner> kieScannerList = new ArrayList<>();
        for (KieContainer kieContainer : kieContainerList) {
            KieScanner kieScanner = kieServices.newKieScanner(kieContainer);
            kieScannerList.add(kieScanner);
            LOGGER.info("Added kieScanner: {}", kieScanner.toString());
        }
        return kieScannerList;
    }

    /**
     * 取得KieServices
     *
     * @return KieServices
     */
    public KieServices getKieServices() {
        return kieServices;
    }

    /**
     * 取得KieContainer清单
     *
     * @return KieContainer列表
     */
    public List<KieContainer> getKieContainerList() {
        return kieContainerList;
    }

    /**
     * 根据baseName及sessionName取的对应的KieContainer
     *
     * @param baseName    baseName
     * @param sessionName sessionName
     * @return KieContainer
     */
    public KieContainer getSessionByName(String baseName, String sessionName) {
        for (KieContainer kieContainer : kieContainerList) {
            Collection<String> sessionNames = kieContainer.getKieSessionNamesInKieBase(baseName);
            if (sessionNames != null && sessionNames.contains(sessionName)) {
                return kieContainer;
            }
        }
        throw new RuleNotFoundException("Rule " + baseName + ":" + sessionName + " not found.");
    }
}
