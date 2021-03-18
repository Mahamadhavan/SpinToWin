package com.xitij.spintoearn.Util;

import android.app.Application;

import com.facebook.ads.AudienceNetworkAds;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AudienceNetworkAds.initialize(this);

        StartAppSDK.init(this, "210129135");

        StartAppSDK.init(this, "210129135", true);
        StartAppAd.disableSplash();


    }
}
