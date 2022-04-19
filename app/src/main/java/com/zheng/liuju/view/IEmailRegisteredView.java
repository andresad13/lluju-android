package com.zheng.liuju.view;

import com.zheng.liuju.bean.Bean;

public interface IEmailRegisteredView   extends IBaseView{
    void onSuccess(Bean.DataBean list);
    void onFail(int errCode, String errMsg);
    //弹框提示
    void showDialog(String  context,boolean type);
    //网络加载的dialog
    void showLoading();
    void dismissLoading();

    void showTime();
    void  reistered();
}
