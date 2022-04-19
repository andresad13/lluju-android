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

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.api.Constant;
import com.zheng.liuju.api.RetrofitUtils;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.AddCardList;
import com.zheng.liuju.bean.EventMeager;
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

public class CreditCardDetailsActivity extends BaseActivity implements DialogUtils.OnButtonClickListener {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.backs)
    LinearLayout backs;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.cardnumber)
    TextView cardnumber;
    @BindView(R.id.expirationdate)
    TextView expirationdate;
    @BindView(R.id.delect)
    RelativeLayout delect;
    @BindView(R.id.visacard)
    ImageView visacard;
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.nodata)
    RelativeLayout nodata;
    @BindView(R.id.cardDetils)
    LinearLayout cardDetils;
    @BindView(R.id.home)
    DragImageView home;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    private PromptDialog promptDialog;
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
                SPUtils.putString(CreditCardDetailsActivity.this, "token", "");
                startActivity(new Intent(CreditCardDetailsActivity.this, LoginActivity.class));

                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            }


        }
    };
    private DialogUtils dialogUtils;
    private AlertDialog dialogs;
    private RetrofitUtils retrofitUtils;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        String cardNumberLast = getIntent().getStringExtra("cardNumberLast");
        nodata.setVisibility(View.GONE);
        cardDetils.setVisibility(View.VISIBLE);
        if (cardNumberLast != null) {
            cardnumber.setText("**** **** **** " + cardNumberLast);
        } else {
            shows(getResources().getString(R.string.acquisitionFailed), false);
            nodata.setVisibility(View.VISIBLE);
            cardDetils.setVisibility(View.GONE);
            return;
        }
        String expirationDate = getIntent().getStringExtra("expirationDate");
        if (expirationDate != null) {
            expirationdate.setText(expirationDate);
        } else {
            nodata.setVisibility(View.VISIBLE);
            cardDetils.setVisibility(View.GONE);
            shows(getResources().getString(R.string.acquisitionFailed), false);

        }
        String cardType = getIntent().getStringExtra("cardType");
        if (cardType != null) {
            if (cardType.contains("visa")) {
                visacard.setImageResource(R.mipmap.visacard);
            } else if (cardType.contains("maestro")) {
                visacard.setImageResource(R.mipmap.mastercard);
            } else {
                visacard.setImageResource(R.mipmap.jcb);
            }
        }
    }

    public void shows(String content, boolean type) {
        if (!isLiving(CreditCardDetailsActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(CreditCardDetailsActivity.this);
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (promptDialog != null) {
            promptDialog.dismiss();
        }
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
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            finish();


        }else if (message.isHome()){

            finish();
        }
    }

    @OnClick({R.id.backs, R.id.delect,R.id.home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backs:
                finish();
                break;
            case R.id.delect:
                delectCard();
                break;
            case  R.id.home:
                EventMeager eventMeager = new EventMeager();
                eventMeager.setHome(true);
                EventBus.getDefault().post(eventMeager);
                break;
        }

    }

    private void delectCard() {
        if (dialogUtils == null) {
            dialogUtils = new DialogUtils();
        }
        dialogUtils.setOnButtonClickListener(this);
        dialogUtils.showDeleteCreditCard(CreditCardDetailsActivity.this, "");
    }

    @Override
    public void onPositiveButtonClick(int type, String context, AlertDialog dialog) {
        dialogs = dialog;
        String id = getIntent().getStringExtra("id");
        if (id != null) {
            delete(id, getToken(CreditCardDetailsActivity.this));
        } else {
            shows(getResources().getString(R.string.acquisitionFailed), false);
        }

    }

    private void delete(String id, String token) {
        if (retrofitUtils == null) {
            retrofitUtils = RetrofitUtils.getInstence();
        }

        Map<String, String> map = new HashMap<>();
        String obj = new Gson().toJson(map);
        showLoading();
        /* retrofit=new Retrofit.Builder().baseUrl(BASE_LOGIN_URL).build();*/
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj);
        Observable<AddCardList> data = retrofitUtils.getservice().deletecardlist(id, token, body);
//改变线程 Rxjava异步
        data.subscribeOn(Schedulers.io())//Io子线程  网络请求

                .observeOn(AndroidSchedulers.mainThread())//主线程  更新控件
                .subscribe(new Observer<AddCardList>() {

                    //onSubscribe:订阅者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    //下一个
                    @Override
                    public void onNext(AddCardList bean) {

                        dismissLoading();
                        Log.e("Infos", bean.toString() + "xxxxxxxx");
                        if (bean.getCode() == Constant.SUCCESS) {
                            shows(bean.getMsg(), true);
                            EventMeager eventMeager = new EventMeager();
                            eventMeager.setupDataCard(true);
                            EventBus.getDefault().post(eventMeager);
                            nodata.setVisibility(View.VISIBLE);
                            cardDetils.setVisibility(View.GONE);
                            tip.setText(getResources().getString(R.string.nocard));
                            dialogs.dismiss();
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
                });//Io子线程  网络请求
    }

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(CreditCardDetailsActivity.this);
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
}
