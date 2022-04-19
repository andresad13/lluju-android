package com.zheng.liuju.bean;

public class RegisterBean {
    String email;
 //   String usertype;
   // String share_code;
    String mobile;
    String pwd;
    String type;
  //  String pwd2;
    String code;
    public RegisterBean(String email,String fomart,String invitationCode,String mobile,String verificationCode,String pwd) {

        this.email = email;
    //    this.usertype = "1";
      //  this.share_code = invitationCode;
        this.mobile =fomart+ mobile;
        this.pwd = pwd;
       this.type= "1";
        this.code = verificationCode;
    }
}
