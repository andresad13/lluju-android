package com.zheng.liuju.views;

import android.content.Context;
import android.util.AttributeSet;

import com.zheng.liuju.adapters.CalendarExpAdapter;
import com.zheng.liuju.vo.MonthWeekData;


/**
 * Created by Bigflower on 2015/12/8.
 */
public class MonthViewExpd extends MonthView {
    private MonthWeekData monthWeekData;
    private CalendarExpAdapter adapter;

    public MonthViewExpd(Context context) {
        super(context);
    }

    public MonthViewExpd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initMonthAdapter(int pagePosition, int cellView, int markView) {
        getMonthWeekData(pagePosition);
        adapter = new CalendarExpAdapter(getContext(), 1, monthWeekData.getData()).setCellViews(cellView, markView);
        this.setAdapter(adapter);
    }

    private void getMonthWeekData(int position) {
        monthWeekData = new MonthWeekData(position);
    }


}
