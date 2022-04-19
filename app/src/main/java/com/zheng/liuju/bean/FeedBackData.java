package com.zheng.liuju.bean;

import java.util.List;

public class FeedBackData {


    /**
     * code : 1
     * msg : Success
     * data : [{"id":25,"name":"订单出错"},{"id":24,"name":"主机故障"},{"id":23,"name":"归还失败"},{"id":22,"name":"借出失败"},{"id":1,"name":"无法充电"}]
     * total : 5
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
         * id : 25
         * name : 订单出错
         */

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
