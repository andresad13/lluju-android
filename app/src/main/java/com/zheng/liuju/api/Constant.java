package com.zheng.liuju.api;

public class Constant {
    public static final String BASE_URL = "https://cdb.lluju.com/";   //服务器
    public static final int SUCCESS = 1;   //api请求成功的CODE
    public static final int AUTHORIZATION = 2;   //支付预授权code
    public static final int MUTUAL = -1;   //账号互顶的CODE
    public static final int TOKEN_MISS = -1;   //token过期的CODE
    public static final int FAILURE = 0;   //弹出电池失败
    public static final String PAY_KEY = "oqZS5ccsywe4JJds7gDx3wU4oty1y2LWsKOxX1ZvxezhXNK53nC7Uwci0LjyN5ufPjNMkMiS1R9YFT4JhGQbd3udONLCTgSIzFvU";
    public static final String Merchant_Account = "sha7n@sric-technology.com";   //token过期的CODE
}
//{"code":2,"msg":"余额充足租借","total":0,"orderID":"202003311651443904172","deviceID":"3568175CA20E6426","batteryID":"3509075CECA1F92A"}