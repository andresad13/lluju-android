package com.zheng.liuju.presenter;


import android.util.Log;

import com.google.gson.Gson;
import com.zheng.liuju.api.Constant;
import com.zheng.liuju.api.RetrofitUtils;
import com.zheng.liuju.bean.AddCardList;
import com.zheng.liuju.bean.AmountList;
import com.zheng.liuju.bean.BorrowFinishBean;
import com.zheng.liuju.bean.BorrowFinishBeans;
import com.zheng.liuju.bean.CardList;
import com.zheng.liuju.bean.Info;
import com.zheng.liuju.bean.Infos;
import com.zheng.liuju.bean.RechargeBean;
import com.zheng.liuju.bean.ScanBean;
import com.zheng.liuju.view.IRechaege;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RechaegePresenter extends BasePresenter<IRechaege> {

    private final RetrofitUtils retrofitUtils;

    public RechaegePresenter(){
        retrofitUtils = RetrofitUtils.getInstence();
    }

    /**
     * 销毁的方法
     */
    public void onDestroy() {
        detachView();
        System.gc();
    }

    public void getAmountList(String token) {
        Map<String,String> map = new HashMap<>();
        getView().showLoading();
        String obj = new Gson().toJson(map);
        /* retrofit=new Retrofit.Builder().baseUrl(BASE_LOGIN_URL).build();*/
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<AmountList> data = retrofitUtils.getservice().amountList(token,body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<AmountList>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(AmountList bean) {

                        getView().dismissLoading();
                        Log.e("Infos",bean.toString()+"xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            getView().upAmountList(bean.getData());
                        } else if (bean.getCode() == Constant.MUTUAL){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else if (bean.getCode() == Constant.TOKEN_MISS){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else {
                            getView().showDialog(bean.getMsg(),false);
                        }

                    }

                    //异常
                    @Override
                    public void onError(Throwable e) {
                        getView().dismissLoading();
                        getView().showDialog(e.getMessage() + "", false);

                    }

                    //成功
                    @Override
                    public void onComplete() {

                    }
                });//Io子线程  网络请求
//主线程  更新控件
//subscribe：赋观察者的方法 Observer：观察者
    }

    public   void  getCardList(String  token,String openId){
        Map<String,String>  map  =  new HashMap<>();
        String obj = new Gson().toJson(map);
        getView().showLoading();
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<CardList> data = retrofitUtils.getservice().cardlist(token,body,openId);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<CardList>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(CardList bean) {

                        getView().dismissLoading();
                        Log.e("Infos",bean.toString()+"xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                           getView().upCardList(bean.getData().getCards());
                        } else if (bean.getCode() == Constant.MUTUAL){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else if (bean.getCode() == Constant.TOKEN_MISS){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else {
                            getView().showDialog(bean.getMsg(),false);
                        }

                    }

                    //异常
                    @Override
                    public void onError(Throwable e) {
                        getView().dismissLoading();
                        getView().showDialog(e.getMessage() + "", false);

                    }

                    //成功
                    @Override
                    public void onComplete() {

                    }
                });//Io子线程  网络请求
    }

    public    void   recharge(String token,String cardId,String moneyId){
        Map<String,String>  map  =  new HashMap<>();
        map.put("cardId",cardId);
        map.put("moneyId",moneyId);
        String obj = new Gson().toJson(map);
        getView().showLoading();
        /* retrofit=new Retrofit.Builder().baseUrl(BASE_LOGIN_URL).build();*/
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<AddCardList> data = retrofitUtils.getservice().recharge(token,body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<AddCardList>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(AddCardList bean) {

                        getView().dismissLoading();
                        Log.e("Infos",bean.toString()+"xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            getView().rechargeSuccess(bean.getCode());
                            getView().showDialog(bean.getMsg(),true);
                        } else if (bean.getCode() == Constant.TOKEN_MISS){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else if (bean.getCode() == Constant.MUTUAL){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else {
                            getView().showDialog(bean.getMsg(),false);
                        }

                    }

                    //异常
                    @Override
                    public void onError(Throwable e) {
                        getView().dismissLoading();
                        getView().showDialog(e.getMessage() + "", false);

                    }

                    //成功
                    @Override
                    public void onComplete() {

                    }
                });//Io子线程  网络请求
    }


    public    void   recharges(String token,String moneyId,String cardId){
        Map<String,String>  map  =  new HashMap<>();
       // map.put("bankcard_id",cardId);
        map.put("money",moneyId);

        map.put("cardId",cardId);
        String obj = new Gson().toJson(map);
        getView().showLoading();
        /* retrofit=new Retrofit.Builder().baseUrl(BASE_LOGIN_URL).build();*/
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<RechargeBean> data = retrofitUtils.getservice().recharges(token,body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<RechargeBean>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(RechargeBean bean) {

                   //     getView().dismissLoading();
                        Log.e("Infos",bean.toString()+"xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            Log.e("Infos",bean.getData().getDataId()+"xxxxxxxx");
                           getView().rechargeOn(bean.getData().getRechargeId()+"",bean.getData().getDataId());
                            //getView().showDialog(bean.getMsg(),true);
                        } /*else  if (bean.getCode() == Constant.AUTHORIZATION) {

                            getView().rechargeSuccessful(bean.getData().getRechargeId());
                           getView().showDialog(bean.getMsg(),true);
                        } */else if (bean.getCode() == Constant.MUTUAL){
                            getView().dismissLoading();
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();

                        }else if (bean.getCode() == Constant.TOKEN_MISS){

                            getView().dismissLoading();
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();

                        }else {
                            getView().dismissLoading();
                            getView().showDialog(bean.getMsg(),false);
                        }

                    }

                    //异常
                    @Override
                    public void onError(Throwable e) {
                        getView().dismissLoading();
                        getView().showDialog(e.getMessage() + "", false);

                    }

                    //成功
                    @Override
                    public void onComplete() {

                    }
                });//Io子线程  网络请求
    }


    public   void   getResult(String token,String orderId,String paymentId){
        Map<String,String> map  =  new HashMap<>();

        String obj = new Gson().toJson(map);


        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<BorrowFinishBeans> data = retrofitUtils.getservice().rechargefinish(orderId,paymentId,token,body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<BorrowFinishBeans>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(BorrowFinishBeans bean) {


                        Log.e("Infos",bean.toString()+"xxxxxxxx");
                        if (bean.getCode() ==  Constant.SUCCESS) {
                            //  getView().showDialog(bean.getMessage(),true);
                          getView().Success(bean.getData().getId(),bean.getData().getAddTime());
                        } else if (bean.getCode() == Constant.TOKEN_MISS){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else if (bean.getCode() == Constant.MUTUAL){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else  if (bean.getCode() == Constant.FAILURE){
                            getView().Failure();
                        } else {
                            getView().getOrder();
                        }

                    }

                    //异常
                    @Override
                    public void onError(Throwable e) {
                        getView().dismissLoading();
                        getView().showDialog(e.getMessage() + "", false);

                    }

                    //成功
                    @Override
                    public void onComplete() {

                    }
                });//Io子线程  网络请求

    }


    public void info(String latitude, String longitude, String token) {

        Info info = new Info(latitude, longitude);
        Gson gson = new Gson();
        String obj = gson.toJson(info);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj);
        Observable<Infos> data = retrofitUtils.getservice().info(token, body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<Infos>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(Infos bean) {


                        Log.e("Infos", bean.toString() + "xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                        getView().userInfo(bean.getData().getAccountMy());
                        } else if (bean.getCode() == Constant.TOKEN_MISS) {
                            getView().dismissLoading();
                           getView().showDialog(bean.getMsg(), false);
                            getView(). onErr();
                        } else if (bean.getCode() == Constant.MUTUAL) {
                            getView().dismissLoading();
                            getView().showDialog(bean.getMsg(), false);
                            getView(). onErr();
                        } else {
                            getView().showDialog(bean.getMsg(), false);

                        }

                    }

                    //异常
                    @Override
                    public void onError(Throwable e) {
                        getView().showDialog(e.getMessage() + "", false);

                    }

                    //成功
                    @Override
                    public void onComplete() {

                    }
                });//Io子线程  网络请求
//主线程  更新控件
//subscribe：赋观察者的方法 Observer：观察者
    }


    public   void  getBorrow(String  token,String latitude,String longitude,String qrcode){
        Map<String,String> map  =  new HashMap<>();

        map.put("latitude",latitude);
        map.put("longitude",longitude);
        map.put("id",qrcode);
        String obj = new Gson().toJson(map);
        getView().showLoading();

        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<ScanBean> data = retrofitUtils.getservice().scan(token,body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<ScanBean>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(ScanBean bean) {

                        getView().dismissLoading();
                        Log.e("Infos",bean.toString()+"xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            //  getView().showDialog(bean.getMessage(),true);
                    //  getView().update(bean.getCode(),bean.getOrderID(),bean.getRecharge_id()+"");
                        } else if (bean.getCode() == Constant.MUTUAL){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else if (bean.getCode() == Constant.TOKEN_MISS){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else if (bean.getCode() == Constant.AUTHORIZATION) {
                            getView().update(bean.getCode(),bean.getOrderID(),"");
                        } else {
                            getView().showDialog(bean.getMsg(),false);
                        }

                    }

                    //异常
                    @Override
                    public void onError(Throwable e) {
                        getView().dismissLoading();
                        getView().showDialog(e.getMessage() + "", false);

                    }

                    //成功
                    @Override
                    public void onComplete() {

                    }
                });//Io子线程  网络请求
    }



    public   void   getResults(String token,String orderId){
        Map<String,String> map  =  new HashMap<>();

        String obj = new Gson().toJson(map);


        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<BorrowFinishBean> data = retrofitUtils.getservice().result(orderId,token,body,"");
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<BorrowFinishBean>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(BorrowFinishBean bean) {


                        Log.e("Infos",bean.toString()+"xxxxxxxx");
                        if (bean.getCode() ==  Constant.SUCCESS) {
                            //  getView().showDialog(bean.getMessage(),true);
                            getView().Success(bean.getData().getNumber()+"");
                        } else if (bean.getCode() == Constant.TOKEN_MISS){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else if (bean.getCode() == Constant.MUTUAL){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else  if (bean.getCode() == Constant.FAILURE){
                            getView().FailureLease();
                        } else {
                            getView().getOrders();
                        }

                    }

                    //异常
                    @Override
                    public void onError(Throwable e) {
                        getView().dismissLoading();
                        getView().showDialog(e.getMessage() + "", false);

                    }

                    //成功
                    @Override
                    public void onComplete() {

                    }
                });//Io子线程  网络请求

    }

}
