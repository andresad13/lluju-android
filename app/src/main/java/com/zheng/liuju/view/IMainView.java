package com.zheng.liuju.view;


import com.zheng.liuju.bean.CardList;
import com.zheng.liuju.bean.Infos;
import com.zheng.liuju.bean.RulesBean;
import com.zheng.liuju.bean.ShopBean;

import java.util.List;

public interface IMainView extends IBaseView {


    void upInformation(Infos.DataBean list);

    void  cardList(List<CardList.DataBean> data);
    //弹框提示
    void showDialog(String  context,boolean type);
    void upMap(List<ShopBean.DataBean> context);
    //网络加载的dialog
    void showLoading();
    void dismissLoading();

    void  permissionSuccess();
    void  cameraPermissionSuccess();
    void   scan(RulesBean bean);
    void  onErr();
}
