package com.zheng.liuju.base;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.bugly.crashreport.CrashReport;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.utils.LanguageUtils;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiLanguageUtil.init(this);
        Fresco.initialize(getApplicationContext());
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
//启用Log日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(loggingInterceptor);
        CrashReport.initCrashReport(getApplicationContext(), "b22fd369a8", false);
       // LanguageUtils.setdefaultLanguage(this,"en");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
