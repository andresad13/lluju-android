package com.zheng.liuju.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.zheng.liuju.R;
import com.zheng.liuju.api.Constant;
import com.zheng.liuju.api.RetrofitUtils;
import com.zheng.liuju.bean.Register;
import com.zheng.liuju.view.ICooperation;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class CooperationPresenter extends BasePresenter<ICooperation> {

    private final RetrofitUtils retrofitUtils;

    public CooperationPresenter(){
        retrofitUtils = RetrofitUtils.getInstence();
    }

    /**
     * 销毁的方法
     */
    public void onDestroy() {
        detachView();
        System.gc();
    }

    public   void     cooper(String content, String shopName, String userName, String userPhone, Context context,String token){

        if (TextUtils.isEmpty(shopName)){

            getView().showDialog(context.getResources().getString(R.string.shopNameTip),false);
            return;
        }

        if (TextUtils.isEmpty(userName)){

            getView().showDialog( context.getResources().getString(R.string.ContactPersonTip),false);
            return;
        }

        if (TextUtils.isEmpty(userPhone)){

            getView().showDialog(    context.getResources().getString(R.string.phoneTleTip),false);
            return;
        }

        if (TextUtils.isEmpty(content)){
            getView().showDialog(   context.getResources().getString(R.string.contentTip),false);
            return;
        }
        getView().showLoading();
        Map<String,String>  map =new HashMap<>();
        map.put("contents",content);
        map.put("shopName",shopName);
        map.put("personName",userName);
        map.put("phone",userPhone);
        String obj = new Gson().toJson(map);
        /* retrofit=new Retrofit.Builder().baseUrl(BASE_LOGIN_URL).build();*/
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<Register> data = retrofitUtils.getservice().cooperation(token,body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<Register>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(Register bean) {
                        getView().dismissLoading();

                        Log.e("Infos",bean.toString()+"xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            getView().showDialog(bean.getMsg(),true);
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
                        getView().dismissLoading();

                    }

                    //成功
                    @Override
                    public void onComplete() {

                    }
                });//Io子线程  网络请求
    }
}
