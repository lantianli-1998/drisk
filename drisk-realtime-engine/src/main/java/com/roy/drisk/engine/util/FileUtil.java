package com.roy.drisk.engine.util;

import java.io.File;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class FileUtil {
    /**
     * 取文件绝对路径
     *
     * @param dir  文件所在目录
     * @param file 文件名
     * @return 文件路径
     */
    public static String getFilePath(String dir, String file) {
        File f = new File(dir, file);
        return f.getAbsolutePath();
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件名
     * @return boolean
     */
    public static boolean isExistFile(String filePath) {
        if (filePath == null || "".equals(filePath))
            return false;
        File f = new File(filePath);
        return f.exists() && f.isFile();
    }

}
