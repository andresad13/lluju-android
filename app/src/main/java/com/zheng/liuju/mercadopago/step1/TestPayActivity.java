package com.zheng.liuju.mercadopago.step1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.haibin.calendarview.Calendar;
import com.mercadopago.model.CardToken;
import com.mercadopago.model.PaymentMethod;
import com.zheng.liuju.R;

public class TestPayActivity extends AppCompatActivity {
    private PaymentMethod paymentMethod;

    private EditText cardNumber;
    private EditText securityCode;
    private Button expiryDate;
    private EditText cardHolderName;
    private EditText identificationNumber;
    private Spinner identificationType;
    private RelativeLayout identificationLayout;

    private CardToken cardToken;
    private Calendar selectedExpiryDate;
    private int expiryMonth;
    private int expiryYear;

    private String exceptionOnMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_pay);


    }
}
