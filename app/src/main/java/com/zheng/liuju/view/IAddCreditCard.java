package com.zheng.liuju.view;

import com.zheng.liuju.bean.Infos;

public interface IAddCreditCard extends IBaseView {
    //弹框提示
    void showDialog(String  context,boolean type);
    //网络加载的dialog
    void showLoading();
    void dismissLoading();
    void  upData(String cardId,String Msg);
    void  onErr();
    void upInformation(Infos.DataBean list);
    void upAvatar(String url,String path);

    void update(int code, String orderID, String payResult);

    void Success(String s);

    void Failure();

    void getOrder();

    void rechargeOn(String s, String dataId);

    void rechargeSuccess(int id, String addTime);

    void rechargeFailure();

    void getrechargeOrder();
}
