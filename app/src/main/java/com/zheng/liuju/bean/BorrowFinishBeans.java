package com.zheng.liuju.bean;

public class BorrowFinishBeans {


    /**
     * code : 1
     * msg : user.success
     * data : {"id":7943,"type":"1","chongzhi":2000,"addTime":"2020-03-30 17:42:38","status":"已支付"}
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
         * id : 7943
         * type : 1
         * chongzhi : 2000.0
         * addTime : 2020-03-30 17:42:38
         * status : 已支付
         */

        private int id;
        private String type;
        private double chongzhi;
        private String addTime;
        private String status;

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
    }
}
