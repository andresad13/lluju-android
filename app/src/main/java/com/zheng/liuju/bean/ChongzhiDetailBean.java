package com.zheng.liuju.bean;

public class ChongzhiDetailBean {


    /**
     * code : 1
     * msg : success
     * data : {"id":7874,"type":"0","userType":null,"orderId":"202003271054383434687","chongzhi":100,"addTime":"2020-03-27 10:54:38","status":"已支付","firstDigits":null,"lastFour":null}
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
         * id : 7874
         * type : 0
         * userType : null
         * orderId : 202003271054383434687
         * chongzhi : 100.0
         * addTime : 2020-03-27 10:54:38
         * status : 已支付
         * firstDigits : null
         * lastFour : null
         */

        private int id;
        private String type;
        private Object userType;
        private String orderId;
        private double chongzhi;
        private String addTime;
        private String status;
        private Object firstDigits;
        private Object lastFour;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getUserType() {
            return userType;
        }

        public void setUserType(Object userType) {
            this.userType = userType;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public double getChongzhi() {
            return chongzhi;
        }

        public void setChongzhi(double chongzhi) {
            this.chongzhi = chongzhi;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getFirstDigits() {
            return firstDigits;
        }

        public void setFirstDigits(Object firstDigits) {
            this.firstDigits = firstDigits;
        }

        public Object getLastFour() {
            return lastFour;
        }

        public void setLastFour(Object lastFour) {
            this.lastFour = lastFour;
        }
    }
}
