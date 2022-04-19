package com.zheng.liuju.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zheng.liuju.R;
import com.zheng.liuju.activity.BillingDetailsActivity;
import com.zheng.liuju.activity.ConsumptionDetailsActivity;
import com.zheng.liuju.activity.LoginActivity;
import com.zheng.liuju.adapter.TransactionDetailsAdapter;
import com.zheng.liuju.bean.ChongzhiListBean;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.bean.ExpensesBean;
import com.zheng.liuju.bean.TransactionBean;
import com.zheng.liuju.bean.TransactionNum;
import com.zheng.liuju.presenter.ExpensesPresenter;
import com.zheng.liuju.presenter.RechargePresenter;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.view.Iexoenses;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;
import com.zheng.liuju.view.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ExpensesRecord  extends Fragment implements AdapterView.OnItemClickListener, Iexoenses, SwipeRefreshLayout.OnRefreshListener, PullToRefreshListView.OnRefreshListener {
    @BindView(R.id.rentlist)
    PullToRefreshListView rentlist;
    @BindView(R.id.srp)
    SwipeRefreshLayout srp;
    @BindView(R.id.calendar)
    ImageView calendar;
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.nodata)
    RelativeLayout nodata;
    private View views;
    private Unbinder bind;
    private PopupWindow popupWindow;
    private PromptDialog promptDialog;
    private LoadingDialog loadingDialog;
    private ExpensesPresenter presenter;
    private int type;
    private int page = 1;
    private String selectTime="";
    private ArrayList<TransactionBean> mlist = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                if (promptDialog != null) {
                    promptDialog.dismiss();
                    promptDialog = null;
                }
            } else if (msg.what == 2) {
                SPUtils.putString(getActivity(), "token", "");
                startActivity(new Intent(getActivity(), LoginActivity.class));

                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            } else if (msg.what == 3) {
                if (rentlist != null) {
                    rentlist.onRefreshComplete();
                }
            }


        }
    };
    private TransactionDetailsAdapter transactionDetailsAdapter;
    private String title;
    private String times="";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.fragment_expenses_record, container, false);
        bind = ButterKnife.bind(this, views);
        EventBus.getDefault().register(this);
        initView();
        return views;
    }
    private void initView() {
        presenter = new ExpensesPresenter();
        presenter.attachView(this);

        presenter.initOrder("", getToken(), type, page);

        rentlist.setOnItemClickListener(this);
        srp.setOnRefreshListener(this);
        rentlist.setOnRefreshListener(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {
        if (message.getFinishs() != null) {




        }
    }

    @OnClick(R.id.calendar)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.calendar:
                showNoneEffect();
                break;
        }
    }

    private String getToken() {
        return SPUtils.getString(getActivity(), "token", "");
    }

    private void showNoneEffect() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vPopupWindow = inflater.inflate(R.layout.date_picker_dialog, null, false);//引入弹窗布局
        DatePicker datePickerStart = vPopupWindow.findViewById(R.id.datePickerStart);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) ;
        int date = calendar.get(Calendar.DAY_OF_MONTH);//日
        datePickerStart.init(year, month, date, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.e("我要的日期",year+"=="+monthOfYear+"");
                String month ="";
                if (monthOfYear+1<10){
                    int m= monthOfYear+1;
                    month="0"+m;
                }else {
                    int m= monthOfYear+1;
                    month=m+"";
                }
                selectTime = year+"-"+month;
            }
        });
        ImageView del = (ImageView)vPopupWindow.findViewById(R.id.del);
        TextView choose = (TextView) vPopupWindow.findViewById(R.id.choose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) ;

                if (selectTime.equals("")){
                    String months ="";
                    if (month+1<10){
                        int m= month+1;
                        months="0"+m;
                    }else {
                        int m= month+1;
                        months=m+"";
                    }
                    selectTime=  year+"-"+months;
                    times = selectTime;
                }else {
                    times = selectTime;
                }
                Log.e("hhh",times+"");
                type=0;
                page=1;
                presenter.initOrder(times,getToken(),type,page);


            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
        hideDay(datePickerStart);
        popupWindow = new PopupWindow(vPopupWindow, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);

        //设置背景透明
        addBackground();
        //设置进出动画
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        //引入依附的布局  activity_main   activity_login
        View parentView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_transaction_details, null);

        //相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
        popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 20, 20);


    }

    private void addBackground() {
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;//调节透明度
        getActivity().getWindow().setAttributes(lp);
        //dismiss时恢复原样
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });

    }

    private void hideDay(DatePicker mDatePicker) {
        try {
            /* 处理android5.0以上的特殊情况 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0) {
                    View daySpinner = mDatePicker.findViewById(daySpinnerId);
                    if (daySpinner != null) {
                        daySpinner.setVisibility(View.GONE);
                    }
                }
            } else {
                Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mDaySpinner".equals(datePickerField.getName()) || ("mDayPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(mDatePicker);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RelativeLayout viewById = view.findViewById(R.id.title);
        TransactionNum transactionNum =    (TransactionNum) viewById.getTag();
        Log.e("hhh",transactionNum.nums+"");

        Intent intent = new Intent(getActivity(), BillingDetailsActivity.class);
        intent.putExtra("id",transactionNum.nums+"");
        intent.putExtra("type","");
        startActivity(intent);
    }

    @Override
    public void showDialog(String context, boolean type) {
        shows(context, type);
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
    public void upData(List<ExpensesBean.DataBean> context, int type) {
       /* for (int i = 0; i < 5; i++) {
            TransactionBean transactionBean = new TransactionBean();
            if (i == 0) {
                transactionBean.type = true;
            } else if (i == 3) {
                transactionBean.type = true;
            } else {
                transactionBean.type = false;
            }
            mlist.add(transactionBean);
        }
        TransactionDetailsAdapter transactionDetailsAdapter = new TransactionDetailsAdapter(getActivity(), mlist);
        rentlist.setAdapter(transactionDetailsAdapter);*/
        if (type==0){
            initData(context);
        }else  if (type==1){
            initData(context);
        }else if (type==2){
            more(context);
        }
    }

    private void more(List<ExpensesBean.DataBean> transactionBeans) {


        handler.sendEmptyMessageDelayed(3,500);

        if (transactionBeans.size()!=0){

            for (int i = 0; i < transactionBeans.size(); i++) {


                if (title.equals("")){
                    TransactionBean transactionBean = new TransactionBean();
                    transactionBean.type = true;
                    transactionBean.dataTitle= transactionBeans.get(i).getAdd_time().split(" ")[0];
                    title=transactionBeans.get(i).getAdd_time().split(" ")[0];
                    mlist.add(transactionBean);
                }else {
                    if (!transactionBeans.get(i).getAdd_time().split(" ")[0].equals(title)){
                        TransactionBean transactionBean = new TransactionBean();
                        transactionBean.type = true;
                        transactionBean.dataTitle= transactionBeans.get(i).getAdd_time().split(" ")[0];
                        title=transactionBeans.get(i).getAdd_time().split(" ")[0];
                        mlist.add(transactionBean);
                    }
                }

                TransactionBean transactionBean = new TransactionBean();
                transactionBean.type = false;
                transactionBean.num = transactionBeans.get(i).getId();
                transactionBean.time= transactionBeans.get(i).getAdd_time();
                transactionBean.money= "+"+getResources().getString(R.string.moenys)+transactionBeans.get(i).getAccount_my();
                title=transactionBeans.get(i).getAdd_time().split(" ")[0];
                mlist.add(transactionBean);

            }
            transactionDetailsAdapter.notifyDataSetChanged();
            page=page+1;
        }else {
            shows(getResources().getString(R.string.noData),true);
        }
    }
    private void initData(List<ExpensesBean.DataBean> transactionBeans) {
        mlist.clear();
        if (transactionDetailsAdapter != null) {
            transactionDetailsAdapter.notifyDataSetChanged();
        }
        if (type == 1) {
            shows(getResources().getString(R.string.RefreshSuccessfully), true);
            srp.setRefreshing(false);
        }

        title = "";
        if (transactionBeans.size() != 0) {
            nodata.setVisibility(View.GONE);
            rentlist.setVisibility(View.VISIBLE);
            for (int i = 0; i < transactionBeans.size(); i++) {


                if (title.equals("")) {
                    TransactionBean transactionBean = new TransactionBean();
                    transactionBean.type = true;
                    transactionBean.dataTitle = transactionBeans.get(i).getAdd_time().split(" ")[0];
                    title = transactionBeans.get(i).getAdd_time().split(" ")[0];
                    mlist.add(transactionBean);
                } else {
                    if (!transactionBeans.get(i).getAdd_time().split(" ")[0].equals(title)) {
                        TransactionBean transactionBean = new TransactionBean();
                        transactionBean.type = true;
                        transactionBean.dataTitle = transactionBeans.get(i).getAdd_time().split(" ")[0];
                        title = transactionBeans.get(i).getAdd_time().split(" ")[0];
                        mlist.add(transactionBean);
                    }
                }

                TransactionBean transactionBean = new TransactionBean();
                if (transactionBeans.get(i).getSourceType()==1){
                    transactionBean.sourceType =getResources().getString(R.string.OrderConsumption);
                    transactionBean.money = getResources().getString(R.string.moenys) + transactionBeans.get(i).getAccount_my();
                }else   if (transactionBeans.get(i).getSourceType()==2) {
                    transactionBean.sourceType =getResources().getString(R.string.BackgroundChange);
                    if (transactionBeans.get(i).getAccount_my()<0){
                        transactionBean.money = getResources().getString(R.string.moenys) + transactionBeans.get(i).getAccount_my();
                    }else {
                        transactionBean.money = getResources().getString(R.string.moenys) + transactionBeans.get(i).getAccount_yajin();
                    }

                }else if (transactionBeans.get(i).getSourceType()==3){
                    transactionBean.sourceType =getResources().getString(R.string.WithdrawalBalance);
                    transactionBean.money = getResources().getString(R.string.moenys) + transactionBeans.get(i).getAccount_my();
                }else if (transactionBeans.get(i).getSourceType()==4){
                    transactionBean.sourceType =getResources().getString(R.string.WithdrawalDeposit);
                    transactionBean.money =  getResources().getString(R.string.moenys) + transactionBeans.get(i).getAccount_yajin();
                }else if (transactionBeans.get(i).getSourceType()==5){
                    transactionBean.sourceType =getResources().getString(R.string.RechargeBalance);
                    transactionBean.money = getResources().getString(R.string.moenys) + transactionBeans.get(i).getAccount_my();
                }else if (transactionBeans.get(i).getSourceType()==6){
                    transactionBean.sourceType =getResources().getString(R.string.RechargeDeposit);
                    transactionBean.money = getResources().getString(R.string.moenys) + transactionBeans.get(i).getAccount_yajin();
                }else if (transactionBeans.get(i).getSourceType() == 7) {
                    transactionBean.sourceType =getResources().getString(R.string.DeductionBalance);
                    transactionBean.money =getResources().getString(R.string.moenys) + transactionBeans.get(i).getAccount_yajin();
                }else if (transactionBeans.get(i).getSourceType() == 8) {
                    transactionBean.sourceType=getResources().getString(R.string.LostPurchase);
                    transactionBean.money =getResources().getString(R.string.moenys) + transactionBeans.get(i).getAccount_yajin();
                }else if (transactionBeans.get(i).getSourceType() == 9) {
                    transactionBean.sourceType=getResources().getString(R.string.WithdrawalDeposit);
                    transactionBean.money =getResources().getString(R.string.moenys) + transactionBeans.get(i).getAccount_yajin();
                }
                else {
                    transactionBean.sourceType =getResources().getString(R.string.RechargeDeposit);
                    transactionBean.money =  getResources().getString(R.string.moenys) + transactionBeans.get(i).getAccount_yajin();
                }
                transactionBean.type = false;
                transactionBean.num = transactionBeans.get(i).getId();
                transactionBean.time = transactionBeans.get(i).getAdd_time();

                title = transactionBeans.get(i).getAdd_time().split(" ")[0];
                mlist.add(transactionBean);

            }
            transactionDetailsAdapter = new TransactionDetailsAdapter(getActivity(), mlist,1);
            rentlist.setAdapter(transactionDetailsAdapter);
        } else {
            nodata.setVisibility(View.VISIBLE);
            rentlist.setVisibility(View.GONE);
        }
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
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (promptDialog != null) {
            promptDialog.dismiss();
        }
        presenter.onDestroy();
        presenter = null;
        System.gc();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onErr() {
        handler.sendEmptyMessageDelayed(2, 3000);
    }

    @Override
    public void onRefresh() {
        type = 1;
        page = 1;
        presenter.initOrder(times,getToken(),type,page);
    }

    @Override
    public void onDownPullRefresh() {

    }

    @Override
    public void onLoadingMore() {
        int pages = page + 1;
        type = 2;
        presenter.initOrder(times,getToken(),type,page);
    }

}
