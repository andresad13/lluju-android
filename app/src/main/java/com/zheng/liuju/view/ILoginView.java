package com.zheng.liuju.view;

public interface ILoginView extends IBaseView {
    //弹框提示
    void showDialog(String  context,boolean type);
    //网络加载的dialog
    void showLoading();
    void dismissLoading();
    void jumpFinish(Class clas);
    void  saveToken(String token,String openId);

}
