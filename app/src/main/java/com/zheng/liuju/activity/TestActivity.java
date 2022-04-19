package com.zheng.liuju.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zheng.liuju.R;

import org.json.JSONObject;

import io.conekta.conektasdk.Card;
import io.conekta.conektasdk.Conekta;
import io.conekta.conektasdk.Token;

public class TestActivity extends AppCompatActivity {
    private Button btnTokenize;
    private TextView outputView;
    private TextView uuidDevice;
    private EditText numberText;
    private EditText monthText;
    private EditText yearText;
    private EditText cvcText;
    private EditText nameText;
    private Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btnTokenize = (Button) findViewById(R.id.btnTokenize);
        outputView = (TextView) findViewById(R.id.outputView);
        uuidDevice = (TextView) findViewById(R.id.uuidDevice);
        numberText = (EditText) findViewById(R.id.numberText);
        nameText = (EditText) findViewById(R.id.nameText);
        monthText = (EditText) findViewById(R.id.monthText);
        yearText = (EditText) findViewById(R.id.yearText);
        cvcText = (EditText) findViewById(R.id.cvcText);
    /*    Conekta.setPublicKey("key_eYvWV7gSDkNYXsmr");
        Conekta.setApiVersion("1.0.0");                       //optional
        Conekta.collectDevice(activity);

        Card card = new Card("Fulanito Pérez", "4242424242424242", "332", "11", "2020");
        Token token = new Token(activity);

        token.onCreateTokenListener( new Token.CreateToken(){
            @Override
            public void onCreateTokenReady(JSONObject data) {
                try {
                  Log.e("JSONObject",data.toString()+"");
                } catch (Exception err) {
                    //Do something on error
                    Log.e("JSONObject",err.getMessage()+"");
                }
            }
        });

        token.create(card);*/
        btnTokenize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean haveInternet = isOnline();
                Log.e("The token::::", "haveInternet="+haveInternet);
                if (haveInternet) {

                    Conekta.setPublicKey("key_B2wXSt6SDrj9qF2XNJ2k6Kg");
                    Conekta.setApiVersion("0.3.0");
                    Conekta.collectDevice(activity);
                    Card card = new Card(nameText.getText().toString(), numberText.getText().toString(), cvcText.getText().toString(), monthText.getText().toString(), yearText.getText().toString());
                    Token token = new Token(activity);

                    //Listen when token is returned
                    token.onCreateTokenListener(new Token.CreateToken() {

                        @Override
                        public void onCreateTokenReady(JSONObject data) {

                            try {
                                Log.e("The token::::", data.toString());
                                Log.e("The token::::", data.getString("id"));
                                outputView.setText("Token id: " + data.getString("id"));
                            } catch (Exception err) {
                                outputView.setText("Error: " + err.toString());
                            }
                            uuidDevice.setText("Uuid device: " + Conekta.deviceFingerPrint(activity));
                        }
                    });

                    //Request for create token
                    token.create(card);
                } else {
                    outputView.setText("You don't have internet");
                }
            }
        });
    }

    public boolean isOnline () {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
