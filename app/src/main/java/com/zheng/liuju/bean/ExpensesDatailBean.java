package com.zheng.liuju.bean;

public class ExpensesDatailBean {


    /**
     * code : 1
     * msg : success
     * data : {"add_time":"2020-03-02 17:30:08","sourceId":"202003021722053917246","sourceType":1,"account_yajin":null,"account_my":-1,"id":2714}
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
         * add_time : 2020-03-02 17:30:08
         * sourceId : 202003021722053917246
         * sourceType : 1
         * account_yajin : null
         * account_my : -1.0
         * id : 2714
         */

        private String add_time;
        private String sourceId;
        private int sourceType;
        private Object account_yajin;
        private double account_my;
        private int id;

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }

        public int getSourceType() {
            return sourceType;
        }

        public void setSourceType(int sourceType) {
            this.sourceType = sourceType;
        }

        public Object getAccount_yajin() {
            return account_yajin;
        }

        public void setAccount_yajin(Object account_yajin) {
            this.account_yajin = account_yajin;
        }

        public double getAccount_my() {
            return account_my;
        }

        public void setAccount_my(double account_my) {
            this.account_my = account_my;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
