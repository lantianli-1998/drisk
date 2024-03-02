package com.roy.drisk.client.application;

import com.roy.drisk.client.contract.ClientSettings;
import com.roy.drisk.client.domain.netty.client.PooledNettyTCPClient;
import com.roy.drisk.client.domain.netty.client.SimpleNettyTCPClient;
import com.roy.drisk.client.infrastructure.SettingsRepo;
import com.roy.drisk.exception.DriskClientException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author lantianli
 * @date 2023/10/26
 * @desc
 */
public class DriskClientBuilder {

    public static DriskNettyClient newDriskNettyClient() throws DriskClientException {
        return newDriskNettyClient(false);
    }

    public static DriskNettyClient newDriskNettyClient(String propFile) throws DriskClientException {
        return newDriskNettyClient(propFile,false);
    }

    public static DriskNettyClient newDriskNettyClient(Boolean pooled){
        final ClientSettings clientSettings;
        try {
            clientSettings = SettingsRepo.create();
            if(pooled){
                return new PooledNettyTCPClient(clientSettings);
            }else{
                return new SimpleNettyTCPClient(clientSettings);
            }
        } catch (IOException e) {
           throw new DriskClientException("创建客户端失败");
        }
    }

    public static DriskNettyClient newDriskNettyClient(String propFile,Boolean pooled){
        final InputStream in = DriskClientBuilder.class.getClassLoader().getResourceAsStream(propFile);
        try {
            Properties prop = new Properties();
            prop.load(in);
            ClientSettings clientSettings = SettingsRepo.create(prop);
            if(pooled){
                return new PooledNettyTCPClient(clientSettings);
            }else{
                return new SimpleNettyTCPClient(clientSettings);
            }
        } catch (IOException e) {
            throw new DriskClientException("加载配置文件出错");
        }
    }


}
