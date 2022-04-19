package com.zheng.liuju.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.CardList;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.bean.Infos;
import com.zheng.liuju.bean.RulesBean;
import com.zheng.liuju.bean.ShopBean;
import com.zheng.liuju.fragment.EmailLoginFragement;
import com.zheng.liuju.fragment.EmailRegisteredFragment;
import com.zheng.liuju.fragment.LoginFragment;
import com.zheng.liuju.fragment.RegisteredFragment;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.presenter.LoginPresenter;
import com.zheng.liuju.view.IMainView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements IMainView {
    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.login)
    LinearLayout login;
    @BindView(R.id.registered)
    LinearLayout registered;
    @BindView(R.id.LoginTv)
    TextView LoginTv;
    @BindView(R.id.LoginView)
    View LoginView;
    @BindView(R.id.registeredTv)
    TextView registeredTv;
    @BindView(R.id.registeredView)
    View registeredView;

    /**
     * 管理fragment
     */
    private HashMap<Integer, Fragment> fragments = new HashMap<>();
    /**
     * 第一个fragment
     */
    public static final int PAGE_COMMON = 0;
    /**
     * 第二个fragment
     */
    public static final int PAGE_TRANSLUCENT = 1;


    public static final int PAGE_COMMON_THREE = 2;
    public static final int PAGE_COMMON_FOUR = 3;
    /**
     * 设置默认的fragment
     */
    private int currentTab;
    //当前activity的fragment控件
    public int fragmentContentId = R.id.fl;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;  //LanguageType.LANGUAGE_EN
    private LoginPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        defaultFragment();
        initFrag();
    //    ArrayList<String> ff=null;
     //   Toast.makeText(this, ff.get(8), Toast.LENGTH_SHORT).show();
      //  LoginTv.setText(ff);
    }

    private void initFrag() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(fragmentContentId, fragments.get(PAGE_COMMON));
        currentTab = PAGE_COMMON;
        ft.commit();
    }

    private void defaultFragment() {
        fragments.put(PAGE_COMMON, new LoginFragment());
        fragments.put(PAGE_TRANSLUCENT, new RegisteredFragment());
        fragments.put(PAGE_COMMON_THREE, new EmailLoginFragement());
        fragments.put(PAGE_COMMON_FOUR, new EmailRegisteredFragment());
    }

    private void initView() {
        //沉浸式
        ImmersionBar.with(this).init();
        //语言设置
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
     /*   presenter = new LoginPresenter();
        presenter.attachView(this);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
     /*   presenter.onDestroy();
        presenter = null;
        System.gc();*/
    }


    /**
     *
     * 接收事件,关闭页面
     * @param message 事件类
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {
        if (message.isSwitchPages()==1){   //1   手机登录  2 邮箱登录  3 手机注册   4 邮箱注册
            changeLogin();
        }else if (message.isSwitchPages()==2){
            chageMail();
        }else  if (message.isSwitchPages()==3){
            changeRegistered();
        }else  if (message.isSwitchPages()==4){
            changeEamilRegistered();
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }





    @Override
    public void upInformation(Infos.DataBean list) {

    }

    @Override
    public void cardList(List<CardList.DataBean> data) {

    }

    @Override
    public void showDialog(String context, boolean type) {

    }

    @Override
    public void upMap(List<ShopBean.DataBean> context) {

    }




    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void permissionSuccess() {

    }

    @Override
    public void cameraPermissionSuccess() {

    }

    @Override
    public void scan(RulesBean bean) {

    }

    @Override
    public void onErr() {

    }


    private void changeTab(int page) {
        //默认的currentTab == 当前的页码，不做任何处理
        if (currentTab == page) {
            return;
        }

        //获取fragment的页码
        Fragment fragment = fragments.get(page);
        //fragment事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //如果该Fragment对象被添加到了它的Activity中，那么它返回true，否则返回false。
        //当前activity中添加的不是这个fragment
        if (!fragment.isAdded()) {
            //所以将他加进去
            ft.add(fragmentContentId, fragment);
        }
        //隐藏当前currentTab的
        ft.hide(fragments.get(currentTab));
        //显示现在page的
        ft.show(fragments.get(page));

        //当前显示的赋值给currentTab
        currentTab = page;

        //activity被销毁？  ！否
        if (!this.isFinishing()) {
            //允许状态丢失
            ft.commitAllowingStateLoss();
        }
    }

    @OnClick({R.id.login, R.id.registered})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:


                changeLogin();
                break;
            case R.id.registered:
                changeRegistered();
                break;
        }
    }

    private   void  changeLogin(){
        LoginTv.setTextColor(getResources().getColor(R.color.white));
        registeredTv.setTextColor(getResources().getColor(R.color.rloginbak));
        registeredView.setVisibility(View.INVISIBLE);
        LoginView.setVisibility(View.VISIBLE);
        changeTab(PAGE_COMMON);
    }

    private   void  chageMail(){
        LoginTv.setTextColor(getResources().getColor(R.color.white));
        registeredTv.setTextColor(getResources().getColor(R.color.rloginbak));
        registeredView.setVisibility(View.INVISIBLE);
        LoginView.setVisibility(View.VISIBLE);
        changeTab(PAGE_COMMON_THREE);
    } private   void  changeEamilRegistered(){
        LoginTv.setTextColor(getResources().getColor(R.color.rloginbak));
        registeredTv.setTextColor(getResources().getColor(R.color.white));
        registeredView.setVisibility(View.VISIBLE);
        LoginView.setVisibility(View.INVISIBLE);
        changeTab(PAGE_COMMON_FOUR);
    }

    private   void  changeRegistered(){
        LoginTv.setTextColor(getResources().getColor(R.color.rloginbak));
        registeredTv.setTextColor(getResources().getColor(R.color.white));
        registeredView.setVisibility(View.VISIBLE);
        LoginView.setVisibility(View.INVISIBLE);
        changeTab(PAGE_TRANSLUCENT);
    }
    public   void    jump(Class   aims){
        presenter.jumpFinish(LoginActivity.this,aims);
    }

}
