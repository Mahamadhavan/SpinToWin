package com.xitij.spintoearn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import com.xitij.spintoearn.Adapter.TabAdapter;
import com.xitij.spintoearn.Interface.AdConsentListener;
import com.xitij.spintoearn.Models.UserBalance;
import com.xitij.spintoearn.R;
import com.xitij.spintoearn.Util.AdsConsent;
import com.xitij.spintoearn.Util.Constant;
import com.xitij.spintoearn.Util.Ex;
import com.xitij.spintoearn.Util.GlobalBus;
import com.xitij.spintoearn.Util.Method;
import com.xitij.spintoearn.Util.RestAPI;
import com.google.ads.consent.ConsentStatus;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Wallet extends AppCompatActivity {
    private Constant constant;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    Toolbar toolbar;
    public static TextView coins;
    public TextView txtcurrence;
    public TextView txtConvartCoin;
    static Button redeem;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String uPoint = null;
    Method method;
    private AdsConsent adsConsent;
    private LinearLayout linearLayout;
    private InterstitialAd interstitialAd_f;
    private InterstitialAd interstitialAd_f_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);


        interstitialAd_f_back=new com.facebook.ads.InterstitialAd(this,getString(R.string.facebook_inter));
        interstitialAd_f_back.loadAd();

        interstitialAd_f=new com.facebook.ads.InterstitialAd(this,getString(R.string.facebook_inter));
        interstitialAd_f.loadAd();

        com.facebook.ads.AdView adView1= new com.facebook.ads.AdView(Wallet.this,getString(R.string.facebook_banner), AdSize.BANNER_HEIGHT_50);
        RelativeLayout adcontainer=findViewById(R.id.banner_container);
        adcontainer.addView(adView1);
        adView1.loadAd();

        toolbar =(Toolbar)findViewById(R.id.toolbarWallet);
        toolbar.getBackground().setAlpha(0);
        toolbar.setBackgroundResource(R.color.colorPrimaryDark);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        constant = new Constant(Wallet.this);
        method=new Method(this);
        coins=(TextView)findViewById(R.id.textViewCoin);
        txtcurrence=(TextView)findViewById(R.id.Curence);
        txtConvartCoin=(TextView)findViewById(R.id.txtConvartwell);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayout_wallet);
        txtcurrence.setText(Constant.settings.getRedeem_currency());
        UserBalance(constant.sharedPreferences.getString(constant.profileId,"profileId"));
      /*  Double Convartcahs=(Double.parseDouble(uBalace)/ Double.parseDouble(Constant.settings.getRedeem_points()));
        txtConvartCoin.setText(Convartcahs.toString());*/
      //  method.loadInter(Wallet.this);

        GlobalBus.getBus().register(this);
        TabAdapter walletAdapter=new TabAdapter(this,getSupportFragmentManager());
        redeem=(Button)findViewById(R.id.redeem);
        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int redpoint=Integer.parseInt(Constant.settings.getMinimum_redeem_points());
                int balance = Integer.parseInt(coins.getText().toString());
//
//                Intent tran = new Intent(Wallet.this, RewardClaim.class);
//                startActivity(tran);


                if(balance >=  redpoint)
                {

                    Intent tran = new Intent(Wallet.this, RewardClaim.class);
                    startActivity(tran);

                }
                else
                {

                    if (interstitialAd_f.isAdLoaded()){
                        interstitialAd_f.show();
                    }

                    Ex.AlertBox("Minimum "+Constant.settings.getMinimum_redeem_points()+" Points to Required to Claim ",Wallet.this);
                    Toast.makeText(Wallet.this, "Minimum "+Constant.settings.getMinimum_redeem_points()+" Points to Required to Claim", Toast.LENGTH_LONG).show();


                 method.loadInter(Wallet.this);
                    //method.showVideoAd(Wallet.this);

                }
            }
        });
        viewPager=(ViewPager)findViewById(R.id.viewPagerwallet);
        viewPager.setAdapter(walletAdapter);

        tabLayout=(TabLayout)findViewById(R.id.tabLayoutwallet);
        tabLayout.setupWithViewPager(viewPager);

        adsConsent=new AdsConsent(this, new AdConsentListener() {
            @Override
            public void onConsentUpdate(ConsentStatus consentStatus) {
               // method.loadInter(Wallet.this);
                method.showBannerAd(linearLayout);
              //  method.showVideoAd(Wallet.this);
            }

        });
        adsConsent.checkForConsent();

    }

    public void UserBalance( String UserID) {
        String login = RestAPI.API_User_Balance + "&user_id=" + UserID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(login, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray(Constant.AppSid);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String success = object.getString("success");
                        if (success.equals("1")) {

                         String total_point = object.getString("points");

                            coins.setText(total_point);
                            Double Convartcahs=(Double.parseDouble(total_point)/ Double.parseDouble(Constant.settings.getRedeem_points()));
                            txtConvartCoin.setText("\u20B9"+Convartcahs.toString());

                            Log.d("Response-s",total_point);
                        } else {
                            Log.d("Response", new String(responseBody));
                        }
                    }
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


    @Subscribe
    public void onEvent(UserBalance rewardNotify) {
        UserBalance(constant.sharedPreferences.getString(constant.profileId,"profileId"));
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {

        if (interstitialAd_f_back.isAdLoaded()) {

            interstitialAd_f_back.show();
        }
        UserBalance rewardNotify = new UserBalance("100");
        EventBus.getDefault().post(rewardNotify);
        super.onBackPressed();
    }
}
