package com.zheng.liuju.bean;

import java.util.List;

public class ExpensesBean {


    /**
     * code : 1
     * msg : user.success
     * data : [{"add_time":"2020-03-27 17:43:46","sourceId":"Deposit withdrawals","sourceType":4,"account_yajin":-100,"account_my":0,"id":2827},{"add_time":"2020-03-27 10:55:51","sourceId":"202003271054383434687","sourceType":6,"account_yajin":100,"account_my":-100,"id":2823}]
     * total : 2
     * page : 0
     * maxpage : 0
     */

    private int code;
    private String msg;
    private int total;
    private int page;
    private int maxpage;
    private List<DataBean> data;

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getMaxpage() {
        return maxpage;
    }

    public void setMaxpage(int maxpage) {
        this.maxpage = maxpage;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * add_time : 2020-03-27 17:43:46
         * sourceId : Deposit withdrawals
         * sourceType : 4
         * account_yajin : -100.0
         * account_my : 0.0
         * id : 2827
         */

        private String add_time;
        private String sourceId;
        private int sourceType;
        private double account_yajin;
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

        public double getAccount_yajin() {
            return account_yajin;
        }

        public void setAccount_yajin(double account_yajin) {
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
