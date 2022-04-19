package com.zheng.liuju.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.uuzuche.lib_zxing.decoding.Intents;
import com.zheng.liuju.api.Constant;
import com.zheng.liuju.api.RetrofitUtils;
import com.zheng.liuju.bean.BorrowFinishBean;
import com.zheng.liuju.bean.CardList;
import com.zheng.liuju.bean.RulesBean;
import com.zheng.liuju.bean.ScanBean;
import com.zheng.liuju.bean.payResultBean;
import com.zheng.liuju.view.ICharging;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class ChargingPresenter extends BasePresenter<ICharging>   {

    private final RetrofitUtils retrofitUtils;
    private String rechargeId;

    public ChargingPresenter(){
        retrofitUtils = RetrofitUtils.getInstence();
    }

    /**
     * 销毁的方法
     */
    public void onDestroy() {
        detachView();
        System.gc();
    }

    public   void  getCardList(String  token,String openId){
        Map<String,String> map  =  new HashMap<>();
        String obj = new Gson().toJson(map);
        getView().showLoading();
        /* retrofit=new Retrofit.Builder().baseUrl(BASE_LOGIN_URL).build();*/
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

    public   void  getBorrow(String  token,String latitude,String longitude,String qrcode,String cardId){//,String cardId
        Map<String,String> map  =  new HashMap<>();

        map.put("latitude",latitude);
        map.put("longitude",longitude);
        map.put("id",qrcode);
        map.put("cardId",cardId);

   //     map.put("bankcard_id",cardId);
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


                            getView().update(bean.getCode(),bean.getOrderID(),bean.getPayResult());
                        } else if (bean.getCode() == Constant.MUTUAL){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else if (bean.getCode() == Constant.TOKEN_MISS){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else if (bean.getCode() == Constant.AUTHORIZATION) {
                            // getView().showDialog(bean.getMessage(),true);
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

    public   void   getResult(String token, String orderId, String paymentId){
        Map<String,String> map  =  new HashMap<>();
        String obj = new Gson().toJson(map);
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<BorrowFinishBean> data = retrofitUtils.getservice().result(orderId,token,body,paymentId);
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

    public     void     getRules(String token,String id){
        Map<String, String> map = new HashMap<>();
        map.put("id",id);
        map.put("latitude","0");
        map.put("longitude","0");
        String obj = new Gson().toJson(map);


        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj);
        Observable<RulesBean> data = retrofitUtils.getservice().rules(token,body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<RulesBean>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(RulesBean bean) {



                        if (bean.getCode() == Constant.SUCCESS) {
                            // getView().showDialog(bean.getMessage(),false);
                            getView().scan(bean);
                        }  else if (bean.getCode() == Constant.MUTUAL){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else if (bean.getCode() == Constant.TOKEN_MISS){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        } else {
                            getView().showDialog(bean.getMsg(),false);
                        }

                    }

                    //异常
                    @Override
                    public void onError(Throwable e) {
                        getView().showDialog(e.getMessage()+"",false);



                    }

                    //成功
                    @Override
                    public void onComplete() {

                    }
                });//Io子线程  网络请求
    }

}
