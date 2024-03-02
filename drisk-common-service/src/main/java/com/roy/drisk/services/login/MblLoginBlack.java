package com.roy.drisk.services.login;

/**
 * @author lantianli
 * @date 2021/11/11
 * @desc
 */
public class MblLoginBlack {
    private String mblno;
    private String txDt;
    private String txTm;
    private int limitDay;

    public MblLoginBlack(String mblno, String txDt, String txTm, int limitDay) {
        this.mblno = mblno;
        this.txDt = txDt;
        this.txTm = txTm;
        this.limitDay = limitDay;
    }

    public String getMblno() {
        return mblno;
    }

    public void setMblno(String mblno) {
        this.mblno = mblno;
    }

    public String getTxDt() {
        return txDt;
    }

    public void setTxDt(String txDt) {
        this.txDt = txDt;
    }

    public String getTxTm() {
        return txTm;
    }

    public void setTxTm(String txTm) {
        this.txTm = txTm;
    }

    public int getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(int limitDay) {
        this.limitDay = limitDay;
    }
}
