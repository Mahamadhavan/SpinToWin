package com.xitij.spintoearn.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAd;
import com.xitij.spintoearn.Interface.AdConsentListener;
import com.xitij.spintoearn.R;
import com.xitij.spintoearn.Util.AdsConsent;
import com.xitij.spintoearn.Util.Constant;
import com.xitij.spintoearn.Util.Ex;
import com.xitij.spintoearn.Util.Method;
import com.xitij.spintoearn.Util.RestAPI;
import com.google.ads.consent.ConsentStatus;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ContactUs extends AppCompatActivity {
    private Toolbar toolbar;
    private Method method;
    private EditText editText_name, editText_email, editText_message;
    private String name, email, message;
    private LinearLayout linearLayout;
    private InputMethodManager imm;
    private Button btnSubmit;
    private ProgressDialog progressDialog;
    private Constant constant;
    private AdsConsent adsConsent;
    private InterstitialAd interstitialAd_f_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        com.facebook.ads.AdView adView1= new com.facebook.ads.AdView(ContactUs.this,getString(R.string.facebook_banner), AdSize.BANNER_HEIGHT_50);
        RelativeLayout adcontainer=findViewById(R.id.banner_container);
        adcontainer.addView(adView1);
        adView1.loadAd();

        interstitialAd_f_back=new com.facebook.ads.InterstitialAd(this,getString(R.string.facebook_inter));
        interstitialAd_f_back.loadAd();

        toolbar =(Toolbar)findViewById(R.id.toolbarContactUs);
        toolbar.getBackground().setAlpha(0);
        toolbar.setBackgroundResource(R.color.colorPrimaryDark);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        method= new Method(ContactUs.this);
        constant= new Constant(ContactUs.this);

        progressDialog = new ProgressDialog(ContactUs.this);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayout_contact_us);

        editText_email=(EditText)findViewById(R.id.editText_email_contact_us);
        editText_name=(EditText)findViewById(R.id.editText_name_contact_us);
        editText_message=(EditText)findViewById(R.id.editTextDetails);
        btnSubmit=(Button)findViewById(R.id.button_contact_us);

        editText_name.setText(constant.sharedPreferences.getString(constant.userName,""));
        editText_email.setText(constant.sharedPreferences.getString(constant.userEmail,""));
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editText_name.getText().toString();
                email = editText_email.getText().toString();
                message = editText_message.getText().toString();
                form();
            }
        });

        adsConsent =new AdsConsent(this, new AdConsentListener() {
            @Override
            public void onConsentUpdate(ConsentStatus consentStatus) {
                method.showBannerAd(linearLayout);
            }

        });
        adsConsent.checkForConsent();
    }


    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void form() {

        editText_name.setError(null);
        editText_email.setError(null);
        editText_message.setError(null);

        if (name.equals("") || name.isEmpty()) {
            editText_name.requestFocus();
            editText_name.setError(getResources().getString(R.string.please_enter_name));
        } else if (!isValidMail(email) || email.isEmpty()) {
            editText_email.requestFocus();
            editText_email.setError(getResources().getString(R.string.please_enter_email));
        } else if (message.equals("") || message.isEmpty()) {
            editText_message.requestFocus();
            editText_message.setError(getResources().getString(R.string.please_enter_message));
        } else {

            editText_name.clearFocus();
            editText_email.clearFocus();
            editText_message.clearFocus();
            editText_name.setText("");
            editText_email.setText("");
            editText_message.setText("");
            if (Ex.isConnectionEnable(ContactUs.this)) {
               contact_us(email, name, message);
            } else {
                Toast.makeText(ContactUs.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void contact_us(String sendEmail, String sendName, String sendMessage) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        String verify = RestAPI.API_Contact_Us +"&contact_email=" +sendEmail + "&contact_name=" + sendName + "&contact_msg=" + sendMessage;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(verify, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response-t", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant.AppSid);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String msg = object.getString("msg");
                        String success = object.getString("success");

                        if (success.equals("1")) {

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ContactUs.this);
                            alertDialogBuilder.setTitle(R.string.app_name);
                            alertDialogBuilder.setMessage(msg);
                            alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
                            alertDialogBuilder.setPositiveButton(ContactUs.this.getResources().getString(R.string.ok_message),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            finish();
                                        }
                                    });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                            Toast.makeText(ContactUs.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ContactUs.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    }

                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onBackPressed() {

        if(interstitialAd_f_back.isAdLoaded()){
            interstitialAd_f_back.show();
        }
        super.onBackPressed();
    }
}
