package com.zheng.liuju.bean;

public class ShopDetailsBean {

    /**
     * code : 1
     * msg : Success
     * data : {"id":8068,"shopId":"AK3387KRZK","shopname":"小刘测试","tel":"13875530990","lat":"22.626082","lng":"113.860870","freetime":5,"price":1,"priceMax":20,"account":99,"addtime":1566288187000,"count":0,"info":"","distance":0,"address":"广东深圳宝安区西乡街道鹤洲社区东方向约0.88公里","logo":"","returnCount":0,"batteryCount":0,"onlines":0,"banner":"https://cdb.lluju.com/img2019/08/02/20190802095505.png"}
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
         * id : 8068
         * shopId : AK3387KRZK
         * shopname : 小刘测试
         * tel : 13875530990
         * lat : 22.626082
         * lng : 113.860870
         * freetime : 5
         * price : 1.0
         * priceMax : 20.0
         * account : 99.0
         * addtime : 1566288187000
         * count : 0
         * info :
         * distance : 0.0
         * address : 广东深圳宝安区西乡街道鹤洲社区东方向约0.88公里
         * logo :
         * returnCount : 0
         * batteryCount : 0
         * onlines : 0
         * banner : https://cdb.lluju.com/img2019/08/02/20190802095505.png
         */

        private int id;
        private String shopId;
        private String shopname;
        private String tel;
        private String lat;
        private String lng;
        private int freetime;
        private double price;
        private double priceMax;
        private double account;
        private long addtime;
        private int count;
        private String info;
        private double distance;
        private String address;
        private String logo;
        private int returnCount;
        private int batteryCount;
        private int onlines;
        private String banner;

        public String getServiceTime() {
            return serviceTime;
        }

        public void setServiceTime(String serviceTime) {
            this.serviceTime = serviceTime;
        }

        private String serviceTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public int getFreetime() {
            return freetime;
        }

        public void setFreetime(int freetime) {
            this.freetime = freetime;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getPriceMax() {
            return priceMax;
        }

        public void setPriceMax(double priceMax) {
            this.priceMax = priceMax;
        }

        public double getAccount() {
            return account;
        }

        public void setAccount(double account) {
            this.account = account;
        }

        public long getAddtime() {
            return addtime;
        }

        public void setAddtime(long addtime) {
            this.addtime = addtime;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public int getReturnCount() {
            return returnCount;
        }

        public void setReturnCount(int returnCount) {
            this.returnCount = returnCount;
        }

        public int getBatteryCount() {
            return batteryCount;
        }

        public void setBatteryCount(int batteryCount) {
            this.batteryCount = batteryCount;
        }

        public int getOnlines() {
            return onlines;
        }

        public void setOnlines(int onlines) {
            this.onlines = onlines;
        }

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }
    }
}
