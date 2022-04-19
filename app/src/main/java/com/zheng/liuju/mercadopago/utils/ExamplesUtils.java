package com.zheng.liuju.mercadopago.utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mercadopago.adapters.ErrorHandlingCallAdapter;
import com.mercadopago.core.MercadoPago;
import com.mercadopago.core.MerchantServer;

import com.mercadopago.model.ApiException;
import com.mercadopago.model.Discount;
import com.mercadopago.model.Item;
import com.mercadopago.model.MerchantPayment;
import com.mercadopago.model.Payment;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;
import com.zheng.liuju.activity.AddCreditCardActivity;
import com.zheng.liuju.mercadopago.step1.CardActivity;
import com.zheng.liuju.mercadopago.step2.SimpleVaultActivity;
import com.zheng.liuju.mercadopago.step3.AdvancedVaultActivity;
import com.zheng.liuju.mercadopago.step4.FinalVaultActivity;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

import retrofit2.Response;

public class ExamplesUtils {

    public static final int SIMPLE_VAULT_REQUEST_CODE = 10;
    public static final int ADVANCED_VAULT_REQUEST_CODE = 11;
    public static final int FINAL_VAULT_REQUEST_CODE = 12;
    public static final int CARD_REQUEST_CODE = 13;


    // * Merchant public key
    //APP_USR-1701a76a-89e1-40ad-9da6-a5ac6ef4e90e
    //TEST-0cec2e72-de6a-43e6-b414-c238b553eba9
    public static final String DUMMY_MERCHANT_PUBLIC_KEY = "APP_USR-1701a76a-89e1-40ad-9da6-a5ac6ef4e90e";//APP_USR-1701a76a-89e1-40ad-9da6-a5ac6ef4e90e
    // DUMMY_MERCHANT_PUBLIC_KEY_AR = "444a9ef5-8a6b-429f-abdf-587639155d88";
    // DUMMY_MERCHANT_PUBLIC_KEY_BR = "APP_USR-f163b2d7-7462-4e7b-9bd5-9eae4a7f99c3";
    // DUMMY_MERCHANT_PUBLIC_KEY_MX = "6c0d81bc-99c1-4de8-9976-c8d1d62cd4f2";
    // DUMMY_MERCHANT_PUBLIC_KEY_VZ = "2b66598b-8b0f-4588-bd2f-c80ca21c6d18";
    // DUMMY_MERCHANT_PUBLIC_KEY_CO = "aa371283-ad00-4d5d-af5d-ed9f58e139f1";

    // * Merchant server vars
    public static final String DUMMY_MERCHANT_BASE_URL = "http://192.168.2.58:8098";   /// //https://api.mercadopago.com
    public static final String DUMMY_MERCHANT_GET_CUSTOMER_URI = "/pay/mercadopago/getCustomer/96615382157372";  ///
    public static final String DUMMY_MERCHANT_CREATE_PAYMENT_URI = "/checkout/examples/doPayment";
    //public static final String DUMMY_MERCHANT_GET_DISCOUNT_URI = "/checkout/examples/getDiscounts";

    // * Merchant access token
    public static final String DUMMY_MERCHANT_ACCESS_TOKEN = "TEST-3580782637306831-010801-cc828385510ca23a644a4272b92ae083-502190411";//
    // DUMMY_MERCHANT_ACCESS_TOKEN_AR = "mla-cards-data";
    // DUMMY_MERCHANT_ACCESS_TOKEN_BR = "mlb-cards-data";
    // DUMMY_MERCHANT_ACCESS_TOKEN_MX = "mlm-cards-data";
    // DUMMY_MERCHANT_ACCESS_TOKEN_VZ = "mlv-cards-data";
    // DUMMY_MERCHANT_ACCESS_TOKEN_VZ = "mco-cards-data";
    // DUMMY_MERCHANT_ACCESS_TOKEN_NO_CCV = "mla-cards-data-tarshop";

    // * Payment item
    public static final String DUMMY_ITEM_ID = "id1";
    public static final Integer DUMMY_ITEM_QUANTITY = 1;
    public static final BigDecimal DUMMY_ITEM_UNIT_PRICE = new BigDecimal("100");

    public static void startCardActivity(Activity activity, String merchantPublicKey, PaymentMethod paymentMethod,String type,String deviceCode,String moneyId) {

        Intent cardIntent = new Intent(activity, AddCreditCardActivity.class);   //CardActivity
        cardIntent.putExtra("merchantPublicKey", merchantPublicKey);
        cardIntent.putExtra("paymentMethod", JsonUtil.getInstance().toJson(paymentMethod));
        cardIntent.putExtra("type", type);
        cardIntent.putExtra("deviceCode",deviceCode);
        cardIntent.putExtra("moneyId",moneyId);
        activity.startActivityForResult(cardIntent, CARD_REQUEST_CODE);
    }

    public static void startSimpleVaultActivity(Activity activity, String merchantPublicKey, String merchantBaseUrl, String merchantGetCustomerUri, String merchantAccessToken, List<String> supportedPaymentTypes) {

    /*    Intent simpleVaultIntent = new Intent(activity, SimpleVaultActivity.class);
        simpleVaultIntent.putExtra("merchantPublicKey", merchantPublicKey);
        simpleVaultIntent.putExtra("merchantBaseUrl", merchantBaseUrl);
        simpleVaultIntent.putExtra("merchantGetCustomerUri", merchantGetCustomerUri);
        simpleVaultIntent.putExtra("merchantAccessToken", merchantAccessToken);
        putListExtra(simpleVaultIntent, "supportedPaymentTypes", supportedPaymentTypes);
        activity.startActivityForResult(simpleVaultIntent, SIMPLE_VAULT_REQUEST_CODE);*/


        Intent simpleVaultIntent = new Intent(activity, SimpleVaultActivity.class);
        simpleVaultIntent.putExtra("merchantPublicKey", merchantPublicKey);
        simpleVaultIntent.putExtra("merchantBaseUrl", merchantBaseUrl);
        simpleVaultIntent.putExtra("merchantGetCustomerUri", merchantGetCustomerUri);
        simpleVaultIntent.putExtra("merchantAccessToken", merchantAccessToken);
        putListExtra(simpleVaultIntent, "supportedPaymentTypes", supportedPaymentTypes);
        activity.startActivityForResult(simpleVaultIntent, SIMPLE_VAULT_REQUEST_CODE);
    }

    public static void startAdvancedVaultActivity(Activity activity, String merchantPublicKey, String merchantBaseUrl, String merchantGetCustomerUri, String merchantAccessToken, BigDecimal amount, List<String> supportedPaymentTypes) {

        Intent advVaultIntent = new Intent(activity, AdvancedVaultActivity.class);
        advVaultIntent.putExtra("merchantPublicKey", merchantPublicKey);
        advVaultIntent.putExtra("merchantBaseUrl", merchantBaseUrl);
        advVaultIntent.putExtra("merchantGetCustomerUri", merchantGetCustomerUri);
        advVaultIntent.putExtra("merchantAccessToken", merchantAccessToken);
        advVaultIntent.putExtra("amount", amount.toString());
        putListExtra(advVaultIntent, "supportedPaymentTypes", supportedPaymentTypes);
        activity.startActivityForResult(advVaultIntent, ADVANCED_VAULT_REQUEST_CODE);
    }

    public static void startFinalVaultActivity(Activity activity, String merchantPublicKey, String merchantBaseUrl, String merchantGetCustomerUri, String merchantAccessToken, BigDecimal amount, List<String> supportedPaymentTypes) {

        Intent finalVaultIntent = new Intent(activity, FinalVaultActivity.class);
        finalVaultIntent.putExtra("merchantPublicKey", merchantPublicKey);
        finalVaultIntent.putExtra("merchantBaseUrl", merchantBaseUrl);
        finalVaultIntent.putExtra("merchantGetCustomerUri", merchantGetCustomerUri);
        finalVaultIntent.putExtra("merchantAccessToken", merchantAccessToken);
        finalVaultIntent.putExtra("amount", amount.toString());
        putListExtra(finalVaultIntent, "supportedPaymentTypes", supportedPaymentTypes);
        activity.startActivityForResult(finalVaultIntent, FINAL_VAULT_REQUEST_CODE);
    }

    public static void createPayment(final Activity activity, String token, Integer installments, Long cardIssuerId, final PaymentMethod paymentMethod, Discount discount) {

        if (paymentMethod != null) {

            LayoutUtil.showProgressLayout(activity);

            // Set item
            Item item = new Item(DUMMY_ITEM_ID, DUMMY_ITEM_QUANTITY,
                    DUMMY_ITEM_UNIT_PRICE);

            // Set payment method id
            String paymentMethodId = paymentMethod.getId();

            // Set campaign id
            Long campaignId = (discount != null) ? discount.getId() : null;

            // Set merchant payment
            MerchantPayment payment = new MerchantPayment(item, installments, cardIssuerId,
                    token, paymentMethodId, campaignId, DUMMY_MERCHANT_ACCESS_TOKEN);

            // Create payment
            ErrorHandlingCallAdapter.MyCall<Payment> call = MerchantServer.createPayment(activity, DUMMY_MERCHANT_BASE_URL, DUMMY_MERCHANT_CREATE_PAYMENT_URI, payment);
            call.enqueue(new ErrorHandlingCallAdapter.MyCallback<Payment>() {
                @Override
                public void success(Response<Payment> response) {

                    new MercadoPago.StartActivityBuilder()
                            .setActivity(activity)
                            .setPayment(response.body())
                            .setPaymentMethod(paymentMethod)
                            .startCongratsActivity();
                }

                @Override
                public void failure(ApiException apiException) {

                    LayoutUtil.showRegularLayout(activity);
                    if (apiException!=null){
                        Log.e("apiException",apiException.getMessage()+"");
                    }else {
                        Log.e("apiException","apiException==null");
                    }
                   // Toast.makeText(activity, apiException.getMessage()+"", Toast.LENGTH_LONG).show();
                }
            });
        } else {

            Toast.makeText(activity, "Invalid payment method", Toast.LENGTH_LONG).show();
        }
    }

    private static void putListExtra(Intent intent, String listName, List<String> list) {

        if (list != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<String>>(){}.getType();
            intent.putExtra(listName, gson.toJson(list, listType));
        }
    }
}
