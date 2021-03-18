package com.xitij.spintoearn.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAd;
import com.xitij.spintoearn.Adapter.CoinHistoryAdapter;
import com.xitij.spintoearn.Models.CoinHistory;
import com.xitij.spintoearn.R;
import com.xitij.spintoearn.Util.Constant;
import com.xitij.spintoearn.Util.RestAPI;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ReferCode extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
     Toolbar toolbar;
     private LinearLayout linearLayout;
     private Constant constant;
     private TextView txtRefercode, txtReferdig;
     private Button btnInvait;
    public ArrayList<CoinHistory> coinHistoryArrayList;
    private InterstitialAd interstitialAd_f;
    private InterstitialAd interstitialAd_f_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_code);

        interstitialAd_f_back=new com.facebook.ads.InterstitialAd(this,getString(R.string.facebook_inter));
        interstitialAd_f_back.loadAd();

        interstitialAd_f=new com.facebook.ads.InterstitialAd(this,getString(R.string.facebook_inter));
        interstitialAd_f.loadAd();

        com.facebook.ads.AdView adView1= new com.facebook.ads.AdView(ReferCode.this,getString(R.string.facebook_banner), AdSize.BANNER_HEIGHT_50);
        RelativeLayout adcontainer=findViewById(R.id.banner_container);
        adcontainer.addView(adView1);
        adView1.loadAd();

        constant=new Constant(this);
        coinHistoryArrayList = new ArrayList<>();
        linearLayout=(LinearLayout)findViewById(R.id.ll_copy_reference_code);


        toolbar =(Toolbar)findViewById(R.id.toolbarWallet);
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
        mRecyclerView=(RecyclerView)findViewById(R.id.refRecycleview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager =new LinearLayoutManager(ReferCode.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ReferUserHistory(constant.sharedPreferences.getString(constant.userCode,"userCode"));

        txtReferdig=(TextView)findViewById(R.id.textViewReferdig);
        txtReferdig.setText("Refer your friend and Earn "+ Constant.ReferPoint + " coins when they registation by your referral code");
        txtRefercode=(TextView)findViewById(R.id.textView_reference_code);
        txtRefercode.setText(constant.sharedPreferences.getString(constant.userCode,"userCode").toUpperCase());
        btnInvait=(Button)findViewById(R.id.invite);

        btnInvait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String shareBody = "https://play.google.com/store/apps/details?id=" + getPackageName();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I have earned cash using "+getResources().getString(R.string.app_name)+" app.you can also earn by downloading app from below link and enter referral code while login-"+txtRefercode.getText().toString()+ " \n" + shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Invite Friend Using"));
                } catch (Exception e) {
                    Log.e("Sahare error", e.getMessage());
                }
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (interstitialAd_f.isAdLoaded()){
                    interstitialAd_f.show();
                }
                ClipboardManager clipboard = (ClipboardManager) ReferCode.this.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", constant.sharedPreferences.getString(constant.userCode,"userCode"));
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ReferCode.this, "Copy text", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ReferUserHistory(String ReferCode) {


        String login = RestAPI.API_Refer_History + "&refer_code=" + ReferCode;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(login,  new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant.AppSid);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String name = object.getString("name");
                        String points = object.getString("points");
                        String date = object.getString("date");
                        Log.d("Response-p", object.getString("points"));
                        coinHistoryArrayList.add(new CoinHistory(name,date,points));
                    }

                    mAdapter=new CoinHistoryAdapter(coinHistoryArrayList);
                    mRecyclerView.setAdapter(mAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // progressDialog.dismiss();
            }
        });


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
