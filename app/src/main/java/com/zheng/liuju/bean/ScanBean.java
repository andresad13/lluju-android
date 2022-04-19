package com.zheng.liuju.bean;

public class ScanBean {


    /**
     * code : 1
     * msg : 发起订单
     * total : 0
     * orderID : 202004101144213962088
     * deviceID : 3568175CA20E6426
     * batteryID : 3509075CECA1F92A
     * payResult : 24642312
     */

    private int code;
    private String msg;
    private int total;
    private String orderID;
    private String deviceID;
    private String batteryID;
    private String payResult;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getBatteryID() {
        return batteryID;
    }

    public void setBatteryID(String batteryID) {
        this.batteryID = batteryID;
    }

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
    }
}
