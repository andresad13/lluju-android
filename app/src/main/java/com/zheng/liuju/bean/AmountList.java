package com.zheng.liuju.bean;

public class AmountList {

    /**
     * code : 1
     * msg : success
     * data : {"account":0,"rechargeSM":"這裏是充值說明","price":"1,5,10,20,50,100,200"}
     * total : 1
     */

    private int code;
    private String msg;
    private DataBean data;
    private int total;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class DataBean {
        /**
         * account : 0.0
         * rechargeSM : 這裏是充值說明
         * price : 1,5,10,20,50,100,200
         */

        private double account;
        private String rechargeSM;
        private String price;

        public double getAccount() {
            return account;
        }

        public void setAccount(double account) {
            this.account = account;
        }

        public String getRechargeSM() {
            return rechargeSM;
        }

        public void setRechargeSM(String rechargeSM) {
            this.rechargeSM = rechargeSM;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
