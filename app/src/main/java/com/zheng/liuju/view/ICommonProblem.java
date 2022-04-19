package com.zheng.liuju.view;

import com.zheng.liuju.bean.CommonBean;

import java.util.List;

public interface ICommonProblem extends IBaseView{
    //弹框提示
    void showDialog(String  context,boolean type);
    //网络加载的dialog
    void showLoading();
    void dismissLoading();
   void  upInformation(List<CommonBean.DataBean>  list);
    void  onErr();
}
