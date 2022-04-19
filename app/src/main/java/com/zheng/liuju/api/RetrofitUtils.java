package com.zheng.liuju.api;

import com.zheng.liuju.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    private static RetrofitUtils INSTENCE;
    private Retrofit retrofit;

    private RetrofitUtils(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(3, TimeUnit.MINUTES);
        builder.readTimeout(3, TimeUnit.MINUTES);
        builder.writeTimeout(3, TimeUnit.MINUTES);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//日志级别
            builder.addInterceptor(loggingInterceptor);
        }
        OkHttpClient client = builder.build();

        //3创建Retrofit
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.BASE_URL)//接口
                .addConverterFactory(GsonConverterFactory.create())//默认Gson解析
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用RxJava2的适配器

                .build();
    }

    public static RetrofitUtils getInstence(){
        if (INSTENCE == null){
            INSTENCE = new RetrofitUtils();
        }
        return INSTENCE;
    }

    //创建方法 方便调用
    public ApiService getservice(){
        return retrofit.create(ApiService.class);
    }




}
