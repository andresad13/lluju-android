package com.zheng.liuju.view;

public interface IAgreement extends IBaseView {
    //弹框提示
    void showDialog(String  context,boolean type);
    //网络加载的dialog
    void showLoading();
    void dismissLoading();
    void  upData(String  data);
    void  onErr();


}
