package com.zheng.liuju.bean;

public class OrderDetailsBean {

    /**
     * code : 1
     * msg : Success
     * data : {"id":34,"orderNum":"202002251400212927095","shopName":"测试店铺","borrowTime":"2020-02-25 14:01:50","price":2,"orderState":"已归还","returnShopName":"测试店铺","returnTime":"2020-02-25 14:02:10","payPrice":0,"start":"2020-02-25 14:01:50","end":"2020-02-25 14:02:10","freeMoney":0,"shopAdr":"北京北京崇文區房管局","jfsm":"押金NT$100.0000，NT$2.0000/時，5分鐘內免費，超過5分鐘按NT$2.0000/時計費，不滿1小時按1小時按1小時算，日封頂NT$20.0000","useTime":0,"shopLogo":"https://www.sha77n.com/img/2020/02/20/6aen20025113595000.jpg","losToBuyTips":"租借過程中若充電寶遺失，可點擊下方的遺失購買按鈕，系統扣除充電寶的成本後會將剩余押金退還至原來賬戶","freeTime":5,"deviceType":"力量威单4G版本","deviceId":"3569195D03473897","batteryId":"3509075C8F261E75","priceYajin":100}
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
         * freeMoney : 0
         * shopAdr : 北京北京崇文區房管局
         * jfsm : 押金NT$100.0000，NT$2.0000/時，5分鐘內免費，超過5分鐘按NT$2.0000/時計費，不滿1小時按1小時按1小時算，日封頂NT$20.0000
         * useTime : 0
         * shopLogo : https://www.sha77n.com/img/2020/02/20/6aen20025113595000.jpg
         * losToBuyTips : 租借過程中若充電寶遺失，可點擊下方的遺失購買按鈕，系統扣除充電寶的成本後會將剩余押金退還至原來賬戶
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
        private double freeMoney;
        private String shopAdr;
        private String jfsm;
        private int useTime;
        private String shopLogo;
        private String losToBuyTips;
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

        public double getFreeMoney() {
            return freeMoney;
        }

        public void setFreeMoney(double freeMoney) {
            this.freeMoney = freeMoney;
        }

        public String getShopAdr() {
            return shopAdr;
        }

        public void setShopAdr(String shopAdr) {
            this.shopAdr = shopAdr;
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

        public String getShopLogo() {
            return shopLogo;
        }

        public void setShopLogo(String shopLogo) {
            this.shopLogo = shopLogo;
        }

        public String getLosToBuyTips() {
            return losToBuyTips;
        }

        public void setLosToBuyTips(String losToBuyTips) {
            this.losToBuyTips = losToBuyTips;
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
