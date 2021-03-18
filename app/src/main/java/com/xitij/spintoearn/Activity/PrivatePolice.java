package com.xitij.spintoearn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAd;
import com.xitij.spintoearn.Interface.AdConsentListener;
import com.xitij.spintoearn.R;
import com.xitij.spintoearn.Util.AdsConsent;
import com.xitij.spintoearn.Util.Constant;
import com.xitij.spintoearn.Util.Method;
import com.google.ads.consent.ConsentStatus;

public class PrivatePolice extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtPolice;
    private LinearLayout linearLayout;
    private AdsConsent adsConsent;
    private Method method;
    private InterstitialAd interstitialAd_f_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police);

        interstitialAd_f_back=new com.facebook.ads.InterstitialAd(this,getString(R.string.facebook_inter));
        interstitialAd_f_back.loadAd();

        com.facebook.ads.AdView adView1= new com.facebook.ads.AdView(PrivatePolice.this,getString(R.string.facebook_banner), AdSize.BANNER_HEIGHT_50);
        RelativeLayout adcontainer=findViewById(R.id.banner_container);
        adcontainer.addView(adView1);
        adView1.loadAd();

        toolbar =(Toolbar)findViewById(R.id.toolbarPolice);
        toolbar.getBackground().setAlpha(0);
        toolbar.setBackgroundResource(R.color.colorPrimaryDark);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txtPolice=(TextView)findViewById(R.id.textview_privacy_police);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayout_policy);
        method=new Method(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
        adsConsent =new AdsConsent(this, new AdConsentListener() {
            @Override
            public void onConsentUpdate(ConsentStatus consentStatus) {
                method.showBannerAd(linearLayout);
            }

        });
        adsConsent.checkForConsent();
        if(Constant.settings!=null){
            txtPolice.setText(Html.fromHtml(Constant.settings.getApp_privacy_policy()));
        }
    }
    @Override
    public void onBackPressed() {

        if(interstitialAd_f_back.isAdLoaded()){
            interstitialAd_f_back.show();
        }



        super.onBackPressed();
    }
}
