package com.roy.drisk.services.login;

import com.roy.drisk.connector.redis.CloseableJedisCommands;
import com.roy.drisk.connector.service.DriskConnectorFactory;
import com.roy.drisk.services.GeneralService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author roy
 * @date 2021/11/11
 * @desc
 */
public class FailedLoginMblService implements GeneralService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void addLoginFailedMbl(MblLoginBlack mblLoginBlack){
        try (CloseableJedisCommands redisClient = DriskConnectorFactory.getRedisClient()) {
            String key = "FailedLoginMbl:" + mblLoginBlack.getMblno();
            final Long res = redisClient.setnx(key, mblLoginBlack.getTxDt() + mblLoginBlack.getTxTm());
            if(res == 1){
                redisClient.expire(key, 60 * 60 * 24 *  mblLoginBlack.getLimitDay());
            }
        } catch (IOException e) {
            logger.error("FailedLoginMblService: failed to get redisclient");
        }
    }

    public Boolean isMblLoginFailed(String mblNo){
        try (CloseableJedisCommands redisClient = DriskConnectorFactory.getRedisClient()) {
            String key = "FailedLoginMbl:" + mblNo;
            final String res = redisClient.get(key);
            return StringUtils.isNotBlank(res);
        } catch (IOException e) {
            logger.error("FailedLoginMblService: failed to get redisclient");
            return false;
        }
    }
}
