package com.zheng.liuju.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LanguageActivity extends BaseActivity {
    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.english)
    RelativeLayout english;
    @BindView(R.id.other)
    RelativeLayout other;
    @BindView(R.id.imageenglish)
    ImageView imageenglish;
    @BindView(R.id.imageother)
    ImageView imageother;
    @BindView(R.id.home)
    DragImageView home;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();

    }

    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        title.setOnHeaderClickListener(v -> finish());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            finish();


        } else if (message.getPush() != null) {
            if (message.getPush().contains("歸還成功")) {

            }
        }else   if (message.isHome()){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @OnClick({R.id.english, R.id.other,R.id.home})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.english:
                selecteEnglish(imageenglish, imageother);
                break;
            case R.id.other:
                selecteEnglish(imageother, imageenglish);
                break;

            case R.id.home:
                EventMeager eventMeager = new EventMeager();
                eventMeager.setHome(true);
                EventBus.getDefault().post(eventMeager);
                break;
        }
    }

    private void selecteEnglish(ImageView selecte, ImageView noselectle) {
        selecte.setImageResource(R.mipmap.selecteds);
        noselectle.setImageResource(R.mipmap.noselecteds);
    }
}
