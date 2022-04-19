package com.zheng.liuju.api;

import com.zheng.liuju.bean.CardList;
import com.zheng.liuju.view.IBaseView;

import java.util.List;

public interface ICardList extends IBaseView {
    //弹框提示
    void showDialog(String  context,boolean type);
    //网络加载的dialog
    void showLoading();
    void dismissLoading();
    void  upData();
    void  onErr();
    void upInformation(List<CardList.DataBean.CardsBean> list);
    void refresh();
    void upAvatar(String url,String path);
}
