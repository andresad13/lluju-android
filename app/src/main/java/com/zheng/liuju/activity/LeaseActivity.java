package com.zheng.liuju.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
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

public class LeaseActivity extends AppCompatActivity {
    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.error)
    TextView error;
    @BindView(R.id.errorTv)
    TextView errorTv;
    @BindView(R.id.online)
    ImageView online;
    @BindView(R.id.home)
    TextView home;
    @BindView(R.id.service)
    TextView service;
    @BindView(R.id.nearby)
    TextView nearby;
    @BindView(R.id.homes)
    DragImageView homes;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    private String num;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lease);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        EventBus.getDefault().register(this);
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        title.setOnHeaderClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        num = getIntent().getStringExtra("num");

        if (num != null) {
            title.setTexts(getResources().getString(R.string.Rentalsuccess));
            online.setImageResource(R.mipmap.online);
            error.setText(R.string.Succeeded);
            String tip = getResources().getString(R.string.leaseTips) + " " + "<font color='#63B15E'><big>" + num + "</big></font>" + " "
                    + getResources().getString(R.string.leaseTipss) + "<br>" + getResources().getString(R.string.leaseTipsss);

            errorTv.setText(Html.fromHtml(tip));
            nearby.setText(getResources().getString(R.string.orderDetails));
        }
        orderId = getIntent().getStringExtra("orderId");

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @OnClick({R.id.home, R.id.service, R.id.nearby,R.id.homes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home:

                break;
            case R.id.service:
                showService();
                break;
            case R.id.nearby:

                jump();

                break;
            case R.id.homes:
                EventMeager eventMeager = new EventMeager();
                eventMeager.setHome(true);
                EventBus.getDefault().post(eventMeager);
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            finish();


        } else   if (message.isHome()){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void jump() {
        if (nearby.getText().toString().equals(getResources().getString(R.string.nearbyBusinesses))) {
            startActivity(new Intent(LeaseActivity.this, NearbyOutletsListActivity.class));
            finish();
        } else {
            Intent intent = new Intent(LeaseActivity.this, RentalDetailsActivity.class);

            intent.putExtra("orderId", orderId);
            startActivity(intent);
            finish();
        }

    }

    private void showService() {
        String phone = SPUtils.getString(LeaseActivity.this, "phone", "");
        String time = SPUtils.getString(LeaseActivity.this, "time", "");
        Log.e("showService", "showService");
        DialogUtils dialogUtils = new DialogUtils();
        dialogUtils.showCustomerService(LeaseActivity.this,
                getResources().getString(R.string.phone) + phone,
                getResources().getString(R.string.workingHours) + time);

    }
}
