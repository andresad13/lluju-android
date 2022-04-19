package com.zheng.liuju.bean;

public class Bean {


    /**
     * message : success
     * retCode : 1
     * data : {"number":"15382157372","usertype":"APP用户","wxname":"wechaname","avatarUrl":"http://120.77.148.232:8089/img//2019/10/10/apss191028310281963.jpg","accountMy":0,"accountYajin":0,"jszcMobile":"10000","groupavatar":"","tel":"13813939898","buyHit":3,"openid":"15382157372","email":"1522419269@qq.com","defaultAccount":"1"}
     */

    private String message;
    private int retCode;
    private DataBean data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * number : 15382157372
         * usertype : APP用户
         * wxname : wechaname
         * avatarUrl : http://120.77.148.232:8089/img//2019/10/10/apss191028310281963.jpg
         * accountMy : 0.0
         * accountYajin : 0.0
         * jszcMobile : 10000
         * groupavatar :
         * tel : 13813939898
         * buyHit : 3
         * openid : 15382157372
         * email : 1522419269@qq.com
         * defaultAccount : 1
         */

        private String number;
        private String usertype;
        private String wxname;
        private String avatarUrl;
        private double accountMy;
        private double accountYajin;
        private String jszcMobile;
        private String groupavatar;
        private String tel;
        private int buyHit;
        private String openid;
        private String email;
        private String defaultAccount;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getUsertype() {
            return usertype;
        }

        public void setUsertype(String usertype) {
            this.usertype = usertype;
        }

        public String getWxname() {
            return wxname;
        }

        public void setWxname(String wxname) {
            this.wxname = wxname;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public double getAccountMy() {
            return accountMy;
        }

        public void setAccountMy(double accountMy) {
            this.accountMy = accountMy;
        }

        public double getAccountYajin() {
            return accountYajin;
        }

        public void setAccountYajin(double accountYajin) {
            this.accountYajin = accountYajin;
        }

        public String getJszcMobile() {
            return jszcMobile;
        }

        public void setJszcMobile(String jszcMobile) {
            this.jszcMobile = jszcMobile;
        }

        public String getGroupavatar() {
            return groupavatar;
        }

        public void setGroupavatar(String groupavatar) {
            this.groupavatar = groupavatar;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public int getBuyHit() {
            return buyHit;
        }

        public void setBuyHit(int buyHit) {
            this.buyHit = buyHit;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDefaultAccount() {
            return defaultAccount;
        }

        public void setDefaultAccount(String defaultAccount) {
            this.defaultAccount = defaultAccount;
        }
    }
}
