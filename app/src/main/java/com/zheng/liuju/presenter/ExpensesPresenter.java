package com.zheng.liuju.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.zheng.liuju.api.Constant;
import com.zheng.liuju.api.RetrofitUtils;
import com.zheng.liuju.bean.ChongzhiListBean;
import com.zheng.liuju.bean.ExpensesBean;
import com.zheng.liuju.view.Iexoenses;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class ExpensesPresenter extends BasePresenter<Iexoenses> {
    private final RetrofitUtils retrofitUtils;

    public ExpensesPresenter(){
        retrofitUtils = RetrofitUtils.getInstence();
    }

    /**
     * 销毁的方法
     */
    public void onDestroy() {
        detachView();
        System.gc();
    }

    public   void  initOrder(String date,String token,int type,int page){
        getView().showLoading();
        Map<String,String> map  = new HashMap<>();
        map.put("page",""+page);

        if (!date.equals("")){
            map.put("createTime",date);
        }
        map.put("pageSize","10");

        String obj=new Gson().toJson(map);

        /* retrofit=new Retrofit.Builder().baseUrl(BASE_LOGIN_URL).build();*/
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<ExpensesBean> data = retrofitUtils.getservice().expensesList(token,body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<ExpensesBean>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(ExpensesBean bean) {

                        getView().dismissLoading();
                        Log.e("Infos",bean.toString()+"xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            getView().upData(bean.getData(),type);
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
}
