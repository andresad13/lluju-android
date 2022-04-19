package com.zheng.liuju.presenter;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.zheng.liuju.R;
import com.zheng.liuju.api.Constant;
import com.zheng.liuju.api.RetrofitUtils;
import com.zheng.liuju.bean.Register;
import com.zheng.liuju.bean.SendBean;
import com.zheng.liuju.view.IEmailRegisteredView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class EmailRegisteredPresenter  extends BasePresenter<IEmailRegisteredView> {
    private final RetrofitUtils retrofitUtils;

    public EmailRegisteredPresenter(){
        retrofitUtils = RetrofitUtils.getInstence();
    }



    /**
     * 销毁的方法
     */
    public void onDestroy() {
        detachView();
        System.gc();
    }

    public   void    isCode(String  phone, Context context){
        if (TextUtils.isEmpty(phone)){
            getView().showDialog(context.getResources().getString(R.string.eamilTip),true);
            return;
        }

        if (!phone.contains("@")){
            getView().showDialog(context.getResources().getString(R.string.incorrect),true);
            return;
        }
        Map<String,String>  map  =new HashMap<>();
        map.put("code_type","1");
        map.put("mail",phone);
        String obj = new Gson().toJson(map);
        getView().showLoading();
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<SendBean> data = retrofitUtils.getservice().sendEamilCode(body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件

                //subscribe：赋观察者的方法 Observer：观察者
                .subscribe(new Observer<SendBean>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(SendBean bean) {
                        getView().dismissLoading();
                        if (bean.getCode()== Constant.SUCCESS){

                            getView().showDialog(bean.getMsg(),true);
                            getView().showTime();
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
    public   void     isRegistered(String username,String code,String password
            ,String mailbox,String sharecode,Context context){

        if (TextUtils.isEmpty(username)){
            getView().showDialog(context.getResources().getString(R.string.yourEmail),true);
            return;
        }
        if (!username.contains("@")){
            getView().showDialog(context.getResources().getString(R.string.incorrect),true);
            return;
        }
        if (password.length()<6){
            getView().showDialog(context.getResources().getString(R.string.pwdTip),true);
            return;
        }
        if (TextUtils.isEmpty(code)){
            getView().showDialog(context.getResources().getString(R.string.VerificationCodeTip),true);
            return;
        }
        if (TextUtils.isEmpty(password)){
            getView().showDialog(context.getResources().getString(R.string.passwordTip),true);
            return;
        }
        if (TextUtils.isEmpty(mailbox)){
            getView().showDialog(context.getResources().getString(R.string.yourEmail),true);
            return;
        }
        requestRegister(username,code,password,mailbox,sharecode,context);
    }

    private void requestRegister(String username, String code, String password, String mailbox, String sharecode, Context context) {

        boolean mainThread = isMainThread();
        Log.e("mainThread",mainThread+"");
        Map<String,String>  map = new HashMap<>();
        map.put("code",code);
        map.put("email",username);
        map.put("pwd",password);
        map.put("type","2");
        String obj = new Gson().toJson(map);
        getView().showLoading();
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<Register> data = retrofitUtils.getservice().register(body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件

                //subscribe：赋观察者的方法 Observer：观察者
                .subscribe(new Observer<Register>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(Register bean) {
                        getView().dismissLoading();
                        if (bean.getCode()== Constant.SUCCESS){

                            getView().showDialog(bean.getMsg(),true);
                            getView().reistered();
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

    public boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

}
