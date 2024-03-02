package com.roy.drisk.message;

import java.util.*;

/**
 * @author roy
 * @date 2021/10/26
 * @desc HashMap处理工具类
 */
public class MessageDataUtil {
    private MessageDataUtil() {
    }

    @SuppressWarnings("unchecked")
    public static Object itemByPath(Object data, String path) {
        if (data == null || path == null || "".equals(path)) {
            return null;
        }

        int idx = path.indexOf('.');
        if (idx == 0) {
            return itemByPath(data, path.substring(1));
        }

        if (data instanceof Map) {
            Map dataMap = (Map) data;
            if (idx < 0) {
                return dataMap.get(path);
            } else {
                Object child = dataMap.get(path.substring(0, idx));
                return itemByPath(child, path.substring(idx + 1));
            }
        } else if (data instanceof List || data instanceof Set) {
            List dataList;
            if (data instanceof Set) {
                SortedSet set = new TreeSet((Set) data);
                dataList = new LinkedList(set);
            } else {
                dataList = (List) data;
            }
            try {
                if (idx < 0) {
                    int keyIdx = Integer.parseInt(path);
                    return dataList.get(keyIdx);
                } else {
                    int keyIdx = Integer.parseInt(path.substring(0, idx));
                    Object child = dataList.get(keyIdx);
                    return itemByPath(child, path.substring(idx + 1));
                }
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            if (idx < 0) {
                return data;
            } else {
                return null;
            }
        }
    }

    public static String stringItemByPath(Object data, String path) {
        Object res = itemByPath(data, path);
        return res == null ? "" : res.toString();
    }
}
