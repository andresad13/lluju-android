package com.zheng.liuju.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.fragment.ExpensesRecord;
import com.zheng.liuju.fragment.RechargeFragment;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransactionDetailsActivity extends AppCompatActivity {
    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.recharge)
    TextView recharge;
    @BindView(R.id.expenses)
    TextView expenses;
    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.home)
    DragImageView home;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
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
    /**
     * 设置默认的fragment
     */
    private int currentTab;
    //当前activity的fragment控件
    public int fragmentContentId = R.id.fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        ButterKnife.bind(this);
        initView();
        defaultFragment();
        initFrag();
    }

    private void initFrag() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(fragmentContentId, fragments.get(PAGE_COMMON));
        currentTab = PAGE_COMMON;
        ft.commit();
    }

    private void defaultFragment() {
        fragments.put(PAGE_COMMON, new RechargeFragment());
        fragments.put(PAGE_TRANSLUCENT, new ExpensesRecord());
    }

    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        EventBus.getDefault().register(this);
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        title.setOnHeaderClickListener(v -> finish());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @OnClick({R.id.recharge, R.id.expenses,R.id.home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recharge:
                rechargeClick();
                break;
            case R.id.expenses:
                expensesClick();
                break;
            case R.id.home:
                EventMeager eventMeager = new EventMeager();
                eventMeager.setHome(true);
                EventBus.getDefault().post(eventMeager);
                break;
        }
    }

    private void expensesClick() {
        expenses.setBackgroundResource(R.drawable.red_recharge_bg);
        expenses.setTextColor(Color.parseColor("#FFFFFF"));
        recharge.setTextColor(Color.parseColor("#868686"));
        recharge.setBackgroundResource(R.drawable.white_recharge_bg);
        changeTab(PAGE_TRANSLUCENT);
    }

    private void rechargeClick() {

        recharge.setBackgroundResource(R.drawable.red_recharge_bg);
        recharge.setTextColor(Color.parseColor("#FFFFFF"));
        expenses.setTextColor(Color.parseColor("#868686"));
        expenses.setBackgroundResource(R.drawable.white_recharge_bg);

        changeTab(PAGE_COMMON);
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

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
}
