package com.roy.drisk.engine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author roy
 * @date 2021/10/27
 * @desc Rule配置类，由Spring从properties中加载以rules为前缀的相关配置。
 */
@Component
@ConfigurationProperties(prefix = "rules")
public class RulesSettings {
    /**
     * 规则文件的Maven坐标，多个以逗号分隔
     */
    private List<String> coordinates;
    /**
     * 自动加载间隔（毫秒）
     */
    private long scanInterval;
    /**
     * 是否由Drools扫描，默认为否，由应用扫描，
     * 此选项是由于使用drools的<code>KieScanner</code>扫描时，
     * 由于未知原因规则文件无法自动加载，具体原因待查，解决后再删除此配置
     */
    private boolean scanByDrools;
    /**
     * 使用Groovy为规则引擎时，groovy的spring context配置文件路径
     */
    private String groovyContextFile;

    public List<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<String> coordinates) {
        this.coordinates = coordinates;
    }

    public long getScanInterval() {
        return scanInterval;
    }

    public void setScanInterval(long scanInterval) {
        this.scanInterval = scanInterval;
    }

    public boolean isScanByDrools() {
        return scanByDrools;
    }

    public void setScanByDrools(boolean scanByDrools) {
        this.scanByDrools = scanByDrools;
    }

    public String getGroovyContextFile() {
        return groovyContextFile;
    }

    public void setGroovyContextFile(String groovyContextFile) {
        this.groovyContextFile = groovyContextFile;
    }

    @Override
    public String toString() {
        return "RulesSettings{" +
                "coordinates=" + coordinates +
                ", scanInterval=" + scanInterval +
                ", scanByDrools=" + scanByDrools +
                ", groovyContextFile='" + groovyContextFile + '\'' +
                '}';
    }
}
