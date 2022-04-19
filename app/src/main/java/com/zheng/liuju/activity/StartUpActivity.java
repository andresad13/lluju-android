package com.zheng.liuju.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.util.SPUtils;

import butterknife.ButterKnife;

public class StartUpActivity extends AppCompatActivity {
    int selectedLanguage = 0;
   // @BindView(R.id.gv)
  //  GifImageView gv;
    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {

            String token = SPUtils.getString(StartUpActivity.this, "token", "");

                if (token.equals("")) {
                    startActivity(new Intent(StartUpActivity.this, LoginActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(StartUpActivity.this, MainActivity.class));
                    finish();
                }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        ButterKnife.bind(this);
        ImmersionBar.with(this).init();
        MultiLanguageUtil.attachBaseContext(this);
        MultiLanguageUtil.getInstance().updateLanguage(LanguageType.LANGUAGE_SPANISH);
        mhandler.sendEmptyMessageDelayed(1, 3000);




    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
