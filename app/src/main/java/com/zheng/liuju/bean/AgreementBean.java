package com.zheng.liuju.bean;

public class AgreementBean {

    /**
     * message : success
     * retCode : 1
     * data : {"content":"&lt;p&gt;This is the agreement.This is the agreement.This is the agreement.This is the agreement.This is the agreement.This is the agreement.This is the agreement.&lt;/p&gt;"}
     */

    private String msg;
    private int code;
    private String data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setRetCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * content : &lt;p&gt;This is the agreement.This is the agreement.This is the agreement.This is the agreement.This is the agreement.This is the agreement.This is the agreement.&lt;/p&gt;
         */

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
