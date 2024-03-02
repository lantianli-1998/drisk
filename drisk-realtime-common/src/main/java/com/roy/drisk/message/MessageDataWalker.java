package com.roy.drisk.message;

import java.math.BigDecimal;
import java.util.Map;

import static com.roy.drisk.message.MessageDataUtil.itemByPath;
import static com.roy.drisk.message.MessageDataUtil.stringItemByPath;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc 业务数据传递类
 */
public class MessageDataWalker {
    private Map<String, Object> reqData;
    private Map<String, Object> rspData;
    private Map<String, Object> tempData;

    public MessageDataWalker(Map<String, Object> reqData, Map<String, Object> rspData, Map<String, Object> tempData) {
        this.reqData = reqData;
        this.rspData = rspData;
        this.tempData = tempData;
    }

    public Object reqObj(String path) {
        return itemByPath(reqData, path);
    }

    public String req(String path) {
        return stringItemByPath(reqData, path);
    }

    public Object rspObj(String path) {
        return itemByPath(rspData, path);
    }

    public String rsp(String path) {
        return stringItemByPath(rspData, path);
    }

    public Object tempObj(String path) {
        return itemByPath(tempData, path);
    }

    public String temp(String path) {
        return stringItemByPath(tempData, path);
    }
}
