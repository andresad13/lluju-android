package com.zheng.liuju.bean;

import java.util.List;

public class ShopBean {

    /**
     * code : 1
     * data : [{"account":99,"address":"广东深圳宝安区西乡街道鹤洲社区东方向约0.88公里","addtime":1566288187000,"batteryCount":0,"count":0,"distance":2128.1,"freetime":5,"id":8068,"info":"","lat":"22.626082","lng":"113.860870","logo":"","onlines":0,"price":1,"priceMax":20,"returnCount":0,"shopId":"AK3387KRZK","shopname":"小刘测试","tel":"13875530990"},{"account":99,"address":"广东深圳宝安区西乡街道鹤洲社区东南方向约1.02公里","addtime":1565852939000,"batteryCount":0,"count":0,"distance":2128.1,"freetime":5,"id":8058,"info":"123","lat":"22.626082","lng":"113.860870","logo":"https://cdb.lluju.com/img2019/08/15/20190815181944.png","no":1,"onlines":0,"price":1,"priceMax":20,"returnCount":0,"shopId":"THX2VHH8MG","shopname":"adming","tel":"13141614589"},{"account":100,"address":"广东深圳宝安区西乡街道鹤洲深业沙河U 中心","addtime":1565936711000,"batteryCount":0,"count":0,"distance":3231.3,"freetime":1,"id":8061,"info":"112132","lat":"22.586996","lng":"113.877319","logo":"https://cdb.lluju.com/img2019/08/22/20190822095130.png","onlines":0,"price":1,"priceMax":10,"returnCount":0,"shopId":"63MAIE76ME","shopname":"测试11","tel":"15581308413"}]
     * msg : success
     * total : 1
     */

    private int code;
    private String msg;
    private int total;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * account : 99.0
         * address : 广东深圳宝安区西乡街道鹤洲社区东方向约0.88公里
         * addtime : 1566288187000
         * batteryCount : 0
         * count : 0
         * distance : 2128.1
         * freetime : 5
         * id : 8068
         * info :
         * lat : 22.626082
         * lng : 113.860870
         * logo :
         * onlines : 0
         * price : 1.0
         * priceMax : 20.0
         * returnCount : 0
         * shopId : AK3387KRZK
         * shopname : 小刘测试
         * tel : 13875530990
         * no : 1
         */

        private double account;
        private String address;
        private long addtime;
        private int batteryCount;
        private int count;
        private double distance;
        private int freetime;
        private int id;
        private String info;
        private String lat;
        private String lng;
        private String logo;
        private int onlines;
        private double price;
        private double priceMax;
        private int returnCount;
        private String shopId;
        private String shopname;
        private String tel;
        private int no;
        private String  phangye;

        public String getPhangye() {
            return phangye;
        }

        public void setPhangye(String phangye) {
            this.phangye = phangye;
        }



        public String getServiceTime() {
            return serviceTime;
        }

        public void setServiceTime(String serviceTime) {
            this.serviceTime = serviceTime;
        }

        private String serviceTime;

        public double getAccount() {
            return account;
        }

        public void setAccount(double account) {
            this.account = account;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public long getAddtime() {
            return addtime;
        }

        public void setAddtime(long addtime) {
            this.addtime = addtime;
        }

        public int getBatteryCount() {
            return batteryCount;
        }

        public void setBatteryCount(int batteryCount) {
            this.batteryCount = batteryCount;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public int getFreetime() {
            return freetime;
        }

        public void setFreetime(int freetime) {
            this.freetime = freetime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
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

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public int getOnlines() {
            return onlines;
        }

        public void setOnlines(int onlines) {
            this.onlines = onlines;
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

        public int getReturnCount() {
            return returnCount;
        }

        public void setReturnCount(int returnCount) {
            this.returnCount = returnCount;
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

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }
    }
}
