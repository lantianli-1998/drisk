package com.roy.drisk.services.blackMbl;

import java.math.BigInteger;

/**
 * @author lantianli
 * @date 2021/11/11
 * @desc
 */
public class DayLimitBlack {
    private String mblNo;
    private Double amt;
    private Long cnt;
    private int limithour;

    public DayLimitBlack(String mblNo, Double amt, Long cnt, int limithour) {
        this.mblNo = mblNo;
        this.amt = amt;
        this.cnt = cnt;
        this.limithour = limithour;
    }

    public String getMblNo() {
        return mblNo;
    }

    public void setMblNo(String mblNo) {
        this.mblNo = mblNo;
    }

    public Double getAmt() {
        return amt;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public Long getCnt() {
        return cnt;
    }

    public void setCnt(Long cnt) {
        this.cnt = cnt;
    }

    public int getLimithour() {
        return limithour;
    }

    public void setLimithour(int limithour) {
        this.limithour = limithour;
    }
}
