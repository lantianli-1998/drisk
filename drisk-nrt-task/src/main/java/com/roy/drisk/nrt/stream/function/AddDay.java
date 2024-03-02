package com.roy.drisk.nrt.stream.function;

import org.apache.commons.lang.StringUtils;
import org.apache.flink.table.functions.ScalarFunction;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * @author roy
 * @date 2021/11/10
 * @desc 公共函数，在交易时间的基础上添加固定的天数
 */
public class AddDay extends ScalarFunction {

    /**
     *
     * @param txDt 交易日期
     * @param format 交易日期与返回日期的格式
     * @param days 要增加的天数
     * @return
     */
    public String eval(String txDt,String format, int days){
        LocalDate localDate = StringUtils.isEmpty(txDt) ? LocalDate.now()
                : LocalDate.parse(txDt, DateTimeFormatter.ofPattern(format));
        final Date date = Date.valueOf(localDate);
        Calendar cal =Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE,-days);
        return new SimpleDateFormat(format).format(cal.getTime());
    }

    public static void main(String[] args) {
        final AddDay addDay = new AddDay();
        System.out.println(addDay.eval("20160612", "yyyyMMdd", 1));
    }
}
