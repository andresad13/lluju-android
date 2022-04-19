package com.zheng.liuju.bean;

public class RegisterRqert {

    String code_type;
    String fomart;
    String mobile;
    public RegisterRqert(String codeType,String fomart,String mobile) {

        this.code_type = codeType;
      //  this.fomart = fomart;
        this.mobile =fomart+ mobile;
    }
}
