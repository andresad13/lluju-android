package com.zheng.liuju.api;

public interface  HttpUtilsCallback {
    void onSuccess(String str);
    void onFail(int errCode, String errMsg);
}
