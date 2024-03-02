package com.roy.drisk.nrt.core;

/**
 * @author roy
 * @date 2021/11/8
 * @desc 交易日志对象，综合所有的交易属性。
 *  需要业务端框架配合，这里简化设计。
 */
public class TxLog {

    /**
     * 公用部分
     */
    public String REQUEST_ID;
    public String TX_RST;
    public String CHECK_TIME;
    public String TABLE_NAME;
    public String errorMessage = "";
    public String RULE_TRIGGERED;
    public String BUSI_CODE;
    /**
     * UENV区
     */
    public String IP = "";
    public String OS = "";
    public String APP_NM = "";
    public String APPVER = "";
    public String MBL_MAC = "";
    public String MBL_BRAND = "";
    public String TERML_ID = "";
    public String TERML_PRV_CD = "";
    public String TERML_CITY_CD = "";
    /**
     * BSINF区域
     **/
    public String TX_CD = "";
    public String TX_TYP = "";
    public String BUS_CNL = "";
    public String TX_DT = "";
    public String TX_AMT = "";
    public String TX_TM = "";
    public String USR_NO = "";
    public String MBL_NO = "";
    public String MESSAGE_ID = "";
    public String JRN_NO = "";

    @Override
    public String toString() {
        return "TxLog{" +
                "REQUEST_ID='" + REQUEST_ID + '\'' +
                ", TX_RST='" + TX_RST + '\'' +
                ", CHECK_TIME='" + CHECK_TIME + '\'' +
                ", TABLE_NAME='" + TABLE_NAME + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", RULE_TRIGGERED='" + RULE_TRIGGERED + '\'' +
                ", BUSI_CODE='" + BUSI_CODE + '\'' +
                ", IP='" + IP + '\'' +
                ", OS='" + OS + '\'' +
                ", APP_NM='" + APP_NM + '\'' +
                ", APPVER='" + APPVER + '\'' +
                ", MBL_MAC='" + MBL_MAC + '\'' +
                ", MBL_BRAND='" + MBL_BRAND + '\'' +
                ", TERML_ID='" + TERML_ID + '\'' +
                ", TERML_PRV_CD='" + TERML_PRV_CD + '\'' +
                ", TERML_CITY_CD='" + TERML_CITY_CD + '\'' +
                ", TX_CD='" + TX_CD + '\'' +
                ", TX_TYP='" + TX_TYP + '\'' +
                ", BUS_CNL='" + BUS_CNL + '\'' +
                ", TX_DT='" + TX_DT + '\'' +
                ", TX_AMT='" + TX_AMT + '\'' +
                ", TX_TM='" + TX_TM + '\'' +
                ", USR_NO='" + USR_NO + '\'' +
                ", MBL_NO='" + MBL_NO + '\'' +
                ", MESSAGE_ID='" + MESSAGE_ID + '\'' +
                ", JRN_NO='" + JRN_NO + '\'' +
                '}';
    }


    public String getREQUEST_ID() {
        return REQUEST_ID;
    }

    public void setREQUEST_ID(String REQUEST_ID) {
        this.REQUEST_ID = REQUEST_ID;
    }

    public String getTX_RST() {
        return TX_RST;
    }

    public void setTX_RST(String TX_RST) {
        this.TX_RST = TX_RST;
    }

    public String getCHECK_TIME() {
        return CHECK_TIME;
    }

    public void setCHECK_TIME(String CHECK_TIME) {
        this.CHECK_TIME = CHECK_TIME;
    }

    public String getTABLE_NAME() {
        return TABLE_NAME;
    }

    public void setTABLE_NAME(String TABLE_NAME) {
        this.TABLE_NAME = TABLE_NAME;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRULE_TRIGGERED() {
        return RULE_TRIGGERED;
    }

    public void setRULE_TRIGGERED(String RULE_TRIGGERED) {
        this.RULE_TRIGGERED = RULE_TRIGGERED;
    }

    public String getBUSI_CODE() {
        return BUSI_CODE;
    }

    public void setBUSI_CODE(String BUSI_CODE) {
        this.BUSI_CODE = BUSI_CODE;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public String getAPP_NM() {
        return APP_NM;
    }

    public void setAPP_NM(String APP_NM) {
        this.APP_NM = APP_NM;
    }

    public String getAPPVER() {
        return APPVER;
    }

    public void setAPPVER(String APPVER) {
        this.APPVER = APPVER;
    }

    public String getMBL_MAC() {
        return MBL_MAC;
    }

    public void setMBL_MAC(String MBL_MAC) {
        this.MBL_MAC = MBL_MAC;
    }

    public String getMBL_BRAND() {
        return MBL_BRAND;
    }

    public void setMBL_BRAND(String MBL_BRAND) {
        this.MBL_BRAND = MBL_BRAND;
    }

    public String getTERML_ID() {
        return TERML_ID;
    }

    public void setTERML_ID(String TERML_ID) {
        this.TERML_ID = TERML_ID;
    }

    public String getTERML_PRV_CD() {
        return TERML_PRV_CD;
    }

    public void setTERML_PRV_CD(String TERML_PRV_CD) {
        this.TERML_PRV_CD = TERML_PRV_CD;
    }

    public String getTERML_CITY_CD() {
        return TERML_CITY_CD;
    }

    public void setTERML_CITY_CD(String TERML_CITY_CD) {
        this.TERML_CITY_CD = TERML_CITY_CD;
    }

    public String getTX_CD() {
        return TX_CD;
    }

    public void setTX_CD(String TX_CD) {
        this.TX_CD = TX_CD;
    }

    public String getTX_TYP() {
        return TX_TYP;
    }

    public void setTX_TYP(String TX_TYP) {
        this.TX_TYP = TX_TYP;
    }

    public String getBUS_CNL() {
        return BUS_CNL;
    }

    public void setBUS_CNL(String BUS_CNL) {
        this.BUS_CNL = BUS_CNL;
    }

    public String getTX_DT() {
        return TX_DT;
    }

    public void setTX_DT(String TX_DT) {
        this.TX_DT = TX_DT;
    }

    public String getTX_AMT() {
        return TX_AMT;
    }

    public void setTX_AMT(String TX_AMT) {
        this.TX_AMT = TX_AMT;
    }

    public String getTX_TM() {
        return TX_TM;
    }

    public void setTX_TM(String TX_TM) {
        this.TX_TM = TX_TM;
    }

    public String getUSR_NO() {
        return USR_NO;
    }

    public void setUSR_NO(String USR_NO) {
        this.USR_NO = USR_NO;
    }

    public String getMBL_NO() {
        return MBL_NO;
    }

    public void setMBL_NO(String MBL_NO) {
        this.MBL_NO = MBL_NO;
    }

    public String getMESSAGE_ID() {
        return MESSAGE_ID;
    }

    public void setMESSAGE_ID(String MESSAGE_ID) {
        this.MESSAGE_ID = MESSAGE_ID;
    }

    public String getJRN_NO() {
        return JRN_NO;
    }

    public void setJRN_NO(String JRN_NO) {
        this.JRN_NO = JRN_NO;
    }
}
