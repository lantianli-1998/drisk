package com.roy.drisk.services.limit;

import com.roy.drisk.connector.redis.CloseableJedisCommands;
import com.roy.drisk.connector.service.DriskConnectorFactory;
import com.roy.drisk.services.GeneralService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author roy
 * @date 2021/11/8
 * @desc
 */
public class LimitService implements GeneralService {

    private Logger logger = LoggerFactory.getLogger(LimitService.class);

    public Boolean updateMblDayLimitInRedis(String mbl_no, String tx_dt,String tx_amt) {
        try(CloseableJedisCommands jedis = DriskConnectorFactory.getRedisClient()){
            String rkey = "LIMIT_DAY:"+tx_dt;
            String hashKey = DigestUtils.md5Hex(mbl_no);
            final String oldLimit = jedis.hget(rkey, hashKey);
            if(StringUtils.isBlank(oldLimit)){
                jedis.hset(rkey,hashKey,tx_amt);
            }else{
                final BigDecimal newlimit = new BigDecimal(oldLimit).add(new BigDecimal(tx_amt));
                String newRes = newlimit.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
                jedis.hset(rkey,hashKey,newRes);
            }
            //日限额数据保留32个小时。这里只考虑规则判断用，暂不考虑过时数据流存。
            jedis.expire(rkey,32*60*60);
            return true;
        }catch (IOException e){
            logger.warn("LimitService.updateLimitInRedis , e:{}",e.getMessage());
            return false;
        }
    }

    public BigDecimal queryMblDayLimit(String mbl_no,String tx_dt){
        try(CloseableJedisCommands jedis = DriskConnectorFactory.getRedisClient()){
            String rkey = "LIMIT_DAY:"+tx_dt;
            String hashKey = DigestUtils.md5Hex(mbl_no);
            final String oldLimit = jedis.hget(rkey, hashKey);
            if(null == oldLimit || "".equals(oldLimit)){
                return new BigDecimal("0.00");
            }else{
                return new BigDecimal(oldLimit);
            }
        }catch (IOException e){
            logger.warn("LimitService.queryMblDayLimit , e:{}",e.getMessage());
            return new BigDecimal("0.00");
        }
    }
}
