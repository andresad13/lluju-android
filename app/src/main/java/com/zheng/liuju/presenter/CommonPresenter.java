package com.zheng.liuju.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.zheng.liuju.api.Constant;
import com.zheng.liuju.api.RetrofitUtils;
import com.zheng.liuju.bean.CommonBean;
import com.zheng.liuju.view.ICommonProblem;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class CommonPresenter extends BasePresenter<ICommonProblem> {
    private final RetrofitUtils retrofitUtils;

    public CommonPresenter(){
        retrofitUtils = RetrofitUtils.getInstence();
    }
    /**
     * 销毁的方法
     */
    public void onDestroy() {
        detachView();
        System.gc();
    }



    public void info(String token) {
        Map<String,String> map  = new HashMap<>();
        map.put("page","1");
        map.put("latitude","");
        map.put("longitude","");
        map.put("pageSize","30");
        map.put("type","APP");
        String obj=new Gson().toJson(map);
  getView().showLoading();
        /* retrofit=new Retrofit.Builder().baseUrl(BASE_LOGIN_URL).build();*/
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<CommonBean> data = retrofitUtils.getservice().problem(token,body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<CommonBean>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(CommonBean bean) {

                        getView().dismissLoading();
                        Log.e("Infos",bean.toString()+"xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            getView().upInformation(bean.getData());
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
//主线程  更新控件
//subscribe：赋观察者的方法 Observer：观察者
    }

}
