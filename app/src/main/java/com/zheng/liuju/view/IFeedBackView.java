package com.zheng.liuju.view;

import com.zheng.liuju.bean.FeedBackData;

import java.util.List;

public interface IFeedBackView extends IBaseView{
    //弹框提示
    void showDialog(String  context,boolean type);
    //网络加载的dialog
    void showLoading();
    void dismissLoading();
    void  onErr();
    void  upInformation(List<FeedBackData.DataBean> mlist);
    void   cameraPermissionSuccess();
}
