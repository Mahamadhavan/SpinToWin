package com.xitij.spintoearn.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;

import com.xitij.spintoearn.Models.CoinHistory;
import com.xitij.spintoearn.Models.Settings;
import com.xitij.spintoearn.Models.SpinCounter;
import com.xitij.spintoearn.Models.User;
import com.xitij.spintoearn.Models.UserBalance;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;

public class Constant {

    public static final String AppSid="spin";

    public static final int POINT_VIDEO_MIN=1;
    public static final int POINT_VIDEO_MAX=100;
    public static final int RATE_POINT=1000;
    public static final int DAILY_CHECK=100;

    public static String Total_Point;

    //Enter milliseconds
    public static final int Waitsecond=8000;

    public static String PublicIP;
    public static String DeviceID;


    public static final String ReferPoint = "10";


    public static final String VIDEO_AD_ID="ca-app-pub-3940256099942544/5224354917";
    public static final String INSTE_AD_ID="ca-app-pub-3940256099942544/1033173712";
    public static final String AD_ID= "ca-app-pub-3940256099942544/6300978111";

    public static int AD_COUNT = 0;
    public static int VIDEO_AD_COUNT= 0;
    public static InterstitialAd mInterstitial;
    public static RewardedVideoAd mRewardedVideoAd;

    public static User user;
    public static UserBalance userBalance;
    public static CoinHistory coinHistory;
    public static Settings settings;
    public static SpinCounter spinCounter;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor sharedEditor;
    private Activity activity;
    private final String loginPerfm = "login";
    public String isLogin = "isLogin";
    private String deviceId = "deviceId";
    public String profileId = "profileId";
    public String userEmail = "userEmail";
    public String userPassword = "userPassword";
    public String userName = "userName";
    public String userPhone = "userPhone";
    public String userCode = "userCode";

    public String Spin_Count = "Spin_Count";

    @SuppressLint("CommitPrefEdits")
    public Constant(Activity activity) {
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences(loginPerfm, 0); // 0 - for private mode
        sharedEditor = sharedPreferences.edit();
    }
}
