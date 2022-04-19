package com.zheng.liuju.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

import com.gigamole.library.ShadowLayout;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.api.Constant;
import com.zheng.liuju.api.RetrofitUtils;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.bean.Info;
import com.zheng.liuju.bean.Infos;
import com.zheng.liuju.bean.LossToBuyBean;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.util.Utils;
import com.zheng.liuju.utils.AnimationUtils;
import com.zheng.liuju.view.DialogUtils;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class MyPurseActivity extends BaseActivity implements DialogUtils.OnButtonClickListener {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.backs)
    LinearLayout backs;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.details)
    LinearLayout details;
    @BindView(R.id.cardlist)
    LinearLayout cardlist;
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.Recharge)
    ShadowLayout Recharge;
    @BindView(R.id.deposit)
    TextView deposit;

    @BindView(R.id.withdraws)
    LinearLayout withdraws;
    @BindView(R.id.withdraw)
    TextView withdraw;
    @BindView(R.id.deposits)
    TextView deposits;
    @BindView(R.id.home)
    DragImageView home;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    private RetrofitUtils retrofitUtils;
    private PromptDialog promptDialog;
    private double balance = 0;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                if (promptDialog != null) {
                    promptDialog.dismiss();
                    promptDialog = null;
                }
            } else if (msg.what == 2) {
                SPUtils.putString(MyPurseActivity.this, "token", "");
                startActivity(new Intent(MyPurseActivity.this, LoginActivity.class));
                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            }


        }
    };
    private LoadingDialog loadingDialog;
    private double withdrawable;
    private DialogUtils dialogUtils;
    private AlertDialog dialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purse);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        EventBus.getDefault().register(this);
        info("0", "0", getToken(MyPurseActivity.this));
        home.setOnClickListener(v -> {


                EventMeager eventMeager = new EventMeager();
            eventMeager.setHome(true);
            EventBus.getDefault().post(eventMeager);
        });


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        handler.removeMessages(1);
        handler.removeMessages(2);
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (promptDialog != null) {
            promptDialog.dismiss();
        }

        System.gc();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            finish();


        } else if (message.getUpUser()) {
            info("0", "0", getToken(MyPurseActivity.this));
        } else if (message.isRechargeSuccessful()) {
            finish();
        }else  if(message.isHome()){

            finish();
        }
    }

    @OnClick({R.id.backs, R.id.details, R.id.Recharge, R.id.back, R.id.cardlist, R.id.withdraw, R.id.withdraws})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backs:
                break;
            case R.id.details:
                startActivity(new Intent(MyPurseActivity.this, TransactionDetailsActivity.class));
                break;
            case R.id.Recharge:
                Intent intent = new Intent(MyPurseActivity.this, RechargeActivity.class);
                //if (balance < 0) {
                intent.putExtra("balance", balance + "");
                // }
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.cardlist:
                if (Utils.isNotFastClick()) {
                    startActivity(new Intent(MyPurseActivity.this, CreditCardListActivity.class));
                }
                break;
            case R.id.withdraw:
                if (Utils.isNotFastClick()) {
                    startActivity(new Intent(MyPurseActivity.this, WithdrawActivity.class));
                }
                break;
            case R.id.withdraws:
               /* if (Utils.isNotFastClick()) {
                    startActivity(new Intent(MyPurseActivity.this, WithdrawActivity.class));
                }*/
                DecimalFormat df = new DecimalFormat("#0.00");
                String format = df.format(withdrawable);
                delectCard(format);
                break;


        }
    }


    private void delectCard(String withdrawable) {
        if (dialogUtils == null) {
            dialogUtils = new DialogUtils();
        }
        dialogUtils.setOnButtonClickListener(this);
        String tips = getResources().getString(R.string.WithdrawableDeposit) + "<font color='#63B15E'>" + " $ " + withdrawable
                + "<br>";
        dialogUtils.showWithdraw(MyPurseActivity.this, tips);
    }


    public void info(String latitude, String longitude, String token) {
        if (retrofitUtils == null) {
            retrofitUtils = RetrofitUtils.getInstence();
        }

        Info info = new Info(latitude, longitude);
        Gson gson = new Gson();
        String obj = gson.toJson(info);
        showLoading();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj);
        Observable<Infos> data = retrofitUtils.getservice().info(token, body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<Infos>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(Infos bean) {
                        dismissLoading();

                        Log.e("Infos", bean.toString() + "xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {

                            if (bean.getData().getAccountMy() != 0) {
                                // if (!bean.getData().getWallet().equals("")) {

                                AnimationUtils.addTextViewAddAnim(deposit,bean.getData().getAccountMy() );
                                balance = bean.getData().getAccountMy();


                            } else {
                                deposit.setText("0");
                            }
                            withdrawable = bean.getData().getAccountYajin();
                            deposits.setText(bean.getData().getAccountYajin() + "");
                        } else if (bean.getCode() == Constant.MUTUAL) {
                            dismissLoading();
                            shows(bean.getMsg(), false);
                            onErr();
                        } else if (bean.getCode() == Constant.TOKEN_MISS) {
                            dismissLoading();
                            shows(bean.getMsg(), false);
                            onErr();
                        } else {
                            dismissLoading();
                            shows(bean.getMsg(), false);
                        }

                    }

                    //异常
                    @Override
                    public void onError(Throwable e) {
                        shows(e.getMessage() + "", false);

                    }

                    //成功
                    @Override
                    public void onComplete() {

                    }
                });//Io子线程  网络请求
//主线程  更新控件
//subscribe：赋观察者的方法 Observer：观察者
    }

    public void shows(String content, boolean type) {
        if (!isLiving(MyPurseActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(MyPurseActivity.this);
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

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(MyPurseActivity.this);
        }
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public void onErr() {
        handler.sendEmptyMessageDelayed(2, 3000);
    }

    @Override
    public void onPositiveButtonClick(int type, String context, AlertDialog dialog) {
        dialogs = dialog;
        if (withdrawable > 0) {
            info(getToken(MyPurseActivity.this), withdrawable + "");
        } else {
            shows(getResources().getString(R.string.InsufficientDeposit), false);
        }
    }


    /**
     * 关于我们数据的获取
     *
     * @param token
     */
    public void info(String token, String amount) {
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal bd = new BigDecimal(amount);
        map.put("amount", bd);
        String obj = new Gson().toJson(map);
        showLoading();

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj);
        Observable<LossToBuyBean> data = retrofitUtils.getservice().refund(token, body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求
                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<LossToBuyBean>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(LossToBuyBean bean) {

                        dismissLoading();
                        Log.e("Infos", bean.toString() + "xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            shows(bean.getMsg(), true);
                            dialogs.dismiss();
                            updata();
                            info("0", "0", getToken(MyPurseActivity.this));

                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                            String addTime = df.format(new Date());
                            Intent intent = new Intent(MyPurseActivity.this, RechargeSuccessfulActivity.class);
                            intent.putExtra("type", "Success");
                            intent.putExtra("addTime", addTime);
                            intent.putExtra("types", "11");
                            intent.putExtra("status", "0");
                            intent.putExtra("id", bean.getData() + "");
                            startActivity(intent);
                        } else if (bean.getCode() == Constant.MUTUAL) {
                            shows(bean.getMsg(), false);
                            onErr();
                        } else if (bean.getCode() == Constant.TOKEN_MISS) {
                            shows(bean.getMsg(), false);
                            onErr();
                        } else {
                            shows(bean.getMsg(), false);
                        }

                    }

                    //异常
                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                        shows(e.getMessage() + "", false);


                    }

                    //成功
                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updata() {
        EventMeager eventMeager = new EventMeager();
        eventMeager.setUpUser(true);
        EventBus.getDefault().post(eventMeager);
    }
}
