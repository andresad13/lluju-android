package com.zheng.liuju.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zheng.liuju.R;
import com.zheng.liuju.api.Constant;
import com.zheng.liuju.api.RetrofitUtils;
import com.zheng.liuju.bean.ForgetPassWordBean;
import com.zheng.liuju.bean.Register;
import com.zheng.liuju.bean.RegisterRqert;
import com.zheng.liuju.bean.SendBean;
import com.zheng.liuju.view.IForPassWordView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class ForgetPassWordPresenter  extends BasePresenter<IForPassWordView> {
    private final RetrofitUtils retrofitUtils;

    public ForgetPassWordPresenter(){
        retrofitUtils = RetrofitUtils.getInstence();
    }

    public   void    isCode(String  phone, Context context, String fomart){
        if (TextUtils.isEmpty(phone)){
            getView().showDialog(context.getResources().getString(R.string.phoneNumberTip),true);
            return;
        }
        RegisterRqert info=new RegisterRqert("2",fomart,phone);
        Gson gson=new Gson();
        String obj=gson.toJson(info);
        getView().showLoading();
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<SendBean> data = retrofitUtils.getservice().sendCode(body);
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



    public   void    isEmailCode(String  phone, Context context){
        if (TextUtils.isEmpty(phone)){
            getView().showDialog(context.getResources().getString(R.string.yourEmail),true);
            return;
        }
        if (!phone.contains("@")){
            getView().showDialog(context.getResources().getString(R.string.incorrect),true);
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("code_type","2");
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

    public   void     isForgetPassWord(String username,String code,String password
            ,Context context ,String fomart){
        if (TextUtils.isEmpty(username)){
            getView().showDialog(context.getResources().getString(R.string.phoneNumberTip),true);
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

        requestRegister(username,code,password,context,fomart);
    }



    public   void     isEmailForgetPassWord(String username,String code,String password
            ,Context context){
        if (TextUtils.isEmpty(username)){
            getView().showDialog(context.getResources().getString(R.string.eamilTip),true);
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

        requestEmailRegister(username,code,password,context);
    }

    private void requestEmailRegister(String username, String code, String password, Context context) {
        Map<String,String>  map = new HashMap<>();
        map.put("code",code);
        map.put("mail",username);
        map.put("pwd",password);
        String obj = new Gson().toJson(map);
        getView().showLoading();
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<Register> data = retrofitUtils.getservice().pwd(body);
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
                        if (bean.getCode()==1){

                            getView().showDialog(bean.getMsg(),true);
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

    private void requestRegister(String username, String code, String password,Context context
            ,String fomart) {
        ForgetPassWordBean info=new ForgetPassWordBean(fomart,username,code,password);
        Gson gson=new Gson();
        String obj=gson.toJson(info);
        getView().showLoading();
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<Register> data = retrofitUtils.getservice().pwd(body);
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
                        if (bean.getCode()==1){

                            getView().showDialog(bean.getMsg(),true);
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

    /**
     * 销毁的方法
     */
    public void onDestroy() {
        detachView();
        System.gc();
    }

}
