package com.zheng.liuju.view;

import com.zheng.liuju.bean.AmountList;
import com.zheng.liuju.bean.CardList;

import java.util.List;

public interface IRechaege extends IBaseView{
    //弹框提示
    void showDialog(String  context,boolean type);
    //网络加载的dialog
    void showLoading();
    void dismissLoading();
    void  upData();
    void  onErr();
    void upAmountList(AmountList.DataBean list);
    void upCardList(List<CardList.DataBean.CardsBean> cardList);
    void  rechargeSuccess(int type);
    void  rechargeOn(String rechargeId,String dataId);
   void  update(int code,String OrderID,String Recharge_id);
    void  Success(int code,String addTime);
    void  Success(String bayonetInt);
    void  Failure();
    void  FailureLease();
    void  getOrder();
    void  userInfo(double accountMy);
    void rechargeSuccessful(String rechargeId);
    void  getOrders();
}
