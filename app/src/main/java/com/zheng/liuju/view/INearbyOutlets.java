package com.zheng.liuju.view;

import com.zheng.liuju.bean.ShopBean;

import java.util.List;

public interface INearbyOutlets extends IBaseView {

    //弹框提示
    void showDialog(String  context,boolean type);
    void upData(List<ShopBean.DataBean> context,int type);
    //网络加载的dialog
    void showLoading();
    void dismissLoading();

    void  onErr();
}
