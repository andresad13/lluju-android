package com.zheng.liuju.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.zheng.liuju.R;

import java.text.SimpleDateFormat;

/**
 * Created by lx on 2019/4/19.
 */

public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener {
    private static final String TAG = "PullToRefreshListView";

    private int firstVisibleItemPosition; // 屏幕显示在第一个的item的索引
    private int downY; // 按下时y轴的偏移量
    private int headerViewHeight; // 头布局的高度
    private View headerView; // 头布局的对象

    private final int DOWN_PULL_REFRESH = 0; // 下拉刷新状态
    private final int RELEASE_REFRESH = 1; // 松开刷新
    private final int REFRESHING = 2; // 正在刷新中
    private int currentState = DOWN_PULL_REFRESH; // 头布局的状态: 默认为下拉刷新状态

    private Animation upAnimation; // 向上旋转的动画
    private Animation downAnimation; // 向下旋转的动画

    private ImageView ivArrow; // 头布局的剪头
    private ProgressBar mProgressBar; // 头布局的进度条
    private TextView tvState; // 头布局的状态
    private TextView tvLastUpdateTime; // 头布局的最后更新时间

    private OnRefreshListener mOnRefershListener;
    private boolean isScrollToBottom; // 是否滑动到底部
    private View footerView; // 脚布局的对象
    private int footerViewHeight; // 脚布局的高度
    private boolean isLoadingMore = false; // 是否正在加载更多中

    private boolean isLoadMore = true;	//是否可以加载更多，有时候，数据已经加载完，就不可加载更多了

    // 实际的padding的距离与界面上偏移距离的比例，越大，拉的越费劲(阻尼系数，damping ratio)
    private final static float RATIO = 3.5f;

    //回弹效果
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 100;
    private int mMaxYOverscrollDistance;

    public PullToRefreshListView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
        init(context);

    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullToRefreshListView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
      //  initHeaderView(context);
        initFooterView(context);
        //initBounceListView(context);
        this.setOnScrollListener(this);
    }


   /* @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN :
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE :
                int moveY = (int) ev.getY();
                // 移动中的y - 按下的y = 间距.
                //			int diff = (moveY - downY) / 2;
                int diff = (int) (((float)moveY - (float)downY) / RATIO);
                // -头布局的高度 + 间距 = paddingTop
                int paddingTop = -headerViewHeight + diff;
                // 如果: -头布局的高度 > paddingTop的值 执行super.onTouchEvent(ev);
                //			if (firstVisibleItemPosition == 0 && -headerViewHeight < paddingTop) {
                //simon修改,diff>0,表示在顶部，正在下拉，头部的view渐渐显示出来
                //simon修改，currentState != REFRESHING，不同时执行两次下拉刷新，只是界面会下拉，不会调用两次刷新方法。加不加都行
                //simon修改，!isLoadingMore，当正在加载更多时，不允许下拉刷新
                if (firstVisibleItemPosition == 0 && diff>0 && currentState != REFRESHING && !isLoadingMore) {
                    if (paddingTop > 0 && currentState == DOWN_PULL_REFRESH) { // 完全显示了.
                        Log.i(TAG, "松开刷新");
                        currentState = RELEASE_REFRESH;
                        refreshHeaderView();
                    } else if (paddingTop < 0 && currentState == RELEASE_REFRESH) { // 没有显示完全
                        Log.i(TAG, "下拉刷新");
                        currentState = DOWN_PULL_REFRESH;
                        refreshHeaderView();
                    }
                    // 下拉头布局
                    headerView.setPadding(0, paddingTop, 0, 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP :
                // 判断当前的状态是松开刷新还是下拉刷新
                if (currentState == RELEASE_REFRESH) {
                    Log.i(TAG, "刷新数据.");
                    // 把头布局设置为完全显示状态
                    headerView.setPadding(0, 0, 0, 0);
                    // 进入到正在刷新中状态
                    currentState = REFRESHING;
                    refreshHeaderView();

                    if (mOnRefershListener != null) {
                        mOnRefershListener.onDownPullRefresh(); // 调用使用者的监听方法
                    }

                } else if (currentState == DOWN_PULL_REFRESH) {
                    // 隐藏头布局
                    headerView.setPadding(0, -headerViewHeight, 0, 0);
                }
                break;
            default :
                break;
        }
        return super.onTouchEvent(ev);
    }*/


    /**
     * 根据currentState刷新头布局的状态
     */
    private void refreshHeaderView() {
     /*   switch (currentState) {
            case DOWN_PULL_REFRESH : // 下拉刷新状态
              //  tvState.setText("下拉刷新");
                ivArrow.startAnimation(downAnimation); // 执行向下旋转
                break;
            case RELEASE_REFRESH : // 松开刷新状态
               //tvState.setText("松开刷新");
                ivArrow.startAnimation(upAnimation); // 执行向上旋转
                break;
            case REFRESHING : // 正在刷新中状态
                ivArrow.clearAnimation();
                ivArrow.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
               // tvState.setText("正在刷新中...");
                break;
            default :
                break;
        }*/
    }

    /**
     * 当滚动状态改变时回调
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //正在滚动时回调，回调2-3次，手指没抛则回调2次。scrollState = 2的这次不回调
        //回调顺序如下
        //第1次：scrollState = SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动
        //第2次：scrollState = SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
        //第3次：scrollState = SCROLL_STATE_IDLE(0) 停止滚动
        //当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1；
        //由于用户的操作，屏幕产生惯性滑动时为2

        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
            // 判断当前是否已经到了底部
            //Simon，isLoadMore() 获取加载更多状态
            //Simon，currentState != REFRESHING，当正在下拉刷新时，不允许加载更多
            if (isScrollToBottom && !isLoadingMore && isLoadMore() && currentState != REFRESHING) {
                isLoadingMore = true;
                // 当前到底部
                Log.i(TAG, "加载更多数据-"+this.getCount());
                footerView.setPadding(0, 0, 0, 0);
                //				this.setSelection(this.getCount());
                if (mOnRefershListener != null) {
                    mOnRefershListener.onLoadingMore();
                }
            }
        }

    }

    /**
     * 当滚动时调用
     *
     * @param firstVisibleItem
     *            当前屏幕显示在顶部的item的position，firstVisibleItem==0	//滑到顶部
     * @param visibleItemCount
     *            当前屏幕显示了多少个条目的总数
     * @param totalItemCount
     *            ListView的总条目的总数
     *
     *	if(visibleItemCount+firstVisibleItem==totalItemCount){//此方法也可判断是否滑到底部
     *     Log.e("log", "滑到底部");
     *  }
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        firstVisibleItemPosition = firstVisibleItem;

        if (getLastVisiblePosition() == (totalItemCount - 1)) {	//滑到底部
            isScrollToBottom = true;
        } else {
            isScrollToBottom = false;
        }

    }

    //设置回弹效果
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
                                   int scrollRangeX, int scrollRangeY, int maxOverScrollX,
                                   int maxOverScrollY, boolean isTouchEvent){
        //This is where the magic happens, we have replaced the incoming maxOverScrollY
        //with our own custom variable mMaxYOverscrollDistance;
        Log.i(TAG, "deltaX="+deltaX);
        Log.i(TAG, "deltaY="+deltaY);
        Log.i(TAG, "scrollX="+scrollX);
        Log.i(TAG, "scrollY="+scrollY);
        Log.i(TAG, "scrollRangeX="+scrollRangeX);
        Log.i(TAG, "scrollRangeY="+scrollRangeY);
        Log.i(TAG, "maxOverScrollX="+maxOverScrollX);
        Log.i(TAG, "mMaxYOverscrollDistance=>"+mMaxYOverscrollDistance);
        Log.i(TAG, "isTouchEvent="+isTouchEvent);
        Log.i(TAG, "---分割线---");

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX,
                mMaxYOverscrollDistance, isTouchEvent);//mMaxYOverscrollDistance

    }

    private void initBounceListView(Context context){
        //get the density of the screen and do some maths with it on the max overscroll distance
        //variable so that you get similar behaviors no matter what the screen size
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final float density = metrics.density;
        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
    }

    /**
     * 初始化脚布局
     */
    private void initFooterView(Context context) {
        footerView = LinearLayout.inflate(context, R.layout.listview_foot,null);
        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        this.addFooterView(footerView);
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView(Context context) {
        headerView = LinearLayout.inflate(context, R.layout.listview_header,null);
        ivArrow = (ImageView) headerView
                .findViewById(R.id.iv_listview_header_arrow);
        mProgressBar = (ProgressBar) headerView
                .findViewById(R.id.pb_listview_header);
        tvState = (TextView) headerView
                .findViewById(R.id.tv_listview_header_state);
        tvLastUpdateTime = (TextView) headerView
                .findViewById(R.id.tv_listview_header_last_update_time);

        // 设置最后刷新时间
        tvLastUpdateTime.setText("最后刷新时间: " + getLastUpdateTime());

        headerView.measure(0, 0); // 系统会帮我们测量出headerView的高度
        headerViewHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        this.addHeaderView(headerView); // 向ListView的顶部添加一个view对象
        initAnimation();
    }

    /***
     * 上下拉刷新完成
     */
    public void onRefreshComplete(){
        //正在加载更多，则隐藏FooterView
        if (isLoadingMore) {
            hideFooterView();
        }else if (currentState == REFRESHING) {
            hideHeaderView();
        }
    }

    /**
     * 隐藏头布局
     */
    private void hideHeaderView() {
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        ivArrow.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        tvState.setText("下拉刷新");
        tvLastUpdateTime.setText("最后刷新时间: " + getLastUpdateTime());
        currentState = DOWN_PULL_REFRESH;
    }

    /**
     * 隐藏脚布局
     */
    private void hideFooterView() {
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        isLoadingMore = false;
    }


    /**
     * 获得系统的最新时间
     *
     * @return
     */
    private String getLastUpdateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(System.currentTimeMillis());
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
		/*
		 * Animation.RELATIVE_TO_SELF 	相对于自身的动画
		 * Animation.RELATIVE_TO_PARENT 相对于父控件的动画
		 * 0.5f，表示在控件自身的 x，y的中点坐标处，为动画的中心。
		 *
		 * 设置动画的变化速率
		 * setInterpolator(newAccelerateDecelerateInterpolator())：先加速，后减速
		 * setInterpolator(newAccelerateInterpolator())：加速
		 * setInterpolator(newDecelerateInterpolator())：减速
		 * setInterpolator(new CycleInterpolator())：动画循环播放特定次数，速率改变沿着正弦曲线
		 * setInterpolator(new LinearInterpolator())：匀速
		 */
        upAnimation = new RotateAnimation(0f, -180f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        upAnimation.setInterpolator(new LinearInterpolator());
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上

        downAnimation = new RotateAnimation(-180f, -360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        downAnimation.setInterpolator(new LinearInterpolator());
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上
    }

    /***
     * 设置 监听
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefershListener = listener;
    }

    public interface OnRefreshListener {
        /**
         * 下拉刷新
         */
        void onDownPullRefresh();

        /**
         * 上拉加载更多
         */
        void onLoadingMore();
    }

    public interface onDownPullRefresh {
        public void onRefresh();
    }

    /***
     * 获取是否可以加载更多
     * @return
     */
    public boolean isLoadMore() {
        return isLoadMore;
    }

    /***
     * 设置是否可以加载更多
     * @param isLoadMore
     */
    public void setLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }
}
