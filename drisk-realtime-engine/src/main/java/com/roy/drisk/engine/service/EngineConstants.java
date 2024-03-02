package com.roy.drisk.engine.service;

import java.io.File;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class EngineConstants {

    /**
     * 规则引擎配置目录路径
     */
    public static final String CONFIG_DIR = new File(System.getProperty("user.dir"),"config").getAbsolutePath();
}
