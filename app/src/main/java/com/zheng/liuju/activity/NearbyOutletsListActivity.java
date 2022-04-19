package com.zheng.liuju.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.adapter.NearbyListAdapter;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.bean.ShopBean;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.presenter.NearbyOutletsPresenter;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.INearbyOutlets;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;
import com.zheng.liuju.view.PullToRefreshListView;
import com.zheng.liuju.view.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NearbyOutletsListActivity extends BaseActivity implements
        AdapterView.OnItemClickListener, View.OnClickListener, INearbyOutlets,
        SwipeRefreshLayout.OnRefreshListener, PullToRefreshListView.OnRefreshListener {

    @BindView(R.id.rentlist)
    PullToRefreshListView rentlist;
    @BindView(R.id.srp)
    SwipeRefreshLayout srp;
    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.nodata)
    RelativeLayout nodata;
    @BindView(R.id.home)
    DragImageView home;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    private NearbyOutletsPresenter presenter;
    private PromptDialog promptDialog;
    private ArrayList<ShopBean.DataBean> mlist = new ArrayList<>();
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
                SPUtils.putString(NearbyOutletsListActivity.this, "token", "");
                startActivity(new Intent(NearbyOutletsListActivity.this, LoginActivity.class));

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
    private NearbyListAdapter nearbyListAdapter;
    private int type;
    private int page;
    private String latitudeCenter;
    private String longitudeCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_outlets_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        EventBus.getDefault().register(this);
        title.setOnHeaderClickListener(this);
        presenter = new NearbyOutletsPresenter();
        presenter.attachView(this);
        srp.setOnRefreshListener(this);
        rentlist.setOnRefreshListener(this);
        rentlist.setOnItemClickListener(this);
        home.setOnClickListener(this);
        page = 1;
        latitudeCenter = SPUtils.getString(NearbyOutletsListActivity.this, "latitudeCenter", "0");
        longitudeCenter = SPUtils.getString(NearbyOutletsListActivity.this, "longitudeCenter", "0");
        presenter.initMap(getLatitude(NearbyOutletsListActivity.this), getLongitude(NearbyOutletsListActivity.this)
                , getToken(NearbyOutletsListActivity.this), type, page, latitudeCenter, longitudeCenter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            finish();


        }else if (message.isHome()){
            finish();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(NearbyOutletsListActivity.this, NearByOutletsDetailsActivity.class);
        intent.putExtra("shopId", mlist.get(position).getShopId());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backs:
                finish();
                break;

            case R.id.home:

                EventMeager eventMeager = new EventMeager();
                eventMeager.setHome(true);
                EventBus.getDefault().post(eventMeager);
                break;
        }
    }

    @Override
    public void showDialog(String context, boolean type) {
        shows(context, type);
    }

    public void shows(String content, boolean type) {
        if (!isLiving(NearbyOutletsListActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(NearbyOutletsListActivity.this);
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
    public void upData(List<ShopBean.DataBean> context, int typ) {
        if (type == 0) {
            initData(context);
        } else if (type == 1) {
            showDialog(getResources().getString(R.string.RefreshSuccessfully), true);
            initData(context);
        } else if (type == 2) {

            addData(context);
        }
    }

    private void addData(List<ShopBean.DataBean> context) {
        Log.e("我进来了", "addData");

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

    private void initData(List<ShopBean.DataBean> context) {
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

                nearbyListAdapter = new NearbyListAdapter(NearbyOutletsListActivity.this, mlist);
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
            loadingDialog = new LoadingDialog(NearbyOutletsListActivity.this);
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
    protected void onDestroy() {
        super.onDestroy();
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
    }


    @Override
    public void onErr() {

        handler.sendEmptyMessageDelayed(2, 3000);
    }

    @Override
    public void onRefresh() {
        type = 1;
        page = 1;
        presenter.initMap(getLatitude(NearbyOutletsListActivity.this), getLongitude(NearbyOutletsListActivity.this)
                , getToken(NearbyOutletsListActivity.this), type, 1, latitudeCenter, longitudeCenter);
    }

    @Override
    public void onDownPullRefresh() {

    }

    @Override
    public void onLoadingMore() {
        int pages = page + 1;
        type = 2;
        presenter.initMap(getLatitude(NearbyOutletsListActivity.this), getLongitude(NearbyOutletsListActivity.this)
                , getToken(NearbyOutletsListActivity.this), type, pages, latitudeCenter, longitudeCenter);
    }
}
