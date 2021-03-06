package com.zheng.liuju.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class MoveImageView extends ImageView {
    private int lastX = 0;
    private int lastY = 0;

    private  int screenWidth = 720; //屏幕宽度
    private  int screenHeight = 1280; //屏幕高度
    private  String TAG = "MoveImageView";

    public MoveImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics dm2 = getResources().getDisplayMetrics();
        screenWidth =    dm2.widthPixels;
        screenHeight =   dm2.heightPixels;
        Log.e(TAG, "screenWidth="+ dm2.widthPixels);
        Log.e(TAG, "screenHeight="+  dm2.heightPixels);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;

                int left = getLeft() + dx;
                int top = getTop() + dy;
                int right = getRight() + dx;
                int bottom = getBottom() + dy;
                if (left < 0) {
                    left = 0;
                    right = left + getWidth();
                }
                if (right > screenWidth) {
                    right = screenWidth;
                    left = right - getWidth();
                }
                if (top < 0) {
                    top = 0;
                    bottom = top + getHeight();
                }
                if (bottom > screenHeight) {
                    bottom = screenHeight;
                    top = bottom - getHeight();
                }
                layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

}
