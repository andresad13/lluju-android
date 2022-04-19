package com.zheng.liuju.bean;

public  class LoginBean {
    public String fomart;
    public String mobile;
    public String latitude;
    public String longitude;
    public String pwd;
    public LoginBean(String fomart, String mobile, String pwd, String latitude, String longitude) {

        this.mobile =fomart+mobile;
        this.pwd =pwd;
        this.latitude =latitude;
        this.longitude =longitude;
    }
}
