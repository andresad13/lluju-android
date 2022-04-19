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
import android.widget.ExpandableListView;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.adapter.MyExtendableListViewAdapter;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.CommonBean;
import com.zheng.liuju.bean.CommonProblemBean;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.presenter.CommonPresenter;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.view.DialogUtils;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.ICommonProblem;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;
import com.zheng.liuju.view.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommonProblemActivity extends BaseActivity implements ICommonProblem {
    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.expend_list)
    ExpandableListView expandableListView;
    @BindView(R.id.service)
    ImageView service;
    @BindView(R.id.home)
    DragImageView home;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    private int conunt = 100;
    private ArrayList<CommonProblemBean> mlist = new ArrayList();
    private MyExtendableListViewAdapter myExtendableListViewAdapter;
    private PromptDialog promptDialog;
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
                SPUtils.putString(CommonProblemActivity.this, "token", "");
                startActivity(new Intent(CommonProblemActivity.this, LoginActivity.class));

                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            }


        }
    };
    private LoadingDialog loadingDialog;
    private CommonPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_problem);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        //设置分组的监听
        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            if (groupPosition == conunt) {
                mlist.get(groupPosition).type = !mlist.get(groupPosition).type;
                myExtendableListViewAdapter.notfig(mlist);
                myExtendableListViewAdapter.notifyDataSetChanged();
                return false;

            }


            for (CommonProblemBean s : mlist) {
                s.type = false;
            }
            mlist.get(groupPosition).type = true;
            myExtendableListViewAdapter.notfig(mlist);
            myExtendableListViewAdapter.notifyDataSetChanged();
            conunt = groupPosition;

            myExtendableListViewAdapter.notfig(mlist);
            myExtendableListViewAdapter.notifyDataSetChanged();
            return false;
        });
        //设置子项布局监听
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> true);
        //控制他只能打开一个组
        expandableListView.setOnGroupExpandListener(groupPosition -> {
            int count = new MyExtendableListViewAdapter(mlist, CommonProblemActivity.this).getGroupCount();
            for (int i = 0; i < count; i++) {
                if (i != groupPosition) {
                    expandableListView.collapseGroup(i);
                }
            }
        });
    }

    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        EventBus.getDefault().register(this);
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        presenter = new CommonPresenter();
        presenter.attachView(this);
        presenter.info(getToken(CommonProblemActivity.this));
        title.setOnHeaderClickListener(v -> finish());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));

    }

    @Override
    public void showDialog(String context, boolean type) {
        shows(context, type);
    }

    public void shows(String content, boolean type) {
        if (!isLiving(CommonProblemActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(CommonProblemActivity.this);
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
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(CommonProblemActivity.this);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            finish();


        }else  if (message.isHome()){
            finish();
        }
    }

    @Override
    public void upInformation(List<CommonBean.DataBean> list) {
        if (list != null) {
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {


                    CommonProblemBean commonProblemBean = new CommonProblemBean();
                    commonProblemBean.groupString = list.get(i).getTitle();
                    commonProblemBean.type = false;
                    commonProblemBean.childStrings = list.get(i).getContent();
                    // Log.e("我要的数据",   commonProblemBean.childStrings);
                    mlist.add(commonProblemBean);
                }

                myExtendableListViewAdapter = new MyExtendableListViewAdapter(mlist, CommonProblemActivity.this);
                expandableListView.setAdapter(myExtendableListViewAdapter);
            } else {
                shows(CommonProblemActivity.this.getResources().getString(R.string.noData), false);
            }
        } else {
            shows(CommonProblemActivity.this.getResources().getString(R.string.noData), false);
        }
    }


    @Override
    public void onErr() {
        handler.sendEmptyMessageDelayed(2, 3000);
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


    private void showService() {
        String phone = SPUtils.getString(CommonProblemActivity.this, "phone", "");
        String time = SPUtils.getString(CommonProblemActivity.this, "time", "");
        Log.e("showService", "showService");
        DialogUtils dialogUtils = new DialogUtils();
        dialogUtils.showCustomerService(CommonProblemActivity.this,
                getResources().getString(R.string.phone) + phone,
                getResources().getString(R.string.workingHours) + time);

    }

    @OnClick({R.id.service,R.id.home})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.service:
                showService();
                break;

            case R.id.home:
                EventMeager eventMeager = new EventMeager();

                eventMeager.setHome(true);

                EventBus.getDefault().post(eventMeager);
                break;
        }

    }
}
