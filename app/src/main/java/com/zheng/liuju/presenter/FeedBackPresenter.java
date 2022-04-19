package com.zheng.liuju.presenter;

import android.Manifest;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.zheng.liuju.R;
import com.zheng.liuju.api.Constant;
import com.zheng.liuju.api.RetrofitUtils;
import com.zheng.liuju.bean.FeedBackData;
import com.zheng.liuju.bean.Files;
import com.zheng.liuju.bean.Register;
import com.zheng.liuju.view.IFeedBackView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class FeedBackPresenter extends BasePresenter<IFeedBackView> {
    private final RetrofitUtils retrofitUtils;
    private List<String> imagenums = new ArrayList<>();
    public List<String> bmp = new ArrayList<String>();
    private String filePath;
    private  String className;
    private  String content;
    private  String deviceID;
    private  String latitude;
    private  String longitude;
    private  String phone;
    private  String pic;
    public FeedBackPresenter(){
        retrofitUtils = RetrofitUtils.getInstence();
    }
    /**
     * 销毁的方法
     */
    public void onDestroy() {
        detachView();
        System.gc();
    }

    public void initData(String token) {
     getView().showLoading();
        Gson gson=new Gson();
        String obj=gson.toJson("");

        /* retrofit=new Retrofit.Builder().baseUrl(BASE_LOGIN_URL).build();*/
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<FeedBackData> data = retrofitUtils.getservice().feedback(token,body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<FeedBackData>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(FeedBackData bean) {
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


    public   void   sendData(ArrayList<String> bmps, String token, String latitude, String longitude,
                             String deviceID, String className, String phone, String content, Context context){
        if (TextUtils.isEmpty(deviceID)) {
            getView().showDialog(context.getResources().getString(R.string.noDeviceId), false);
            //Toast.makeText(this, "設備編號不能為空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(className)) {
            getView().showDialog(context.getResources().getString(R.string.noCommonType), false);
            // Toast.makeText(this, "問題類型不能為空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            getView().showDialog(context.getResources().getString(R.string.phoneNumberTip), false);
            //  Toast.makeText(this, "問題描述不能為空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(content)) {
            getView().showDialog(context.getResources().getString(R.string.noEamil), false);

            return;
        }
        this.className=className;
        this. content=content;
      this. deviceID=deviceID;
        this.latitude=latitude;
        this. longitude=longitude;
        this. phone=phone;

        upFils(bmps,token);

    }

    public   void   upFils(ArrayList<String> bmp,String token){
        this.bmp.addAll(bmp);
        if (bmp.size()!=0){
            for (int i = 0; i <bmp.size() ; i++) {
                upLoadImg(token,bmp.get(0));
            }
        }else {
            upDataFeed("",token);
        }


    }


    public void upLoadImg(String token,String  path) {

        File file = new File(path);
        getView().showLoading();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Observable<Files> data = retrofitUtils.getservice().uploadFiles(token,body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<Files>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(Files bean) {

                        getView().dismissLoading();
                        Log.e("Infos",bean.toString()+"xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            imagenums.add(bean.getData().getPath());
                            if (imagenums.size()==bmp.size()){
                                if (imagenums.size()==1){
                                    upDataFeed(imagenums.get(0),token);
                                }else  if (imagenums.size()>1){
                                    for (int i = 0; i <imagenums.size() ; i++) {
                                        if (i==0){
                                            filePath = imagenums.get(i);
                                        }else {
                                            filePath=filePath+","+imagenums.get(i);
                                        }

                                    }
                                    Log.e("我要的数据",filePath);
                                    upDataFeed(filePath,token);
                                }

                            }

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

    private void upDataFeed(String filePath,String token) {
        Map<String,String> map  =   new HashMap<>();
        map.put("reason",className);
        map.put("mask",content);
        map.put("deviceID",deviceID);
        map.put("latitude",latitude);
        map.put("longitude",longitude);
        map.put("mobile",phone);
        map.put("pic",filePath);
        String obj = new Gson().toJson(map);
        getView().showLoading();
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"),obj);
        Observable<Register> data = retrofitUtils.getservice().feedbacks(token,body);
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
                        if (bean.getCode()==Constant.SUCCESS){

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

}
