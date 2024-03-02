package com.roy.drisk.message;

import java.io.Serializable;
import java.util.Map;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc 请求数据
 */
public class RequestMessage extends CommonMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, Object> data;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RequestMessage{" +
                "data=" + data +
                "} " + super.toString();
    }
}
