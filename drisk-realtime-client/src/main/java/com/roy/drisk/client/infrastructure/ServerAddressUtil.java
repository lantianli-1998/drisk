package com.roy.drisk.client.infrastructure;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lantianli
 * @date 2023/10-03
 * Time: 15:41
 */
public abstract class ServerAddressUtil {
    private static final String SERVER_SEP = ",";
    private static final String HOSTPORT_SEP = ":";
    private static final int DEFAULT_PORT = 9866;

    public static List<InetSocketAddress> parseLine(String line) {
        List<InetSocketAddress> addresses = new ArrayList<>();
        if (line == null)
            return addresses;
        line = line.trim();
        if ("".equals(line))
            return addresses;
        String[] parts = line.split(SERVER_SEP);
        if (parts.length < 1)
            return addresses;
        for (String hostAndPort : parts) {
            InetSocketAddress address = parseServerAddress(hostAndPort);
            if (address != null)
                addresses.add(address);
        }
        return addresses;
    }

    private static InetSocketAddress parseServerAddress(String hostAndPort) {
        if (hostAndPort == null)
            return null;
        hostAndPort = hostAndPort.trim();
        if ("".equals(hostAndPort))
            return null;
        String[] parts = hostAndPort.split(HOSTPORT_SEP);
        if (parts.length < 1)
            return null;
        if (parts[0] == null || "".equals(parts[0]))
            return null;
        String host = parts[0];
        int port = DEFAULT_PORT;
        if (parts.length >= 2 && parts[1] != null && !"".equals(parts[1]))
            port = Integer.parseInt(parts[1]);
        return new InetSocketAddress(host, port);
    }
}
