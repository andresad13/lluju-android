package com.zheng.liuju.bean;

import java.util.List;

public class OrderListBean {

    /**
     * code : 1
     * msg : success
     * data : [{"id":34,"orderNum":"202002251400212927095","shopName":"测试店铺","borrowTime":"2020-02-25 14:01:50","price":2,"orderState":"已归还","returnShopName":"测试店铺","returnTime":"2020-02-25 14:02:10","payPrice":0,"start":"2020-02-25 14:01:50","end":"2020-02-25 14:02:10","jfsm":"押金NT$100.0000，NT$2.0000/時，5分鐘內免費，超過5分鐘按NT$2.0000/時計費，不滿1小時按1小時按1小時算，日封頂NT$20.0000","useTime":0,"freeTime":5,"deviceType":"力量威单4G版本","deviceId":"3569195D03473897","batteryId":"3509075C8F261E75","priceYajin":100},{"id":33,"orderNum":"202002251028270585578","shopName":"测试店铺","borrowTime":"2020-02-25 10:29:20","price":2,"orderState":"已归还","returnShopName":"测试店铺","returnTime":"2020-02-25 10:29:48","payPrice":0,"start":"2020-02-25 10:29:20","end":"2020-02-25 10:29:48","jfsm":"押金NT$100.0000，NT$2.0000/時，5分鐘內免費，超過5分鐘按NT$2.0000/時計費，不滿1小時按1小時按1小時算，日封頂NT$20.0000","useTime":0,"freeTime":5,"deviceType":"力量威单4G版本","deviceId":"3569195D03473897","batteryId":"3509075CECA877AE","priceYajin":100},{"id":31,"orderNum":"202002251024289461548","shopName":"测试店铺","borrowTime":"2020-02-25 10:25:14","price":2,"orderState":"已归还","returnShopName":"测试店铺","returnTime":"2020-02-25 10:26:23","payPrice":0,"start":"2020-02-25 10:25:14","end":"2020-02-25 10:26:23","jfsm":"押金NT$100.0000，NT$2.0000/時，5分鐘內免費，超過5分鐘按NT$2.0000/時計費，不滿1小時按1小時按1小時算，日封頂NT$20.0000","useTime":1,"freeTime":5,"deviceType":"力量威单4G版本","deviceId":"3569195D03473897","batteryId":"3509075CECC7F249","priceYajin":100}]
     * total : 3
     * page : 1
     * maxpage : 1
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
         * id : 34
         * orderNum : 202002251400212927095
         * shopName : 测试店铺
         * borrowTime : 2020-02-25 14:01:50
         * price : 2.0
         * orderState : 已归还
         * returnShopName : 测试店铺
         * returnTime : 2020-02-25 14:02:10
         * payPrice : 0.0
         * start : 2020-02-25 14:01:50
         * end : 2020-02-25 14:02:10
         * jfsm : 押金NT$100.0000，NT$2.0000/時，5分鐘內免費，超過5分鐘按NT$2.0000/時計費，不滿1小時按1小時按1小時算，日封頂NT$20.0000
         * useTime : 0
         * freeTime : 5
         * deviceType : 力量威单4G版本
         * deviceId : 3569195D03473897
         * batteryId : 3509075C8F261E75
         * priceYajin : 100.0
         */

        private int id;
        private String orderNum;
        private String shopName;
        private String borrowTime;
        private double price;
        private String orderState;
        private String returnShopName;
        private String returnTime;
        private double payPrice;
        private String start;
        private String end;
        private String jfsm;
        private int useTime;
        private int freeTime;
        private String deviceType;
        private String deviceId;
        private String batteryId;
        private double priceYajin;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getBorrowTime() {
            return borrowTime;
        }

        public void setBorrowTime(String borrowTime) {
            this.borrowTime = borrowTime;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getOrderState() {
            return orderState;
        }

        public void setOrderState(String orderState) {
            this.orderState = orderState;
        }

        public String getReturnShopName() {
            return returnShopName;
        }

        public void setReturnShopName(String returnShopName) {
            this.returnShopName = returnShopName;
        }

        public String getReturnTime() {
            return returnTime;
        }

        public void setReturnTime(String returnTime) {
            this.returnTime = returnTime;
        }

        public double getPayPrice() {
            return payPrice;
        }

        public void setPayPrice(double payPrice) {
            this.payPrice = payPrice;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getJfsm() {
            return jfsm;
        }

        public void setJfsm(String jfsm) {
            this.jfsm = jfsm;
        }

        public int getUseTime() {
            return useTime;
        }

        public void setUseTime(int useTime) {
            this.useTime = useTime;
        }

        public int getFreeTime() {
            return freeTime;
        }

        public void setFreeTime(int freeTime) {
            this.freeTime = freeTime;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getBatteryId() {
            return batteryId;
        }

        public void setBatteryId(String batteryId) {
            this.batteryId = batteryId;
        }

        public double getPriceYajin() {
            return priceYajin;
        }

        public void setPriceYajin(double priceYajin) {
            this.priceYajin = priceYajin;
        }
    }
}
