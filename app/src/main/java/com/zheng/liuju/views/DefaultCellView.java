package com.zheng.liuju.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zheng.liuju.CellConfig;
import com.zheng.liuju.MarkStyleExp;
import com.zheng.liuju.vo.DayData;


/**
 * Created by bob.sun on 15/8/28.
 */
public class DefaultCellView extends BaseCellView {
    public TextView textView;
    private AbsListView.LayoutParams matchParentParams;
    public DefaultCellView(Context context) {
        super(context);
        initLayout();
    }

    public DefaultCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    private void initLayout(){
        matchParentParams = new AbsListView.LayoutParams((int) CellConfig.cellWidth, (int) CellConfig.cellHeight);
        this.setLayoutParams(matchParentParams);
        this.setOrientation(VERTICAL);
        textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0));
        this.addView(textView);
    }

    @Override
    public void setDisplayText(DayData day) {
        textView.setText(day.getText());
    }

    @Override
    protected void onMeasure(int measureWidthSpec,int measureHeightSpec){
        super.onMeasure(measureWidthSpec, measureHeightSpec);
    }

    public boolean setDateChoose() {
        setBackgroundDrawable(MarkStyleExp.choose);
        textView.setTextColor(Color.WHITE);
        return true ;
    }

    public void setDateToday(){
       setBackgroundDrawable(MarkStyleExp.today);
        textView.setTextColor(Color.parseColor("#63B15E"));   //Color.rgb(105, 75, 125)
    }

    public void setDateNormal() {
        textView.setTextColor(Color.BLACK);
        setBackgroundDrawable(null);
    }

    public void setTextColor(String text, int color) {
        textView.setText(text);
        if (color != 0) {
            textView.setTextColor(color);
        }
    }
}
