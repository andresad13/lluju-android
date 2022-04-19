package com.zheng.liuju.view;

import com.zheng.liuju.bean.OrderListBean;


import java.util.List;

public interface IRental extends IBaseView {
    //弹框提示
    void showDialog(String  context,boolean type);
    void upData(List<OrderListBean.DataBean> context, int type);
    //网络加载的dialog
    void showLoading();
    void dismissLoading();

    void  onErr();
}
