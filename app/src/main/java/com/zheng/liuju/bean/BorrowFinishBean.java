package com.zheng.liuju.bean;

public class BorrowFinishBean {


    /**
     * code : 1
     * msg : Rental success
     * data : {"number":6,"time":"2020-03-25 16:04:50","priceYajin":100,"logoUrl":"https://cdb.lluju.com/img2019/08/02/20190802095505.png"}
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
         * number : 6
         * time : 2020-03-25 16:04:50
         * priceYajin : 100.0
         * logoUrl : https://cdb.lluju.com/img2019/08/02/20190802095505.png
         */

        private int number;
        private String time;
        private double priceYajin;
        private String logoUrl;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getPriceYajin() {
            return priceYajin;
        }

        public void setPriceYajin(double priceYajin) {
            this.priceYajin = priceYajin;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }
    }
}
