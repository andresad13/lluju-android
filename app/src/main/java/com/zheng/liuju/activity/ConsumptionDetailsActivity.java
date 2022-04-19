package com.zheng.liuju.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.view.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConsumptionDetailsActivity extends AppCompatActivity {
    @BindView(R.id.title)
    TitleView title;
    private int selectedLanguage = LanguageType.LANGUAGE_EN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption_details);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        title.setOnHeaderClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }
}