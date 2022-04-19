package com.zheng.liuju.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InstructionsUseActivity extends AppCompatActivity {

    @BindView(R.id.user)
    ImageView user;
    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.home)
    DragImageView home;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions_use);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {

        ImmersionBar.with(this).statusBarDarkFont(true).init();
        EventBus.getDefault().register(this);
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        user.setImageResource(R.mipmap.user);   //资源文件中的一张图片
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();    //获取屏幕的宽度
        user.measure(0, 0);  //这一步很重要
        int height = user.getMeasuredHeight() * width / user.getMeasuredWidth();  //这里就是将高度等比例缩放，其中imageView.getMeasuredHeight()和imageView.getMeasuredWidth()是计算图片本身的宽高
        user.setLayoutParams(new LinearLayout.LayoutParams(width, height));
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

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {
        if (message.getFinishs() != null) {
            finish();


        }else if (message.isHome()){

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
