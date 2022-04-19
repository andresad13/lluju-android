package com.zheng.liuju.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.view.DialogUtils;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargeSuccessfulActivity extends BaseActivity {
    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.successful)
    ImageView successful;
    @BindView(R.id.recharge)
    TextView recharge;
    @BindView(R.id.time)
    TextView time;
    /*    @BindView(R.id.order)
        CardView order;*/
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.home)
    DragImageView home;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    private String type;
    private String id;

    private String types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_successful);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        EventBus.getDefault().register(this);
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        title.setOnHeaderClickListener(v -> finish());
        type = getIntent().getStringExtra("type");
        if (type != null) {
            if (type.equals("0")) {
                successful.setImageResource(R.mipmap.failure);
                recharge.setText(getResources().getString(R.string.failedTip));
                time.setText(getResources().getString(R.string.failedTips));
                tip.setVisibility(View.GONE);
            }
        }
        String addTime = getIntent().getStringExtra("addTime");
        if (addTime != null) {
            time.setText(addTime);
        }
        id = getIntent().getStringExtra("id");
        types = getIntent().getStringExtra("types");

        //  intent.putExtra("types", "");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @OnClick({R.id.tip,R.id.home})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tip:
                orderDetails();
                break;
            case R.id.home:
                EventMeager eventMeager = new EventMeager();
                eventMeager.setHome(true);
                EventBus.getDefault().post(eventMeager);
                break;
        }

    }

    private void orderDetails() {
        if (type.equals("0")) {
            showService();
        } else {
            Intent intent = new Intent(RechargeSuccessfulActivity.this, BillingDetailsActivity.class);
            intent.putExtra("id", id + "");
            if (types != null) {
                intent.putExtra("type", id + "");
            }
            startActivity(intent);
            finish();
        }
    }

    private void showService() {
        String phone = SPUtils.getString(RechargeSuccessfulActivity.this, "phone", "");
        String time = SPUtils.getString(RechargeSuccessfulActivity.this, "time", "");
        Log.e("showService", "showService");
        DialogUtils dialogUtils = new DialogUtils();
        dialogUtils.showCustomerService(RechargeSuccessfulActivity.this,
                getResources().getString(R.string.phone) + phone,
                getResources().getString(R.string.workingHours) + time);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            finish();

        } else if (message.isHome()){
            finish();
        }
    }
}
