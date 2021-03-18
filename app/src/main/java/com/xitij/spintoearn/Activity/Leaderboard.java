package com.xitij.spintoearn.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAd;
import com.xitij.spintoearn.R;

public class Leaderboard extends AppCompatActivity {
    Toolbar toolbar;
    private InterstitialAd interstitialAd_f_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        toolbar =(Toolbar)findViewById(R.id.toolbar);
        toolbar.getBackground().setAlpha(0);
        toolbar.setTitle("Leader board");
        toolbar.setBackgroundResource(R.color.colorPrimaryDark);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        interstitialAd_f_back=new com.facebook.ads.InterstitialAd(this,getString(R.string.facebook_inter));
        interstitialAd_f_back.loadAd();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                startActivity(new Intent(Leaderboard.this,MainActivity.class));
                finishAffinity();
            }
        });



        com.facebook.ads.AdView adView1= new com.facebook.ads.AdView(Leaderboard.this,getString(R.string.facebook_banner), AdSize.BANNER_HEIGHT_50);
        RelativeLayout adcontainer=findViewById(R.id.banner_container);
        adcontainer.addView(adView1);
        adView1.loadAd();


    }

    @Override
    public void onBackPressed() {
        if(interstitialAd_f_back.isAdLoaded()){
            interstitialAd_f_back.show();
        }
        super.onBackPressed();
    }
}
