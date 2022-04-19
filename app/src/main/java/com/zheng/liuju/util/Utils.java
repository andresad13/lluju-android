package com.zheng.liuju.util;

public class Utils {
    public static final int DELAY = 1000;
    private static long lastClickTime = 0;
    public static boolean isNotFastClick(){
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > DELAY) {
            lastClickTime = currentTime;
            return true;
        }else{
            return false;
        }
    }
}
