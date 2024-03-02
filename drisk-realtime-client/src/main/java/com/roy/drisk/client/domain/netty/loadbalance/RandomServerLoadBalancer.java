package com.roy.drisk.client.domain.netty.loadbalance;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

/**
 * User: QC
 * Date: 2016-05-03
 * Time: 16:26
 */
public class RandomServerLoadBalancer implements ServerLoadBalancer {
    private List<InetSocketAddress> transportServers;
    private Random random = new Random();

    public RandomServerLoadBalancer(List<InetSocketAddress> transportServers) {
        this.transportServers = transportServers;
    }

    @Override
    public InetSocketAddress getAddress() {
        if (transportServers.size() <= 1) {
            return transportServers.get(0);
        }
        return transportServers.get(random.nextInt(transportServers.size()));
    }
}
