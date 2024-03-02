package com.roy.drisk.services.blackMbl;

import com.roy.drisk.connector.hbase.HBaseUtil;
import com.roy.drisk.connector.hbase.RowMapper;
import com.roy.drisk.connector.redis.CloseableJedisCommands;
import com.roy.drisk.connector.service.DriskConnectorFactory;
import com.roy.drisk.services.GeneralService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lantianli
 * @date 2021/11/7
 * @desc
 */
public class BlackMblService implements GeneralService {
    private Logger logger = LoggerFactory.getLogger(BlackMblService.class);
    //判断手机号是否在外部黑名单
    public boolean isMblInBlack(String mblNo){
        final HBaseUtil hBaseUtil = new HBaseUtil(DriskConnectorFactory.getHBaseConnection());
        final String blackReason = hBaseUtil.get("MBL_BLACK_NAME", DigestUtils.md5Hex(mblNo), new RowMapper<String>() {
            @Override
            public String mapRow(Result result, int rowNum) throws Exception {
                if(result.isEmpty()){
                    return "";
                }else{
                    return new String(result.getValue("info".getBytes(StandardCharsets.UTF_8), "reason".getBytes(StandardCharsets.UTF_8)));
                }
            }
        });
        return StringUtils.isNotEmpty(blackReason);
    }

    public void addMblBlack(String mblNo, String reason){
        final HBaseUtil hBaseUtil = new HBaseUtil(DriskConnectorFactory.getHBaseConnection());

        Map<String,String> record = new HashMap<>();
        record.put("blackMbl",mblNo);
        record.put("reason",reason);

        Map<String, Map<String,String>> families = new HashMap<>();
        families.put("info",record);

        hBaseUtil.put("MBL_BLACK_NAME",
                DigestUtils.md5Hex(mblNo),
                families);
    }
    //判断手机号是否在日限额黑名单
    public boolean isMblInDayLimtBlack(String mblNo){
        try(final CloseableJedisCommands redisClient = DriskConnectorFactory.getRedisClient()){
            String key = "LJ002_BLACK:"+DigestUtils.md2Hex(mblNo);
            final String res = redisClient.get(key);
            return StringUtils.isNotBlank(res);
        }catch (IOException e){
            logger.error("BlackMblService.isMblInDayLimtBlack get redisclient failed ",e);
            return false;
        }
    }

    public void addMblDayLimitBlack(DayLimitBlack dayLimitBlack){
        try(final CloseableJedisCommands redisClient = DriskConnectorFactory.getRedisClient()){
            String key = "LJ002_BLACK:"+DigestUtils.md2Hex(dayLimitBlack.getMblNo());
            redisClient.set(key,""+dayLimitBlack.getAmt()+"-"+dayLimitBlack.getCnt());
            redisClient.expire(key,60*60*12);
        }catch (IOException e){
            logger.error("BlackMblService.addMblDayLimitBlack get redisclient failed ",e);
        }
    }
}

