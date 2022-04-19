package com.zheng.liuju.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gigamole.library.ShadowLayout;
import com.zheng.liuju.R;
import com.zheng.liuju.bean.Bean;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.presenter.EmailRegisteredPresenter;
import com.zheng.liuju.view.IEmailRegisteredView;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EmailRegisteredFragment extends Fragment implements View.OnFocusChangeListener, View.OnTouchListener, IEmailRegisteredView, TextWatcher {
    @BindView(R.id.point)
    TextView point;
    @BindView(R.id.sv)
    ScrollView sv;
    @BindView(R.id.phoneview)
    View phoneview;
    @BindView(R.id.codepoint)
    TextView codepoint;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.obtainCode)
    TextView obtainCode;
    @BindView(R.id.codeview)
    View codeview;
    @BindView(R.id.passwordpoint)
    TextView passwordpoint;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.mingwen)
    ImageView mingwen;
    @BindView(R.id.mingwens)
    RelativeLayout mingwens;
    @BindView(R.id.passwordview)
    View passwordview;
    @BindView(R.id.mailpoint)
    TextView mailpoint;
    @BindView(R.id.mailbox)
    EditText mailbox;
    @BindView(R.id.mailboxview)
    View mailboxview;
    @BindView(R.id.sharecode)
    EditText sharecode;
    @BindView(R.id.sharecodeview)
    View sharecodeview;
    @BindView(R.id.cv)
    ShadowLayout cv;

    @BindView(R.id.registeredTip)
    TextView registeredTip;
    @BindView(R.id.mailboxs)
    RelativeLayout mailboxs;
    private View views;

    @BindView(R.id.username)
    EditText username;
    private Unbinder bind;
    private PromptDialog promptDialog;
    private EmailRegisteredPresenter presenter;
    private boolean plaintext;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                if (promptDialog != null) {
                    promptDialog.dismiss();
                    promptDialog = null;
                }
            } else if (msg.what == 2) {
                EventMeager eventMeager = new EventMeager();
                eventMeager.setSwitchPages(2);//1   手机登录  2 邮箱登录  3 手机注册   4 邮箱注册
                EventBus.getDefault().post(eventMeager);
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
    private int CODE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.fragment_email_registered, container, false);
        bind = ButterKnife.bind(this, views);

        initView();
        return views;
    }

    private void initView() {
        EventBus.getDefault().register(this);
        username.setOnFocusChangeListener(this);
        sv.setOnTouchListener(this);
        code.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        mailbox.setOnFocusChangeListener(this);
        sharecode.setOnFocusChangeListener(this);
        presenter = new EmailRegisteredPresenter();
        presenter.attachView(this);
        String tips = getResources().getString(R.string.registeredTop) + "<font color='#F45344'>" + " * </font>"
                + getResources().getString(R.string.registeredTops);
        registeredTip.setText(Html.fromHtml(tips));
        username.addTextChangedListener(this);
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

    private String getUserName() {
        return username.getText().toString();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.username:
           //     usernameHide(hasFocus);
                break;
            case R.id.code:
                Log.e("code", "code");
                usernameHides(hasFocus, v.getId());
                break;
            case R.id.password:
                usernameHides(hasFocus, v.getId());
                break;
            case R.id.mailbox:
                usernameHides(hasFocus, v.getId());
                break;
            case R.id.sharecode:
                usernameHides(hasFocus, v.getId());
                break;

        }
    }

/*    private void usernameHide(boolean hasFocus) {
        if (hasFocus) {
            // 此处为得到焦点时的处理内容
            phoneview.setBackgroundColor(getResources().getColor(R.color.linecolor));
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
    }*/

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
                case R.id.mailbox:
                    mailboxview.setBackgroundColor(getResources().getColor(R.color.linecolor));
                    break;
                case R.id.sharecode:
                    sharecodeview.setBackgroundColor(getResources().getColor(R.color.linecolor));
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
                case R.id.mailbox:
                    mailboxview.setBackgroundColor(Color.parseColor("#D3DFEF"));
                    break;
                case R.id.sharecode:
                    sharecodeview.setBackgroundColor(Color.parseColor("#D3DFEF"));
                    break;

            }
        }
    }
/*
    private void remove() {
        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("rotation", 0f, 0f);
        PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("translationX", 0f, -95f);
        ObjectAnimator.ofPropertyValuesHolder(quhao, p1, p3).setDuration(500).start();

    }

    private void reduction() {
        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("rotation", 0f, 0f);
        PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("translationX", 0f, 95f);
        ObjectAnimator.ofPropertyValuesHolder(quhao, p1, p3).setDuration(10).start();
    }*/

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        inputClose(sv, getActivity());
        return getActivity().onTouchEvent(event);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        bind.unbind();
        presenter.onDestroy();
        handler.removeMessages(1);
        handler.removeMessages(2);
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (promptDialog != null) {
            promptDialog.dismiss();
        }

        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }

    /**
     * 接收事件,关闭页面
     *
     * @param message 事件类
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {
      /*  if (message.isSwitchPages()) {


        }*/
    }

    @OnClick({R.id.obtainCode, R.id.cv,  R.id.mingwens,R.id.mailboxs})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.obtainCode:
                presenter.isCode(getPhone(), getActivity());
                break;
            case R.id.cv:
                /*  */
              presenter.isRegistered(getPhone(), getCode(), getPassword(), getMailbox(), getSharecode(), getActivity());
                break;
       /*     case R.id.quhaos:
                startActivityForResult(new Intent(getActivity(), PickActivity.class), CODE);
                break;*/
            case R.id.mingwens:
                plaintexts();
                break;

            case  R.id.mailboxs:
                nextMailBox();
                break;
        }
    }
    private void nextMailBox() {
        //1   手机登录  2 邮箱登录  3 手机注册   4 邮箱注册
        EventMeager  eventMeager  =new EventMeager();
        eventMeager.setSwitchPages(3);
        EventBus.getDefault().post(eventMeager);
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

    private String getSharecode() {
        return sharecode.getText().toString().trim();
    }

    private String getMailbox() {
        return mailbox.getText().toString().trim();
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



    public void shows(String content, boolean type) {
        if (!isLiving(getActivity())) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(getActivity());
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
    public void onSuccess(Bean.DataBean list) {

    }

    @Override
    public void onFail(int errCode, String errMsg) {

    }

    @Override
    public void showDialog(String context, boolean type) {
        shows(context, type);
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getActivity());
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
    public void showTime() {
        countdown();
    }

    @Override
    public void reistered() {
        handler.sendEmptyMessageDelayed(2, 500);
    }




    public void countdown() {
        timer.start();
        obtainCode.setEnabled(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String eamil = s.toString().trim();
        if (eamil.contains("@")){
            mailbox.setText(eamil);
        }
    }
}
