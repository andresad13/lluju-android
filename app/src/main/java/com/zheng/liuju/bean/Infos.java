package com.zheng.liuju.bean;

public class Infos {


    /**
     * code : 1
     * msg : success
     * data : {"number":"4915382157372","usertype":"APP用户","wxname":"4915382157372","avatarUrl":null,"accountMy":null,"accountYajin":null,"jszcMobile":"10000","groupavatar":null,"tel":"13813939898","buyHit":null,"openid":"4915382157372","email":"1522419263@qq.com","defaultAccount":"1"}
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
         * number : 4915382157372
         * usertype : APP用户
         * wxname : 4915382157372
         * avatarUrl : null
         * accountMy : null
         * accountYajin : null
         * jszcMobile : 10000
         * groupavatar : null
         * tel : 13813939898
         * buyHit : null
         * openid : 4915382157372
         * email : 1522419263@qq.com
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

        public String getInvateCode() {
            return invateCode;
        }

        public void setInvateCode(String invateCode) {
            this.invateCode = invateCode;
        }

        private String invateCode;

        public String getDownloadAppUrl() {
            return downloadAppUrl;
        }

        public void setDownloadAppUrl(String downloadAppUrl) {
            this.downloadAppUrl = downloadAppUrl;
        }

        private String  downloadAppUrl;

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
