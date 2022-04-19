package com.zheng.liuju.views;

import android.content.Context;
import android.util.AttributeSet;

import com.zheng.liuju.listeners.OnDateClickListener;
import com.zheng.liuju.vo.DateData;


/**
 * Created by bob.sun on 15/8/28.
 */
public abstract class BaseMarkView extends BaseCellView{
    private OnDateClickListener clickListener;
    private DateData date;

    public BaseMarkView(Context context) {
        super(context);
    }

    public BaseMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
