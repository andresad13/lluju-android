package com.zheng.liuju.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;

import com.gigamole.library.ShadowLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.presenter.CooperationPresenter;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.ICooperation;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;
import com.zheng.liuju.view.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CooperationActivity extends BaseActivity implements ICooperation, View.OnTouchListener {
    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.banner)
    ImageView banner;
    @BindView(R.id.cv)
    ShadowLayout cv;
    @BindView(R.id.shopName)
    EditText shopName;
    @BindView(R.id.userName)
    EditText userName;
    @BindView(R.id.userPhone)
    EditText userPhone;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.sv)
    ScrollView sv;
    @BindView(R.id.home)
    DragImageView home;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    private CooperationPresenter presenter;
    private PromptDialog promptDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                if (promptDialog != null) {
                    promptDialog.dismiss();
                    promptDialog = null;
                }
            } else if (msg.what == 2) {
                SPUtils.putString(CooperationActivity.this, "token", "");
                startActivity(new Intent(CooperationActivity.this, LoginActivity.class));

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
        setContentView(R.layout.activity_cooperation);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        EventBus.getDefault().register(this);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        presenter = new CooperationPresenter();
        presenter.attachView(this);
        sv.setOnTouchListener(this);
        banner.setImageResource(R.mipmap.cooperation);   //资源文件中的一张图片
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();    //获取屏幕的宽度
        banner.measure(0, 0);  //这一步很重要
        int height = banner.getMeasuredHeight() * width / banner.getMeasuredWidth();  //这里就是将高度等比例缩放，其中imageView.getMeasuredHeight()和imageView.getMeasuredWidth()是计算图片本身的宽高
        banner.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        title.setOnHeaderClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @OnClick({R.id.cv,R.id.home})
    public void onViewClicked(View view) {

        switch (view.getId()){

            case R.id.cv:
                presenter.cooper(getContent(), getShopName(), getUserName(), getUserPhone(), CooperationActivity.this, getToken(CooperationActivity.this));
                break;

            case R.id.home:
                EventMeager eventMeager = new EventMeager();

                eventMeager.setHome(true);

                EventBus.getDefault().post(eventMeager);
                break;

        }

    }


    private String getContent() {
        return content.getText().toString().trim();
    }

    private String getShopName() {
        return shopName.getText().toString().trim();
    }

    private String getUserName() {
        return userName.getText().toString().trim();
    }

    private String getUserPhone() {
        return userPhone.getText().toString().trim();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        presenter.onDestroy();
        presenter = null;
        if (promptDialog!=null){
            promptDialog.dismiss();
        }

        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void showDialog(String context, boolean type) {
        shows(context, type);
    }

    private void shows(String content, boolean type) {
        if (!isLiving(CooperationActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(CooperationActivity.this);
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
             loadingDialog = new LoadingDialog(CooperationActivity.this);
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
    public boolean onTouch(View v, MotionEvent event) {
        inputClose(sv, CooperationActivity.this);
        return false;
    }

    public static void inputClose(View view, Context context) {
        if (view instanceof EditText) {
            view.clearFocus();
        }
        try {
            InputMethodManager im = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
