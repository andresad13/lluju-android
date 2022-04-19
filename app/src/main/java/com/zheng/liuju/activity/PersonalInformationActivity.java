package com.zheng.liuju.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.bean.Infos;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.presenter.PersonalPresenter;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.view.DialogUtils;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.IPersonal;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;
import com.zheng.liuju.view.TitleView;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalInformationActivity extends TakePhotoActivity implements DialogUtils.OnButtonClickListener, IPersonal
        , TakePhoto.TakeResultListener, InvokeListener {
    @BindView(R.id.sdview)
    SimpleDraweeView sdview;
    @BindView(R.id.nickname)
    RelativeLayout nickname;
    @BindView(R.id.email)
    RelativeLayout email;
    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.tel)
    TextView tel;
    @BindView(R.id.emails)
    TextView emails;
    @BindView(R.id.home)
    DragImageView home;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    private PersonalPresenter presenter;
    private String filePath = "";
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
                SPUtils.putString(PersonalInformationActivity.this, "token", "");
                startActivity(new Intent(PersonalInformationActivity.this, LoginActivity.class));

                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            }
        }
    };
    private LoadingDialog loadingDialog;
    private String paths = "";
    private String urls;
    private AlertDialog dialogs;
    private String username;
    private String emailss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            finish();


        } else if (message.isHome()){
            finish();
        }
    }

    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        presenter = new PersonalPresenter();
        presenter.attachView(this);
        title.setOnHeaderClickListener(v -> finish());
        presenter.info("0", "0", getToken());
    }

    /**
     * 获取token
     *
     * @return token值
     */
    public String getToken() {
        String token = SPUtils.getString(PersonalInformationActivity.this, "token", "");
        return token;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @OnClick({R.id.sdview, R.id.nickname, R.id.email,R.id.home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sdview:
                showAvatars();
                break;
            case R.id.nickname:
                showNickname();
                break;
            case R.id.email:
                showEmails();
                break;
            case R.id.home:
                EventMeager eventMeager = new EventMeager();
                eventMeager.setHome(true);
                EventBus.getDefault().post(eventMeager);
                break;
        }
    }

    private String getUserName() {
        return userName.getText().toString().trim();
    }

    private String getTel() {
        return tel.getText().toString().trim();
    }

    private String getEmail() {
        return emails.getText().toString().trim();
    }

    private void showEmails() {
        DialogUtils dialogUtils = new DialogUtils();
        dialogUtils.setOnButtonClickListener(this);
        dialogUtils.showEmail(PersonalInformationActivity.this, getEmail());
    }

    private void showNickname() {
        DialogUtils dialogUtils = new DialogUtils();
        dialogUtils.setOnButtonClickListener(this);
        dialogUtils.showNickName(PersonalInformationActivity.this, getUserName());
    }

    private void showAvatars() {
        DialogUtils dialogUtils = new DialogUtils();
        dialogUtils.setOnButtonClickListener(this);
        dialogUtils.showAvatar(PersonalInformationActivity.this);
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
        if (dialogs != null) {
            dialogs.dismiss();
        }
        System.gc();
    }


    @Override
    public void permissionSuccess(int type) {
        switch (type) {
            case 0:     //拍照
                onClicks(getTakePhoto());
                break;

            case 1:    //从相册选择
                getPic();
                break;
        }
    }

    @Override
    public void showDialog(String context, boolean type) {
        shows(context, type);
    }

    public void shows(String content, boolean type) {
        if (!isLiving(PersonalInformationActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(PersonalInformationActivity.this);
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
            loadingDialog = new LoadingDialog(PersonalInformationActivity.this);
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
    public void upData() {
        if (dialogs != null) {
            dialogs.dismiss();
        }
        if (urls != null) {
            sdview.setImageURI(urls);
        }

        if (emailss != null) {
            emails.setText(emailss);
        }


        if (username != null) {

            userName.setText(username);

        }

        EventMeager eventMeager = new EventMeager();
        eventMeager.setUpUser(true);
        EventBus.getDefault().post(eventMeager);
    }

    @Override
    public void onErr() {
        handler.sendEmptyMessageDelayed(2, 3000);
    }


    @Override
    public void upInformation(Infos.DataBean list) {
        if (list != null) {
            if (list.getAvatarUrl() != null) {
                if (!list.getAvatarUrl().equals("")) {
                    sdview.setImageURI(list.getAvatarUrl());
                }
            }

            if (list.getWxname() != null) {
                if (!list.getWxname().equals("")) {
                    //    userName.setText(list.getWxname());
                    if (list.getWxname().length() > 10) {
                        userName.setText(list.getWxname().substring(0, 10) + "");
                    } else {
                        userName.setText(list.getWxname());
                    }
                    username = list.getWxname();
                }
            }

            if (list.getTel() != null) {
                if (!list.getTel().equals("")) {
                    tel.setText(list.getTel());
                }
            }

            if (list.getEmail() != null) {
                if (!list.getEmail().equals("")) {
                    emails.setText(list.getEmail());
                    emailss = list.getEmail();
                }
            }
        }
    }

    @Override
    public void upAvatar(String url, String path) {
        urls = url;
        paths = path;
        presenter.updata(getEmail(), path, getUserName(), PersonalInformationActivity.this, getToken());

    }

    private void getPic() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

        //  takePhoto.onPickMultiple(1);
        getTakePhoto().onPickMultipleWithCrop(1, getCropOptions(), selectedLanguage);
    }

    private void onClicks(TakePhoto takePhoto) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

        takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
    }

    private CropOptions getCropOptions() {

        int height = Integer.parseInt(800 + "");
        int width = Integer.parseInt(800 + "");
        boolean withWonCrop = true;
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setOutputX(width).setOutputY(height);
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        //  showImg(result.getImages());
        Log.e("我要的数据", result.getImages().get(0).getOriginalPath());

        filePath = result.getImages().get(0).getOriginalPath();
        File file = new File(filePath);

        Uri imageUri = Uri.fromFile(file);
        sdview.setImageURI(imageUri);
        presenter.upLoadImg(getToken(), file);

    }

    @Override
    public void onPositiveButtonClick(int type, String context, AlertDialog dialog) {
        switch (type) {
            case 0:     //拍照
                presenter.requestPermission(PersonalInformationActivity.this, selectedLanguage, type);
                break;
            case 1:   //从相册选择
                presenter.requestPermission(PersonalInformationActivity.this, selectedLanguage, type);
                break;
            case 2:   //修改昵称
                dialogs = dialog;
                if (TextUtils.isEmpty(context)) {
                    shows(getResources().getString(R.string.nicknameTip), false);
                    return;
                }
                username = context;
                presenter.updata(getEmail(), paths, context, PersonalInformationActivity.this, getToken());
                break;
            case 3:   //修改电子邮箱
                dialogs = dialog;
                if (TextUtils.isEmpty(context)) {
                    shows(getResources().getString(R.string.Eamils), false);
                    return;
                }
                emailss = context;
                presenter.updata(context, paths, getUserName(), PersonalInformationActivity.this, getToken());
                break;
        }
    }


}
