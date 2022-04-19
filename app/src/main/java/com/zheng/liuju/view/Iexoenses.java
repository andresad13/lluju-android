package com.zheng.liuju.view;

import com.zheng.liuju.bean.ChongzhiListBean;
import com.zheng.liuju.bean.ExpensesBean;


import java.util.List;

public interface Iexoenses extends IBaseView{
    //弹框提示
    void showDialog(String context, boolean type);
    void upData(List<ExpensesBean.DataBean> context, int type);
    //网络加载的dialog
    void showLoading();
    void dismissLoading();

    void  onErr();
}
