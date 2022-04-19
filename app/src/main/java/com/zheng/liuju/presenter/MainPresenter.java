package com.zheng.liuju.presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.google.gson.Gson;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.zheng.liuju.api.Constant;
import com.zheng.liuju.api.RetrofitUtils;
import com.zheng.liuju.bean.Info;
import com.zheng.liuju.bean.Infos;
import com.zheng.liuju.bean.RulesBean;
import com.zheng.liuju.bean.ServiceBean;
import com.zheng.liuju.bean.ShopBean;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.view.IMainView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MainPresenter  extends BasePresenter<IMainView> {
    private final RetrofitUtils retrofitUtils;

    public MainPresenter(){
        retrofitUtils = RetrofitUtils.getInstence();
    }

    /**
     * @param
     */


    public   void  initMap(String centerLat,String centerLng,String myLat,String myLng,String token){
        Map<String,String> map  = new HashMap<>();
      map.put("latitudeCenter",centerLat);
        map.put("longitudeCenter",centerLng);
        map.put("latitude",myLat);
        map.put("longitude",myLng);
        String obj=new Gson().toJson(map);

        /* retrofit=new Retrofit.Builder().baseUrl(BASE_LOGIN_URL).build();*/
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<ShopBean> data = retrofitUtils.getservice().mapShop(token,body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<ShopBean>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(ShopBean bean) {


                        Log.e("Infos",bean.toString()+"xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            getView().upMap(bean.getData());
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
                        getView().showDialog(e.getMessage() + "", false);

                    }

                    //成功
                    @Override
                    public void onComplete() {

                    }
                });//Io子线程  网络请求
    }



    public void info(String latitude,String longitude,String token) {
        Info info=new Info(latitude,longitude);
        Gson gson=new Gson();
        String obj=gson.toJson(info);

        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<Infos> data = retrofitUtils.getservice().info(token,body);
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


                  Log.e("Infos",bean.toString()+"xxxxxxxx");
                if (bean.getCode() == Constant.SUCCESS) {
                    getView().upInformation(bean.getData());
                } else if (bean.getCode() == Constant.MUTUAL){
                    getView().showDialog(bean.getMsg()+"",false);
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


    /**
     * 销毁的方法
     */
    public void onDestroy() {
        detachView();
        System.gc();
    }

    /**
     * 跳转页面不销毁不传值
     * @param context  上下文
     * @param clas      目标的class
     */
    public    void  jump(Context  context,Class clas){
        context.startActivity(new Intent(context,clas));
    }

    /**
     * 跳转页面并销毁上个页面
     * @param context     上下文
     * @param clas        目标的class
     */
    public    void  jumpFinish(Activity context, Class clas){
        context.startActivity(new Intent(context,clas));
        context.finish();
    }
    /**
     * 跳转页面并传值
     * @param context     上下文
     * @param clas        目标的class
     * @param CODE        返回值
     */
    public    void  jumpValue(Activity context, Class clas,int CODE){
        context.startActivityForResult(new Intent(context,clas),CODE);
    }

    public void  requestPermission(Context context,int language){
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .build(language), new AcpListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onGranted() {
                getView().permissionSuccess();
            }

            @Override
            public void onDenied(List<String> permissions) {



            }
        });
    }


     public   void  cameraRequestPermission(Context context,int language){
         Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(Manifest.permission.CAMERA)
                 .build(language), new AcpListener() {
             @Override
             public void onGranted() {
                /* Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                 if (language == 3) {
                     intent.putExtra("text", "掃碼");
                 } else {
                     intent.putExtra("text", "Scan code");
                 }
                 startActivityForResult(intent, REQUEST_CODE);*/
                getView().cameraPermissionSuccess();
             }

             @Override
             public void onDenied(List<String> permissions) {
                 // shows("扫一扫打开失败",false);
                /* Toast.makeText(MainActivity.this, getResources().getString(R.string.openScan), Toast.LENGTH_SHORT).show();
                 scan.setEnabled(true);*/

             }
         });
     }
    public void service(String token,Context context) {
        Map<String, String> map = new HashMap<>();
        map.put("", "");
        String obj = new Gson().toJson(map);


        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj);
        Observable<ServiceBean> data = retrofitUtils.getservice().service(token, body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<ServiceBean>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(ServiceBean bean) {


                        Log.e("Infos", bean.toString() + "xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            if (bean.getData() != null) {
                              //  phone.setText(bean.getData().getServicePhone());
                                //time.setText(bean.getData().getServiceTime());
                                SPUtils.putString(context,"phone",bean.getData().getTel());
                                SPUtils.putString(context,"time",bean.getData().getBusinessHours());
                            }
                        }  else if (bean.getCode() == Constant.MUTUAL){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else if (bean.getCode() == Constant.TOKEN_MISS){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }
                    }

                    //异常
                    @Override
                    public void onError(Throwable e) {




                    }

                    //成功
                    @Override
                    public void onComplete() {

                    }
                });//Io子线程  网络请求
//主线程  更新控件
//subscribe：赋观察者的方法 Observer：观察者
    }

  /*  public   void  getCardList(String  token){
        Map<String,String>  map  =  new HashMap<>();
        String obj = new Gson().toJson(map);
        getView().showLoading();
        *//* retrofit=new Retrofit.Builder().baseUrl(BASE_LOGIN_URL).build();*//*
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<CardList> data = retrofitUtils.getservice().cardlist(token,body);
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
                        if (bean.getRetCode() == 1) {
                            getView().cardList(bean.getData());
                        } else if (bean.getRetCode() == 2){
                            getView().showDialog(bean.getMessage(),false);
                            getView().onErr();
                        }else if (bean.getRetCode() == 3){
                            getView().showDialog(bean.getMessage(),false);
                            getView().onErr();
                        }else {
                            getView().showDialog(bean.getMessage(),false);
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
    }*/


    public     void     getRules(String token,String id){
        getView().showLoading();
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
                          getView().dismissLoading();
                        if (bean.getCode() == Constant.SUCCESS) {
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
