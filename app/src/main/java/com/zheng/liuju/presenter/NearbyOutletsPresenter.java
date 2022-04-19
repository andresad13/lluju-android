package com.zheng.liuju.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.zheng.liuju.api.Constant;
import com.zheng.liuju.api.RetrofitUtils;
import com.zheng.liuju.bean.ShopBean;
import com.zheng.liuju.view.INearbyOutlets;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class NearbyOutletsPresenter extends BasePresenter<INearbyOutlets> {
    private final RetrofitUtils retrofitUtils;

    public NearbyOutletsPresenter(){
        retrofitUtils = RetrofitUtils.getInstence();
    }

    /**
     * 销毁的方法
     */
    public void onDestroy() {
        detachView();
        System.gc();
    }

    public   void  initMap(String myLat,String myLng,String token,int type,int page,String latitudeCenter,String longitudeCenter){
        getView().showLoading();
        Map<String,String> map  = new HashMap<>();
        map.put("page",""+page);
        map.put("pageSize","10");
        map.put("latitude",myLat);
        map.put("longitude",myLng);
        map.put("latitudeCenter",latitudeCenter);
        map.put("longitudeCenter",longitudeCenter);
        String obj=new Gson().toJson(map);

        /* retrofit=new Retrofit.Builder().baseUrl(BASE_LOGIN_URL).build();*/
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<ShopBean> data = retrofitUtils.getservice().shopList(token,body);
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

                        getView().dismissLoading();
                        Log.e("Infos",bean.toString()+"xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            getView().upData(bean.getData(),type);
                        } else if (bean.getCode() == Constant.MUTUAL){
                            getView().showDialog(bean.getMsg(),false);
                            getView().onErr();
                        }else if (bean.getCode() == Constant.SUCCESS){
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


}
