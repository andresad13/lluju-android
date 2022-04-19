package com.zheng.liuju.mercadopago;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zheng.liuju.R;
import com.zheng.liuju.mercadopago.step1.Step1Activity;
import com.zheng.liuju.mercadopago.step2.Step2Activity;

public class MainPayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pay);
    }
    public void runStep1(View view) {

        runStep(new Step1Activity());
    }

    public void runStep2(View view) {

        runStep(new Step2Activity());
    }
    private void runStep(Activity activity) {

        Intent exampleIntent = new Intent(this, activity.getClass());
        startActivity(exampleIntent);
    }
}
