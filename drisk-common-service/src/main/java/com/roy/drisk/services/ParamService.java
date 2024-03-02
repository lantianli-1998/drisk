package com.roy.drisk.services;

import java.math.BigDecimal;

/**
 * @author lantianli
 * @date 2021/11/9
 * @desc 获取所有业务参数的服务。
 *  业务参数应该是有个内部系统进行维护的，这里就用服务代替了
 */
public class ParamService implements GeneralService {

    //个人消费日限额 500
    public BigDecimal getMblDayLimit(){
        return new BigDecimal(500.00);
    }

    //LJ002: 同一手机号在${LJ001Param1}天内，转账次数超过${LJ001Param2}次，且转账金额总数低于${LJ001Param3}，则进入转账黑名单，禁止支付交易${LJ001Param4}小时
    public int getLJ002Param1(){
        return 3;
    }
    public int getLJ002Param2(){
        return 10;
    }
    public String getLJ002Param3(){
        return "100.00";
    }
    public int getLJ002Param4(){
        return 12;
    }

    //LG002: 手机号在${LG002Param1}天内连续登陆失败${LG002Param2}次，则加入黑名单，${LG002Param3}天内禁止登陆
    public int getLG002Param1(){
        return 1;
    }
    public int getLG002Param2(){
        return 5;
    }
    public int getLG002Param3(){
        return 3;
    }
}
