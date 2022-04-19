package com.zheng.liuju.mercadopago.step2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mercadopago.adapters.CustomerCardsAdapter;
import com.mercadopago.adapters.ErrorHandlingCallAdapter;
import com.mercadopago.core.MercadoPago;
import com.mercadopago.core.MerchantServer;
import com.mercadopago.model.ApiException;
import com.mercadopago.model.Card;
import com.mercadopago.model.CardToken;
import com.mercadopago.model.Cardholder;
import com.mercadopago.model.Customer;
import com.mercadopago.model.Identification;
import com.mercadopago.model.Issuer;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.PaymentMethodRow;
import com.mercadopago.model.SavedCardToken;
import com.mercadopago.model.SecurityCode;
import com.mercadopago.model.Token;
import com.mercadopago.util.ApiUtil;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;
import com.mercadopago.util.MercadoPagoUtil;
import com.zheng.liuju.R;
import com.zheng.liuju.bean.CustomerBean;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Response;

public class SimpleVaultActivity extends AppCompatActivity {
    // Activity parameters
    protected String mMerchantAccessToken;
    protected String mMerchantBaseUrl;
    protected String mMerchantGetCustomerUri;
    protected String mMerchantPublicKey;

    // Input controls
    protected View mSecurityCodeCard;
    protected EditText mSecurityCodeText;
    protected FrameLayout mCustomerMethodsLayout;
    protected TextView mCustomerMethodsText;
    protected ImageView mCVVImage;
    protected TextView mCVVDescriptor;
    protected Button mSubmitButton;

    // Current values
    protected List<Card> mCards;
    protected CardToken mCardToken;
    protected PaymentMethodRow mSelectedPaymentMethodRow;
    protected PaymentMethod mSelectedPaymentMethod;
    protected PaymentMethod mTempPaymentMethod;

    // Local vars
    protected Activity mActivity;
    protected String mExceptionOnMethod;
    protected MercadoPago mMercadoPago;
    protected List<String> mSupportedPaymentTypes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_vault);

        mMerchantPublicKey = this.getIntent().getStringExtra("merchantPublicKey");
        mMerchantBaseUrl = this.getIntent().getStringExtra("merchantBaseUrl");
        mMerchantGetCustomerUri = this.getIntent().getStringExtra("merchantGetCustomerUri");
        mMerchantAccessToken = this.getIntent().getStringExtra("merchantAccessToken");
        if (this.getIntent().getStringExtra("supportedPaymentTypes") != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<String>>(){}.getType();
            mSupportedPaymentTypes = gson.fromJson(this.getIntent().getStringExtra("supportedPaymentTypes"), listType);
        }

        if ((mMerchantPublicKey != null) && (!mMerchantPublicKey.equals(""))) {

            // Set activity
            mActivity = this;
            mActivity.setTitle(getString(R.string.mpsdk_title_activity_vault));

            // Set layout controls
            mSecurityCodeCard = findViewById(R.id.securityCodeCard);
            mCVVImage = (ImageView) findViewById(R.id.cVVImage);
            mCVVDescriptor = (TextView) findViewById(R.id.cVVDescriptor);
            mSubmitButton = (Button) findViewById(R.id.submitButton);
            mCustomerMethodsLayout = (FrameLayout) findViewById(R.id.customerMethodLayout);
            mCustomerMethodsText = (TextView) findViewById(R.id.customerMethodLabel);
            mSecurityCodeText = (EditText) findViewById(R.id.securityCode);

            // Init controls visibility
            mSecurityCodeCard.setVisibility(View.GONE);

            // Init MercadoPago object with public key
            mMercadoPago = new MercadoPago.Builder()
                    .setContext(mActivity)
                    .setPublicKey(mMerchantPublicKey)
                    .build();

            // Set customer method first value
            mCustomerMethodsText.setText(getString(com.mercadopago.R.string.mpsdk_select_pm_label));

            // Set "Go" button
            setFormGoButton(mSecurityCodeText);

            // Hide main layout and go for customer's cards
            if ((mMerchantBaseUrl != null) && (!mMerchantBaseUrl.equals("") && (mMerchantGetCustomerUri != null) && (!mMerchantGetCustomerUri.equals("")))) {
                try {
                    getCustomerCardsAsync();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
            returnIntent.putExtra("message", "Invalid parameters");
         //   finish();
        }
    }

    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("backButtonPressed", true);
        setResult(RESULT_CANCELED, returnIntent);
     //   finish();
    }

    public void refreshLayout(View view) throws ParseException {

        // Retry method call
        if (mExceptionOnMethod.equals("getCustomerCardsAsync")) {
            getCustomerCardsAsync();
        } else if (mExceptionOnMethod.equals("getCreateTokenCallback")) {
            if (mSelectedPaymentMethodRow != null) {
                createSavedCardToken();
            } else if (mCardToken != null) {
                createNewCardToken();
            }
        }
    }

    public void onCustomerMethodsClick(View view) {

        if ((mCards != null) && (mCards.size() > 0)) {  // customer cards activity

            new MercadoPago.StartActivityBuilder()
                    .setActivity(mActivity)
                    .setCards(mCards)
                    .startCustomerCardsActivity();

        } else {  // payment method activity

            new MercadoPago.StartActivityBuilder()
                    .setActivity(mActivity)
                    .setPublicKey(mMerchantPublicKey)
                    .setSupportedPaymentTypes(mSupportedPaymentTypes)
                    .startPaymentMethodsActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MercadoPago.CUSTOMER_CARDS_REQUEST_CODE) {

            resolveCustomerCardsRequest(resultCode, data);

        } else if (requestCode == MercadoPago.PAYMENT_METHODS_REQUEST_CODE) {

            resolvePaymentMethodsRequest(resultCode, data);

        } else if (requestCode == MercadoPago.NEW_CARD_REQUEST_CODE) {

            resolveNewCardRequest(resultCode, data);
        }
    }

    protected void setContentView() {

        setContentView(R.layout.activity_simple_vault);
    }

    protected void resolveCustomerCardsRequest(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            PaymentMethodRow selectedPaymentMethodRow = JsonUtil.getInstance().fromJson(data.getStringExtra("paymentMethodRow"), PaymentMethodRow.class);

            if (selectedPaymentMethodRow.getCard() != null) {

                // Set selection status
                mCardToken = null;
                mSelectedPaymentMethodRow = selectedPaymentMethodRow;
                mSelectedPaymentMethod = null;
                mTempPaymentMethod = null;

                // Set customer method selection
                setCustomerMethodSelection();

            } else {

                startPaymentMethodsActivity();
            }
        } else {

            if ((data != null) && (data.getStringExtra("apiException") != null)) {
                finishWithApiException(data);
            }
        }
    }

    protected void resolvePaymentMethodsRequest(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            mTempPaymentMethod = JsonUtil.getInstance().fromJson(data.getStringExtra("paymentMethod"), PaymentMethod.class);

            // Call new card activity
            startNewCardActivity();

        } else {

            if ((data != null) && (data.getStringExtra("apiException") != null)) {
                finishWithApiException(data);
            } else if ((mSelectedPaymentMethodRow == null) && (mCardToken == null)) {
                // if nothing is selected
                finish();
            }
        }
    }

    protected void resolveNewCardRequest(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            // Set selection status
            mCardToken = JsonUtil.getInstance().fromJson(data.getStringExtra("cardToken"), CardToken.class);
            mSelectedPaymentMethodRow = null;
            mSelectedPaymentMethod = mTempPaymentMethod;

            // Set customer method selection
            mCustomerMethodsText.setText(CustomerCardsAdapter.getPaymentMethodLabel(mActivity, mSelectedPaymentMethod.getName(),
                    mCardToken.getCardNumber().substring(mCardToken.getCardNumber().length() - 4, mCardToken.getCardNumber().length())));
            mCustomerMethodsText.setCompoundDrawablesWithIntrinsicBounds(MercadoPagoUtil.getPaymentMethodIcon(mActivity, mSelectedPaymentMethod.getId()), 0, 0, 0);

            // Set security card visibility
            showSecurityCodeCard(mSelectedPaymentMethod);

            // Set button visibility
            mSubmitButton.setEnabled(true);

        } else {

            if (data != null) {
                if (data.getStringExtra("apiException") != null) {

                    finishWithApiException(data);

                } else if (data.getBooleanExtra("backButtonPressed", false)) {

                    startPaymentMethodsActivity();
                }
            }
        }
    }

    protected void getCustomerCardsAsync() throws ParseException {

        LayoutUtil.showProgressLayout(mActivity);
        ErrorHandlingCallAdapter.MyCall<Customer> call = MerchantServer.getCustomer(this, mMerchantBaseUrl, mMerchantGetCustomerUri, mMerchantAccessToken);
        call.enqueue(new ErrorHandlingCallAdapter.MyCallback<Customer>() {
            @Override
            public void success(Response<Customer> response) {

                mCards = response.body().getCards();
                LayoutUtil.showRegularLayout(mActivity);
            }

            @Override
            public void failure(ApiException apiException) {
                Log.e("failure","apiException="+apiException.getMessage());
                mExceptionOnMethod = "getCustomerCardsAsync";
                ApiUtil.finishWithApiException(mActivity, apiException);
            }
        });
        /*     String  json   ="{\n" +
                     "    \"code\": 1,\n" +
                     "    \"msg\": \"success\",\n" +
                     "    \"data\": {\n" +
                     "        \"id\": \"540455292-S1qOZ3RdX8rsOP\",\n" +
                     "        \"email\": \"huzhihong123@gmail.com\",\n" +
                     "        \"firstName\": null,\n" +
                     "        \"lastName\": null,\n" +
                     "        \"phone\": {\n" +
                     "            \"areaCode\": \"57\",\n" +
                     "            \"number\": \"3115424071\"\n" +
                     "        },\n" +
                     "        \"identification\": {\n" +
                     "            \"type\": \"DNI\",\n" +
                     "            \"number\": \"694852609962\"\n" +
                     "        },\n" +
                     "        \"defaultAddress\": null,\n" +
                     "        \"address\": {\n" +
                     "            \"id\": null,\n" +
                     "            \"zipCode\": null,\n" +
                     "            \"streetName\": null,\n" +
                     "            \"streetNumber\": null\n" +
                     "        },\n" +
                     "        \"dateRegistered\": null,\n" +
                     "        \"description\": null,\n" +
                     "        \"dateCreated\": \"2020-04-07 12:00:00\",\n" +
                     "        \"dateLastUpdated\": \"2020-04-07 12:00:00\",\n" +
                     "        \"metadata\": null,\n" +
                     "        \"defaultCard\": \"1586249540353\",\n" +
                     "        \"cards\": [\n" +
                     "            {\n" +
                     "                \"idempotenceKey\": null,\n" +
                     "                \"lastApiResponse\": null,\n" +
                     "                \"marketplaceAccessToken\": null,\n" +
                     "                \"id\": \"1586249540353\",\n" +
                     "                \"customerId\": \"540455292-S1qOZ3RdX8rsOP\",\n" +
                     "                \"expirationMonth\": 11,\n" +
                     "                \"expirationYear\": 2025,\n" +
                     "                \"firstSixDigits\": \"503175\",\n" +
                     "                \"lastFourDigits\": \"0604\",\n" +
                     "                \"paymentMethod\": {\n" +
                     "                    \"id\": \"master\",\n" +
                     "                    \"name\": \"master\",\n" +
                     "                    \"paymentTypeId\": \"credit_card\",\n" +
                     "                    \"thumbnail\": \"http://img.mlstatic.com/org-img/MP3/API/logos/master.gif\",\n" +
                     "                    \"secureThumbnail\": \"https://www.mercadopago.com/org-img/MP3/API/logos/master.gif\"\n" +
                     "                },\n" +
                     "                \"securityCode\": {\n" +
                     "                    \"length\": 3,\n" +
                     "                    \"cardLocation\": \"back\"\n" +
                     "                },\n" +
                     "                \"issuer\": {\n" +
                     "                    \"id\": \"204\",\n" +
                     "                    \"name\": \"Mastercard\"\n" +
                     "                },\n" +
                     "                \"cardholder\": {\n" +
                     "                    \"name\": \"APRO HAO\",\n" +
                     "                    \"identification\": {\n" +
                     "                        \"number\": \"58464656655\",\n" +
                     "                        \"subtype\": null,\n" +
                     "                        \"type\": \"CC\"\n" +
                     "                    }\n" +
                     "                },\n" +
                     "                \"dateCreated\": \"2020-04-07 16:51:16\",\n" +
                     "                \"dateLastUpdated\": \"2020-04-07 16:51:16\",\n" +
                     "                \"paymentMethodId\": null\n" +
                     "            }\n" +
                     "        ],\n" +
                     "        \"addresses\": [],\n" +
                     "        \"liveMode\": null\n" +
                     "    },\n" +
                     "    \"total\": 1\n" +
                   "}";
        CustomerBean customerBean = new Gson().fromJson(json, CustomerBean.class);
        List<CustomerBean.DataBean.CardsBean> cards = customerBean.getData().getCards();
     //   Card
        mCards=new ArrayList<>();*/




    /*    for (int i = 0; i <cards.size() ; i++) {
            Card card = new Card();
         //   card.setCardHolder(cards.get(i).getCardholder());
            Cardholder  cardholder = new Cardholder();
            cardholder.setName(cards.get(i).getCardholder().getName());
            Identification  identification = new Identification();
            identification.setNumber(cards.get(i).getCardholder().getIdentification().getNumber());
            identification.setType(cards.get(i).getCardholder().getIdentification().getType());
            cardholder.setIdentification(identification);
            card.setCardHolder(cardholder);
            card.setCustomerId(cards.get(i).getCustomerId());


            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            Date parse = format.parse(cards.get(i).getDateCreated());
            card.setDateCreated(parse);
            Date parses = format.parse(cards.get(i).getDateLastUpdated());
            card.setDateLastUpdated(parses);

            card.setExpirationMonth(cards.get(i).getExpirationMonth());

            card.setExpirationYear(cards.get(i).getExpirationYear());

            card.setFirstSixDigits(cards.get(i).getFirstSixDigits());

            card.setId(cards.get(i).getId());



           card.setLastFourDigits(cards.get(i).getLastFourDigits());

            Issuer  issuer = new Issuer();
            issuer.setId(Long.parseLong(cards.get(i).getIssuer().getId()) );
            issuer.setName(cards.get(i).getIssuer().getName());
            card.setIssuer(issuer);

            PaymentMethod  paymentMethod  = new PaymentMethod();
            paymentMethod.setId(cards.get(0).getPaymentMethod().getId());
            paymentMethod.setName(cards.get(0).getPaymentMethod().getName());
            paymentMethod.setPaymentTypeId(cards.get(0).getPaymentMethod().getPaymentTypeId());

           // paymentMethod.setAdditionalInfoNeeded();
            card.setPaymentMethod(paymentMethod);


            SecurityCode  securityCode  = new SecurityCode();
            securityCode.setCardLocation(cards.get(0).getSecurityCode().getCardLocation());
            securityCode.setLength(cards.get(0).getSecurityCode().getLength());

            card.setSecurityCode(securityCode);
            mCards.add(card);*/
      //  }

    }

    protected void showSecurityCodeCard(PaymentMethod paymentMethod) {

        if (paymentMethod != null) {

            if (isSecurityCodeRequired()) {

                if ("amex".equals(paymentMethod.getId())) {
                    mCVVDescriptor.setText(String.format(getString(com.mercadopago.R.string.mpsdk_cod_seg_desc_amex), 4));
                } else {
                    mCVVDescriptor.setText(String.format(getString(com.mercadopago.R.string.mpsdk_cod_seg_desc), 3));
                }

                int res = MercadoPagoUtil.getPaymentMethodImage(mActivity, paymentMethod.getId());
                if (res != 0) {
                    mCVVImage.setImageDrawable(getResources().getDrawable(res));
                }

                mSecurityCodeCard.setVisibility(View.VISIBLE);

            } else {

                mSecurityCodeCard.setVisibility(View.GONE);
            }
        }
    }

    protected String getSelectedPMBin() {

        if (mSelectedPaymentMethodRow != null) {
            return mSelectedPaymentMethodRow.getCard().getFirstSixDigits();
        } else {
            return mCardToken.getCardNumber().substring(0, MercadoPago.BIN_LENGTH);
        }
    }

    private boolean isSecurityCodeRequired() {

        if (mSelectedPaymentMethodRow != null) {
            return mSelectedPaymentMethodRow.getCard().isSecurityCodeRequired();
        } else {
            return mSelectedPaymentMethod.isSecurityCodeRequired(getSelectedPMBin());
        }
    }

    private void setFormGoButton(final EditText editText) {

        editText.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    submitForm(v);
                }
                return false;
            }
        });
    }

    protected void setCustomerMethodSelection() {

        // Set payment method
        mSelectedPaymentMethod = mSelectedPaymentMethodRow.getCard().getPaymentMethod();

        // Set customer method selection
        mCustomerMethodsText.setText(mSelectedPaymentMethodRow.getLabel());
        mCustomerMethodsText.setCompoundDrawablesWithIntrinsicBounds(mSelectedPaymentMethodRow.getIcon(), 0, 0, 0);

        // Set security card visibility
        showSecurityCodeCard(mSelectedPaymentMethodRow.getCard().getPaymentMethod());

        // Set button visibility
        mSubmitButton.setEnabled(true);
    }

    public void submitForm(View view) {

        LayoutUtil.hideKeyboard(mActivity);

        // Create token
        if (mSelectedPaymentMethodRow != null) {

            createSavedCardToken();

        } else if (mCardToken != null) {

            createNewCardToken();
        }
    }

    protected void createNewCardToken() {

        // Validate CVV
        try {
            mCardToken.setSecurityCode(mSecurityCodeText.getText().toString());
            mCardToken.validateSecurityCode(this, mSelectedPaymentMethod);
            mSecurityCodeText.setError(null);
        }
        catch (Exception ex) {
            mSecurityCodeText.setError(ex.getMessage());
            mSecurityCodeText.requestFocus();
            return;
        }

        // Create token
        LayoutUtil.showProgressLayout(mActivity);
        ErrorHandlingCallAdapter.MyCall<Token> call = mMercadoPago.createToken(mCardToken);
        call.enqueue(getCreateTokenCallback());
    }

    protected void createSavedCardToken() {

        SavedCardToken savedCardToken = new SavedCardToken(mSelectedPaymentMethodRow.getCard().getId(), mSecurityCodeText.getText().toString());

        // Validate CVV
        try {
            savedCardToken.validateSecurityCode(this, mSelectedPaymentMethodRow.getCard());
            mSecurityCodeText.setError(null);
        }
        catch (Exception ex) {
            mSecurityCodeText.setError(ex.getMessage());
            mSecurityCodeText.requestFocus();
            return;
        }

        // Create token
        LayoutUtil.showProgressLayout(mActivity);
        ErrorHandlingCallAdapter.MyCall<Token> call = mMercadoPago.createToken(savedCardToken);
        call.enqueue(getCreateTokenCallback());
    }

    protected ErrorHandlingCallAdapter.MyCallback<Token> getCreateTokenCallback() {

        return new ErrorHandlingCallAdapter.MyCallback<Token>() {
            @Override
            public void success(Response<Token> response) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("token", response.body().getId());
                returnIntent.putExtra("paymentMethod", JsonUtil.getInstance().toJson(mSelectedPaymentMethod));
                setResult(RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void failure(ApiException apiException) {

                mExceptionOnMethod = "getCreateTokenCallback";
                ApiUtil.finishWithApiException(mActivity, apiException);
            }
        };
    }

    protected void finishWithApiException(Intent data) {

        setResult(RESULT_CANCELED, data);
     //   finish();
    }

    protected void startNewCardActivity() {

        new MercadoPago.StartActivityBuilder()
                .setActivity(mActivity)
                .setPublicKey(mMerchantPublicKey)
                .setPaymentMethod(mTempPaymentMethod)
                .setRequireSecurityCode(false)
                .startNewCardActivity();
    }

    protected void startPaymentMethodsActivity() {

        new MercadoPago.StartActivityBuilder()
                .setActivity(mActivity)
                .setPublicKey(mMerchantPublicKey)
                .setSupportedPaymentTypes(mSupportedPaymentTypes)
                .startPaymentMethodsActivity();
    }

}
