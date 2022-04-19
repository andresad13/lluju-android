package com.zheng.liuju.bean;

import java.util.List;

public class ChongzhiListBean {


    /**
     * code : 1
     * msg : success
     * data : [{"id":7949,"type":"0","orderId":"202003311001446176597","chongzhi":1100,"addTime":"2020-03-31 10:01:45","status":"待支付"},{"id":7948,"type":"0","orderId":"202003311001252127495","chongzhi":1100,"addTime":"2020-03-31 10:01:25","status":"待支付"},{"id":7945,"type":"0","orderId":"202003310913516162069","chongzhi":1100,"addTime":"2020-03-31 09:13:52","status":"待支付"},{"id":7935,"type":"1","chongzhi":2000,"addTime":"2020-03-30 17:07:53","status":"已支付"},{"id":7921,"type":"0","orderId":"202003301530144146427","chongzhi":1100,"addTime":"2020-03-30 15:30:14","status":"待支付"},{"id":7920,"type":"0","orderId":"202003301528379643032","chongzhi":1100,"addTime":"2020-03-30 15:28:38","status":"待支付"},{"id":7919,"type":"0","orderId":"202003301528111412716","chongzhi":1100,"addTime":"2020-03-30 15:28:11","status":"待支付"},{"id":7918,"type":"1","chongzhi":2000,"addTime":"2020-03-30 15:27:54","status":"待支付"},{"id":7917,"type":"1","chongzhi":2000,"addTime":"2020-03-30 15:26:54","status":"待支付"},{"id":7916,"type":"0","orderId":"202003301526016374487","chongzhi":1100,"addTime":"2020-03-30 15:26:02","status":"待支付"}]
     * total : 97
     * page : 1
     * maxpage : 10
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
         * id : 7949
         * type : 0
         * orderId : 202003311001446176597
         * chongzhi : 1100.0
         * addTime : 2020-03-31 10:01:45
         * status : 待支付
         */

        private int id;
        private String type;
        private String orderId;
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
    }
}
