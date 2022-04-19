package com.zheng.liuju.bean;

public class ResultBean {
    /**
     * message : Requesting
     * retCode : 5016
     * data : {"orderId":"3102c751c44ba896652b1ce77f1801"}
     */

    private String msg;
    private int code;
    private ResultBean.DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ResultBean.DataBean getData() {
        return data;
    }

    public void setData(ResultBean.DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * orderId : 3102c751c44ba896652b1ce77f1801
         */

        private String slotNum;

        public String getSlotNum() {
            return slotNum;
        }

        public void setSlotNum(String slotNum) {
            this.slotNum = slotNum;
        }
    }
}
