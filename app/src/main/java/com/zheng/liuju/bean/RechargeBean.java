package com.zheng.liuju.bean;

public class RechargeBean {

    /**
     * code : 1
     * msg : user.success
     * data : {"rechargeId":7889,"dataId":"502190411-de18f345-d656-4867-bafd-82cff4badb4e"}
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
         * rechargeId : 7889
         * dataId : 502190411-de18f345-d656-4867-bafd-82cff4badb4e
         */

        private int rechargeId;
        private String dataId;

        public int getRechargeId() {
            return rechargeId;
        }

        public void setRechargeId(int rechargeId) {
            this.rechargeId = rechargeId;
        }

        public String getDataId() {
            return dataId;
        }

        public void setDataId(String dataId) {
            this.dataId = dataId;
        }
    }
}
