package com.zheng.liuju.base;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.utils.LanguageUtils;

public class BaseActivity extends AppCompatActivity {

    /**
     * 获取token
     * @param context  上下文
     * @return    token值
     */
    public   String    getToken(Context  context){
        String token = SPUtils.getString(context, "token", "");
        return token;
    }

    /**
     * 存token的值
     * @param context  上下文
     * @param token    token值
     */
    public   void     setToken(Context  context,String token){
       SPUtils.putString(context, "token", token);

    }

    /**
     * 获取经度
     * @param context  上下文
     * @return    经度值
     */
    public   String    getLongitude(Context  context){
        String longitude = SPUtils.getString(context, "Longitude", "0");
        return longitude;
    }

    /**
     * 存经度
     * @param context  上下文
     * @param longitude    经度值
     */
    public   void     setLongitude(Context  context,String longitude){
        SPUtils.putString(context, "longitude", longitude);

    }

    /**
     * 获取纬度
     * @param context  上下文
     * @return    纬度值
     */
    public   String    getLatitude(Context  context){
        String latitude = SPUtils.getString(context, "Latitude", "0");
        return latitude;
    }

    /**
     * 存纬度
     * @param context  上下文
     * @param latitude    经度值
     */
    public   void     setLatitude(Context  context,String latitude){
        SPUtils.putString(context, "latitude", latitude);

    }

    /**
     * 是否第一次登陆
     * @param context  上下文
     *
     * @return          是否第一次登陆
     */
    public   boolean   getFirst(Context context){
        boolean first = SPUtils.getBoolean(context, "first", false);
        return  first;
    }
    /**
     * 存第一次登陆值
     * @param context  上下文
     * @param first    改值
     */
    public   void     setFirst(Context  context,boolean first){
        SPUtils.putBoolean(context, "first",first);
  }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert mInputMethodManager != null;
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        LanguageUtils.setdefaultLanguage(this,"en");
    }
}
