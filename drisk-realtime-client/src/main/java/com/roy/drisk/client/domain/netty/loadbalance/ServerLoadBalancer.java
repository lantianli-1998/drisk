package com.roy.drisk.client.domain.netty.loadbalance;

import java.net.InetSocketAddress;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc
 */
public interface ServerLoadBalancer {
    InetSocketAddress getAddress();
}
