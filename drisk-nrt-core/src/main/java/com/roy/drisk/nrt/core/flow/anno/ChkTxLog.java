package com.roy.drisk.nrt.core.flow.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lantianli
 * @date 2021/11/8
 * @desc
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ChkTxLog {
    //要处理的交易类型
    String[] txTyp() default {};
    //要处理的消息结果
    String txRst() default "ALL";
    //处理顺序
    int order() default 0;
}
