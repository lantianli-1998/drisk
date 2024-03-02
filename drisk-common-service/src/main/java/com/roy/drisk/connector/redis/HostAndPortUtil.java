package com.roy.drisk.connector.redis;

import redis.clients.jedis.HostAndPort;

import java.util.HashSet;
import java.util.Set;

/**
 * @author roy
 * @date 2021/10/27
 * @desc Redis连接字符串处理类
 */
public class HostAndPortUtil {
    private static final String HOST_SEP = ",";
    private static final String HOST_PORT_SEP = ":";
    private static final int DEFAULT_PORT = 6379;

    public static Set<HostAndPort> parseHostsAndPorts(String hostsAndPortsString) {
        Set<HostAndPort> nodes = new HashSet<>();
        String[] parts = hostsAndPortsString.split(HOST_SEP);
        if (parts.length > 0) {
            for (String hostAndPortString : parts) {
                nodes.add(parseHostAndPort(hostAndPortString));
            }
        }
        return nodes;
    }

    private static HostAndPort parseHostAndPort(String hostAndPortString) {
        String[] pair = hostAndPortString.split(HOST_PORT_SEP);
        int port = DEFAULT_PORT;
        if (pair.length >= 2) {
            port = Integer.parseInt(pair[1]);
        }
        return new HostAndPort(pair[0], port);
    }
}
