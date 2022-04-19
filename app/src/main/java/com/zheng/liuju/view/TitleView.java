package com.zheng.liuju.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zheng.liuju.R;

public class TitleView extends LinearLayout {
    private String titleText;
    private TextView titleTv;
    private ImageView back;
    private LinearLayout backs;
    private  boolean   hide;


    public TitleView(Context context) {
        super(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //加载视图的布局
        LayoutInflater.from(context).inflate(R.layout.view_title,this,true);

        //加载自定义的属性
        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.Headers);
        titleText=a.getString(R.styleable.Headers_titleText);
        hide = a.getBoolean(R.styleable.Headers_titleTexthide,false);

        //回收资源，这一句必须调用
        a.recycle();
    }

    /**
     * 此方法会在所有的控件都从xml文件中加载完成后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //获取子控件
        backs = (LinearLayout) findViewById(R.id.backs);
        titleTv = (TextView) findViewById(R.id.titleTv);
        back = (ImageView) findViewById(R.id.back);

        //将从资源文件中加载的属性设置给子控件
        if (!TextUtils.isEmpty(titleText))
            setPageTitleText(titleText);


    }

    /**
     * 设置标题文字
     * @param text
     */
    public void setPageTitleText(String text) {
        titleTv.setText(text);
    }

    /**
     * 设置按钮点击事件监听器
     * @param listener
     */
    public void setOnHeaderClickListener(OnClickListener listener) {
        backs.setOnClickListener(listener);
    }
    public  void  setTexts(String text){
        titleTv.setText(text);
    }
}
