package com.zheng.liuju.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gigamole.library.ShadowLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.country.PickActivity;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.presenter.ForgetPassWordPresenter;
import com.zheng.liuju.view.IForPassWordView;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;
import com.zheng.liuju.view.TitleView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetPassWordActivity extends BaseActivity implements View.OnFocusChangeListener,
        View.OnClickListener, IForPassWordView {
    @BindView(R.id.point)
    TextView point;
    @BindView(R.id.quhao)
    TextView quhao;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.codepoint)
    TextView codepoint;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.obtainCode)
    TextView obtainCode;
    @BindView(R.id.passwordpoint)
    TextView passwordpoint;
    @BindView(R.id.cv)
    ShadowLayout cv;
    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.quhaos)
    TextView quhaos;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.phoneview)
    View phoneview;
    @BindView(R.id.codeview)
    View codeview;
    @BindView(R.id.passwordview)
    View passwordview;
    @BindView(R.id.mingwen)
    ImageView mingwen;
    @BindView(R.id.mingwens)
    RelativeLayout mingwens;
    @BindView(R.id.phones)
    RelativeLayout phones;
    @BindView(R.id.phone)
    LinearLayout phone;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.mailboxs)
    TextInputLayout mailboxs;
    @BindView(R.id.mailbox)
    RelativeLayout mailbox;
    @BindView(R.id.phoneviews)
    View phoneviews;
    @BindView(R.id.eamil)
    LinearLayout eamil;
    private  int  type=1;  //  1 手机号   2 邮箱
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    private PromptDialog promptDialog;
    private int CODE = 1;
    private boolean plaintext;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (promptDialog != null) {
                promptDialog.dismiss();
                promptDialog = null;
            }


        }
    };
    CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Subscribe(threadMode = ThreadMode.MAIN)
        @Override
        public void onTick(long millisUntilFinished) {
            obtainCode.setText(millisUntilFinished / 1000 + "S ");
        }

        @Override
        public void onFinish() {
            obtainCode.setEnabled(true);
            obtainCode.setText(getResources().getString(R.string.resend) + " ");
        }
    };
    private LoadingDialog loadingDialog;
    private ForgetPassWordPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_word);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onDestroy() {

        presenter.onDestroy();
        handler.removeMessages(1);
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (promptDialog != null) {
            promptDialog.dismiss();
        }
        handler.removeMessages(1);
        super.onDestroy();
    }

    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        //语言设置
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        username.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);
        titleView.setOnHeaderClickListener(this);
        code.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        quhaos.setOnClickListener(this);
        obtainCode.setOnClickListener(this);
        presenter = new ForgetPassWordPresenter();
        presenter.attachView(this);
        mingwens.setOnClickListener(this);
        cv.setOnClickListener(this);
        phones.setOnClickListener(this);
        mailbox.setOnClickListener(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.username:
                usernameHide(hasFocus);
                break;
            case R.id.code:
                Log.e("code", "code");
                usernameHides(hasFocus, v.getId());
                break;
            case R.id.password:
                usernameHides(hasFocus, v.getId());
                break;
            case R.id.email:
                usernameHides(hasFocus, v.getId());
                break;

        }
    }

    private void usernameHides(boolean hasFocus, int id) {
        if (hasFocus) {
            // 此处为得到焦点时的处理内容
            switch (id) {
                case R.id.code:
                    Log.e("code", "code");
                    codeview.setBackgroundColor(getResources().getColor(R.color.linecolor));
                    break;
                case R.id.password:
                    passwordview.setBackgroundColor(getResources().getColor(R.color.linecolor));
                    break;
                case R.id.email:
                    phoneviews.setBackgroundColor(getResources().getColor(R.color.linecolor));
                    break;
            }


        } else {
            switch (id) {
                case R.id.code:
                    codeview.setBackgroundColor(Color.parseColor("#D3DFEF"));
                    break;
                case R.id.password:
                    passwordview.setBackgroundColor(Color.parseColor("#D3DFEF"));
                    break;
                case R.id.email:
                    phoneviews.setBackgroundColor(Color.parseColor("#D3DFEF"));
                    break;

            }
        }
    }

    private void usernameHide(boolean hasFocus) {
        if (hasFocus) {
            phoneview.setBackgroundColor(getResources().getColor(R.color.linecolor));
            // 此处为得到焦点时的处理内容
            int visibility = quhao.getVisibility();
            Log.e("visibility", visibility + "");
            if (visibility != 0) {
                quhao.setVisibility(View.VISIBLE);
                point.setVisibility(View.VISIBLE);
                username.setHint("");
                remove();
            }
        } else {
            phoneview.setBackgroundColor(Color.parseColor("#D3DFEF"));
            // 此处为失去焦点时的处理内容
            if (getUserName().equals("")) {
                username.setHint(getResources().getString(R.string.phoneNumber));
                reduction();
                quhao.setVisibility(View.GONE);
                point.setVisibility(View.GONE);
            }
        }
    }

    private void remove() {
        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("rotation", 0f, 0f);
        PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("translationX", 0f, -95f);
        ObjectAnimator.ofPropertyValuesHolder(quhao, p1, p3).setDuration(500).start();

    }

    private void reduction() {
        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("rotation", 0f, 0f);
        PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("translationX", 0f, 95f);
        ObjectAnimator.ofPropertyValuesHolder(quhao, p1, p3).setDuration(10).start();
    }

    private String getUserName() {
        return username.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backs:
                finish();
                break;
            case R.id.quhaos:
                startActivityForResult(new Intent(ForgetPassWordActivity.this, PickActivity.class), CODE);
                break;
            case R.id.obtainCode:
                if (type==1){
                    presenter.isCode(getPhone(), ForgetPassWordActivity.this, getFomart());
                }else {
                    presenter.isEmailCode(getEmail(),ForgetPassWordActivity.this);
                }

                break;
            case R.id.mingwens:
                plaintexts();
                break;
            case R.id.cv:
                if (type==1){
                    presenter.isForgetPassWord(getPhone(), getCode(), getPassword(), ForgetPassWordActivity.this, getFomart());
                }else {
                    presenter.isEmailForgetPassWord(getEmail(), getCode(), getPassword(), ForgetPassWordActivity.this);
                }
                break;
            case  R.id.phones:
                hintPhone();
                break;
            case  R.id.mailbox:
                hintMailbox();
                break;
        }
    }

    private void hintMailbox() {
        type=1;
        eamil.setVisibility(View.GONE);
        phone.setVisibility(View.VISIBLE);
    }

    private void hintPhone() {
        type=2;
        phone.setVisibility(View.GONE);
        eamil.setVisibility(View.VISIBLE);
    }


    private void plaintexts() {
        if (plaintext) {
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mingwen.setImageResource(R.mipmap.mingwen);
            plaintext = false;
        } else {
            password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mingwen.setImageResource(R.mipmap.anwen);
            plaintext = true;
        }
    }

    private String getCode() {
        return code.getText().toString().trim();
    }

    private String getPassword() {
        return password.getText().toString().trim();
    }

    private String getPhone() {
        return username.getText().toString().trim();
    }

    private String getEmail() {
        return email.getText().toString().trim();
    }
    private String getFomart() {
        return quhaos.getText().toString().trim().substring(1);
    }

    @Override
    public void showDialog(String context, boolean type) {
        shows(context, type);
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

    public void shows(String content, boolean type) {
        if (!isLiving(ForgetPassWordActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(ForgetPassWordActivity.this);
        }
        promptDialog.setTvTip(content + "", type);
        promptDialog.show();
        handler.sendEmptyMessageDelayed(1, 2000);
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(ForgetPassWordActivity.this);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "onActivityResult");
        if (resultCode == CODE) {
            String three = data.getStringExtra("fomart");

            //设置结果显示框的显示数值
            if (!three.equals("")) {
                quhaos.setText("+" + String.valueOf(three));

            }


        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showTime() {
        countdown();
    }

    public void countdown() {
        timer.start();
        obtainCode.setEnabled(false);
    }
}
