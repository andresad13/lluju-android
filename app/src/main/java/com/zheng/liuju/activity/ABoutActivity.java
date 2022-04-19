package com.zheng.liuju.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.api.Constant;
import com.zheng.liuju.api.RetrofitUtils;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.bean.ServiceBean;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;
import com.zheng.liuju.view.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 关于我们页面
 */
public class ABoutActivity extends BaseActivity {
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.versionNumber)
    TextView versionNumber;
    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.home)
    DragImageView home;


    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    private RetrofitUtils retrofitUtils;
    private PromptDialog promptDialog;
    @SuppressLint("HandlerLeak")
    //msg.what==1 弹框的关闭 msg.what==2 token过期或者账号互顶,重新登录
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                if (promptDialog != null) {
                    promptDialog.dismiss();
                    promptDialog = null;
                }
            } else if (msg.what == 2) {
                SPUtils.putString(ABoutActivity.this, "token", "");
                startActivity(new Intent(ABoutActivity.this, LoginActivity.class));

                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            }


        }
    };
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 页面初始化
     */
    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        EventBus.getDefault().register(this);
        retrofitUtils = RetrofitUtils.getInstence();
        versionNumber.setText(getResources().getString(R.string.versionNumber) + ": v" + getVersionCode());
        info(getToken(ABoutActivity.this));
        title.setOnHeaderClickListener(v -> finish());
    }

    /**
     * 接收事件,关闭页面
     *
     * @param message 事件类
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {
        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            finish();

        } else if (message.isHome()) {

            finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
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

    /**
     * 多语言的设置
     *
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    /**
     * 关于我们数据的获取
     *
     * @param token
     */
    public void info(String token) {
        Map<String, String> map = new HashMap<>();
        map.put("", "");
        String obj = new Gson().toJson(map);
        showLoading();

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

                        dismissLoading();
                        Log.e("Infos", bean.toString() + "xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            if (bean.getData() != null) {
                                phone.setText(bean.getData().getTel());
                                time.setText(bean.getData().getWebSite());
                                email.setText(bean.getData().getCustomerServiceEmail());
                            }
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
                });
    }

    private void onErr() {
        handler.sendEmptyMessageDelayed(2, 2000);
    }

    /**
     * 项目公用的弹框
     *
     * @param content
     * @param type    弹框的类型 true为正确弹框,false 为错误提示
     */
    private void showDialogs(String content, boolean type) {
        if (!isLiving(ABoutActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(ABoutActivity.this);
        }
        promptDialog.setTvTip(content + "", type);
        promptDialog.show();
        handler.sendEmptyMessageDelayed(1, 2000);
    }

    /**
     * 判断activity是否销毁
     *
     * @param activity
     * @return
     */
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

    /**
     * 网络请求的loading
     */
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(ABoutActivity.this);
        }
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 获取版本号信息
     *
     * @return
     */
    private String getVersionCode() {
        // 包管理器 可以获取清单文件信息
        PackageManager packageManager = getPackageManager();
        try {
            // 获取包信息
            // 参1 包名 参2 获取额外信息的flag 不需要的话 写0
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @OnClick(R.id.home)
    public void onViewClicked() {
        EventMeager   eventMeager = new EventMeager();
        eventMeager.setHome(true);
        EventBus.getDefault().post(eventMeager);
    }
}
