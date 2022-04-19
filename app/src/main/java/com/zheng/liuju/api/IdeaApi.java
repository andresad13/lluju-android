package com.zheng.liuju.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class IdeaApi {

    private static IdeaApi instence;

    private IdeaApi() {
        //   日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor((message) -> {

            try {
                String text = URLDecoder.decode(message, "utf-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();


        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constant.BASE_URL)//接口
                .addConverterFactory(GsonConverterFactory.create())//默认Gson解析
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用RxJava2的适配器
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

    }
    public static IdeaApi getApiService(){
        if (instence == null){
            instence = new IdeaApi();
        }
        return instence;
    }
}
