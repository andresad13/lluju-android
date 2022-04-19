package com.zheng.liuju.fragment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gigamole.library.ShadowLayout;
import com.zheng.liuju.R;
import com.zheng.liuju.activity.AgreementActivity;
import com.zheng.liuju.activity.ForgetPassWordActivity;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.country.PickActivity;
import com.zheng.liuju.presenter.LoginPresenter;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.utils.Utils;
import com.zheng.liuju.view.ILoginView;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginFragment extends Fragment implements View.OnFocusChangeListener, ILoginView {
    @BindView(R.id.quhao)
    TextView quhao;
    @BindView(R.id.username)
    EditText username;

    @BindView(R.id.loginButton)
    ShadowLayout loginButton;
    @BindView(R.id.forget)
    TextView forget;
    @BindView(R.id.quhaos)
    TextView quhaos;
    @BindView(R.id.mingwens)
    RelativeLayout mingwens;
    @BindView(R.id.userAgreement)
    TextView userAgreement;
    @BindView(R.id.mingwen)
    ImageView mingwen;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.phoneview)
    View phoneview;
    @BindView(R.id.passwordview)
    View passwordview;
    @BindView(R.id.mailbox)
    RelativeLayout mailbox;
    private View views;
    private Unbinder bind;
    private LoginPresenter presenter;
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
    private PromptDialog promptDialog;
    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.fragment_login, container, false);
        bind = ButterKnife.bind(this, views);
        EventBus.getDefault().register(this);
        initView();
        return views;
    }

    private void initView() {
        presenter = new LoginPresenter();
        presenter.attachView(this);
        username.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);

    }

    private String getUserName() {
        return username.getText().toString();
    }

    private String getPassword() {
        return password.getText().toString();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.username:
                usernameHide(hasFocus);
                break;

            case R.id.password:
                usernameHides(hasFocus);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {
        if (message.getFinishs() != null) {




        }
    }




    private void usernameHides(boolean hasFocus) {
        if (hasFocus) {
            // 此处为得到焦点时的处理内容
            passwordview.setBackgroundColor(getResources().getColor(R.color.linecolor));
        } else {
            passwordview.setBackgroundColor(Color.parseColor("#D3DFEF"));
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

    @Override
    public void onDestroy() {
        bind.unbind();
        EventBus.getDefault().unregister(this);
        handler.removeMessages(1);
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (promptDialog != null) {
            promptDialog.dismiss();
        }
        presenter.onDestroy();
        presenter = null;
        System.gc();
        super.onDestroy();
    }


    @OnClick({R.id.quhao, R.id.loginButton, R.id.forget, R.id.quhaos, R.id.mingwens, R.id.userAgreement,R.id.mailbox})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.quhao:
                break;
            case R.id.loginButton:
                //jumpFinishs(MainActivity.class);
                presenter.requestLogin(getUserName(), getPassword(), getActivity(), getFomart());
                break;
            case R.id.forget:
                if (Utils.isNotFastClick()) {
                    jump(ForgetPassWordActivity.class);
                }

                break;

            case R.id.quhaos:
                if (Utils.isNotFastClick()) {
                    startActivityForResult(new Intent(getActivity(), PickActivity.class), CODE);
                }
                break;
            case R.id.mingwens:
                plaintexts();
                break;
            case R.id.userAgreement:
                if (Utils.isNotFastClick()) {
                    Intent intent = new Intent(getActivity(), AgreementActivity.class);
                    intent.putExtra("title", getResources().getString(R.string.userAgreements));
                    startActivity(intent);
                }
                break;

            case  R.id.mailbox:
            nextMailBox();
                break;
        }


    }

    private void nextMailBox() {
        //1   手机登录  2 邮箱登录  3 手机注册   4 邮箱注册
        EventMeager  eventMeager  =new EventMeager();
        eventMeager.setSwitchPages(2);
      EventBus.getDefault().post(eventMeager);
    }

    private String getFomart() {
        return quhaos.getText().toString().trim().substring(1);
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


    private void jumpFinishs(Class cla) {
        presenter.jumpFinish(getActivity(), cla);
    }

    private void jump(Class cla) {
        presenter.jump(getActivity(), cla);
    }


    @Override
    public void showDialog(String context, boolean type) {
        shows(context, type);
    }

    private void shows(String content, boolean type) {
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
    public void jumpFinish(Class clas) {
        jumpFinishs(clas);
    }

    @Override
    public void saveToken(String token,String openId) {
        SPUtils.putString(getActivity(), "token", token);
        SPUtils.putString(getActivity(), "openId", openId);
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


}
