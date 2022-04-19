package com.zheng.liuju.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gyf.immersionbar.ImmersionBar;
import com.mercadopago.adapters.ErrorHandlingCallAdapter;
import com.mercadopago.core.MercadoPago;
import com.mercadopago.model.ApiException;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.SavedCardToken;
import com.mercadopago.model.Token;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;
import com.zheng.liuju.R;
import com.zheng.liuju.adapter.RechargeAdapter;
import com.zheng.liuju.adapter.RechargeCardListAdapter;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.AmountList;
import com.zheng.liuju.bean.CardList;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.bean.PayNumBean;
import com.zheng.liuju.bean.RechargeCardListBean;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.mercadopago.utils.ExamplesUtils;
import com.zheng.liuju.presenter.RechaegePresenter;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.view.DialogUtils;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.IRechaege;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;
import com.zheng.liuju.view.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class RechargeActivity extends BaseActivity implements AdapterView.OnItemClickListener, IRechaege, DialogUtils.OnButtonClickListener {
    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.banner)
    ImageView banner;
    @BindView(R.id.gv)
    GridView gv;
    @BindView(R.id.recharge)
    TextView recharge;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.cardList)
    ListView cardLists;
    @BindView(R.id.navigation)
    LinearLayout navigation;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.payTip)
    TextView payTip;
    @BindView(R.id.rechargeTip)
    TextView rechargeTip;
    @BindView(R.id.home)
    DragImageView home;
    /* @BindView(R.id.other)
     LinearLayout other;*/
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;

    private ArrayList<PayNumBean> arrayList;
    private RechargeAdapter rechargeAdapter;
    private RechaegePresenter presenter;
    private PromptDialog promptDialog;
    private String cardId = "";
    private String moneyId = "";
    private String orderId;
    private boolean lease;
    private String deviceCode;

    private static final String REQUESTED_CODE_MESSAGE = "Requested code: ";
    private static final String PAYMENT_WITH_STATUS_MESSAGE = "Payment with status: ";
    private static final String RESULT_CODE_MESSAGE = " Result code: ";
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
                SPUtils.putString(RechargeActivity.this, "token", "");
                startActivity(new Intent(RechargeActivity.this, LoginActivity.class));

                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            } else if (msg.what == 3) {
                loop = loop + 1;
                presenter.getResult(getToken(RechargeActivity.this), orderId, paymentId);
            } else if (msg.what == 4) {
                loop = loop + 1;
                presenter.getResults(getToken(RechargeActivity.this), orderId);
            }


        }
    };
    private LoadingDialog loadingDialog;
    private RechargeCardListAdapter cardListAdapter;
    private ArrayList<RechargeCardListBean> cardListBeans;
    private DialogUtils dialogUtils;
    private AlertDialog dialogs;
    private int loop;
    private LinearLayout other;
    private String balance;
    private String paymentId;
    private String securityCode;
    protected MercadoPago mMercadoPago;
    protected List<String> mSupportedPaymentTypes = new ArrayList<String>() {{
        add("credit_card");
        add("debit_card");
        add("prepaid_card");
    }};
    private boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        EventBus.getDefault().register(this);
        banner.setImageResource(R.mipmap.banner);   //资源文件中的一张图片
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();    //获取屏幕的宽度
        banner.measure(0, 0);  //这一步很重要
        int height = banner.getMeasuredHeight() * width / banner.getMeasuredWidth();  //这里就是将高度等比例缩放，其中imageView.getMeasuredHeight()和imageView.getMeasuredWidth()是计算图片本身的宽高
        banner.setLayoutParams(new LinearLayout.LayoutParams(width, height));


        gv.setOnItemClickListener(this);

        title.setOnHeaderClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        presenter = new RechaegePresenter();
        presenter.attachView(this);
        balance = getIntent().getStringExtra("balance");
        if (balance != null) {
            String tips = getResources().getString(R.string.RechargeTip) + "<font color='#63B15E'>" + " $ " + balance + "</font>";
            rechargeTip.setText(Html.fromHtml(tips));
        } else {
            rechargeTip.setText(getResources().getString(R.string.RechargeTip));
        }
        presenter.getAmountList(getToken(RechargeActivity.this));


        cardLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (RechargeCardListBean feedBackBean : cardListBeans) {
                    feedBackBean.type = false;
                }
                cardListAdapter.notifyDataSetChanged();
                RechargeCardListBean feedBackBean = new RechargeCardListBean();
                feedBackBean.type = true;
                if (position <= cardListBeans.size() - 1) {
                    feedBackBean.CardNumberLast4 = cardListBeans.get(position).CardNumberLast4;
                    feedBackBean.cardNumberBin = cardListBeans.get(position).cardNumberBin;
                    feedBackBean.cardBrand = cardListBeans.get(position).cardBrand;
                    feedBackBean.id = cardListBeans.get(position).id;
                    cardId = cardListBeans.get(position).id;
                    feedBackBean.thumbnail = cardListBeans.get(position).thumbnail;
                    cardListBeans.set(position, feedBackBean);
                    cardListAdapter.notifyDataSetChanged();
                }

            }
        });


        deviceCode = getIntent().getStringExtra("deviceCode");


        boolean leases = getIntent().getBooleanExtra("lease", false);
        if (leases) {
            lease = leases;
        }
        mMercadoPago = new MercadoPago.Builder()
                .setContext(this)
                .setPublicKey(ExamplesUtils.DUMMY_MERCHANT_PUBLIC_KEY)
                .build();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (balance != null) {
            double balances = Double.parseDouble(balance);
            if (balances < 0) {
                double absbalances = Math.abs(balances);
                if (position <= arrayList.size() - 1) {
                    if (absbalances > Double.parseDouble(arrayList.get(position).payNum)) {
                        shows(getResources().getString(R.string.leseTip), false);
                        return;
                    }
                }

            }
        }
        for (PayNumBean feedBackBean : arrayList) {
            feedBackBean.type = false;
        }
        rechargeAdapter.notifyDataSetChanged();
        PayNumBean feedBackBean = new PayNumBean();
        feedBackBean.type = true;
        feedBackBean.payNum = arrayList.get(position).payNum;
        feedBackBean.pay = arrayList.get(position).pay;
        feedBackBean.id = arrayList.get(position).id;
        moneyId = arrayList.get(position).id;

        money.setText(arrayList.get(position).pay);
        arrayList.set(position, feedBackBean);
        rechargeAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.total, R.id.recharge,R.id.home})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.total:
                break;
            case R.id.recharge:
                //    startActivity(new Intent(RechargeActivity.this, RechargeSuccessfulActivity.class));
                rechargeMoney();
                break;
            case R.id.home:
                EventMeager eventMeager = new EventMeager();
                eventMeager.setHome(true);
                EventBus.getDefault().post(eventMeager);
                break;

        }
    }

    private void addCard() {
        if (TextUtils.isEmpty(moneyId)) {
            showDialog(getResources().getString(R.string.moneyId), true);
            return;
        }
        /*  presenter.recharges(getToken(RechargeActivity.this), moneyId, "");*/
        new MercadoPago.StartActivityBuilder()
                .setActivity(this)
                .setPublicKey(ExamplesUtils.DUMMY_MERCHANT_PUBLIC_KEY)
                .setSupportedPaymentTypes(mSupportedPaymentTypes)
                .startPaymentMethodsActivity();
    }

    private void rechargeMoney() {
        if (TextUtils.isEmpty(moneyId)) {
            showDialog(getResources().getString(R.string.moneyId), true);
            return;
        }
        if (cardListBeans == null) {
            showNoCardDigle();

        } else {
            if (TextUtils.isEmpty(cardId)) {
                showDialog(getResources().getString(R.string.payment), true);
                return;
            }
            presenter.recharges(getToken(RechargeActivity.this), moneyId, cardId);
            //     showSecurityCode();
            // showNoCardDigle();
            //   return;
        }

     /*   } else {
            if (TextUtils.isEmpty(cardId)) {
                showDialog(getResources().getString(R.string.paytype), true);
                return;
            }
            if (TextUtils.isEmpty(moneyId)) {
                showDialog(getResources().getString(R.string.moneyId), true);
                return;
            }
*/
        //   presenter.recharges(getToken(RechargeActivity.this), moneyId, cardId);
        // }


    }

    private void showSecurityCode() {
        if (dialogUtils == null) {
            dialogUtils = new DialogUtils();
        }
        dialogUtils.showSecurityCode(RechargeActivity.this);
        dialogUtils.setOnButtonClickListener(this);
    }

    private void showNoCardDigle() {
        if (dialogUtils == null) {
            dialogUtils = new DialogUtils();
        }
        dialogUtils.setOnButtonClickListener(this);
        dialogUtils.showDeleteCreditCard(RechargeActivity.this, getResources().getString(R.string.LeaseTip));
    }

    @Override
    public void showDialog(String context, boolean type) {
        shows(context, type);
    }

    public void shows(String content, boolean type) {
        if (!isLiving(RechargeActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(RechargeActivity.this);
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
            loadingDialog = new LoadingDialog(RechargeActivity.this);
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

    }

    @Override
    public void onErr() {
        handler.sendEmptyMessageDelayed(2, 3000);
    }

    @Override
    public void upAmountList(AmountList.DataBean list) {
        if (list.getPrice() != null) {
            if (!list.getPrice().equals("")) {
                String[] split = list.getPrice().split(",");
                if (split.length != 0) {
                    arrayList = new ArrayList<>();
                    for (int i = 0; i < split.length; i++) {
                        PayNumBean payNumBean = new PayNumBean();
                        payNumBean.type = false;
                        payNumBean.pay = "$ " + split[i];
                        payNumBean.payNum = split[i];

                        payNumBean.id = split[i];
                        arrayList.add(payNumBean);

                    }
                    rechargeAdapter = new RechargeAdapter(RechargeActivity.this, arrayList);
                    gv.setAdapter(rechargeAdapter);
                }
                //   presenter.getCardList(getToken(RechargeActivity.this));
            }
        }
    /*    if (list.size() == 0) {

        } else {
            arrayList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                PayNumBean payNumBean = new PayNumBean();
                payNumBean.type = false;
                payNumBean.pay = "MXN " + list.get(i).getAmount();
                payNumBean.id = list.get(i).getId();
                arrayList.add(payNumBean);
            }
            rechargeAdapter = new RechargeAdapter(RechargeActivity.this, arrayList);
            gv.setAdapter(rechargeAdapter);
        }*/

        String openId = SPUtils.getString(RechargeActivity.this, "openId", "");
        presenter.getCardList(getToken(RechargeActivity.this), openId);
    }

    @Override
    public void upCardList(List<CardList.DataBean.CardsBean> cardList) {

        if (cardList == null) {
            payTip.setVisibility(View.GONE);

            payTip.setText(getResources().getString(R.string.LeaseTip));
        } else if (cardList.size() == 0) {
            payTip.setVisibility(View.GONE);
            payTip.setText(getResources().getString(R.string.LeaseTip));
        } else {
            // payTip.setVisibility(View.GONE);

            cardListBeans = new ArrayList<>();
            View headerView = LayoutInflater.from(this).inflate(R.layout.recharge_classify_bottom, null);
            other = (LinearLayout) headerView.findViewById(R.id.other);
            other.setOnClickListener(v -> addCard());
            //  first = false;
            if (!first) {
                cardLists.addFooterView(headerView);
                first = true;
            }

            for (int i = 0; i < cardList.size(); i++) {
                RechargeCardListBean rechargeCardListBean = new RechargeCardListBean();
                rechargeCardListBean.id = cardList.get(i).getId() + "";
                rechargeCardListBean.cardNumberBin = "**** ****";
                rechargeCardListBean.cardBrand = cardList.get(i).getIssuer().getName();
                rechargeCardListBean.type = false;
                rechargeCardListBean.CardNumberLast4 ="**** **** ****"+ cardList.get(i).getLastFourDigits();
                rechargeCardListBean.thumbnail = cardList.get(i).getPaymentMethod().getThumbnail();
                cardListBeans.add(rechargeCardListBean);
            }

            cardListAdapter = new RechargeCardListAdapter(RechargeActivity.this, cardListBeans);
            cardLists.setAdapter(cardListAdapter);

        }
    }

    @Override
    public void rechargeSuccess(int type) {
        EventMeager eventMeager = new EventMeager();
        eventMeager.setUpUser(true);
        EventBus.getDefault().post(eventMeager);

    }

    @Override
    public void rechargeOn(String rechargeId, String dataId) {
        loop=0;
        orderId = rechargeId;
        //goPayment( dataId);
        paymentId = dataId;
        presenter.getResult(getToken(RechargeActivity.this), rechargeId, paymentId);
    }

    @Override
    public void update(int type, String orderIds, String RechargeId) {
        showLoading();
        loop = 0;
        orderId = orderIds;


        presenter.getResults(getToken(RechargeActivity.this), orderId);
    }


    @Override
    public void Success(int code, String addTime) {

        if (lease) {
            presenter.info("0", "0", getToken(RechargeActivity.this));

        } else {
            dismissLoading();

            Intent intent = new Intent(RechargeActivity.this, RechargeSuccessfulActivity.class);
            intent.putExtra("type", "Success");
            intent.putExtra("addTime", addTime);
            intent.putExtra("id", code + "");
            startActivity(intent);
       //     finish();
            EventMeager eventMeager = new EventMeager();
            eventMeager.setRechargeSuccessful(true);
            EventBus.getDefault().post(eventMeager);
        }

    }

    @Override
    public void Success(String bayonetInt) {
        dismissLoading();

        Intent intent = new Intent(RechargeActivity.this, LeaseActivity.class);
        intent.putExtra("num", bayonetInt + "");
        intent.putExtra("orderId", orderId);
        startActivity(intent);
      //  finish();
        EventMeager eventMeager = new EventMeager();
        eventMeager.setRentalSuccess(true);
        EventBus.getDefault().post(eventMeager);
    }

    @Override
    public void Failure() {
        dismissLoading();
        Intent intent = new Intent(RechargeActivity.this, RechargeSuccessfulActivity.class);
        intent.putExtra("type", "0");
        startActivity(intent);
    }

    @Override
    public void FailureLease() {
        dismissLoading();
        Intent intent = new Intent(RechargeActivity.this, LeaseActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void getOrder() {
        if (loop == 20) {
            dismissLoading();
            Intent intent = new Intent(RechargeActivity.this, RechargeSuccessfulActivity.class);
            intent.putExtra("type", "0");
            startActivity(intent);
            return;
        }
        handler.sendEmptyMessageDelayed(3, 3000);
    }

    @Override
    public void userInfo(double accountMy) {
        if (accountMy >= 0) {
            presenter.getBorrow(getToken(RechargeActivity.this), "0", "0", deviceCode);
        } else {
            String tips = getResources().getString(R.string.RechargeTip) + "<font color='#63B15E'>" + " $ " + accountMy + "</font>";
            rechargeTip.setText(Html.fromHtml(tips));
            dismissLoading();
            Intent intent = new Intent(RechargeActivity.this, RechargeSuccessfulActivity.class);
            intent.putExtra("type", "Success");
            startActivity(intent);
        }
    }

    @Override
    public void rechargeSuccessful(String rechargeId) {
        showLoading();
        loop = 0;
        orderId = rechargeId;
        presenter.getResult(getToken(RechargeActivity.this), orderId, paymentId + "");
    }

    @Override
    public void getOrders() {
        if (loop == 20) {
            dismissLoading();
            Intent intent = new Intent(RechargeActivity.this, LeaseActivity.class);
            startActivity(intent);
            return;
        }
        handler.sendEmptyMessageDelayed(4, 3000);
    }

    private void goPayment(String dataId) {
     /*   MercadoPagoCheckout build = new MercadoPagoCheckout.Builder("TEST-0cec2e72-de6a-43e6-b414-c238b553eba9",dataId)//RechargeId
                .build();
        build.startPayment(this,1);*/
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resolveCheckoutResult(RechargeActivity.this, requestCode, resultCode, data, 1);
            }
        });

    }*/

    /**
     * 销毁页面的信息
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {

            finish();


        } else if (message.getupDataCard()) {
            String openId = SPUtils.getString(RechargeActivity.this, "openId", "");
            presenter.getCardList(getToken(RechargeActivity.this), openId);
        }else  if (message.isHome()){

            finish();
        }else if (message.isRentalSuccess()) {
            finish();
        }else  if (message.isRechargeSuccessful()){
            finish();
        }
    }

    public void resolveCheckoutResult(final Activity context, final int requestCode, final int resultCode,
                                      final Intent data, final int reqCodeCheckout) {
      /*  ViewUtils.showRegularLayout(context);

        if (requestCode == reqCodeCheckout) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                final Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);

                Log.e("我进来了","111111");
                Log.e("xxx",new StringBuilder().append(PAYMENT_WITH_STATUS_MESSAGE).append(payment).toString());
                showLoading();
                loop=0;
                paymentId = payment.getId();
                //  Log.e("paymentId",paymentId+"");
                presenter.getResult(getToken(RechargeActivity.this),orderId, paymentId +"");
            } else if (resultCode == RESULT_CANCELED) {
            //    Failure();
                if (data != null && data.getExtras() != null &&
                        data.getExtras().containsKey(MercadoPagoCheckout.EXTRA_ERROR)) {
                    final MercadoPagoError mercadoPagoError =
                            (MercadoPagoError) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR);
                    //  Toast.makeText(context, "Error: " + mercadoPagoError, Toast.LENGTH_LONG).show();
                    Log.e("xxx","Error"+mercadoPagoError);


                } else {
                *//*    Toast.makeText(context, new StringBuilder()
                            .append("Cancel - ")
                            .append(REQUESTED_CODE_MESSAGE)
                            .append(requestCode)
                            .append(RESULT_CODE_MESSAGE)
                            .append(resultCode), Toast.LENGTH_LONG)
                            .show();*//*
                    Log.e("xxx","Cancel - "+REQUESTED_CODE_MESSAGE+"msg="+RESULT_CODE_MESSAGE);
                    Log.e("我进来了","333333");
                }
            } else {
              //  Failure();
                Log.e("xxx","Cancel - "+REQUESTED_CODE_MESSAGE+"msg="+RESULT_CODE_MESSAGE);
                Log.e("我进来了","4444444");
            }
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        presenter.onDestroy();
        presenter = null;
        handler.removeMessages(1);
        handler.removeMessages(2);
        handler.removeMessages(3);
        handler.removeMessages(4);
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
    public void onPositiveButtonClick(int type, String context, AlertDialog dialog) {
        dialogs = dialog;
        dialogs.dismiss();

        switch (type) {
            case 3:  //安全码
                showLoading();
                securityCode = context;
                createSavedCardToken();
                break;

            case 11:    //新增卡
                new MercadoPago.StartActivityBuilder()
                        .setActivity(this)
                        .setPublicKey(ExamplesUtils.DUMMY_MERCHANT_PUBLIC_KEY)
                        .setSupportedPaymentTypes(mSupportedPaymentTypes)
                        .startPaymentMethodsActivity();
                break;
        }


        // Intent intent = new Intent(RechargeActivity.this, AddCreditCardActivity.class);
        //startActivity(intent);
    }

    private void createSavedCardToken() {
        SavedCardToken savedCardToken = new SavedCardToken(cardId, securityCode);


        // Create token
        LayoutUtil.showProgressLayout(RechargeActivity.this);
        ErrorHandlingCallAdapter.MyCall<Token> call = mMercadoPago.createToken(savedCardToken);
        call.enqueue(getCreateTokenCallback());
    }

    protected ErrorHandlingCallAdapter.MyCallback<Token> getCreateTokenCallback() {

        return new ErrorHandlingCallAdapter.MyCallback<Token>() {
            @Override
            public void success(Response<Token> response) {


                Log.e("token", response.body().getId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoading();
                    }
                });

                Log.e("token", response.body().getId() + "");

            }

            @Override
            public void failure(ApiException apiException) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoading();
                        showDialog(getResources().getString(R.string.acquisitionFailed), false);

                    }
                });
                // Log.e("token","failure");
            }
        };
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "onActivityResult");


        if (requestCode == MercadoPago.PAYMENT_METHODS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.e("apiException", "RESULT_OK");
                // Set payment method
                PaymentMethod paymentMethod = JsonUtil.getInstance().fromJson(data.getStringExtra("paymentMethod"), PaymentMethod.class);

                // Call new card activity

                if (lease){
                    ExamplesUtils.startCardActivity(this, ExamplesUtils.DUMMY_MERCHANT_PUBLIC_KEY, paymentMethod, "3",deviceCode,moneyId);
                }else {
                    ExamplesUtils.startCardActivity(this, ExamplesUtils.DUMMY_MERCHANT_PUBLIC_KEY, paymentMethod, "2",null,moneyId);
                }

            } else {

                if ((data != null) && (data.getStringExtra("apiException") != null)) {
                    Toast.makeText(getApplicationContext(), data.getStringExtra("apiException"), Toast.LENGTH_LONG).show();
                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

}
