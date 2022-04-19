package com.zheng.liuju.presenter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zheng.liuju.R;

import com.zheng.liuju.activity.MainActivity;
import com.zheng.liuju.api.Constant;
import com.zheng.liuju.api.RetrofitUtils;
import com.zheng.liuju.bean.Login;
import com.zheng.liuju.bean.LoginBean;
import com.zheng.liuju.view.ILoginView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class LoginPresenter extends BasePresenter<ILoginView> {
    private final RetrofitUtils retrofitUtils;

    public LoginPresenter(){
        retrofitUtils = RetrofitUtils.getInstence();
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
     * 跳转页面
     * @param context     上下文
     * @param clas        目标的class
     */
    public    void  jump(Activity context, Class clas){
        context.startActivity(new Intent(context,clas));

    }
    public void onDestroy() {
        detachView();
        System.gc();
    }

    public  void   requestLogin(String phone, String password, Context context,String fomart){
        if (TextUtils.isEmpty(phone)){
            getView().showDialog(context.getResources().getString(R.string.phoneNumberTip),true);
            return;
        }

        if (password.length()<6){
            getView().showDialog(context.getResources().getString(R.string.pwdTip),true);
            return;
        }
        if (TextUtils.isEmpty(password)){
            getView().showDialog(context.getResources().getString(R.string.passwordTip),true);
            return;
        }
        requestRegister(fomart,phone,password);
    }

    private void requestRegister(String fomart, String phone, String password) {
        LoginBean info=new LoginBean(fomart,phone,password,"","");
        Gson gson=new Gson();
        String obj=gson.toJson(info);
        getView().showLoading();
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<Login> data = retrofitUtils.getservice().login(body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件

                //subscribe：赋观察者的方法 Observer：观察者
                .subscribe(new Observer<Login>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(Login bean) {
                        getView().dismissLoading();
                        if (bean.getCode()== Constant.SUCCESS){

                         //   getView().showDialog(bean.getMessage(),true);
                            getView().saveToken(bean.getPtoKen(),bean.getOpenid());
                            getView().jumpFinish(MainActivity.class);
                            //getView().showTime();
                        }else {
                            getView().showDialog(bean.getMsg(),false);
                        }
                    }

                    //异常
                    @Override
                    public void onError(Throwable e) {
                        //    Log.e("=========", e.getMessage() + "");
                        getView().dismissLoading();
                        getView().showDialog(e.getMessage() + "",false);
                    }

                    //成功
                    @Override
                    public void onComplete() {

                    }
                });
    }
}
