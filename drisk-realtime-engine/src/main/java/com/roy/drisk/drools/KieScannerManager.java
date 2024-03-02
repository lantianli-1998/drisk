package com.roy.drisk.drools;

import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import com.roy.drisk.engine.config.RulesSettings;

import java.io.File;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @author lantianli
 * @date 2023/10/31
 * @desc
 * <code>KieScannerManager</code>用于管理规则文件的自动加载。
 * 当配置的<code>scanInterval</code>小于等于0时，不做自动加载，否则按配置的时间扫描规则文件的变化，
 * 当配置的<code>scanByDrools</code>为true时由drools的<code>KieScanner</code>负责定时扫描，
 * 否则由<code>KieScannerManager</code>自带的定时任务线程池执行定时扫描。
 */
@Component
@Lazy
public class KieScannerManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(KieScannerManager.class);
    private static final String USER_HOME_ENV = "user.home";
    private static final String RESOLVER_FILE = "resolver-status.properties";
    @Autowired
    private Environment environment;
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;
    private List<ReleaseId> releaseIdList;
    private List<KieScanner> kieScannerList;
    private RulesSettings settings;
    private ScheduledFuture scheduledFuture;
    private volatile boolean started = false;

    public void initScanner(RulesSettings settings,
                            List<ReleaseId> releaseIdList, List<KieScanner> kieScannerList) {
        this.settings = settings;
        this.releaseIdList = releaseIdList;
        this.kieScannerList = kieScannerList;
        startScanners();
    }

    /**
     * 开始规则文件变更扫描
     */
    public void startScanners() {
        if (this.started) {
            return;
        }
        LOGGER.info("KieScannerManager started.");
        this.started = true;
        long interval = settings.getScanInterval();
        if (interval > 0) {
            scanInterval(interval);
        } else {
            scanOnce();
        }
    }

    /**
     * 停止规则文件变更扫描
     */
    public void stopScanners() {
        if (!this.started) {
            return;
        }
        LOGGER.info("KieScannerManager stopped.");
        this.started = false;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        this.kieScannerList.forEach(KieScanner::stop);
    }

    private void scanInterval(long interval) {
        if (!this.started) {
            LOGGER.warn("KieScannerManager not started.");
            return;
        }
        LOGGER.debug("KieScannerManager is ready to scan every {} milliseconds.", interval);
        if (settings.isScanByDrools()) {
            kieScannerList.forEach(kieScanner -> kieScanner.start(interval));
        } else {
            scheduledFuture = taskScheduler.scheduleWithFixedDelay(this::scan, interval);
        }
    }

    private void scanOnce() {
        if (!this.started) {
            LOGGER.warn("KieScannerManager not started.");
            return;
        }
        LOGGER.debug("KieScannerManager is ready to scan...");
        if (settings.isScanByDrools()) {
            kieScannerList.forEach(KieScanner::scanNow);
        } else {
            scan();
        }
    }

    /**
     * 销毁规则文件变更扫描器
     */
    public void shutdownScanners() {
        LOGGER.info("KieScannerManager is ready to shutdown...");
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        this.kieScannerList.forEach(KieScanner::shutdown);
        LOGGER.info("KieScannerManager is shutdown.");
    }

    public void scanNow() {
        scanOnce();
    }

    private void scan() {
        for (int i = 0; i < kieScannerList.size(); i++) {
            try {
                deleteResolverFile(releaseIdList.get(i));
                kieScannerList.get(i).scanNow();
            } catch (Exception e) {
                LOGGER.error("Scanner for {} error: {}", releaseIdList.get(i), e.getMessage());
            }
        }
    }

    // KieScanner由于未知的原因无法自动扫描，需要删除resolverFile
    private void deleteResolverFile(ReleaseId releaseId) {
        File resolverFile = getResolverFile(releaseId);
        if (resolverFile.exists() && resolverFile.isFile()) {
            if (resolverFile.delete()) {
                LOGGER.trace("KieScannerManager delete file {}", resolverFile.getAbsolutePath());
            }
        }
    }

    private File getResolverFile(ReleaseId releaseId) {
        StringBuilder sb = new StringBuilder();
        sb.append(environment.getProperty(USER_HOME_ENV))
                .append(File.separator).append(".m2")
                .append(File.separator).append("repository")
                .append(File.separator).append(releaseId.getGroupId().replace('.', File.separatorChar))
                .append(File.separator).append(releaseId.getArtifactId())
                .append(File.separator);
        String version = releaseId.getVersion();
        if (version.endsWith("SNAPSHOT")) {
            sb.append(version).append(File.separator);
        }
        sb.append(RESOLVER_FILE);
        return new File(sb.toString());
    }
}
