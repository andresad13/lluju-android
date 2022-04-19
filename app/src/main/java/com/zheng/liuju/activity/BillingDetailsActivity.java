package com.zheng.liuju.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.api.Constant;
import com.zheng.liuju.api.RetrofitUtils;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.ChongzhiDetailBean;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.bean.ExpensesDatailBean;
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

public class BillingDetailsActivity extends BaseActivity {
    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.moeny)
    TextView moeny;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.paymentMethod)
    TextView paymentMethod;
    @BindView(R.id.cardNumber)
    TextView cardNumber;
    @BindView(R.id.TransactionNumber)
    TextView TransactionNumber;
    @BindView(R.id.tobalance)
    TextView tobalance;
    @BindView(R.id.cardName)
    LinearLayout cardName;
    @BindView(R.id.home)
    DragImageView home;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    private RetrofitUtils retrofitUtils;
    private PromptDialog promptDialog;
    private LoadingDialog loadingDialog;
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
                SPUtils.putString(BillingDetailsActivity.this, "token", "");
                startActivity(new Intent(BillingDetailsActivity.this, LoginActivity.class));

                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_details);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        retrofitUtils = RetrofitUtils.getInstence();
        EventBus.getDefault().register(this);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        title.setOnHeaderClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String id = getIntent().getStringExtra("id");
        if (id != null) {
            String token = getToken(BillingDetailsActivity.this);
            String type = getIntent().getStringExtra("type");

            if (type == null) {
                info(token, id);
            } else {
                expenses(token, id);
            }
        }
    }


    /**
     * 充值详情
     *
     * @param token
     */
    public void expenses(String token, String id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        String obj = new Gson().toJson(map);
        showLoading();

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj);
        Observable<ExpensesDatailBean> data = retrofitUtils.getservice().expensesDatail(token, body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求
                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<ExpensesDatailBean>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(ExpensesDatailBean bean) {

                        dismissLoading();
                        Log.e("Infos", bean.toString() + "xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            initDatas(bean.getData());
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


    private void initDatas(ExpensesDatailBean.DataBean data) {
        if (data.getSourceType() == 1) {
            paymentMethod.setText(getResources().getString(R.string.OrderConsumption));
            moeny.setText(getResources().getString(R.string.moenys) + data.getAccount_my());

        } else if (data.getSourceType() == 2) {
            paymentMethod.setText(getResources().getString(R.string.BackgroundChange));
            if (data.getAccount_my() < 0) {
                moeny.setText(getResources().getString(R.string.moenys) + data.getAccount_my());
            } else {
                moeny.setText(getResources().getString(R.string.moenys) + data.getAccount_yajin());
            }

        } else if (data.getSourceType() == 3) {
            paymentMethod.setText(getResources().getString(R.string.WithdrawalBalance));
            moeny.setText(getResources().getString(R.string.moenys) + data.getAccount_my());
        } else if (data.getSourceType() == 4) {
            paymentMethod.setText(getResources().getString(R.string.WithdrawalDeposit));
            moeny.setText(getResources().getString(R.string.moenys) + data.getAccount_yajin());
        } else if (data.getSourceType() == 5) {
            paymentMethod.setText(getResources().getString(R.string.RechargeBalance));
            moeny.setText(getResources().getString(R.string.moenys) + data.getAccount_my());
        } else if (data.getSourceType() == 6) {
            paymentMethod.setText(getResources().getString(R.string.RechargeDeposit));
            moeny.setText(getResources().getString(R.string.moenys) + data.getAccount_yajin());
        } else if (data.getSourceType() == 7) {
            paymentMethod.setText(getResources().getString(R.string.DeductionBalance));
            moeny.setText(getResources().getString(R.string.moenys) + data.getAccount_yajin());
        } else if (data.getSourceType() == 8) {
            paymentMethod.setText(getResources().getString(R.string.LostPurchase));
            moeny.setText(getResources().getString(R.string.moenys) + data.getAccount_yajin());
        } else if (data.getSourceType() == 9) {
            paymentMethod.setText(getResources().getString(R.string.WithdrawalDeposit));
            moeny.setText(getResources().getString(R.string.moenys) + data.getAccount_yajin());
        } else {
            paymentMethod.setText(getResources().getString(R.string.RechargeDeposit));
            moeny.setText(getResources().getString(R.string.moenys) + data.getAccount_yajin());
        }
        tobalance.setText(getResources().getString(R.string.RentalConsumption));

        status.setText(getResources().getString(R.string.success));
        TransactionNumber.setText(data.getSourceId() + "");
        cardName.setVisibility(View.GONE);
        time.setText(data.getAdd_time() + "");
        tobalance.setText(getResources().getString(R.string.RentalConsumption));

       /* if (data.getStatus().equals("已支付")){
            status.setText(getResources().getString(R.string.payed));
        }else  if (data.getStatus().equals("未支付")){
            status.setText(getResources().getString(R.string.unpaid));
        }else  if (data.getStatus().equals("支付失败")){
            status.setText(getResources().getString(R.string.PaymentFailed));
        }else  if (data.getStatus().equals("请求中")){
            status.setText(getResources().getString(R.string.Request));
        }*/

      /*  if (data.getType().equals("0")){
            paymentMethod.setText(getResources().getString(R.string.RechargeDeposit));
        }else {
            paymentMethod.setText(getResources().getString(R.string.RechargeBalance));
        }*/
     /*   cardNumber.setText("**** **** **** "+data.getLast4Digits());
        TransactionNumber.setText(data.getTrade_no()+"");*/
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));

    }

    /**
     * 关于我们数据的获取
     *
     * @param token
     */
    public void info(String token, String id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        String obj = new Gson().toJson(map);
        showLoading();

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj);
        Observable<ChongzhiDetailBean> data = retrofitUtils.getservice().ChongzhiDetail(token, body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求
                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<ChongzhiDetailBean>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(ChongzhiDetailBean bean) {

                        dismissLoading();
                        Log.e("Infos", bean.toString() + "xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            initData(bean.getData());
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

    private void initData(ChongzhiDetailBean.DataBean data) {
        moeny.setText("+" + getResources().getString(R.string.moenys) + data.getChongzhi());
        time.setText(data.getAddTime() + "");
        if (data.getStatus().equals("已支付")) {
            status.setText(getResources().getString(R.string.payed));
        } else if (data.getStatus().equals("未支付")) {
            status.setText(getResources().getString(R.string.unpaid));
        } else if (data.getStatus().equals("支付失败")) {
            status.setText(getResources().getString(R.string.PaymentFailed));
        } else if (data.getStatus().equals("请求中")) {
            status.setText(getResources().getString(R.string.Request));
        } else if (data.getStatus().equals("待支付")) {
            status.setText(getResources().getString(R.string.paid));
        }

        if (data.getType().equals("0")) {
            paymentMethod.setText(getResources().getString(R.string.RechargeDeposit));
        } else {
            paymentMethod.setText(getResources().getString(R.string.RechargeBalance));
        }
        if (data.getLastFour() == null) {
            cardNumber.setText("");
        } else {
            cardNumber.setText("**** **** **** " + data.getLastFour());
        }
        if (data.getOrderId() == null) {
            TransactionNumber.setText("");
        } else {
            TransactionNumber.setText(data.getOrderId() + "");
        }

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
        if (!isLiving(BillingDetailsActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(BillingDetailsActivity.this);
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
            loadingDialog = new LoadingDialog(BillingDetailsActivity.this);
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
     * 接收事件,关闭页面
     *
     * @param message 事件类
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {
        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            finish();


        }else  if (message.isHome()){

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

    @OnClick(R.id.home)
    public void onViewClicked() {

        EventMeager eventMeager = new EventMeager();

        eventMeager.setHome(true);

        EventBus.getDefault().post(eventMeager);
    }
}
