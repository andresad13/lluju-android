package com.zheng.liuju.view;

import com.zheng.liuju.bean.CardList;
import com.zheng.liuju.bean.RulesBean;

import java.util.List;

public interface ICharging extends IBaseView {
    //弹框提示
    void showDialog(String  context,boolean type);
    //网络加载的dialog
    void showLoading();
    void dismissLoading();
    void  onErr();
    void upCardList(List<CardList.DataBean.CardsBean> cardList);
    void update(int  type,String orderId,String RechargeId);
    void Success(String num);
    void Failure();
    void getOrder();
    void scan(RulesBean bean);

}
