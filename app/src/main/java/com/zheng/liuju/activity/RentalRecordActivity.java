package com.zheng.liuju.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.CellConfig;
import com.zheng.liuju.R;
import com.zheng.liuju.adapter.RentalRecordListAdapter;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.bean.OrderListBean;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.listeners.OnDateClickListener;
import com.zheng.liuju.listeners.OnExpDateClickListener;
import com.zheng.liuju.listeners.OnMonthScrollListener;
import com.zheng.liuju.presenter.RentalPresenter;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.IRental;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;
import com.zheng.liuju.view.PullToRefreshListView;
import com.zheng.liuju.view.TitleView;
import com.zheng.liuju.views.ExpCalendarView;
import com.zheng.liuju.views.WeekColumnView;
import com.zheng.liuju.vo.DateData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RentalRecordActivity extends BaseActivity implements AdapterView.OnItemClickListener, IRental, SwipeRefreshLayout.OnRefreshListener, PullToRefreshListView.OnRefreshListener {
    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.rentlist)
    PullToRefreshListView rentlist;
    @BindView(R.id.srp)
    SwipeRefreshLayout srp;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.scelt)
    RelativeLayout scelt;
    @BindView(R.id.backss)
    ImageView backss;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.filters)
    ImageView filters;
    @BindView(R.id.week)
    WeekColumnView week;
    @BindView(R.id.calendar_exp)
    ExpCalendarView expCalendarView;
    @BindView(R.id.main_expandIV)
    RelativeLayout mainExpandIV;
    @BindView(R.id.YearMonthTv)
    TextView YearMonthTv;
    @BindView(R.id.nodata)
    RelativeLayout nodata;
    @BindView(R.id.home)
    DragImageView home;
    private boolean all;


    private DateData selectedDate;
    private DateData twoDate;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    private int currentPage = 500;
    private int currentMonth;
    private int currentYear;
    private RentalPresenter presenter;
    private PromptDialog promptDialog;
    private ArrayList<OrderListBean.DataBean> mlist = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                if (promptDialog != null) {
                    promptDialog.dismiss();
                    promptDialog = null;
                }
            } else if (msg.what == 2) {
                SPUtils.putString(RentalRecordActivity.this, "token", "");
                startActivity(new Intent(RentalRecordActivity.this, LoginActivity.class));

                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            } else if (msg.what == 3) {
                if (rentlist != null) {
                    rentlist.onRefreshComplete();
                }
            }


        }
    };
    private LoadingDialog loadingDialog;
    private RentalRecordListAdapter nearbyListAdapter;
    private int type;
    private int page;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_record);
        ButterKnife.bind(this);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        EventBus.getDefault().register(this);
        title.setOnHeaderClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Calendar calendar = Calendar.getInstance();
        selectedDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        expCalendarView.markDate(selectedDate);

        CellConfig.Month2WeekPos = CellConfig.middlePosition;
        CellConfig.ifMonth = false;

        CellConfig.weekAnchorPointDate = selectedDate;
        expCalendarView.shrink();
        imageInit();


        rentlist.setOnItemClickListener(this);

        home.setOnClickListener(v -> {
            EventMeager eventMeager = new EventMeager();

            eventMeager.setHome(true);

            EventBus.getDefault().post(eventMeager);
        });

        filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!all) {
                    filters.setImageResource(R.mipmap.filters);
                    all = true;

                    expCalendarView.unMarkDate(selectedDate);
                    expCalendarView.unMarkDate(twoDate);
                    expCalendarView.invalidate();
                    type = 0;
                    page = 1;
                    time = "";
                    presenter.initOrder(getLatitude(RentalRecordActivity.this), getLongitude(RentalRecordActivity.this)
                            , getToken(RentalRecordActivity.this), type, page, time);

                } else {

                    currentPage = 500;
                    expCalendarView.setCurrentItem(currentPage);
                    YearMonthTv.setText(getMonth((Calendar.getInstance().get(Calendar.MONTH) + 1)) + " " + Calendar.getInstance().get(Calendar.YEAR));
                    CellConfig.Month2WeekPos = CellConfig.middlePosition;
                    CellConfig.ifMonth = false;
                    backss.setImageResource(R.mipmap.bottom);
                    CellConfig.weekAnchorPointDate = selectedDate;
                    expCalendarView.markDate(selectedDate);
                    expCalendarView.shrink();
                    expCalendarView.invalidate();
                    //   expCalendarView.invalidate();
                    filters.setImageResource(R.mipmap.filter);
                    expCalendarView.setMarkedStyle(10, Color.parseColor("#F45344"));
                    expCalendarView.invalidate();
                    all = false;

                    type = 0;
                    page = 1;
                    time = getNowadaysTime();
                    presenter.initOrder(getLatitude(RentalRecordActivity.this), getLongitude(RentalRecordActivity.this)
                            , getToken(RentalRecordActivity.this), type, page, time);
                }
            }
        });
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        currentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1);

        YearMonthTv.setText(getMonth((Calendar.getInstance().get(Calendar.MONTH) + 1)) + " " + Calendar.getInstance().get(Calendar.YEAR));

//      Set up listeners.
        expCalendarView.setOnDateClickListener(new OnExpDateClickListener()).setOnMonthScrollListener(new OnMonthScrollListener() {
            @Override
            public void onMonthChange(int year, int month) {
                // YearMonthTv.setText(year, month));
                YearMonthTv.setText(getMonth(month) + " " + year);
                currentMonth = month;
                currentYear = year;
            }

            @Override
            public void onMonthScroll(float positionOffset) {
                Log.e("listener", "onMonthScroll:" + positionOffset);
            }
        });

        expCalendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                expCalendarView.getMarkedDates().removeAdd();
                expCalendarView.markDate(date);
                // selectedDate = date;
                twoDate = date;
                all = false;
                filters.setImageResource(R.mipmap.filter);
                Log.e("我要的数据", date.getYear() + "_" + date.getMonth() + "_" + date.getDay());
                String moneth = "";
                String day = "";
                if (date.getMonth() < 10) {
                    moneth = "0" + date.getMonth();
                } else {
                    moneth = date.getMonth() + "";
                }
                if (date.getDay() < 10) {
                    day = "0" + date.getDay();
                } else {
                    day = date.getDay() + "";
                }
                time = date.getYear() + "-" + moneth + "-" + day;
                type = 0;
                page = 1;
                presenter.initOrder(getLatitude(RentalRecordActivity.this), getLongitude(RentalRecordActivity.this)
                        , getToken(RentalRecordActivity.this), type, page, time);
                int month = date.getMonth();
                int year = date.getYear();
                if (year > currentYear) {

                    expCalendarView.setCurrentItem(currentPage + 1);
                } else if (year == currentYear) {
                    if (month > currentMonth) {
                        expCalendarView.setCurrentItem(currentPage + 1);

                    }
                }


            }
        });


        expCalendarView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("资产", "选择位置：" + position);
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        presenter = new RentalPresenter();
        presenter.attachView(this);
        srp.setOnRefreshListener(this);
        rentlist.setOnRefreshListener(this);
        time = getNowadaysTime();
        page = 1;
        presenter.initOrder(getLatitude(RentalRecordActivity.this), getLongitude(RentalRecordActivity.this)
                , getToken(RentalRecordActivity.this), type, page, time);
    }

    private boolean ifExpand = false;

    private void imageInit() {
        final RelativeLayout expandIV = (RelativeLayout) findViewById(R.id.main_expandIV);
        final ImageView backss = (ImageView) findViewById(R.id.backss);
        expandIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifExpand) {
                    CellConfig.Month2WeekPos = CellConfig.middlePosition;
                    CellConfig.ifMonth = false;
                    backss.setImageResource(R.mipmap.bottom);
                    CellConfig.weekAnchorPointDate = twoDate;
                    expCalendarView.shrink();
                    view.setVisibility(View.GONE);
                    scelt.setVisibility(View.GONE);
                    line.setVisibility(View.GONE);
                    filters.setVisibility(View.VISIBLE);
                } else {
                    CellConfig.Week2MonthPos = CellConfig.middlePosition;
                    CellConfig.ifMonth = true;
                    backss.setImageResource(R.mipmap.top);
                    expCalendarView.expand();
                    view.setVisibility(View.VISIBLE);
                    scelt.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    filters.setVisibility(View.GONE);
                }
                ifExpand = !ifExpand;
            }
        });
    }

    /**
     * @return 获取当前日期
     */
    private String getNowadaysTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(RentalRecordActivity.this, RentalDetailsActivity.class);
        intent.putExtra("orderId", mlist.get(position).getOrderNum());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        //  expCalendarView=null;
        expCalendarView.unMarkDate(selectedDate);
        if (twoDate != null) {
            expCalendarView.unMarkDate(twoDate);
        }

        //2020-04-01  增加代码 (翻转多页日期,退出重新,日期乱码)
        Calendar calendar = Calendar.getInstance();
        selectedDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        expCalendarView.travelTo(selectedDate);
        EventBus.getDefault().unregister(this);
        presenter.onDestroy();
        presenter = null;
        handler.removeMessages(1);
        handler.removeMessages(2);
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (promptDialog != null) {
            promptDialog.dismiss();
        }

        System.gc();
        super.onDestroy();
    }

    @Override
    public void showDialog(String context, boolean type) {
        shows(context, type);
    }

    public void shows(String content, boolean type) {
        if (!isLiving(RentalRecordActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(RentalRecordActivity.this);
        }
        promptDialog.setTvTip(content + "", type);
        promptDialog.show();
        handler.sendEmptyMessageDelayed(1, 2000);
    }

    private static boolean isLiving(Activity activity) {

        if (activity == null) {
            Log.d("wisely", "activity == null");
            return false;
        }

        if (activity.isFinishing()) {
            Log.d("wisely", "activity is finishing");
            return false;
        }


        return true;
    }

    @Override
    public void upData(List<OrderListBean.DataBean> context, int type) {
        if (type == 0) {
            initData(context);
        } else if (type == 1) {
            showDialog(getResources().getString(R.string.RefreshSuccessfully), true);
            initData(context);
        } else if (type == 2) {

            addData(context);
        }
    }

    private void addData(List<OrderListBean.DataBean> context) {
        if (context != null) {
            if (context.size() != 0) {
                mlist.addAll(context);
                page = page + 1;
                if (nearbyListAdapter != null) {
                    nearbyListAdapter.notifyDataSetChanged();

                }
            } else {
                showDialog(getResources().getString(R.string.noData), true);
            }
        } else {
            showDialog(getResources().getString(R.string.noData), true);
        }
        handler.sendEmptyMessageDelayed(3, 3000);
    }

    private void initData(List<OrderListBean.DataBean> context) {
        if (mlist.size() != 0) {
            srp.setRefreshing(false);
            mlist.clear();
            if (nearbyListAdapter != null) {
                nearbyListAdapter.notifyDataSetChanged();
            }
        }
        if (context != null) {
            if (context.size() != 0) {
                nodata.setVisibility(View.GONE);
                srp.setVisibility(View.VISIBLE);
                rentlist.setVisibility(View.VISIBLE);
                mlist.addAll(context);
                nearbyListAdapter = new RentalRecordListAdapter(RentalRecordActivity.this, mlist);

                rentlist.setAdapter(nearbyListAdapter);
            } else {
                nodata.setVisibility(View.VISIBLE);
                srp.setVisibility(View.GONE);
                rentlist.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(RentalRecordActivity.this);
        }
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    @Override
    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onErr() {
        handler.sendEmptyMessageDelayed(2, 3000);
    }

    @Override
    public void onRefresh() {
        type = 1;
        page = 1;
        presenter.initOrder(getLatitude(RentalRecordActivity.this), getLongitude(RentalRecordActivity.this)
                , getToken(RentalRecordActivity.this), type, page, time);
    }

    @Override
    public void onDownPullRefresh() {

    }

    @Override
    public void onLoadingMore() {
        int pages = page + 1;
        type = 2;
        presenter.initOrder(getLatitude(RentalRecordActivity.this), getLongitude(RentalRecordActivity.this)
                , getToken(RentalRecordActivity.this), type, pages, time);
    }

    private String getMonth(int month) {
        String months = "";
        if (month == 1) {
            months = "January";
        } else if (month == 2) {
            months = "February";
        } else if (month == 3) {
            months = "March";
        } else if (month == 4) {
            months = "April";
        } else if (month == 5) {
            months = "May";
        } else if (month == 6) {
            months = "June";
        } else if (month == 7) {
            months = "July";
        } else if (month == 8) {
            months = "August";
        } else if (month == 9) {
            months = "September";
        } else if (month == 10) {
            months = "October";
        } else if (month == 11) {
            months = "November";
        } else if (month == 12) {
            months = "December";
        }
        return months;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            finish();

        } else if (message.isUpRental()) {
            type = 1;
            page = 1;
            presenter.initOrder(getLatitude(RentalRecordActivity.this), getLongitude(RentalRecordActivity.this)
                    , getToken(RentalRecordActivity.this), type, page, time);
        }else if (message.isHome()){
            finish();
        }
    }
}
