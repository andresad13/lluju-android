package com.zheng.liuju.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.mercadopago.adapters.ErrorHandlingCallAdapter;
import com.mercadopago.adapters.IdentificationTypesAdapter;
import com.mercadopago.core.MercadoPago;
import com.mercadopago.model.ApiException;
import com.mercadopago.model.CardToken;
import com.mercadopago.model.IdentificationType;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Token;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;
import com.mercadopago.util.MercadoPagoUtil;
import com.zheng.liuju.R;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.bean.Infos;
import com.zheng.liuju.country.PickActivity;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.presenter.AddCardPresenter;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.utils.Utils;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.IAddCreditCard;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;
import com.zheng.liuju.view.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

/**
 * 增加信用卡页面
 */
public class AddCreditCardActivity extends BaseActivity implements View.OnTouchListener, View.OnFocusChangeListener
        , IAddCreditCard {
    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.cardnumber)
    TextView cardnumber;
    @BindView(R.id.expirationdate)
    TextView expirationdate;
    @BindView(R.id.securitycode)
    TextView securitycode;
    @BindView(R.id.cardholder)
    EditText cardholder;
    @BindView(R.id.passwordview)
    View passwordview;
    @BindView(R.id.cardnumbers)
    EditText cardnumbers;
    @BindView(R.id.cardnumberview)
    View cardnumberview;
    @BindView(R.id.expiration)
    EditText expiration;
    @BindView(R.id.expirationview)
    View expirationview;
    @BindView(R.id.securitycodes)
    EditText securitycodes;
    @BindView(R.id.securitycodeview)
    View securitycodeview;
    @BindView(R.id.sv)
    ScrollView sv;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.defaults)
    ImageView defaults;
    @BindView(R.id.identificationTypeText)
    TextView identificationTypeText;
    @BindView(R.id.identificationNumberText)
    TextView identificationNumberText;
    @BindView(R.id.identificationNumber)
    EditText mIdentificationNumber;
    @BindView(R.id.identificationType)
    Spinner mIdentificationType;
    @BindView(R.id.identificationLayout)
    RelativeLayout mIdentificationLayout;
    @BindView(R.id.code)
    View code;
    @BindView(R.id.quhaos)
    TextView quhaos;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.home)
    DragImageView home;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    private PromptDialog promptDialog;
    private LoadingDialog loadingDialog;
    private PaymentMethod mPaymentMethod;
    @SuppressLint("HandlerLeak")
    //msg.what==1 弹框的关闭 msg.what==2 token过期或者账号互顶,重新登录
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                if (promptDialog != null) {
                    promptDialog.dismiss();
                    promptDialog = null;
                }
            } else if (msg.what == 2) {
                SPUtils.putString(AddCreditCardActivity.this, "token", "");
                startActivity(new Intent(AddCreditCardActivity.this, LoginActivity.class));

                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            } else if (msg.what == 3) {
                Intent in = new Intent();
                in.putExtra("cardId", token);
                setResult(1, in);
                finish();
            }else  if (msg.what==4){
                loop = loop + 1;
                presenter.getResult(getToken(AddCreditCardActivity.this), orderId, paymentId + "");
            }else if (msg.what==5){
                loop = loop + 1;
                presenter.getResults(getToken(AddCreditCardActivity.this), orderId, paymentId);
            }


        }
    };
    private CardToken mCardToken;
    private AddCardPresenter presenter;
    private Activity activity = this;
    private String month = "";
    private String year = "";
    private MercadoPago mMercadoPago;
    private String mMerchantPublicKey;
    private int months;
    private int years;
    private String token = "";
    private String type;   //    null 增加卡   1 增加卡,交押金,租借    2增加卡 ,交余额   3 增加卡交余额,租借
    private int CODE = 1;
    private  String deviceCode;
    private int loop;
    private String orderId;
    private String paymentId;
    private String moneyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit_card);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    /**
     * 多语言设置
     *
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @Override
    protected void onDestroy() {
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

        System.gc();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 销毁页面的信息
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            Intent in = new Intent();
            in.putExtra("cardId", "");
            setResult(1, in);
            finish();


        }else if (message.isHome()){
            finish();
        }else if (message.isRentalSuccess()) {
            finish();
        }
    }

    /**
     * 初始化页面
     */
    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        presenter = new AddCardPresenter();
        presenter.attachView(this);
        title.setOnHeaderClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.putExtra("cardId", "");
                setResult(1, in);
                finish();
            }
        });
        sv.setOnTouchListener(this);
        cardholder.setOnFocusChangeListener(this);
        cardnumbers.setOnFocusChangeListener(this);
        expiration.setOnFocusChangeListener(this);
        mIdentificationNumber.setOnFocusChangeListener(this);
        securitycodes.setOnFocusChangeListener(this);
        //信用卡即时数据处理
        cardnumbers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cardNu = s.toString();
                String cardNum = cardNu.replace(" ", "");
                boolean maestro = maestro(cardNum);
                boolean visa = visa(cardNum);

                if (maestro) {
                    //defaults.setImageResource(R.mipmap.mastercard);
                } else if (visa) {
                    //defaults.setImageResource(R.mipmap.visacard);
                } else {
                    //defaults.setImageResource(R.mipmap.jcb);
                }
                if (TextUtils.isEmpty(cardNum)) {
                    //defaults.setImageResource(R.mipmap.defaults);
                }
                if (cardNum.length() == 1) {
                    cardnumber.setText("*");
                } else if (cardNum.equals("")) {
                    cardnumber.setText("");
                } else if (cardNum.length() == 2) {
                    cardnumber.setText("**");
                } else if (cardNum.length() == 3) {
                    cardnumber.setText("***");
                } else if (cardNum.length() == 4) {
                    cardnumber.setText("****");
                } else if (cardNum.length() == 5) {
                    cardnumber.setText("**** *");
                } else if (cardNum.length() == 6) {
                    cardnumber.setText("**** **");
                } else if (cardNum.length() == 7) {
                    cardnumber.setText("**** ***");
                } else if (cardNum.length() == 8) {
                    cardnumber.setText("**** ****");
                } else if (cardNum.length() == 9) {
                    cardnumber.setText("**** **** *");
                } else if (cardNum.length() == 10) {
                    cardnumber.setText("**** **** **");
                } else if (cardNum.length() == 11) {
                    cardnumber.setText("**** **** ***");
                } else if (cardNum.length() == 12) {
                    cardnumber.setText("**** **** ****");
                } else if (cardNum.length() > 12) {
                    if (cardNum.length() <= 16) {
                        cardnumber.setText("**** **** **** " + cardNum.substring(12, cardNum.length()));
                    } else if (cardNum.length() <= 19) {
                        cardnumber.setText("**** **** **** " + cardNum.substring(12, 16) + " " + cardNum.substring(16, cardNum.length()));
                    } else {
                        cardnumber.setText("**** **** **** " + cardNum.substring(12, 16) + " " + cardNum.substring(16, 19) + "...");
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //安全码数据处理
        expiration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cardNu = s.toString();
                String cardNum = cardNu.replace(" ", "");
                Log.e("我要的数据", "start=" + start + ",before=" + before + ",count=" + count);

                if (before == 0) {
                    if (cardNum.length() == 2) {
                        expiration.setText(s + "/");
                        expiration.setSelection(expiration.getText().length());
                    }
                }


                expirationdate.setText(expiration.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        securitycodes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cardNu = s.toString();
                String cardNum = cardNu.replace(" ", "");
                securitycode.setText(cardNum);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mPaymentMethod = JsonUtil.getInstance().fromJson(this.getIntent().getStringExtra("paymentMethod"), PaymentMethod.class);
        if (mPaymentMethod.getId() != null) {
            // ImageView pmImage = (ImageView) findViewById(com.mercadopago.R.id.pmImage);
            defaults.setImageResource(MercadoPagoUtil.getPaymentMethodIcon(this, mPaymentMethod.getId()));
        }
        mMerchantPublicKey = this.getIntent().getStringExtra("merchantPublicKey");
        mMercadoPago = new MercadoPago.Builder()
                .setContext(this)
                .setPublicKey(mMerchantPublicKey)
                .build();
        getIdentificationTypesAsync();
        // cardIntent.putExtra("type", type);
        type = getIntent().getStringExtra("type");

        deviceCode=getIntent().getStringExtra("deviceCode");

        moneyId = getIntent().getStringExtra("moneyId");
    }

    private void getIdentificationTypesAsync() {

        LayoutUtil.showProgressLayout(AddCreditCardActivity.this);

        ErrorHandlingCallAdapter.MyCall<List<IdentificationType>> call = mMercadoPago.getIdentificationTypes();
        call.enqueue(new ErrorHandlingCallAdapter.MyCallback<List<IdentificationType>>() {
            @Override
            public void success(Response<List<IdentificationType>> response) {

                mIdentificationType.setAdapter(new IdentificationTypesAdapter(AddCreditCardActivity.this, response.body()));


                String s = new Gson().toJson(response);
                Log.e("我要的数据", s);
                // Set form "Go" button
                //   setFormGoButton(mIdentificationNumber);

                LayoutUtil.showRegularLayout(AddCreditCardActivity.this);
            }

            @Override
            public void failure(ApiException apiException) {

                if (apiException.getStatus() == 404) {

                    // No identification type for this country
                    mIdentificationLayout.setVisibility(View.GONE);

                    // Set form "Go" button
                    //  setFormGoButton(mCardHolderName);

                    //  LayoutUtil.showRegularLayout(mActivity);

                } else {

                    //mExceptionOnMethod = "getIdentificationTypesAsync";
                    //ApiUtil.finishWithApiException(mActivity, apiException);
                }
            }
        });
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        inputClose(sv, AddCreditCardActivity.this);
        return false;
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.cardholder:
                Log.e("code", "code");
                usernameHides(hasFocus, v.getId());
                break;
            case R.id.cardnumbers:
                usernameHides(hasFocus, v.getId());
                break;
            case R.id.expiration:
                usernameHides(hasFocus, v.getId());
                break;
            case R.id.securitycodes:
                usernameHides(hasFocus, v.getId());
                break;
            case R.id.identificationNumber:
                usernameHides(hasFocus, v.getId());
                break;

        }
    }

    private void usernameHides(boolean hasFocus, int id) {
        if (hasFocus) {
            // 此处为得到焦点时的处理内容
            switch (id) {
                case R.id.cardholder:
                    Log.e("code", "code");
                    passwordview.setBackgroundColor(Color.parseColor("#64B15E"));
                    break;
                case R.id.cardnumbers:
                    cardnumberview.setBackgroundColor(Color.parseColor("#64B15E"));
                    break;
                case R.id.expiration:
                    expirationview.setBackgroundColor(Color.parseColor("#64B15E"));
                    break;
                case R.id.securitycodes:
                    securitycodeview.setBackgroundColor(Color.parseColor("#64B15E"));
                    break;
                case R.id.identificationNumber:
                    code.setBackgroundColor(Color.parseColor("#64B15E"));
                    break;
            }


        } else {
            switch (id) {
                case R.id.cardholder:
                    passwordview.setBackgroundColor(Color.parseColor("#D3DFEF"));
                    break;
                case R.id.cardnumbers:
                    cardnumberview.setBackgroundColor(Color.parseColor("#D3DFEF"));
                    break;
                case R.id.expiration:
                    expirationview.setBackgroundColor(Color.parseColor("#D3DFEF"));
                    break;
                case R.id.securitycodes:
                    securitycodeview.setBackgroundColor(Color.parseColor("#D3DFEF"));
                    break;
                case R.id.identificationNumber:
                    code.setBackgroundColor(Color.parseColor("#D3DFEF"));
                    break;

            }
        }
    }


    @Override
    public void showDialog(String context, boolean type) {
        shows(context, type);
    }

    public void shows(String content, boolean type) {
        if (!isLiving(AddCreditCardActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(AddCreditCardActivity.this);
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
            loadingDialog = new LoadingDialog(AddCreditCardActivity.this);
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
    public void upData(String cardId,String Msg) {

        EventMeager eventMeager = new EventMeager();
        eventMeager.setupDataCard(true);
        EventBus.getDefault().post(eventMeager);

        if (type != null) {
            if (type.equals("1")){
             //   handler.sendEmptyMessageDelayed(3, 1000);
                presenter.getBorrow(getToken(AddCreditCardActivity.this), "0", "0", deviceCode, cardId);
            }else if (type.equals("2")){
                presenter.recharges(getToken(AddCreditCardActivity.this), moneyId, cardId);
            }else if (type.equals("3")){
                presenter.recharges(getToken(AddCreditCardActivity.this), moneyId, cardId);
            }

        }else {
            showDialog(Msg,true);
        }


    }

    @Override
    public void onErr() {
        handler.sendEmptyMessageDelayed(2, 3000);
    }

    @Override
    public void upInformation(Infos.DataBean list) {

    }

    @Override
    public void upAvatar(String url, String path) {

    }

    @Override
    public void update(int type, String orderIds, String RechargeId) {

        loop = 0;
        orderId = orderIds;
        showLoading();
        paymentId = RechargeId;
        presenter.getResult(getToken(AddCreditCardActivity.this), orderId, RechargeId);

    }

    @Override
    public void Success(String num) {
        dismissLoading();

        Intent intent = new Intent(AddCreditCardActivity.this, LeaseActivity.class);
        intent.putExtra("num", num + "");
        intent.putExtra("orderId", orderId);
        startActivity(intent);

        EventMeager eventMeager = new EventMeager();
        eventMeager.setRentalSuccess(true);
        EventBus.getDefault().post(eventMeager);
      //  finish();
    }




    @Override
    public void rechargeSuccess(int code, String addTime) {
        loop = 0;
        if (type.equals("3")) {
          //  presenter.info("0", "0", getToken(RechargeActivity.this));
            presenter.getBorrow(getToken(AddCreditCardActivity.this), "0", "0", deviceCode, "");

        } else {
            dismissLoading();
            EventMeager eventMeager = new EventMeager();
            eventMeager.setRechargeSuccessful(true);
            EventBus.getDefault().post(eventMeager);
            Intent intent = new Intent(AddCreditCardActivity.this, RechargeSuccessfulActivity.class);
            intent.putExtra("type", "Success");
            intent.putExtra("addTime", addTime);
            intent.putExtra("id", code + "");
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void rechargeFailure() {
        dismissLoading();
        Intent intent = new Intent(AddCreditCardActivity.this, RechargeSuccessfulActivity.class);
        intent.putExtra("type", "0");
        startActivity(intent);
    }


    @Override
    public void getrechargeOrder() {
        if (loop == 20) {
            dismissLoading();
            Intent intent = new Intent(AddCreditCardActivity.this, RechargeSuccessfulActivity.class);
            intent.putExtra("type", "0");
            startActivity(intent);
            return;
        }
        handler.sendEmptyMessageDelayed(5, 3000);
    }


    @Override
    public void rechargeOn(String rechargeId, String dataId) {
        orderId = rechargeId;
        //goPayment( dataId);
        paymentId = dataId;
        presenter.getResults(getToken(AddCreditCardActivity.this), rechargeId, paymentId);
    }


    @Override
    public void getOrder() {
        if (loop == 20) {
            Intent intent = new Intent(AddCreditCardActivity.this, LeaseActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        handler.sendEmptyMessageDelayed(4, 3000);
    }

    @Override
    public void Failure() {
        dismissLoading();
        Intent intent = new Intent(AddCreditCardActivity.this, LeaseActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.confirm, R.id.quhaos,R.id.home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                addCard();
                break;
            case R.id.quhaos:
                if (Utils.isNotFastClick()) {
                    startActivityForResult(new Intent(AddCreditCardActivity.this, PickActivity.class), CODE);
                }
                break;

            case R.id.home:
                EventMeager   eventMeager = new EventMeager();
                eventMeager.setHome(true);
                EventBus.getDefault().post(eventMeager);
                break;
        }

    }

    private void addCard() {
        if (TextUtils.isEmpty(getCardHolder())) {
            showDialog(getResources().getString(R.string.cardholder), true);
            return;
        }
        if (TextUtils.isEmpty(getCardNumbers())) {
            showDialog(getResources().getString(R.string.cardNumberTip), true);
            return;
        }

        if (TextUtils.isEmpty(getExpiration())) {
            showDialog(getResources().getString(R.string.validityTip), true);
            return;
        }


        if (TextUtils.isEmpty(getSecurityCodes())) {
            showDialog(getResources().getString(R.string.securityTip), true);
            return;
        }


        if (TextUtils.isEmpty(phone.getText().toString())) {
            showDialog(getResources().getString(R.string.phoneTleTip), true);
            return;
        }

        if (!email.getText().toString().contains("@")) {
            showDialog(getResources().getString(R.string.incorrect), true);
            return;
        }
        if (TextUtils.isEmpty(email.getText().toString())) {
            showDialog(getResources().getString(R.string.EamilsTip), true);
            return;
        }
        if (TextUtils.isEmpty(mIdentificationNumber.getText().toString())) {
            showDialog(getResources().getString(R.string.identificationNumbers), true);
            return;
        }
        Boolean haveInternet = isOnline();
        String[] split = getExpiration().split("/");
        if (split.length == 2) {
            if (TextUtils.isEmpty(split[0]) | TextUtils.isEmpty(split[1])) {
                showDialog(getResources().getString(R.string.validityTypeTip), true);
                return;
            } else {
                month = split[0];
                year = split[1];
            }
        } else {
            showDialog(getResources().getString(R.string.validityTypeTip), true);
            return;
        }

        if (haveInternet) {
            showLoading();
          /*   Conekta.setPublicKey("key_cP3RnqgtyzJDbeqbPXzeccA");
            Conekta.setApiVersion("0.3.0");
            Conekta.collectDevice(activity);
            Card card = new Card(getCardHolder(), getCardNumbers(), getSecurityCodes(), month, year);
            Token token = new Token(activity);

            //Listen when token is returned
            token.onCreateTokenListener(new Token.CreateToken() {

                @Override
                public void onCreateTokenReady(JSONObject data) {

                    try {
                        Log.e("The token::::", data.toString());
                        Log.e("The token::::", data.getString("id"));
                        String id = data.getString("id");
                        presenter.addCardList(getToken(AddCreditCardActivity.this), id, getCardHolder());
                    } catch (Exception err) {
                        //outputView.setText("Error: " + err.toString());
                        showDialog(err.toString() + "", false);
                    }
                    //uuidDevice.setText("Uuid device: " + Conekta.deviceFingerPrint(activity));
                }
            });
*/
            //Request for create token
            //  token.create(card);
            try {
                months = Integer.parseInt(month);
                years = Integer.parseInt(year);
            } catch (Exception e) {

            }
            if (months == 0) {
                showDialog(getResources().getString(R.string.validityTypeTip), true);
                return;
            }
            if (years == 0) {
                showDialog(getResources().getString(R.string.validityTypeTip), true);
                return;
            }


            mCardToken = new CardToken(getCardNumbers(), months, years, getSecurityCodes(), getCardHolder(),
                    getIdentificationTypeId(getIdentificationType()), getIdentificationNumber());
            Log.e("我要的数据", new Gson().toJson(mCardToken));
            // if (validateForm(mCardToken)) {

            // Create token]

            new Thread() {
                @Override
                public void run() {

                    createTokenAsync();
                }
            }.start();
            // }
        } else {
            //  outputView.setText("You don't have internet");
            showDialog(getResources().getString(R.string.noInternet), false);
        }
    }

    protected void validateCardNumber(CardToken cardToken) throws Exception {

        cardToken.validateCardNumber(this, mPaymentMethod);
    }

    private void createTokenAsync() {

        //   LayoutUtil.showProgressLayout(mActivity);
        //   mMercadoPago.createToken();
        ErrorHandlingCallAdapter.MyCall<Token> call = mMercadoPago.createToken(mCardToken);
        call.enqueue(new ErrorHandlingCallAdapter.MyCallback<Token>() {
            @Override
            public void success(Response<Token> response) {

                //  Intent returnIntent = new Intent();
                // returnIntent.putExtra("token", response.body().getId());
                Log.e("token", response.body().getId());

                token = response.body().getId();
                presenter.addCardList(getToken(AddCreditCardActivity.this), token, email.getText().toString(),
                        quhaos.getText().toString().substring(1), phone.getText().toString());


            }

            @Override
            public void failure(ApiException apiException) {


                //     mExceptionOnMethod = "createTokenAsync";
                //    ApiUtil.finishWithApiException(mActivity, apiException);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoading();

                        if (apiException != null) {
                            showDialog(apiException.getMessage() + "", false);
                        } else {
                            showDialog(getResources().getString(R.string.addfailed), false);
                        }

                    }
                });

            }
        });
    }

    private IdentificationType getIdentificationType() {

        return (IdentificationType) mIdentificationType.getSelectedItem();
    }

    private String getIdentificationTypeId(IdentificationType identificationType) {

        if (identificationType != null) {
            return identificationType.getId();
        } else {
            return null;
        }
    }

    private String getCardHolder() {
        return cardholder.getText().toString();
    }

    private String getCardNumbers() {
        return cardnumbers.getText().toString();
    }

    private String getExpiration() {
        return expiration.getText().toString();
    }

    private String getSecurityCodes() {
        return securitycodes.getText().toString();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    //maestro验证
    public static boolean maestro(String strEmail) {
        String strPattern = "^5[1-5][0-9]{5,}|222[1-9][0-9]{3,}|22[3-9][0-9]{4,}|2[3-6][0-9]{5,}|27[01][0-9]{4,}|2720[0-9]{3,}$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }

    //visa验证
    public static boolean visa(String strEmail) {
        String strPattern = "^4[0-9]{6,}$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }


    private String getIdentificationNumber() {

        if (!this.mIdentificationNumber.getText().toString().equals("")) {
            return this.mIdentificationNumber.getText().toString();
        } else {
            return null;
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
}
