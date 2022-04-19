package com.zheng.liuju.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.zheng.liuju.utils.ScreenUtil;

@SuppressLint("AppCompatCustomView")
public class DragImageView  extends ImageView {


    private static final String TAG = "TouchView";

    /**
     * 记录X轴最后位置
     */
    int lastX = 0;
    /**
     * 记录Y轴最后位置
     */
    int lastY = 0;
    /**
     * 记录Action_Down - X轴位置
     */
    int downX = 0;
    /**
     * 记录Action_Down - Y轴位置
     */
    int downY = 0;
    /**
     * 记录Action_Down 时间
     */
    long lastDownInMills;

    final int DEFAULT_LIMITLEFT = 0;
    final int DEFAULT_LIMITTOP = 0;

    int limitLeft;
    int limitTop;
    int limitRight;
    int limitBottom;

    int screenWidth;
    int screenHeight;

    private OnClickListener l;

    public DragImageView(Context context) {
        super(context);
    }

    public DragImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenWidth =      DipUtil.getScreenWidth(context);       //metrics.widthPixels;
        screenHeight =      DipUtil.getScreenHeight(context);    //metrics.heightPixels;
        setLimitAuto();
    }

    public DragImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 自适应边界
     */
    private void setLimitAuto() {
        setLimitAuto(null);
    }

    /**
     * 根据parentView自适应边界
     *
     * @param parentView
     */
    public void setLimitAuto(ViewGroup parentView) {
        if (parentView == null) {
            this.limitLeft = DEFAULT_LIMITLEFT;
            this.limitTop = DEFAULT_LIMITTOP;
            this.limitRight = screenWidth;
            this.limitBottom = screenHeight;
        } else {
            this.limitLeft = parentView.getLeft();
            this.limitTop = parentView.getTop();
            this.limitRight = parentView.getRight();
            this.limitBottom = parentView.getBottom();
        }
    }

    /**
     * 设定自定义边界
     *
     * @param limitLeft
     * @param limitTop
     * @param limitRight
     * @param limitBottom
     */
    public void setLimitParams(int limitLeft, int limitTop, int limitRight, int limitBottom) {
        this.limitLeft = limitLeft;
        this.limitTop = limitTop;
        this.limitRight = limitRight;
        this.limitBottom = limitBottom;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.l = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //TODO 按下瞬间记录所有信息
                lastDownInMills = System.currentTimeMillis();
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                downX = (int) event.getRawX();
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //TODO 计算相对位置
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                int left = getLeft() + dx;
                int top = getTop() + dy;
                int right = getRight() + dx;
                int bottom = getBottom() + dy;

                //TODO 限制边界
                if (left < limitLeft) {
                    left = limitLeft;
                    right = left + getWidth();
                }
                if (top < limitTop) {
                    top = limitTop;
                    bottom = top + getHeight();
                }
                if (right > limitRight) {
                    right = limitRight;
                    left = limitRight - getWidth();
                }
                if (bottom > limitBottom) {
                    bottom = limitBottom;
                    top = limitBottom - getHeight();
                }

                //TODO 更新view的位置
                layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                dx = (int) event.getRawX() - downX;
                dy = (int) event.getRawY() - downY;
                if (dx + dy == 0 && System.currentTimeMillis() - lastDownInMills < 500) {
                    //TODO x + y移动距离小于2px 且 触碰时间小于500ms 触发点击事件
                    if (l != null) {
                        l.onClick(this);
                    }
                }
                break;
        }
        return true;
    }


  /*  *//**
     * 开始的位置
     *//*
    private int startLacation = 10;

    *//**
     * 可拖拽按钮-触摸开始时间
     *//*
    private long startTime = 0;

    *//**
     * 判断是点击还是移动
     *//*
    private boolean isClick = true;

    *//**
     * 按下时View的位置
     *//*
    private int downX;
    private int downY;
    *//**
     * 状态栏的高度
     *//*
    private int statusHeight;
    *//**
     * 屏幕的宽高
     *//*
    private int screenWidth;
    private int screenHeight;

    public DragImageView(Context context) {
        super(context);
        init();
    }

    public DragImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        statusHeight = DipUtil.getStatusHeight(getContext());
        screenWidth = DipUtil.getScreenWidth(getContext());
        screenHeight = DipUtil.getScreenHeight(getContext());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isClick = true;
                startTime = System.currentTimeMillis();
                downX = (int) event.getRawX();
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                isClick = false;
                //手指有移动才更新位置
                if (Math.abs(x - downX) > startLacation || Math.abs(y - downY) > startLacation) {
                    moveViewByLayout(this, x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                boolean upTime = System.currentTimeMillis() - startTime < 100;
                if (isClick) {
                    if (upTime) {
                        performClick();
                    } else {
                        performClick();
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    *//**
     * 通过layout方法，移动view
     * 优点：对view所在的布局，要求不苛刻，不要是RelativeLayout，而且可以修改view的大小
     *
     * @param view
     * @param rawX
     * @param rawY
     *//*
    private void moveViewByLayout(View view, int rawX, int rawY) {
        //记录左上点的位置
        int l = rawX - view.getWidth() / 2;
        int t = rawY - statusHeight - view.getHeight() / 2;

        //检测左右边界
        if (l < 0) {
            l = 0;
        } else if (l > screenWidth - view.getWidth()) {
            l = screenWidth - view.getWidth();
        }

        //检测上下边界
        if (t < 0) {
            t = 0;
        } else if (t > screenHeight - statusHeight - view.getHeight()) {
            t = screenHeight - statusHeight - view.getHeight();
        }

        //记录右下点的位置
        int r = l + view.getWidth();
        int b = t + view.getHeight();

        view.layout(l, t, r, b);
    }*/
 /*   private int width;
    private int height;
    private int screenWidth;
    private int screenHeight;
    private Context context;

    //是否拖动
    private boolean isDrag=false;

    public boolean isDrag() {
        return isDrag;
    }
    public DragImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width=getMeasuredWidth();
        height=getMeasuredHeight();
        screenWidth= ScreenUtil.getScreenWidth(context);
        screenHeight=ScreenUtil.getScreenHeight(context);

        Log.e("screenHeight",screenHeight+"");

    }
    public int getStatusBarHeight(){
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }


    private float downX;
    private float downY;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (this.isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isDrag=false;
                    downX = event.getX();
                    downY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.e("kid","ACTION_MOVE");
                    final float xDistance = event.getX() - downX;
                    final float yDistance = event.getY() - downY;
                    int l,r,t,b;
                    //当水平或者垂直滑动距离大于10,才算拖动事件
                    if (Math.abs(xDistance) >10 ||Math.abs(yDistance)>10) {
                        Log.e("kid","Drag");
                        isDrag=true;
                        l = (int) (getLeft() + xDistance);
                        r = l+width;
                        t = (int) (getTop() + yDistance);
                        b = t+height;
                        //不划出边界判断,此处应按照项目实际情况,因为本项目需求移动的位置是手机全屏,
                        // 所以才能这么写,如果是固定区域,要得到父控件的宽高位置后再做处理
                        if(l<0){
                            l=0;
                            r=l+width;
                        }else if(r>screenWidth){
                            r=screenWidth;
                            l=r-width;
                        }
                        if(t<0){
                            t=0;
                            b=t+height;
                        }else if(b>screenHeight){
                            b=screenHeight;
                            t=b-height;
                        }

                        this.layout(l, t, r, b);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    setPressed(false);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    setPressed(false);
                    break;
            }
            return true;
        }
        return false;
    }*/


}
