package com.zheng.liuju.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.api.Constant;
import com.zheng.liuju.api.RetrofitUtils;
import com.zheng.liuju.banner.Banner;
import com.zheng.liuju.banner.BannerConfig;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.bean.ShopDetailsBean;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.view.GlideImageLoader;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class NearByOutletsDetailsActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.backs)
    LinearLayout backs;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.shoptime)
    ImageView shoptime;
    @BindView(R.id.address)
    ImageView address;
    @BindView(R.id.phone)
    ImageView phone;
    @BindView(R.id.description)
    ImageView description;
    @BindView(R.id.sdview)
    SimpleDraweeView sdview;
    @BindView(R.id.navigation)
    ImageView navigation;
    @BindView(R.id.shopName)
    TextView shopName;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.addres)
    TextView addres;
    @BindView(R.id.tel)
    TextView tel;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.go)
    RelativeLayout go;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what==1){
                if (promptDialog != null) {
                    promptDialog.dismiss();
                    promptDialog = null;
                }
            } else if (msg.what == 2) {
                SPUtils.putString(NearByOutletsDetailsActivity.this, "token", "");
                startActivity(new Intent(NearByOutletsDetailsActivity.this, LoginActivity.class));

                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            }


        }
    };
    private RetrofitUtils retrofitUtils;
    private PromptDialog promptDialog;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_outlets_details);
        ButterKnife.bind(this);
        retrofitUtils = RetrofitUtils.getInstence();
        initView();
    }

    private void initView() {
        ImmersionBar.with(this).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        EventBus.getDefault().register(this);
        backs.setOnClickListener(this);
        //设置图片加载器
        String shopId = getIntent().getStringExtra("shopId");
        if (shopId!=null){
            infoss(getLatitude(NearByOutletsDetailsActivity.this),getLongitude(NearByOutletsDetailsActivity.this)
                    ,getToken(NearByOutletsDetailsActivity.this),shopId);
        }else {
            showDialogs(getResources().getString(R.string.acquisitionFailed), false);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            finish();


        } else if (message.getPush() != null) {
            if (message.getPush().contains("歸還成功")) {

            }

        }
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(1);
        handler.removeMessages(2);
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (promptDialog != null) {
            promptDialog.dismiss();
        }

        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backs:
                finish();
                break;
        }
    }


    public void infoss(String latitude, String longitude, String token, String shopId) {
        Map<String, String> map = new HashMap<>();


        String obj = new Gson().toJson(map);
        showLoading();
        /* retrofit=new Retrofit.Builder().baseUrl(BASE_LOGIN_URL).build();*/
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj);
        Observable<ShopDetailsBean> data = retrofitUtils.getservice().details(shopId,latitude,longitude,token, body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<ShopDetailsBean>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(ShopDetailsBean bean) {

                        dismissLoading();
                        Log.e("Infos", bean.toString() + "xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            upInformation(bean.getData());
                        } else if (bean.getCode() == Constant.MUTUAL) {
                            showDialogs(bean.getMsg(), false);
                            onErr();
                        } else if (bean.getCode() == Constant.TOKEN_MISS) {
                            showDialogs(bean.getMsg(), false);
                            onErr();
                        } else {
                            showDialogs(bean.getMsg(), false);
                        }

                    }

                    //异常
                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();

                        showDialogs(e.getMessage() + "", false);

                    }

                    //成功
                    @Override
                    public void onComplete() {

                    }
                });//Io子线程  网络请求
//主线程  更新控件
//subscribe：赋观察者的方法 Observer：观察者
    }

    private void onErr() {
        handler.sendEmptyMessageDelayed(2, 3000);
    }

    private void showDialogs(String context, boolean type) {
        shows(context, type);
    }

    public void shows(String content, boolean type) {
        if (!isLiving(NearByOutletsDetailsActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(NearByOutletsDetailsActivity.this);
        }
        promptDialog.setTvTip(content + "", type);
        promptDialog.show();
        handler.sendEmptyMessageDelayed(1, 2000);
    }

    private static boolean isLiving(Activity activity) {

        if (activity == null) {
            Log.d("wisely", "activity == null");
            return false;
        }

        if (activity.isFinishing()) {
            Log.d("wisely", "activity is finishing");
            return false;
        }


        return true;
    }

    private void upInformation(ShopDetailsBean.DataBean data) {
        if (data != null) {

            String shopBanner = data.getBanner();
            if (shopBanner != null) {
                if (!shopBanner.equals("")) {
                    String[] split = shopBanner.split(",");
                    List<String> strings = Arrays.asList(split);
                    banner.setImageLoader(new GlideImageLoader());
                    //设置图片集合

                    banner.setImages(strings);
                    banner.setPadding(120);
                    banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                    //  banner.setBannerAnimation(Transformer.ZoomIn);
                    banner.setIndicatorGravity(BannerConfig.CENTER);
                    //banner设置方法全部调用完毕时最后调用
                    banner.start();
                } else {
                    ArrayList<String> mlist = new ArrayList<>();
                    banner.setImageLoader(new GlideImageLoader());
                    //设置图片集合

                    banner.setImages(mlist);
                    banner.setPadding(120);
                    banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                    //  banner.setBannerAnimation(Transformer.ZoomIn);
                    banner.setIndicatorGravity(BannerConfig.CENTER);
                    //banner设置方法全部调用完毕时最后调用
                    banner.start();
                }
            } else {
                ArrayList<String> mlist = new ArrayList<>();
                banner.setImageLoader(new GlideImageLoader());
                //设置图片集合

                banner.setImages(mlist);
                banner.setPadding(120);
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                //  banner.setBannerAnimation(Transformer.ZoomIn);
                banner.setIndicatorGravity(BannerConfig.CENTER);
                //banner设置方法全部调用完毕时最后调用
                banner.start();
            }
            shopName.setText(data.getShopname());
            sdview.setImageURI(data.getLogo());
            addres.setText(data.getAddress());
            tel.setText(data.getTel());
            content.setText(data.getInfo());

            time.setText(data.getServiceTime()+"");
            if ((int) data.getDistance() >= 1000) {
                int dis = (int) data.getDistance() / 1000;
                distance.setText( dis + "km");
            } else {
                distance.setText((int) data.getDistance() + "m");
            }

            if (data.getOnlines()>0){

            }else {
                go.setBackgroundResource(R.drawable.hui_gray_radius);
            }
            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigations(getLatitude(NearByOutletsDetailsActivity.this),getLongitude(NearByOutletsDetailsActivity.this)
                            ,data.getLat(),data.getLng());
                }
            });

        } else {
            showDialogs(getResources().getString(R.string.acquisitionFailed), false);
        }

    }

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(NearByOutletsDetailsActivity.this);
        }
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }


    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    private void navigations(String latitude,String longitude,String Latitude,String Longitude) {
        if (isAvilible(this, "com.google.android.apps.maps")) {

            Uri parse = Uri.parse("http://maps.google.com/maps?f=d&source=s_d" +
                    "&saddr=" + latitude + "," + longitude + "&daddr=" + Latitude+ "," + Longitude + "&hl=zh&t=m&dirflg=d");
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    parse);
            intent.setPackage("com.google.android.apps.maps");
            this.startActivity(intent);


        } else {
            shows(getResources().getString(R.string.noGoogleMap), false);


        }
    }

    public static boolean isAvilible(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);

    }
}
