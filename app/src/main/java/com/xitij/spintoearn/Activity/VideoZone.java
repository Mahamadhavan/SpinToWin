package com.xitij.spintoearn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAd;
import com.xitij.spintoearn.Adapter.AdsItemsAdapter;
import com.xitij.spintoearn.Interface.AdConsentListener;
import com.xitij.spintoearn.Models.AdsItems;
import com.xitij.spintoearn.R;
import com.xitij.spintoearn.Util.AdsConsent;
import com.xitij.spintoearn.Util.Constant;
import com.xitij.spintoearn.Util.Events;
import com.xitij.spintoearn.Util.Ex;
import com.xitij.spintoearn.Util.GlobalBus;
import com.xitij.spintoearn.Util.Method;
import com.google.ads.consent.ConsentStatus;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import static com.xitij.spintoearn.Util.Constant.DeviceID;

public class VideoZone extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private AdsItemsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Toolbar toolbar;
    private TextView textView;
    private ImageView imageView;
    public ArrayList<AdsItems> adsItemsArrayList;
    private AdsConsent adsConsent;
    private Constant constant;
    private Method method;
    private LinearLayout linearLayout;
    private InterstitialAd interstitialAd_f_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videozone);

        interstitialAd_f_back=new com.facebook.ads.InterstitialAd(this,getString(R.string.facebook_inter));
        interstitialAd_f_back.loadAd();

        com.facebook.ads.AdView adView1= new com.facebook.ads.AdView(VideoZone.this,getString(R.string.facebook_banner), AdSize.BANNER_HEIGHT_50);
        RelativeLayout adcontainer=findViewById(R.id.banner_container);
        adcontainer.addView(adView1);
        adView1.loadAd();

        toolbar =(Toolbar)findViewById(R.id.toolbarVideoZone);
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
        method=new Method(this);
        constant=new Constant(VideoZone.this);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayout_videoZone);
        adsItemsArrayList= new ArrayList<>();
        AdsItems adsItems=new AdsItems( R.drawable.ic_g_video,"Video Ad 1","See Videos Earn Coin",Constant.settings.getVideo_add_point());
        AdsItems adsItems1=new AdsItems( R.drawable.ic_g_video,"Video Ad 2","See Videos Earn Coin",Constant.settings.getVideo_add_point());
        AdsItems adsItems2=new AdsItems( R.drawable.ic_g_video,"Video Ad 3","See Videos Earn Coin",Constant.settings.getVideo_add_point());
        AdsItems adsItems3=new AdsItems( R.drawable.ic_g_video,"Video Ad 4","See Videos Earn Coin",Constant.settings.getVideo_add_point());
        adsItemsArrayList.add(adsItems);
        adsItemsArrayList.add(adsItems1);
        adsItemsArrayList.add(adsItems2);
        adsItemsArrayList.add(adsItems3);

        GlobalBus.getBus().register(this);
        mRecyclerView =(RecyclerView)findViewById(R.id.adsRecycleview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager =new LinearLayoutManager(VideoZone.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Ex.getIPaddress();
        mAdapter=new AdsItemsAdapter(adsItemsArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdsItemsAdapter.OnItemClickListener() {
            @Override
          public void onItemClick(int position) {

           String msg= adsItemsArrayList.get(position).getAds_tittle();
           if(Constant.VIDEO_AD_COUNT+1 <= Integer.parseInt(Constant.settings.getVideo_ads_limit()))
                {
                    if (Constant.mRewardedVideoAd != null && Constant.mRewardedVideoAd.isLoaded()) {
                        Constant.mRewardedVideoAd.show();
                    } else {
                        method.showVideoAd(VideoZone.this, constant.sharedPreferences.getString(constant.profileId, "profileId"), DeviceID);
                        //Toast.makeText(VideoZone.this, "Video Ads not load  ", Toast.LENGTH_SHORT).show();
                    }
                }
                else
               {
                  Toast.makeText(VideoZone.this, "Daily Video Ads Limit Over", Toast.LENGTH_LONG).show();
               }
          }
        });

        adsConsent=new AdsConsent(this, new AdConsentListener() {
            @Override
            public void onConsentUpdate(ConsentStatus consentStatus) {
                method.showBannerAd(linearLayout);
                method.showVideoAd(VideoZone.this,constant.sharedPreferences.getString(constant.profileId,"profileId"),DeviceID);
            }

        });
        adsConsent.checkForConsent();
    }
    @Subscribe
    public void onEvent(Events.VideoAdsReload videoAdsReload) {
        method.showVideoAd(VideoZone.this,constant.sharedPreferences.getString(constant.profileId,"profileId"),DeviceID);
    }
    @Override
    public void onBackPressed() {

        if (interstitialAd_f_back.isAdLoaded()) {
            interstitialAd_f_back.show();
        }
        super.onBackPressed();
    }
}
