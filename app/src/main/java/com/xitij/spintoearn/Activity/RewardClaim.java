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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAd;
import com.xitij.spintoearn.Interface.AdConsentListener;
import com.xitij.spintoearn.Models.UserBalance;
import com.xitij.spintoearn.R;
import com.xitij.spintoearn.Util.AdsConsent;
import com.xitij.spintoearn.Util.Constant;
import com.xitij.spintoearn.Util.Ex;
import com.xitij.spintoearn.Util.Method;
import com.xitij.spintoearn.Util.RestAPI;
import com.google.ads.consent.ConsentStatus;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class RewardClaim extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Constant constant;
    Toolbar toolbar;
    private Spinner spinner;
    private ArrayList<String> arrayList;
    private EditText Amount, Details;
    private Button btnClime;
    private String Payment_Type;
    private ProgressDialog progressDialog;
    private Method method;
    private AdsConsent adsConsent;
    private LinearLayout linearLayout;
    private InterstitialAd interstitialAd_f;
    private InterstitialAd interstitialAd_f_back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_claim);


        interstitialAd_f = new com.facebook.ads.InterstitialAd(this, getString(R.string.facebook_inter));
        interstitialAd_f.loadAd();

        interstitialAd_f_back = new com.facebook.ads.InterstitialAd(this, getString(R.string.facebook_inter));
        interstitialAd_f_back.loadAd();

        com.facebook.ads.AdView adView1 = new com.facebook.ads.AdView(RewardClaim.this, getString(R.string.facebook_banner), AdSize.BANNER_HEIGHT_50);
        RelativeLayout adcontainer = findViewById(R.id.banner_container);
        adcontainer.addView(adView1);
        adView1.loadAd();

        Intent intent = getIntent();
        progressDialog = new ProgressDialog(RewardClaim.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar_reward_point_claim);
        toolbar.getBackground().setAlpha(0);
        toolbar.setBackgroundResource(R.color.colorPrimaryDark);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        constant = new Constant(RewardClaim.this);
        Amount = (EditText) findViewById(R.id.amount);
        spinner = (Spinner) findViewById(R.id.spinnerRewardClaim);
        btnClime = (Button) findViewById(R.id.buttonPointClaim);
        Details = (EditText) findViewById(R.id.editTextdetail);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout_reward_point_claim);
        method = new Method(this);
        arrayList = new ArrayList<>();
        SpineerItem();
        spinner.setOnItemSelectedListener(this);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        Amount.setEnabled(false);
        Amount.setText(Constant.userBalance.getuBalance());
        btnClime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (interstitialAd_f.isAdLoaded()) {
                    interstitialAd_f.show();
                }

                Details.clearFocus();
                UserBalance rewardNotify = new UserBalance("10");
                EventBus.getDefault().post(rewardNotify);
                NullCheck();
            }
        });
        adsConsent = new AdsConsent(this, new AdConsentListener() {
            @Override
            public void onConsentUpdate(ConsentStatus consentStatus) {
                method.showBannerAd(linearLayout);
            }

        });
        adsConsent.checkForConsent();
    }

    public void NullCheck() {

        String detail = Details.getText().toString();

        Details.setError(null);
        if (Payment_Type.equals(getResources().getString(R.string.select_payment_type)) || Payment_Type.equals("") || Payment_Type.isEmpty()) {
            Toast.makeText(RewardClaim.this, getResources().getString(R.string.please_select_payment), Toast.LENGTH_SHORT).show();
        } else if (detail.equals("") || detail.isEmpty()) {
            Details.requestFocus();
            Details.setError(getResources().getString(R.string.please_enter_payment_details));
        } else {
            if (Ex.isConnectionEnable(RewardClaim.this)) {
                PaymentRequestSubmit(constant.sharedPreferences.getString(constant.profileId, "profileId"), Constant.userBalance.getuBalance(), Payment_Type, detail, Constant.DeviceID);
            } else {
                Toast.makeText(this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void PaymentRequestSubmit(final String user_id, final String user_points, String payment_mode, String detail, String device_id) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        String api = RestAPI.API_Payment_Request + "&user_id=" + user_id + "&device_id=" + device_id + "&user_points=" + user_points + "&payment_mode=" + payment_mode + "&payment_details=" + detail;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(api, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response-wr", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant.AppSid);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String msg = object.getString("msg");
                        String success = object.getString("success");

                        if (success.equals("1")) {
                            progressDialog.dismiss();

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RewardClaim.this);
                            alertDialogBuilder.setTitle(R.string.app_name);
                            alertDialogBuilder.setMessage("Redeem request sent!" + "\nPayment will be processed with in 15 to 20 Days" + "\nKeep Playing And Earning");
//                            alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
                            alertDialogBuilder.setPositiveButton(RewardClaim.this.getResources().getString(R.string.ok_message),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            UserBalance rewardNotify = new UserBalance("100");
                                            EventBus.getDefault().post(rewardNotify);
                                            finish();
                                        }
                                    });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RewardClaim.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
            }
        });
    }

    private void SpineerItem() {
        arrayList.add(getResources().getString(R.string.select_payment_type));
        if (Constant.settings != null) {
            if (!Constant.settings.getPayment_method1().equals("")) {
                arrayList.add(Constant.settings.getPayment_method1());
            }
            if (!Constant.settings.getPayment_method2().equals("")) {
                arrayList.add(Constant.settings.getPayment_method2());
            }
            if (!Constant.settings.getPayment_method3().equals("")) {
                arrayList.add(Constant.settings.getPayment_method3());
            }
            if (!Constant.settings.getPayment_method4().equals("")) {
                arrayList.add(Constant.settings.getPayment_method4());
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //first list item selected by default and sets the preset accordingly
        if (position == 0) {
            ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textView_upload_fragment));
            Payment_Type = arrayList.get(position);
        } else {
            ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.toolbar));
            Payment_Type = arrayList.get(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        if (interstitialAd_f_back.isAdLoaded()) {
            interstitialAd_f_back.show();
        }

        super.onBackPressed();
    }
}
