package com.zheng.liuju.activity;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.view.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WithdrawActivity extends BaseActivity {

@BindView(R.id.title)
TitleView title;
    @BindView(R.id.withdraw)
    EditText withdraw;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
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
