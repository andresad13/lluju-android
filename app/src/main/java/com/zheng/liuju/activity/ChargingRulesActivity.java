package com.zheng.liuju.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gigamole.library.ShadowLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.mercadopago.core.MercadoPago;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.util.JsonUtil;
import com.zheng.liuju.R;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.CardList;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.bean.RulesBean;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.mercadopago.utils.ExamplesUtils;
import com.zheng.liuju.presenter.ChargingPresenter;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.view.DialogUtils;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.ICharging;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;
import com.zheng.liuju.view.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChargingRulesActivity extends BaseActivity implements ICharging, DialogUtils.OnButtonClickListener {
    @BindView(R.id.rentimmediately)
    ShadowLayout rentimmediately;
    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.banner)
    ImageView banner;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.zujiesm)
    TextView zujiesm;
    @BindView(R.id.deposit)
    TextView deposit;
    @BindView(R.id.freeDurations)
    TextView freeDurations;
    @BindView(R.id.dayCap)
    TextView dayCap;
    @BindView(R.id.totalCap)
    TextView totalCap;
    @BindView(R.id.prices)
    TextView prices;
    @BindView(R.id.home)
    DragImageView home;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    private DialogUtils dialogUtils;
    private AlertDialog dialogs;
    private PromptDialog promptDialog;
    private int loop;
    private int payType;    //0 充押金 1 充余额 2直接租借
    private static final String REQUESTED_CODE_MESSAGE = "Requested code: ";
    private static final String PAYMENT_WITH_STATUS_MESSAGE = "Payment with status: ";
    private static final String RESULT_CODE_MESSAGE = " Result code: ";
    protected MercadoPago mMercadoPago;
    protected List<String> mSupportedPaymentTypes = new ArrayList<String>() {{
        add("credit_card");
        add("debit_card");
        add("prepaid_card");
    }};

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
                SPUtils.putString(ChargingRulesActivity.this, "token", "");
                startActivity(new Intent(ChargingRulesActivity.this, LoginActivity.class));

                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            } else if (msg.what == 3) {
                loop = loop + 1;
                presenter.getResult(getToken(ChargingRulesActivity.this), orderId, paymentId + "");
            }


        }
    };
    private LoadingDialog loadingDialog;
    private ChargingPresenter presenter;
    private String deviceCode;
    private String orderId;
    private RulesBean person;
    private String cardId = "";
    private List<CardList.DataBean.CardsBean> cardLists;
    private String paymentId = "";
    private String TAG = "ChargingRulesActivity";
    private int ADD = 1;
    private String securityCode = "";
    private double userAccoutMy;
    private double yajin1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_rules);
        ButterKnife.bind(this);
        initView();
        getIn();
    }

    /**
     * 初始化数据
     */
    private void initView() {
        EventBus.getDefault().register(this);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        presenter = new ChargingPresenter();
        presenter.attachView(this);

        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        title.setOnHeaderClickListener(v -> finish());
        deviceCode = getIntent().getStringExtra("deviceCode");
        if (deviceCode == null) {
            deviceCode = "";
        }
        person = (RulesBean) getIntent().getSerializableExtra("bean");
        if (person != null) {
            //  deposit.setText("MXN "+person.ge()+"/hour ");
            //   contents.setText(" Free for MXN "+person.getData().getFreeDuration()+" minutes, daily capped MXN"+" "+person.getData().getMaxPrice()+", total capped MXN "+person.getData().getDeposit());
            deposit.setText("$ " + person.getYajin());
            freeDurations.setText(person.getFreetime() + " min");
            prices.setText("$ " + person.getPrice());
            dayCap.setText("$ " + person.getFengding());
            totalCap.setText("$ " + person.getYajin());
        }
        banner.setImageResource(R.mipmap.deposit);   //资源文件中的一张图片
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();    //获取屏幕的宽度
        banner.measure(0, 0);  //这一步很重要
        int height = banner.getMeasuredHeight() * width / banner.getMeasuredWidth();  //这里就是将高度等比例缩放，其中imageView.getMeasuredHeight()和imageView.getMeasuredWidth()是计算图片本身的宽高
        banner.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        Log.e("我要的数据", getDateTime());

        mMercadoPago = new MercadoPago.Builder()
                .setContext(this)
                .setPublicKey(ExamplesUtils.DUMMY_MERCHANT_PUBLIC_KEY)
                .build();
    }


    /**
     * 退出各个页面的事件
     *
     * @param message 事件类
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            finish();

        } else if (message.isRechargeSuccessful()) {
            presenter.getRules(getToken(ChargingRulesActivity.this), deviceCode);
        } else if (message.isRentalSuccess()) {
            finish();
        }else if (message.isHome()){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        presenter.onDestroy();
        presenter = null;
        handler.removeMessages(1);
        handler.removeMessages(2);
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

    /**
     * 语言切换
     *
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @OnClick({R.id.rentimmediately,R.id.home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rentimmediately:


                borrow();
                //APP_USR-1701a76a-89e1-40ad-9da6-a5ac6ef4e90e
              /*  MercadoPagoCheckout build = new MercadoPagoCheckout.Builder("TEST-0cec2e72-de6a-43e6-b414-c238b553eba9","502190411-a615ba92-862a-4429-8c20-3546d9affbd4")//RechargeId
                        .build();
                build.startPayment(this,1);*/
                break;

            case R.id.home:

                EventMeager eventMeager = new EventMeager();

                eventMeager.setHome(true);

                EventBus.getDefault().post(eventMeager);
                break;
        }
    }

    /**
     * 租借充电宝(如果押金不足,去下单充值押金,如果余额不足提示,跳转充值余额)
     */
    private void borrow() {


        borrowFinish("");
    }

    /**
     * 公用的信息弹框
     *
     * @param context
     * @param type    弹框的类型 true 为正确的弹框 false 为失败的弹框
     */
    @Override
    public void showDialog(String context, boolean type) {
        shows(context, type);
    }

    private void shows(String content, boolean type) {
        if (!isLiving(ChargingRulesActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(ChargingRulesActivity.this);
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
            loadingDialog = new LoadingDialog(ChargingRulesActivity.this);
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


    @Override
    public void upCardList(List<CardList.DataBean.CardsBean> cardList) {

       /* if (cardList == null) {
         //   showNoCardDigle();
            cardId="";
            borrowFinish(cardId);
        } else if (cardList.size() == 0) {
            cardId="";
            borrowFinish(cardId);
        } else if (cardList.size() == 1) {
           *//* cardId=cardList.get(0).getId()+"";
            borrowFinish(cardId);*//*
            cardLists = new ArrayList<>();
            cardLists.addAll(cardList);
            cardId=null;
            borrowFinish(cardId);
        } else if (cardList.size() > 1) {
            cardLists = new ArrayList<>();
            cardLists.addAll(cardList);
            cardId=null;
            borrowFinish(cardId);

        }*/
        if (cardList == null) {
            if (payType==3){
                showBankCardList(cardList);
            }else{
                showNoCardDigles();
            }

        } else if (cardList.size() == 0) {
            if (payType==3){
                showBankCardList(cardList);
            }else {
                showNoCardDigles();
            }

        } else {
            showBankCardList(cardList);
        }

        // }
//Log.e("cardList",cardList.size()+"");


    }

    private void showNoCardDigles() {
        if (dialogUtils == null) {
            dialogUtils = new DialogUtils();
        }
        dialogUtils.setOnButtonClickListener(this);
        dialogUtils.showDeleteCreditCard(ChargingRulesActivity.this, getResources().getString(R.string.LeaseTip));
    }


    private void borrowFinish(String cardId) {
        payType = 0;
        if (person != null) {
            double yajin = person.getUserAccountYajin();  //我的押金
            //店铺押金
            yajin1 = person.getYajin();
            userAccoutMy = person.getUserAccoutMy();
            if (yajin >= yajin1) {
                if (userAccoutMy < 0) {
                    showNoCardDigle(getResources().getString(R.string.balanceTip) + "<font color='#FF0000'>" + " $ " + userAccoutMy + "</font>" + getResources().getString(R.string.balanceTips));
                } else {
                    payType = 2;    //押金,余额充足
                    presenter.getBorrow(getToken(ChargingRulesActivity.this), "0", "0", deviceCode, "");//,cardId


                }
            } else {
                //我加的代码   2020 4  23    //余额充足
                if (userAccoutMy >= yajin1) {
                  payType = 3;
                    //      presenter.getBorrow(getToken(ChargingRulesActivity.this), "0", "0", deviceCode,"","");//,cardId
                    String openId = SPUtils.getString(ChargingRulesActivity.this, "openId", "");
                    presenter.getCardList(getToken(ChargingRulesActivity.this), openId);//,context
                } else {
                    showNoCardDigle(getResources().getString(R.string.depositsTip) + "<font color='#FF0000'>" + " $ " + yajin + "</font>" + getResources().getString(R.string.balanceTips));
                }


            }
        }

        //     presenter.getBorrow(getToken(ChargingRulesActivity.this), "0", "0", deviceCode);//,cardId
    }


    @Override
    public void update(int type, String orderIds, String RechargeId) {
        loop = 0;
        orderId = orderIds;
        showLoading();
        paymentId = RechargeId;
        presenter.getResult(getToken(ChargingRulesActivity.this), orderId, RechargeId);
        // }
        //TEST-3580782637306831-010801-cc828385510ca23a644a4272b92ae083-502190411   APP_USR-1701a76a-89e1-40ad-9da6-a5ac6ef4e90e

    }

    /*private void goPayment(String RechargeId) {

        Intent in = new Intent(getApplicationContext(), PayTabActivity.class);
        in.putExtra(PaymentParams.MERCHANT_EMAIL,  Constant.Merchant_Account);
        in.putExtra(PaymentParams.SECRET_KEY, Constant.PAY_KEY);//Add your Secret Key Here
        in.putExtra(PaymentParams.LANGUAGE, PaymentParams.ENGLISH);
        in.putExtra(PaymentParams.TRANSACTION_TITLE, "Test Paytabs android library");
        in.putExtra(PaymentParams.AMOUNT, person.getYajin());//getYajin()
        in.putExtra(PaymentParams.CURRENCY_CODE, "SAR");
        in.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, "009733");
        String email = SPUtils.getString(ChargingRulesActivity.this, "email", "");
        in.putExtra(PaymentParams.CUSTOMER_EMAIL,email );//email
        in.putExtra(PaymentParams.ORDER_ID, RechargeId);
        in.putExtra(PaymentParams.PRODUCT_NAME, "Product 1, Product 2");
        //Billing Address
        in.putExtra(PaymentParams.ADDRESS_BILLING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_BILLING, "Manama");
        in.putExtra(PaymentParams.STATE_BILLING, "Manama");
        in.putExtra(PaymentParams.COUNTRY_BILLING, "BHR");
        in.putExtra(PaymentParams.POSTAL_CODE_BILLING, "00973"); //Put Country Phone code if Postal code not available '00973'

        //Shipping Address
        in.putExtra(PaymentParams.ADDRESS_SHIPPING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_SHIPPING, "Manama");
        in.putExtra(PaymentParams.STATE_SHIPPING, "Manama");
        in.putExtra(PaymentParams.COUNTRY_SHIPPING, "BHR");
        in.putExtra(PaymentParams.POSTAL_CODE_SHIPPING, "00973"); //Put Country Phone code if Postal code not available '00973'

        //Payment Page Style
        in.putExtra(PaymentParams.PAY_BUTTON_COLOR, "#F45344");

        //Tokenization
        in.putExtra(PaymentParams.IS_TOKENIZATION, true);
        startActivityForResult(in, PaymentParams.PAYMENT_REQUEST_CODE);

    }*/


    /*    public  void resolveCheckoutResult(final Activity context, final int requestCode, final int resultCode,
                                                 final Intent data, final int reqCodeCheckout) {
            ViewUtils.showRegularLayout(context);

            if (requestCode == reqCodeCheckout) {
                if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                    final Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
                    *//*Toast.makeText(context, new StringBuilder()
                        .append(PAYMENT_WITH_STATUS_MESSAGE)
                        .append(payment), Toast.LENGTH_LONG)
                        .show();*//*
                Log.e("我进来了","111111");
                Log.e("xxx",new StringBuilder().append(PAYMENT_WITH_STATUS_MESSAGE).append(payment).toString());
                showLoading();
                loop=0;
                paymentId = payment.getId();
              //  Log.e("paymentId",paymentId+"");
                presenter.getResult(getToken(ChargingRulesActivity.this),orderId, paymentId +"");
            } else if (resultCode == RESULT_CANCELED) {
                Failure();
                if (data != null && data.getExtras() != null &&
                        data.getExtras().containsKey(MercadoPagoCheckout.EXTRA_ERROR)) {
                    final MercadoPagoError mercadoPagoError =
                            (MercadoPagoError) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR);
                  //  Toast.makeText(context, "Error: " + mercadoPagoError, Toast.LENGTH_LONG).show();
                    //Log.e("xxx","Error"+mercadoPagoError);


                } else {

                   *//* Toast.makeText(context, new StringBuilder()
                            .append("Cancel - ")
                            .append(REQUESTED_CODE_MESSAGE)
                            .append(requestCode)
                            .append(RESULT_CODE_MESSAGE)
                            .append(resultCode), Toast.LENGTH_LONG)
                            .show();*//*
                    Log.e("我进来了","333333");
                }
            } else {
               Failure();
               *//* Toast.makeText(context, new StringBuilder()
                        .append(REQUESTED_CODE_MESSAGE)
                        .append(requestCode)
                        .append(RESULT_CODE_MESSAGE)
                        .append(resultCode), Toast.LENGTH_LONG)
                        .show();*//*
                Log.e("我进来了","4444444");
            }
        }
    }*/
    @Override
    public void Success(String num) {
        dismissLoading();
        EventMeager eventMeager = new EventMeager();
        eventMeager.setRentalSuccess(true);
        EventBus.getDefault().post(eventMeager);
        Intent intent = new Intent(ChargingRulesActivity.this, LeaseActivity.class);
        intent.putExtra("num", num + "");
        intent.putExtra("orderId", orderId);
        startActivity(intent);
        finish();
    }

    @Override
    public void Failure() {
        dismissLoading();
        Intent intent = new Intent(ChargingRulesActivity.this, LeaseActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void getOrder() {
        if (loop == 20) {
            Intent intent = new Intent(ChargingRulesActivity.this, LeaseActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        handler.sendEmptyMessageDelayed(3, 3000);
    }

    @Override
    public void scan(RulesBean bean) {
        person = bean;
    }

    private void showBankCardList(List<CardList.DataBean.CardsBean> cardList) {
        if (dialogUtils == null) {
            dialogUtils = new DialogUtils();
        }
        dialogUtils.setOnButtonClickListener(this);
       // if (payType == 3) {
         //   dialogUtils.showSelectBankCard(ChargingRulesActivity.this, cardList, userAccoutMy + "");
       // } else {
        if(cardList==null){
            cardList = new ArrayList<>();
        }
            dialogUtils.showSelectBankCard(ChargingRulesActivity.this, cardList, userAccoutMy + "");
       // }

    }

    private void showNoCardDigle(String tip) {
        if (dialogUtils == null) {
            dialogUtils = new DialogUtils();
        }
        dialogUtils.setOnButtonClickListener(this);
        dialogUtils.showDeleteCreditCard(ChargingRulesActivity.this, tip);
    }

    @Override
    public void onPositiveButtonClick(int type, String context, AlertDialog dialog) {
        dialogs = dialog;
        switch (type) {
            case 1:
                dialogs.dismiss();
                Intent intent = new Intent(ChargingRulesActivity.this, RechargeActivity.class);
                intent.putExtra("balance", person.getUserAccoutMy() + "");
                intent.putExtra("lease", true);
                intent.putExtra("deviceCode", deviceCode);
                startActivity(intent);
                break;
            case 4:  //增加银行卡
                dialogs.dismiss();
                new MercadoPago.StartActivityBuilder()
                        .setActivity(this)
                        .setPublicKey(ExamplesUtils.DUMMY_MERCHANT_PUBLIC_KEY)
                        .setSupportedPaymentTypes(mSupportedPaymentTypes)
                        .startPaymentMethodsActivity();


             /*   Intent intents = new Intent(ChargingRulesActivity.this, AddCreditCardActivity.class);
                intents.putExtra("type","0");
                startActivityForResult(intents,ADD);*/
                //  presenter.getBorrow(getToken(ChargingRulesActivity.this), "0", "0", deviceCode);//,context
                break;
            case 5:   //押金
                dialogs.dismiss();
                String openId = SPUtils.getString(ChargingRulesActivity.this, "openId", "");
                presenter.getCardList(getToken(ChargingRulesActivity.this), openId);//,context
                break;

            case 10:   //选择银行卡
                dialogs.dismiss();
                if (context.equals("")){
                    if (userAccoutMy >= yajin1) {
                        presenter.getBorrow(getToken(ChargingRulesActivity.this), "0", "0", deviceCode, context);
                    }else {
                         showNoCardDigle(getResources().getString(R.string.balanceTip) + "<font color='#FF0000'>" + " $ " + userAccoutMy + "</font>" + getResources().getString(R.string.balanceTips));

                    }
                  //  showNoCardDigle(getResources().getString(R.string.balanceTip) + "<font color='#FF0000'>" + " $ " + userAccoutMy + "</font>" + getResources().getString(R.string.balanceTips));
                }else {
                    presenter.getBorrow(getToken(ChargingRulesActivity.this), "0", "0", deviceCode, context);
                }

                //  cardId = context;
                // presenter.getBorrow(getToken(ChargingRulesActivity.this), "0", "0", deviceCode);//,context
                //  showSecurityCode();
                break;

            case 3:  //输入安全码
                //dialogs.dismiss();
                //  showLoading();
                //    securityCode = context;
                //   createSavedCardToken();

                break;
            case 11:
                dialogs.dismiss();
                new MercadoPago.StartActivityBuilder()
                        .setActivity(this)
                        .setPublicKey(ExamplesUtils.DUMMY_MERCHANT_PUBLIC_KEY)
                        .setSupportedPaymentTypes(mSupportedPaymentTypes)
                        .startPaymentMethodsActivity();
                break;
        }

    }

/*    private void createSavedCardToken() {
        Log.e("createSavedCardToken","cardId="+cardId+"securityCode="+securityCode);
        SavedCardToken savedCardToken = new SavedCardToken(cardId, securityCode);


        // Create token
        LayoutUtil.showProgressLayout(ChargingRulesActivity.this);
        ErrorHandlingCallAdapter.MyCall<Token> call = mMercadoPago.createToken(savedCardToken);
        call.enqueue(getCreateTokenCallback());
    }*/

    /*
        protected ErrorHandlingCallAdapter.MyCallback<Token> getCreateTokenCallback() {

            return new ErrorHandlingCallAdapter.MyCallback<Token>() {
                @Override
                public void success(Response<Token> response) {


                    Log.e("token",response.body().getId());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissLoading();
                        }
                    });

                    Log.e("token",response.body().getId()+"");
                presenter.getBorrow(getToken(ChargingRulesActivity.this),"0","0",deviceCode,response.body().getId(),cardId);
                }

                @Override
                public void failure(ApiException apiException) {
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             dismissLoading();
                      showDialog(getResources().getString(R.string.acquisitionFailed),false);

                         }
                     });
                   // Log.e("token","failure");
                }
            };
        }*/
    private void showSecurityCode() {
        if (dialogUtils == null) {
            dialogUtils = new DialogUtils();
        }
        dialogUtils.showSecurityCode(ChargingRulesActivity.this);
        dialogUtils.setOnButtonClickListener(this);

    }

    /**
     * * 生成时间戳
     *      
     */
    private static String getDateTime() {
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date());
    }

    private void getIn() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            // 完整的url信息
            String url = uri.toString();
            Log.e(TAG, "id: " + url);
            // scheme部分
            String scheme = uri.getScheme();
            Log.e(TAG, "scheme: " + scheme);
            // host部分
            String host = uri.getHost();
            Log.e(TAG, "host: " + host);
            //port部分
            int port = uri.getPort();
            Log.e(TAG, "host: " + port);
            // 访问路径
            String path = uri.getPath();
            Log.e(TAG, "path: " + path);
            List<String> pathSegments = uri.getPathSegments();
            // Query部分
            String query = uri.getQuery();
            Log.e(TAG, "query: " + query);
            //获取指定参数值
            String goodsId = uri.getQueryParameter("goodsId");
            Log.e(TAG, "goodsId: " + goodsId);
        } else {
            Log.e(TAG, "url==null");
        }
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
                ExamplesUtils.startCardActivity(this, ExamplesUtils.DUMMY_MERCHANT_PUBLIC_KEY, paymentMethod, "1",deviceCode,null);
            } else {

                if ((data != null) && (data.getStringExtra("apiException") != null)) {
                    Toast.makeText(getApplicationContext(), data.getStringExtra("apiException"), Toast.LENGTH_LONG).show();
                }
            }
        }/*else  if (resultCode == ADD) {
            String three = data.getStringExtra("cardId");

            //设置结果显示框的显示数值
            if (!three.equals("")) {
             //  quhaos.setText("+" + String.valueOf(three));
              presenter.getBorrow(getToken(ChargingRulesActivity.this), "0", "0", deviceCode,three);
            }


        }*/


        super.onActivityResult(requestCode, resultCode, data);
    }


}
