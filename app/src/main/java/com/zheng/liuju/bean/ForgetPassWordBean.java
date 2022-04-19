package com.zheng.liuju.bean;

public class ForgetPassWordBean {

    private  String code;
    private String pwd;
    public  String  fomart;
    public  String   mobile;
    public ForgetPassWordBean(String fomart, String username, String code, String password) {
       // this.fomart =fomart;
        this.mobile  =fomart+ username;
        this.pwd = password;
        this.code = code;
    }
}
