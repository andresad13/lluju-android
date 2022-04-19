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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gyf.immersionbar.ImmersionBar;
import com.mercadopago.core.MercadoPago;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.util.JsonUtil;
import com.zheng.liuju.R;
import com.zheng.liuju.adapter.CreditCardListAdapter;
import com.zheng.liuju.api.ICardList;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.CardList;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.mercadopago.utils.ExamplesUtils;
import com.zheng.liuju.presenter.CardListPresenter;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.view.DialogUtils;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreditCardListActivity extends BaseActivity implements ICardList, DialogUtils.OnButtonClickListener {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.backs)
    LinearLayout backs;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.pulllist)
    ListView pulllist;

    @BindView(R.id.add)
    RelativeLayout add;
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.nodata)
    RelativeLayout nodata;
    @BindView(R.id.delctTip)
    TextView delctTip;
    @BindView(R.id.home)
    DragImageView home;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    private ArrayList<CardList.DataBean.CardsBean> mlist = new ArrayList<>();
    private CardListPresenter presenter;
    private PromptDialog promptDialog;
    private boolean init;  //初始化或者删除卡的标记
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
                SPUtils.putString(CreditCardListActivity.this, "token", "");
                startActivity(new Intent(CreditCardListActivity.this, LoginActivity.class));

                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            }


        }
    };
    private LoadingDialog loadingDialog;
    private String cardIds;
    private DialogUtils dialogUtils;
    private AlertDialog dialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        presenter = new CardListPresenter();
        presenter.attachView(this);
        pulllist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //   Intent intent = new Intent(CreditCardListActivity.this, CreditCardDetailsActivity.class);
               /* CardList.DataBean dataBean = mlist.get(position);
                String cardNumberLast4 = dataBean.getLast_4_digits();
                //  String   expirationDate=dataBean.getCardExpMonth()+"/"+dataBean.getCardExpYear();
                intent.putExtra("cardNumberLast", cardNumberLast4);
                // intent.putExtra("expirationDate",expirationDate);
                intent.putExtra("cardType", dataBean.getBank());
                intent.putExtra("id", dataBean.getId());*/
                //    startActivity(intent);
            }
        });
        String openId = SPUtils.getString(CreditCardListActivity.this, "openId", "");
        presenter.getCardList(getToken(CreditCardListActivity.this), openId);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    public void deleteCard(String cardId) {
        cardIds = cardId;
        //      presenter.deleteCardList(getToken(CreditCardListActivity.this),cardId);

        showDelete();

    }

    private void showDelete() {
        if (dialogUtils == null) {
            dialogUtils = new DialogUtils();
        }
        dialogUtils.showDeleteCreditCard(CreditCardListActivity.this, "");
        dialogUtils.setOnButtonClickListener(this);

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
        if (dialogs != null) {
            dialogs.dismiss();
        }
        System.gc();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            finish();


        } else if (message.getupDataCard()) {
            String openId = SPUtils.getString(CreditCardListActivity.this, "openId", "");
            presenter.getCardList(getToken(CreditCardListActivity.this), openId);
        } else if (message.isHome()){
            finish();
        }
    }

    @OnClick({R.id.backs, R.id.add,R.id.home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backs:
                finish();
                break;
            case R.id.add:
                //  startActivity(new Intent(CreditCardListActivity.this, AddCreditCardActivity.class));

                new MercadoPago.StartActivityBuilder()
                        .setActivity(this)
                        .setPublicKey(ExamplesUtils.DUMMY_MERCHANT_PUBLIC_KEY)
                        .setSupportedPaymentTypes(mSupportedPaymentTypes)
                        .startPaymentMethodsActivity();
                break;

            case R.id.home:
                EventMeager eventMeager = new EventMeager();

                eventMeager.setHome(true);

                EventBus.getDefault().post(eventMeager);

                break;

        }

    }


    @Override
    public void showDialog(String context, boolean type) {
        shows(context, type);
    }

    public void shows(String content, boolean type) {
        if (!isLiving(CreditCardListActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(CreditCardListActivity.this);
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
            loadingDialog = new LoadingDialog(CreditCardListActivity.this);
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
    public void upInformation(List<CardList.DataBean.CardsBean> list) {
      /*  if (!init){

            init=true;
        }else {
            showDialog(getResources().getString(R.string.successfullyDeleted),true);
        }*/
        mlist.clear();
        if (list == null) {
            pulllist.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
            tip.setText(getResources().getString(R.string.nocard));
        } else if (list.size() == 0) {
            //showDialog(getResources().getString(R.string.acquisitionFailed),false);
            pulllist.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
            tip.setText(getResources().getString(R.string.nocard));
        } else {
            String tips = "<font color='#FF0000'>" + "* </font>" + getResources().getString(R.string.delectTip);
            delctTip.setText(Html.fromHtml(tips));
            mlist.addAll(list);
            pulllist.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
            CreditCardListAdapter creditCardListAdapter = new CreditCardListAdapter(CreditCardListActivity.this, list);
            pulllist.setAdapter(creditCardListAdapter);
        }

    }

    @Override
    public void refresh() {
        String openId = SPUtils.getString(CreditCardListActivity.this, "openId", "");
        presenter.getCardList(getToken(CreditCardListActivity.this), openId);
    }

    @Override
    public void upAvatar(String url, String path) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MercadoPago.PAYMENT_METHODS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.e("apiException", "RESULT_OK");
                // Set payment method
                PaymentMethod paymentMethod = JsonUtil.getInstance().fromJson(data.getStringExtra("paymentMethod"), PaymentMethod.class);

                // Call new card activity
                ExamplesUtils.startCardActivity(this, ExamplesUtils.DUMMY_MERCHANT_PUBLIC_KEY, paymentMethod, null,null,null);
            } else {

                if ((data != null) && (data.getStringExtra("apiException") != null)) {
                    Toast.makeText(getApplicationContext(), data.getStringExtra("apiException"), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onPositiveButtonClick(int type, String context, AlertDialog dialog) {
        dialogs = dialog;
        dialogs.dismiss();
        presenter.deleteCardList(getToken(CreditCardListActivity.this), cardIds);
    }
}
