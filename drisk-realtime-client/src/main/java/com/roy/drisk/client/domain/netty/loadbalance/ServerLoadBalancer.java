package com.roy.drisk.client.domain.netty.loadbalance;

import java.net.InetSocketAddress;

/**
 * @author roy
 * @date 2021/10/26
 * @desc
 */
public interface ServerLoadBalancer {
    InetSocketAddress getAddress();
}
