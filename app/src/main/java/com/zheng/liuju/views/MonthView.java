package com.zheng.liuju.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.zheng.liuju.adapters.CalendarAdapter;
import com.zheng.liuju.vo.MonthData;


/**
 * Created by bob.sun on 15/8/27.
 */
public class MonthView extends GridView {
    private MonthData monthData;
    private CalendarAdapter adapter;

    public MonthView(Context context) {
        super(context);
        this.setNumColumns(7);
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setNumColumns(7);
    }

    /**
     * @deprecated
     * @param monthData
     * @return
     */
    public MonthView displayMonth(MonthData monthData){
        adapter = new CalendarAdapter(getContext(), 1, monthData.getData());
        return this;
    }

}
