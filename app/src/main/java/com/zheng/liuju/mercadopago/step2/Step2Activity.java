package com.zheng.liuju.mercadopago.step2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mercadopago.adapters.ErrorHandlingCallAdapter;
import com.mercadopago.core.MercadoPago;
import com.mercadopago.model.ApiException;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.SavedCardToken;
import com.mercadopago.model.Token;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;
import com.zheng.liuju.R;
import com.zheng.liuju.mercadopago.utils.ExamplesUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class Step2Activity extends AppCompatActivity {
    protected MercadoPago mMercadoPago;
    protected List<String> mSupportedPaymentTypes = new ArrayList<String>(){{
        add("credit_card");
        add("debit_card");
        add("prepaid_card");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2);
        mMercadoPago = new MercadoPago.Builder()
                .setContext(this)
                .setPublicKey(ExamplesUtils.DUMMY_MERCHANT_PUBLIC_KEY)
                .build();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ExamplesUtils.SIMPLE_VAULT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.e("RESULT_OK",data.getStringExtra("token")+"");
                // Create payment
                ExamplesUtils.createPayment(this, data.getStringExtra("token"),
                        1, null, JsonUtil.getInstance().fromJson(data.getStringExtra("paymentMethod"), PaymentMethod.class), null);

            } else {
//                Log.e("RESULT_OK",data.getStringExtra("apiException")+"");
                if ((data != null) && (data.getStringExtra("apiException") != null)) {
                    Toast.makeText(getApplicationContext(), data.getStringExtra("apiException"), Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == MercadoPago.CONGRATS_REQUEST_CODE) {

            LayoutUtil.showRegularLayout(this);
        }
    }

    public void submitForm(View view) {

        // Call simple vault activity
    /*    ExamplesUtils.startSimpleVaultActivity(this, ExamplesUtils.DUMMY_MERCHANT_PUBLIC_KEY,
                ExamplesUtils.DUMMY_MERCHANT_BASE_URL, ExamplesUtils.DUMMY_MERCHANT_GET_CUSTOMER_URI,
                ExamplesUtils.DUMMY_MERCHANT_ACCESS_TOKEN, mSupportedPaymentTypes);*/
        createSavedCardToken();
    }

    protected void createSavedCardToken() {

        SavedCardToken savedCardToken = new SavedCardToken("1586249540353", "123");


        // Create token
        LayoutUtil.showProgressLayout(Step2Activity.this);
        ErrorHandlingCallAdapter.MyCall<Token> call = mMercadoPago.createToken(savedCardToken);
        call.enqueue(getCreateTokenCallback());
    }

    protected ErrorHandlingCallAdapter.MyCallback<Token> getCreateTokenCallback() {

        return new ErrorHandlingCallAdapter.MyCallback<Token>() {
            @Override
            public void success(Response<Token> response) {

                Intent returnIntent = new Intent();
                Log.e("token",response.body().getId());
                //  returnIntent.putExtra("token", response.body().getId());
                //returnIntent.putExtra("paymentMethod", JsonUtil.getInstance().toJson(mSelectedPaymentMethod));
               // setResult(RESULT_OK, returnIntent);
                //finish();
            }

            @Override
            public void failure(ApiException apiException) {

                Log.e("token","failure");
            }
        };
    }
}
