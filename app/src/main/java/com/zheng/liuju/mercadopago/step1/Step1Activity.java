package com.zheng.liuju.mercadopago.step1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mercadopago.core.MercadoPago;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;
import com.zheng.liuju.R;
import com.zheng.liuju.mercadopago.utils.ExamplesUtils;

import java.util.ArrayList;
import java.util.List;

public class Step1Activity extends AppCompatActivity {
    protected List<String> mSupportedPaymentTypes = new ArrayList<String>(){{
        add("credit_card");
        add("debit_card");
        add("prepaid_card");
    }};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step1);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MercadoPago.PAYMENT_METHODS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.e("apiException","RESULT_OK");
                // Set payment method
                PaymentMethod paymentMethod = JsonUtil.getInstance().fromJson(data.getStringExtra("paymentMethod"), PaymentMethod.class);

                // Call new card activity
                ExamplesUtils.startCardActivity(this, ExamplesUtils.DUMMY_MERCHANT_PUBLIC_KEY, paymentMethod,null,null,null);
            } else {

                if ((data != null) && (data.getStringExtra("apiException") != null)) {
                    Toast.makeText(getApplicationContext(), data.getStringExtra("apiException"), Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == ExamplesUtils.CARD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // Create payment
                ExamplesUtils.createPayment(this, data.getStringExtra("token"),
                        1, null, JsonUtil.getInstance().fromJson(data.getStringExtra("paymentMethod"), PaymentMethod.class), null);

            } else {

                if (data != null) {
                    if (data.getStringExtra("apiException") != null) {

                        Toast.makeText(getApplicationContext(), data.getStringExtra("apiException"), Toast.LENGTH_LONG).show();

                    } else if (data.getBooleanExtra("backButtonPressed", false)) {

                        new MercadoPago.StartActivityBuilder()
                                .setActivity(this)
                                .setPublicKey(ExamplesUtils.DUMMY_MERCHANT_PUBLIC_KEY)
                                .setSupportedPaymentTypes(mSupportedPaymentTypes)
                                .startPaymentMethodsActivity();
                    }
                }
            }
        } else if (requestCode == MercadoPago.CONGRATS_REQUEST_CODE) {

            LayoutUtil.showRegularLayout(this);
        }
    }

    public void submitForm(View view) {

        // Call payment methods activity
        new MercadoPago.StartActivityBuilder()
                .setActivity(this)
                .setPublicKey(ExamplesUtils.DUMMY_MERCHANT_PUBLIC_KEY)
                .setSupportedPaymentTypes(mSupportedPaymentTypes)
                .startPaymentMethodsActivity();
    }
}
