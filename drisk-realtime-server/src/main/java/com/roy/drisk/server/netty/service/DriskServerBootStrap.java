package com.roy.drisk.server.netty.service;

import com.roy.drisk.server.netty.server.NettyTCPServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc 启动Netty服务端
 */
@Component
public class DriskServerBootStrap {
    @Autowired
    private NettyTCPServer server;

    public void boot() throws Throwable {
        try {
            server.start();
        } catch (Throwable t) {
            server.stop();
            throw t;
        }
    }

}
