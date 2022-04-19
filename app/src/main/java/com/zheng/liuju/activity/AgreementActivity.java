package com.zheng.liuju.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.presenter.AgreementPresenter;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.IAgreement;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;
import com.zheng.liuju.view.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgreementActivity extends BaseActivity implements IAgreement {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.home)
    DragImageView home;
    private PromptDialog promptDialog;
    private AgreementPresenter presenter;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    //msg.what==1 弹框的关闭 msg.what==2 token过期或者账号互顶,重新登录
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
                SPUtils.putString(AgreementActivity.this, "token", "");
                startActivity(new Intent(AgreementActivity.this, LoginActivity.class));

                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            }


        }
    };
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        EventBus.getDefault().register(this);
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        presenter = new AgreementPresenter();
        presenter.attachView(this);
        title.setOnHeaderClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //根据标题请求数据
        String titles = getIntent().getStringExtra("title");
        if (titles != null) {
            if (titles.equals(getResources().getString(R.string.userAgreements))) {
                presenter.reqestAgreements(getToken(AgreementActivity.this));
            } else if (titles.equals(getResources().getString(R.string.privacyAgreement))) {
                presenter.reqestPrivacy(getToken(AgreementActivity.this));
            }
            title.setTexts(titles);
        }

    }

    //项目弹框
    @Override
    public void showDialog(String context, boolean type) {
        shows(context, type);
    }

    /**
     * 项目公用的弹框
     *
     * @param content
     * @param type    弹框的类型 true为正确弹框,false 为错误提示
     */
    public void shows(String content, boolean type) {
        if (!isLiving(AgreementActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(AgreementActivity.this);
        }
        promptDialog.setTvTip(content + "", type);
        promptDialog.show();
        handler.sendEmptyMessageDelayed(1, 2000);
    }

    //多语言设置
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    //判断activity是否销毁
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

    /**
     * 请求网络数据的loading
     */
    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(AgreementActivity.this);
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

    //对数据的处理
    @Override
    public void upData(String data) {
        content.setText(Html.fromHtml(data));
    }

    //网络请求异常的处理
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

    //销毁页面数据的处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            finish();


        } else if (message.isHome()) {
            finish();
        }
    }

    @OnClick(R.id.home)
    public void onViewClicked() {

        EventMeager eventMeager = new EventMeager();
        eventMeager.setHome(true);
        EventBus.getDefault().post(eventMeager);
    }
}
