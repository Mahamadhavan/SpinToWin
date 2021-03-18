package com.xitij.spintoearn.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.xitij.spintoearn.Interface.InterstitialAdView;
import com.xitij.spintoearn.Interface.VideoAds;
import com.xitij.spintoearn.Models.UserBalance;
import com.xitij.spintoearn.R;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.xitij.spintoearn.Util.Constant.AppSid;
import static com.xitij.spintoearn.Util.Constant.DeviceID;
import static com.xitij.spintoearn.Util.Constant.mInterstitial;
import static com.xitij.spintoearn.Util.Constant.mRewardedVideoAd;

public class Method {
    private Constant constant;
    // User Loging logs added
    private InterstitialAdView interstitialAdView;
    private AdView adView;
    private Activity activity;
    private VideoAds videoAd;
    public static boolean share = false, loginBack = false, allowPermitionExternalStorage = false, personalization_ad = false;
    private Context _context;
    private DBHelper dbHelper;
    StartAppAd startAppAd;

    public Method(Context context) {
        this._context = context;
        startAppAd = new StartAppAd(context);
        startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO);
    }

    public static void UserLoginLogs(final String userid, final String logStatus, final String deviceID) {


        String register = RestAPI.API_Login_Logs + "&user_id=" + userid + "&deviceid=" + deviceID + "&user_ip=" + Constant.PublicIP + "&logs_status=" + logStatus;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(register, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(AppSid);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String msg = object.getString("msg");
                        String success = object.getString("success");

                        if (success.equals("1")) {
                            Log.d("Response", msg);
                        } else {
                            Log.d("Response", msg);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Response-F", new String(responseBody));
            }
        });
    }

    // Get User balance
    public static void UserBalance(String UserID, final TextView txt) {
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
                            String bal = object.getString("points");
                            Constant.userBalance = new UserBalance(bal);
                            txt.setText(bal);
                            Log.d("Response", bal);
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

    public static void UserBalance(final String UserID) {
        String login = RestAPI.API_User_Balance + "&user_id=" + UserID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(login, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response-u", new String(responseBody));
                String res = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray(Constant.AppSid);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String success = object.getString("success");
                        if (success.equals("1")) {
                            String bal = object.getString("points");

                            Log.d("Response-u", bal);

                        } else {
                            Log.d("Response-u", new String(responseBody));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Response-UF", new String(responseBody));
            }
        });
    }

    // Update User balance
    public static void BalanceUpdate(final String UserID, final String CoinType, final String Coin, final String Ip, final TextView txt, final int pointtype) {

        String login = RestAPI.API_Balance_Update + "&user_id=" + UserID + "&active_type=" + CoinType + "&points=" + Coin + "&user_ip="
                + Ip + "&device_id=" + DeviceID + "&point_type=" + pointtype;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(login, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response-b", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray(Constant.AppSid);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String success = object.getString("success");

                        if (success.equals("1")) {
                            txt.setText("");
                            String bal = object.getString("points");
                            Constant.userBalance = new UserBalance(bal);
                            txt.setText(bal);
                            Log.d("Response", bal);
                        } else {
                            Log.d("Response", new String(responseBody));
                            // Ex.okAlertBox(getResources().getString(R.string.login_failed_message));
                            //Toast.makeText(Login.this, getResources().getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
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

    public static void BalanceUpdate(final String UserID, final String CoinType, final String Coin, final String Ip, final int pointtype) {

        String login = RestAPI.API_Balance_Update + "&user_id=" + UserID + "&active_type=" + CoinType + "&points=" + Coin + "&user_ip="
                + Ip + "&device_id=" + DeviceID + "&point_type=" + pointtype;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(login, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response-b", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray(Constant.AppSid);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String success = object.getString("success");
                        if (success.equals("1")) {
                            String bal = object.getString("points");
                            Constant.userBalance = new UserBalance(bal);
                            Log.d("Response", bal);
                        } else {
                            Log.d("Response", new String(responseBody));
                            // Ex.okAlertBox(getResources().getString(R.string.login_failed_message));
                            //Toast.makeText(Login.this, getResources().getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
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


    //---------------Banner Ad---------------//
    private void showPersonalizedAds(LinearLayout linearLayout) {

        if (Constant.settings.isBanner_ad()) {
            AdView adView = new AdView(_context);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            adView.setAdUnitId(Constant.settings.getBanner_ad_id());
            adView.setAdSize(AdSize.SMART_BANNER);
            linearLayout.addView(adView);
            adView.loadAd(adRequest);
        }
    }

    private void showNonPersonalizedAds(LinearLayout linearLayout) {
        Bundle extras = new Bundle();
        extras.putString("npa", "1");
        if (Constant.settings.isBanner_ad()) {
            AdView adView = new AdView(_context);
            AdRequest adRequest = new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                    .build();
            adView.setAdUnitId(Constant.settings.getBanner_ad_id());
            adView.setAdSize(AdSize.SMART_BANNER);
            linearLayout.addView(adView);
            adView.loadAd(adRequest);
        }
    }

    public void showBannerAd(LinearLayout linearLayout) {
        Log.d("Response-ads", Boolean.toString(Constant.settings.isBanner_ad()));
        if (ConsentInformation.getInstance(_context).getConsentStatus() == ConsentStatus.NON_PERSONALIZED) {
            showNonPersonalizedAds(linearLayout);
        } else {
            showPersonalizedAds(linearLayout);
        }
    }
    //---------------Banner Ad---------------//

    //---------------Rewarded video ad---------------//

    public void showVideoAd(final Activity activity, final String uid, final String Device) {
        if (Constant.settings.isRewarded_video_ads()) {
            Video_ads_limit_count(activity, uid, Device);
            if (Constant.VIDEO_AD_COUNT <= Integer.parseInt(Constant.settings.getVideo_ads_limit())) {
                final ProgressDialog progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage(activity.getString(R.string.adsloading));
                progressDialog.setCancelable(false);
                progressDialog.show();
                mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
                if (Constant.settings != null) {
                    if (Constant.settings.isRewarded_video_ads()) {
                        if (mRewardedVideoAd != null) {
                            AdRequest adRequest;
                            if (ConsentInformation.getInstance(activity).getConsentStatus() == ConsentStatus.PERSONALIZED) {
                                adRequest = new AdRequest.Builder().build();
                            } else {
                                Bundle extras = new Bundle();
                                extras.putString("npa", "1");
                                adRequest = new AdRequest.Builder()
                                        .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                                        .build();
                            }

                            mRewardedVideoAd.loadAd(Constant.settings.getRewarded_video_ads_id(), adRequest);
                            mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                                @Override
                                public void onRewarded(RewardItem reward) {
                                    Log.d("reward_video_ad", "reward");

                                    Toast.makeText(activity, "Reward Point Added", Toast.LENGTH_SHORT).show();
                                    BalanceUpdate(uid, "Video Ads", Constant.settings.getVideo_add_point(), Constant.PublicIP, 1);
                                    Ads_Count_Update(uid, Device);
                                    Video_ads_limit_count(activity, uid, Device);
                                }

                                @Override
                                public void onRewardedVideoAdLeftApplication() {
                                    Log.d("reward_video_ad", "AdLeftApplication");
                                }

                                @Override
                                public void onRewardedVideoAdFailedToLoad(int i) {
                                    progressDialog.dismiss();
                                    Toast.makeText(activity, "Video Ads load Failed", Toast.LENGTH_SHORT).show();
                                    Log.d("reward_video_ad", "Failed");
                                }

                                @Override
                                public void onRewardedVideoAdClosed() {
                                    Events.VideoAdsReload adsReload = new Events.VideoAdsReload("100");
                                    EventBus.getDefault().post(adsReload);
                                    Log.d("reward_video_ad", "close");


                                }

                                @Override
                                public void onRewardedVideoAdLoaded() {
                                    //  mRewardedVideoAd.show();
                                    progressDialog.dismiss();
                                    Toast.makeText(activity, "Video Ads load", Toast.LENGTH_SHORT).show();
                                    Log.d("reward_video_ad", "Video Ads load");
                                }

                                @Override
                                public void onRewardedVideoAdOpened() {
                                    Log.d("reward_video_ad", "open");
                                }

                                @Override
                                public void onRewardedVideoStarted() {
                                    Log.d("reward_video_ad", "start");
                                }

                                @Override
                                public void onRewardedVideoCompleted() {
                                    Log.d("reward_video_ad", "completed");

                                }
                            });
                        }
                    } else {
                        progressDialog.dismiss();

                    }
                } else {
                    progressDialog.dismiss();
                }
            } else {
                Toast.makeText(activity, "Daily Ads Limit Over", Toast.LENGTH_SHORT).show();
            }
        } else if (startAppAd != null) {

            startAppAd.setVideoListener(new VideoListener() {
                @Override
                public void onVideoCompleted() {
                    // Grant user with the reward


                    Toast.makeText(activity, "Reward Point Added", Toast.LENGTH_SHORT).show();
                    BalanceUpdate(uid, "Video Ads", Constant.settings.getVideo_add_point(), Constant.PublicIP, 1);
                    Ads_Count_Update(uid, Device);
                    Video_ads_limit_count(activity, uid, Device);
                }
            });

            startAppAd.showAd();

            startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO);


        } else {
            Toast.makeText(activity, "Daily Ads Limit Over", Toast.LENGTH_SHORT).show();
        }
    }

    //---------------Rewarded video ad---------------//

    //---------------Interstitial Ad---------------//

    public void loadInter(Activity activity) {
        mInterstitial = new InterstitialAd(activity);
        if (Constant.settings.isInterstital_ad()) {
            Constant.AD_COUNT = Constant.AD_COUNT + 1;
            if (Constant.AD_COUNT == Integer.parseInt(Constant.settings.getAds_frequency_limit())) {
                Constant.AD_COUNT = 0;
                AdRequest adRequest;
                if (ConsentInformation.getInstance(activity).getConsentStatus() == ConsentStatus.PERSONALIZED) {
                    adRequest = new AdRequest.Builder().build();
                } else {
                    Bundle extras = new Bundle();
                    extras.putString("npa", "1");
                    adRequest = new AdRequest.Builder()
                            .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                            .build();
                }
                mInterstitial.setAdUnitId(Constant.settings.getInterstital_ad_id());
                mInterstitial.loadAd(adRequest);
                mInterstitial.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                    }

                    @Override
                    public void onAdLeftApplication() {
                        super.onAdLeftApplication();
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }
                });
            }
        }
    }

    //---------------Interstitial Ad---------------//

    public void Video_ads_limit_count(final Activity activity, final String uid, final String Device) {
        String login = RestAPI.API_Video_Ads_Count + "&user_id=" + uid + "&device_id=" + Device;
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
                            int Counter = Integer.parseInt(object.getString("ads_count"));
                            Constant.VIDEO_AD_COUNT = Counter;

                            int CountLimit = Integer.parseInt(Constant.settings.getDaily_spin_limit());

                        } else {

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void Ads_Count_Update(final String uid, final String Device) {
        String login = RestAPI.API_Video_Ads_Count_update + "&user_id=" + uid + "&device_id=" + Device;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(login, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response-count", new String(responseBody));
                String res = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray(Constant.AppSid);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String success = object.getString("success");

                        if (success.equals("1")) {
                        } else {
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }


}
