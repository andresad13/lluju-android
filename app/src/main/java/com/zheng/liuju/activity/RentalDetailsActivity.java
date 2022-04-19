package com.zheng.liuju.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.zheng.liuju.bean.LossToBuyBean;
import com.zheng.liuju.bean.OrderDetailsBean;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.view.DialogUtils;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

public class RentalDetailsActivity extends BaseActivity implements DialogUtils.OnButtonClickListener {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.backs)
    LinearLayout backs;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.customer)
    ImageView customer;
    @BindView(R.id.lightning)
    ImageView lightning;
    @BindView(R.id.cv)
    ShadowLayout cv;
    @BindView(R.id.orderStatus)
    TextView orderStatus;
    @BindView(R.id.orderNunber)
    TextView orderNunber;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.endTime)
    TextView endTime;
    @BindView(R.id.useTime)
    TextView useTime;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.freeTime)
    TextView freeTime;
    @BindView(R.id.deviceType)
    TextView deviceType;
    @BindView(R.id.shopName)
    TextView shopName;
    @BindView(R.id.returnShopName)
    TextView returnShopName;
    @BindView(R.id.shopAdr)
    TextView shopAdr;
    @BindView(R.id.payPrice)
    TextView payPrice;
    @BindView(R.id.returnTime)
    LinearLayout returnTime;
    @BindView(R.id.returnShop)
    LinearLayout returnShop;
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
                SPUtils.putString(RentalDetailsActivity.this, "token", "");
                startActivity(new Intent(RentalDetailsActivity.this, LoginActivity.class));

                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            }


        }
    };
    private LoadingDialog loadingDialog;
    private AlertDialog dialogs;
    private String orderId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        String orderId = getIntent().getStringExtra("orderId");
        retrofitUtils = RetrofitUtils.getInstence();
        info(getToken(RentalDetailsActivity.this), orderId);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @OnClick({R.id.back, R.id.backs, R.id.customer, R.id.cv,R.id.home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.backs:
                finish();
                break;
            case R.id.customer:
                showService();
                break;
            case R.id.cv:
                showLost();
                break;
            case R.id.home:
                EventMeager eventMeager = new EventMeager();
                eventMeager.setHome(true);
                EventBus.getDefault().post(eventMeager);
                break;
        }
    }

    private void showLost() {
        Log.e("showService", "showService");
        DialogUtils dialogUtils = new DialogUtils();
        dialogUtils.showLostPurchase(RentalDetailsActivity.this);
        dialogUtils.setOnButtonClickListener(this);
    }

    private void showService() {
        String phone = SPUtils.getString(RentalDetailsActivity.this, "phone", "");
        String time = SPUtils.getString(RentalDetailsActivity.this, "time", "");
        Log.e("showService", "showService");
        DialogUtils dialogUtils = new DialogUtils();
        dialogUtils.showCustomerService(RentalDetailsActivity.this,
                getResources().getString(R.string.phone) + phone,
                getResources().getString(R.string.workingHours) + time);

    }

    /**
     * 网络请求的loading
     */
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(RentalDetailsActivity.this);
        }
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public void info(String token, String orderId) {
        Map<String, String> map = new HashMap<>();
        map.put("", "");
        String obj = new Gson().toJson(map);
        showLoading();

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj);
        Observable<OrderDetailsBean> data = retrofitUtils.getservice().orderdetails(token, orderId);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求
                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<OrderDetailsBean>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(OrderDetailsBean bean) {

                        dismissLoading();
                        Log.e("Infos", bean.toString() + "xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            initData(bean);
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

    private void initData(OrderDetailsBean bean) {
        if (bean.getData().getOrderState().contains("租借中")) {
            orderStatus.setText(getResources().getString(R.string.rent));
            orderStatus.setTextColor(Color.parseColor("#f45344"));
            cv.setVisibility(View.VISIBLE);
            returnTime.setVisibility(View.GONE);
            returnShop.setVisibility(View.GONE);
        } else if (bean.getData().getOrderState().contains("已归还")) {
            orderStatus.setTextColor(Color.parseColor("#000000"));
            orderStatus.setText(getResources().getString(R.string.Returned));
        } else if (bean.getData().getOrderState().contains("已撤销")) {
            orderStatus.setTextColor(Color.parseColor("#000000"));
            orderStatus.setText(getResources().getString(R.string.revoked));
            returnTime.setVisibility(View.GONE);
            returnShop.setVisibility(View.GONE);
        } else if (bean.getData().getOrderState().contains("存疑")) {
            orderStatus.setTextColor(Color.parseColor("#f45344"));
            orderStatus.setText(getResources().getString(R.string.doubt));
            returnTime.setVisibility(View.GONE);
            returnShop.setVisibility(View.GONE);
        } else if (bean.getData().getOrderState().contains("购买单")) {
            orderStatus.setTextColor(Color.parseColor("#000000"));
            orderStatus.setText(getResources().getString(R.string.purchase));
            returnTime.setVisibility(View.GONE);
            returnShop.setVisibility(View.GONE);
        } else if (bean.getData().getOrderState().contains("购买订单")) {
            orderStatus.setTextColor(Color.parseColor("#000000"));
            orderStatus.setText(getResources().getString(R.string.purchase));
            returnTime.setVisibility(View.GONE);
            returnShop.setVisibility(View.GONE);
        } else if (bean.getData().getOrderState().contains("故障单")) {
            orderStatus.setTextColor(Color.parseColor("#f45344"));
            orderStatus.setText(getResources().getString(R.string.Failure));
            returnTime.setVisibility(View.GONE);
            returnShop.setVisibility(View.GONE);
        } else if (bean.getData().getOrderState().contains("超时单")) {  //timeout
            orderStatus.setTextColor(Color.parseColor("#f45344"));
            orderStatus.setText(getResources().getString(R.string.timeout));
            returnTime.setVisibility(View.GONE);
            returnShop.setVisibility(View.GONE);
        } else if (bean.getData().getOrderState().contains("已支付")) {  //payed
            orderStatus.setTextColor(Color.parseColor("#f45344"));
            orderStatus.setText(getResources().getString(R.string.payed));
            returnTime.setVisibility(View.GONE);
            returnShop.setVisibility(View.GONE);
        } else {
            orderStatus.setText(bean.getData().getOrderState() + "");
            returnTime.setVisibility(View.GONE);
            returnShop.setVisibility(View.GONE);
        }

        orderId = bean.getData().getOrderNum();
        orderNunber.setText(bean.getData().getOrderNum());
        time.setText(bean.getData().getStart() + "");
        endTime.setText(bean.getData().getEnd() + "");
        useTime.setText(bean.getData().getUseTime() + "min");
        price.setText("1h/$" + bean.getData().getPrice());
        freeTime.setText(bean.getData().getFreeTime() + "min");
        deviceType.setText(bean.getData().getDeviceType());
        shopName.setText(bean.getData().getShopName());
        returnShopName.setText(bean.getData().getReturnShopName() + "");
        shopAdr.setText(bean.getData().getShopAdr());
        payPrice.setText("$" + bean.getData().getPayPrice());
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
        if (!isLiving(RentalDetailsActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(RentalDetailsActivity.this);
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

    @Override
    public void onPositiveButtonClick(int type, String context, AlertDialog dialog) {
        dialogs = dialog;
        lossToBuy(orderId, getToken(RentalDetailsActivity.this));
    }


    private void lossToBuy(String orderId, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("", "");
        String obj = new Gson().toJson(map);
        showLoading();

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj);
        Observable<LossToBuyBean> data = retrofitUtils.getservice().lossToBuy(orderId, token, body);
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
                            info(getToken(RentalDetailsActivity.this), orderId);
                            cv.setVisibility(View.GONE);
                            upRental();
                            dialogs.dismiss();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                            String addTime = df.format(new Date());
                            Intent intent = new Intent(RentalDetailsActivity.this, RechargeSuccessfulActivity.class);
                            intent.putExtra("type", "Success");
                            intent.putExtra("types", "");
                            intent.putExtra("addTime", addTime);
                            intent.putExtra("status", "0");
                            intent.putExtra("id", bean.getData() + "");
                            startActivity(intent);
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

    private void upRental() {
        EventMeager eventMeager = new EventMeager();
        eventMeager.setUpRental(true);
        EventBus.getDefault().post(eventMeager);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            finish();


        } else if (message.getPush() != null) {


        }else if (message.isHome()){

            finish();
        }
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
        if (dialogs != null) {
            dialogs.dismiss();
        }
        System.gc();
    }
}
