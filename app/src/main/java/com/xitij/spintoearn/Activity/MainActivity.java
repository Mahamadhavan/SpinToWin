package com.xitij.spintoearn.Activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;

import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.RewardedVideoAd;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.xitij.spintoearn.Interface.AdConsentListener;
import com.xitij.spintoearn.Models.Settings;
import com.xitij.spintoearn.Models.SpinCounter;
import com.xitij.spintoearn.Models.UserBalance;
import com.xitij.spintoearn.R;
import com.xitij.spintoearn.Spinner.SpinningWheelView;
import com.xitij.spintoearn.Util.AdsConsent;
import com.xitij.spintoearn.Util.BackgroundSoundService;
import com.xitij.spintoearn.Util.Constant;
import com.xitij.spintoearn.Util.Ex;
import com.xitij.spintoearn.Util.GlobalBus;
import com.xitij.spintoearn.Util.Method;
import com.xitij.spintoearn.Util.RestAPI;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentStatus;
import com.google.android.gms.ads.AdView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import pl.droidsonroids.gif.GifImageView;


import static com.xitij.spintoearn.Util.Constant.DeviceID;


public class MainActivity extends AppCompatActivity implements SpinningWheelView.OnRotationListener<String> {
    private SpinningWheelView wheelView;
    private ImageView btnspin;
    Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private TextView point, txtEmail, txtPname, txtPhone;
    private Constant constant;
    private View header;
    public static String sel_item;
    public final String con = "0";
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private ProgressDialog progressDialog;
    private SpinCounter spinCounter;
    private static String Count;
    private AdView adView;
    public static LinearLayout linearLayout;
    private ConsentForm form;
    private Method method;
    AdsConsent adsConsent;
    private InterstitialAd interstitialAd_f;
    private MediaPlayer player, player2;
    private Animation a;
    private GifImageView gifImageView;
    private MediaPlayer player3;
    private Dialog main_dialogue;
    private RewardedVideoAd rewardedVideoAd;

    InterstitialAd interstitialAd;
    Button btn_leader;

    StartAppAd startAppAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        startService(new Intent(this, BackgroundSoundService.class));

        super.onCreate(savedInstanceState);

        startAppAd = new StartAppAd(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        gifImageView = findViewById(R.id.gif);
        a = AnimationUtils.loadAnimation(this, R.anim.rotate);

        loadAds();

        Banner banner = findViewById(R.id.startAppBanner);

        banner.loadAd(350, 50);

/*
        rewardedVideoAd = new RewardedVideoAd(this, getString(R.string.facebook_reward));
        rewardedVideoAd.loadAd();
*/


/*
        player2=new MediaPlayer();
        AssetFileDescriptor afd2 = null;
        try {
            afd2=getAssets().openFd("backgr.mp3");
            player2.setDataSource(afd2.getFileDescriptor(),afd2.getStartOffset(),afd2.getLength());
            player2.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player2.setLooping(true);

        player2.setVolume(30,30);
        player2.start();


*/

        AssetFileDescriptor afd3 = null;
        try {
            afd3 = getAssets().openFd("win.mp3");
            player3 = new MediaPlayer();
            player3.setDataSource(afd3.getFileDescriptor(), afd3.getStartOffset(), afd3.getLength());

        } catch (Exception e) {
            e.printStackTrace();
        }


        AssetFileDescriptor afd = null;
        try {
            afd = getAssets().openFd("back.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
        player = new MediaPlayer();
        try {
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Loadfbads();
        LoadSpinintFB();

        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                LoadSpinintFB();
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                LoadSpinintFB();

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        com.facebook.ads.AdView adView1 = new com.facebook.ads.AdView(MainActivity.this, getString(R.string.facebook_banner), AdSize.BANNER_HEIGHT_50);
        RelativeLayout adcontainer = findViewById(R.id.banner_container);
        adcontainer.addView(adView1);
        adView1.loadAd();

        adView1.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                //Toast.makeText(MainActivity.this, adError.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        constant = new Constant(MainActivity.this);
        method = new Method(this);
        wheelView = (SpinningWheelView) findViewById(R.id.wheel);
        btnspin = findViewById(R.id.buttonSpin);
        point = (TextView) findViewById(R.id.point);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        linearLayout = (LinearLayout) findViewById(R.id.adView);

        toolbar.getBackground().setAlpha(0);
        toolbar.setBackgroundResource(R.color.colorPrimaryDark);
        setSupportActionBar(toolbar);
        point.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(MainActivity.this, R.drawable.coins), null, null, null);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        GlobalBus.getBus().register(this);
        wheelView.setItems(R.array.dummy);
        btn_leader = findViewById(R.id.btn_leader);


        wheelView.setOnRotationListener(this);

        setupnavigationDrawer();

        progressDialog = new ProgressDialog(MainActivity.this);

        // navigationView.setBackgroundResource(R.drawable.menu_background);
        header = navigationView.getHeaderView(0);
        txtEmail = (TextView) header.findViewById(R.id.email);
        txtPname = (TextView) header.findViewById(R.id.textViewName);
        txtPhone = (TextView) header.findViewById(R.id.textViewPhone);
        txtEmail.setText(constant.sharedPreferences.getString(constant.userEmail, "userEmail"));
        txtPname.setText(constant.sharedPreferences.getString(constant.userName, "userName"));
        txtPhone.setText(constant.sharedPreferences.getString(constant.userPhone, "userPhone"));
        Ex.getIPaddress();
        DeviceID = GetDeviceID();
        Method.UserBalance(constant.sharedPreferences.getString(constant.profileId, "profileId"), point);
        LoadSettings();
        btnspin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //gifImageView.startAnimation(a);

                try {
                    player3.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                loadAds();

                player.start();

                String login = RestAPI.API_Spin_Count + "&user_id=" + constant.sharedPreferences.getString(constant.profileId, "profileId") + "&device_id=" + DeviceID;
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
                                    int Counter = Integer.parseInt(object.getString("spin_count"));

                                    int CountLimit = Integer.parseInt(Constant.settings.getDaily_spin_limit());

                                    if (Counter >= CountLimit) {
                                        Toast.makeText(MainActivity.this, "Your Daily Limit Over !! Try Tomorrow", Toast.LENGTH_LONG).show();
                                    } else {
                                        Spin_Count(constant.sharedPreferences.getString(constant.profileId, "profileId"), DeviceID);
                                        wheelView.rotate(60, 5000, 60);
                                    }

                                } else {
                                    Toast.makeText(MainActivity.this, "No Internet Connection!! Or API Error", Toast.LENGTH_LONG).show();
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
        });


        adsConsent = new AdsConsent(this, new AdConsentListener() {
            @Override
            public void onConsentUpdate(ConsentStatus consentStatus) {
                method.showBannerAd(linearLayout);
                method.loadInter(MainActivity.this);
            }

        });
    }

    private void LoadSpinintFB() {
        interstitialAd = new com.facebook.ads.InterstitialAd(this, getString(R.string.facebook_inter));
        interstitialAd.loadAd();
    }

    @Override
    protected void onResume() {

        startService(new Intent(this, BackgroundSoundService.class));
        super.onResume();
    }

    @Override
    protected void onPause() {
        stopService(new Intent(MainActivity.this, BackgroundSoundService.class));
        super.onPause();
    }

    protected void onDestroy() {
        //stop service and stop music
        stopService(new Intent(MainActivity.this, BackgroundSoundService.class));
        super.onDestroy();
    }

    private void Loadfbads() {

        interstitialAd_f = new com.facebook.ads.InterstitialAd(this, getString(R.string.facebook_inter));
        interstitialAd_f.loadAd();
        interstitialAd_f.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

                Loadfbads();

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

                Loadfbads();

            }

            @Override
            public void onError(Ad ad, AdError adError) {

                Loadfbads();

            }

            @Override
            public void onAdLoaded(Ad ad) {

                shoadsWithDelay();

            }

            @Override
            public void onAdClicked(Ad ad) {

                Loadfbads();

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

    }

    private void shoadsWithDelay() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (interstitialAd_f.isAdLoaded()) {
                    interstitialAd_f.show();
                }

            }
        }, 1000 * 60 * 3);

    }

    public void Spin_Count(final String uid, final String Device) {
        String login = RestAPI.API_Spin_Count_Update + "&user_id=" + uid + "&device_id=" + Device;
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

    @Override
    public void onRotation() {


        Log.d("Rotation", "On Rotation");
    }

    @Override
    public void onStopRotation(final String item) {

        gifImageView.clearAnimation();
        player3.start();
        if (player.isPlaying()) {
            player.stop();
        }
        try {
            player.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!item.equalsIgnoreCase("Oops!")) {
            sel_item = item;

            try {


                LayoutInflater dialogue_layout = LayoutInflater.from(MainActivity.this);
                final View dialogueview = dialogue_layout.inflate(R.layout.alert_native_lout, null);
                main_dialogue = new Dialog(MainActivity.this);
                main_dialogue.setContentView(dialogueview);
                final Button btn_cancel, btn_claim;
                final TextView tv_alert_msg, tv_count;

/*
                com.facebook.ads.AdView adView2=new com.facebook.ads.AdView(MainActivity.this,getString(R.string.facebook_banner),AdSize.BANNER_HEIGHT_50);
                RelativeLayout adcontainer=dialogueview.findViewById(R.id.banner_container);
                adcontainer.addView(adView2);
                adView2.loadAd();*/

                btn_cancel = dialogueview.findViewById(R.id.btn_cancel);
                btn_claim = dialogueview.findViewById(R.id.btn_claim);
                btn_cancel.setText("Cancel");
                btn_claim.setText("Claim");

                tv_alert_msg = dialogueview.findViewById(R.id.tv_alert_msg);
                tv_count = dialogueview.findViewById(R.id.tv_count);
                tv_count.setTextSize(TypedValue.COMPLEX_UNIT_PX, 100);


                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(main_dialogue.getWindow().getAttributes());
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                main_dialogue.getWindow().setAttributes(layoutParams);
                main_dialogue.setCancelable(false);

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        main_dialogue.dismiss();

                        if (interstitialAd.isAdLoaded()) {
                            interstitialAd.show();
                        } else {

                            if (Constant.mInterstitial.isLoaded()) {
                                Constant.mInterstitial.show();
                                method.loadInter(MainActivity.this);
                            }

                            if (startAppAd != null && startAppAd.isReady()) {

                                startAppAd.showAd(MainActivity.this);

                            }

                        }


                    }
                });

                btn_claim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getClaim(item);

                    }
                });

                main_dialogue.show();

                new CountDownTimer(Constant.Waitsecond, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        btn_claim.setEnabled(false);  //BUTTON_POSITIVE is False
                        tv_count.setText("" + (millisUntilFinished / 1000) + "");
                        tv_alert_msg.setText("Please Wait to " + (millisUntilFinished / 1000) + " seconds for add coins into wallet!!");

                    }

                    @Override
                    public void onFinish() {

                        btn_claim.setEnabled(true); //BUTTON_POSITIVE is Enable
                    }
                }.start();


            } catch (Exception e) {
            }
        } else {
            Toast.makeText(MainActivity.this, "Try Again!", Toast.LENGTH_LONG).show();
        }
    }

    public void loadAds() {

        startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC, new AdEventListener() {
            @Override
            public void onReceiveAd(com.startapp.sdk.adsbase.Ad ad) {
//                Utility.logE("startAppAd ==>> onReceiveAd");
            }

            @Override
            public void onFailedToReceiveAd(com.startapp.sdk.adsbase.Ad ad) {
//                Utility.logE("startAppAd ==>> onFailedToReceiveAd");
            }

        });

        startAppAd.setVideoListener(new VideoListener() {
            @Override
            public void onVideoCompleted() {
//                Utility.logE("startAppAd ==>> onVideoCompleted");
            }
        });

    }

    public void showVideoAds() {

        if (startAppAd.isReady()) {
            startAppAd.showAd();
        }

        loadAds();
    }


    private void getClaim(String item) {
        String ip = Constant.PublicIP;
        Method.BalanceUpdate(constant.sharedPreferences.getString(constant.profileId, "profileId"), "Spin", item, ip, point, 1);
        Toast.makeText(MainActivity.this, item + " Coin Added", Toast.LENGTH_LONG).show();

                 /*       rewardedVideoAd.isAdLoaded()){
                            rewardedVideoAd.show();*/

        if (interstitialAd.isAdLoaded()) {
            interstitialAd.show();
        } else {
            if (Constant.mInterstitial.isLoaded()) {
                Constant.mInterstitial.show();
            }


            if (startAppAd != null && startAppAd.isReady()) {
                startAppAd.showAd(MainActivity.this);

            }
        }

        method.loadInter(MainActivity.this);


        main_dialogue.dismiss();
    }

    private void setupnavigationDrawer() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {


                    case R.id.lucky: {
                        drawer.closeDrawer(Gravity.START, false);
                        Intent tran = new Intent(MainActivity.this, BidSpin.class);
                        startActivity(tran);
                        break;
                    }
                    case R.id.wallet: {
                        drawer.closeDrawer(Gravity.START, false);
                        Intent tran = new Intent(MainActivity.this, Wallet.class);
                        startActivity(tran);
                        break;
                    }
                    case R.id.mnvideozone: {
                        drawer.closeDrawer(Gravity.START, false);
                        startActivity(new Intent(MainActivity.this, VideoZone.class));
                        break;
                    }


                    case R.id.leaderboard: {
                        drawer.closeDrawer(Gravity.START, false);
                        Intent intent = new Intent(MainActivity.this, Leaderboard.class);
                        startActivity(intent);
                        break;

                    }
                    case R.id.mnshare: {
                        ShareApp();
                        break;
                    }
                    case R.id.mnrefer: {
                        drawer.closeDrawer(Gravity.START, false);
                        Intent tran = new Intent(MainActivity.this, ReferCode.class);
                        startActivity(tran);
                        break;
                    }

                    case R.id.mnmore: {
                        drawer.closeDrawer(Gravity.START, false);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                        break;
                    }
                    case R.id.mncontact: {
                        drawer.closeDrawer(Gravity.START, false);
                        Intent homeIntent1 = new Intent(MainActivity.this, ContactUs.class);
                        startActivity(homeIntent1);
                        break;
                    }

                    case R.id.mnprivacy: {
                        drawer.closeDrawer(Gravity.START, false);
                        Intent homeIntent1 = new Intent(MainActivity.this, PrivatePolice.class);
                        startActivity(homeIntent1);
                        break;
                    }
                    case R.id.mnabout_us: {
                        drawer.closeDrawer(Gravity.START, false);
                        Intent homeIntent1 = new Intent(MainActivity.this, AboutUs.class);
                        startActivity(homeIntent1);
                        break;
                    }
                    case R.id.mnlogout: {
                        SharedPreferences preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        Method.UserLoginLogs(constant.sharedPreferences.getString(constant.profileId, "profileId"), "Logout", Constant.DeviceID);
                        editor.clear();
                        editor.commit();
                        finish();
                        Intent homeIntent = new Intent(MainActivity.this, Login.class);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(homeIntent);
                        //  Intent homeIntent1 = new Intent(MainActivity.this, Privacy.class);
                        //   startActivity(homeIntent1);
                        break;

                    }

                }

                return true;
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void ShareApp() {
        try {
            String shareBody = "https://play.google.com/store/apps/details?id=" + getPackageName();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I have earned cash using this app. You can also earn download app - \n" + shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share Using"));
        } catch (Exception e) {
            Log.e("Sahare error", e.getMessage());
        }
    }

    public void LoadSettings() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(RestAPI.API_Settings, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response-ls", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant.AppSid);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String app_name = object.getString("app_name");
                        String app_logo = object.getString("app_logo");
                        String app_version = object.getString("app_version");
                        String app_author = object.getString("app_author");
                        String app_contact = object.getString("app_contact");
                        String app_email = object.getString("app_email");
                        String app_website = object.getString("app_website");
                        String app_description = object.getString("app_description");
                        String app_developed_by = object.getString("app_developed_by");
                        String app_faq = object.getString("app_faq");
                        String app_privacy_policy = object.getString("app_privacy_policy");
                        String publisher_id = object.getString("publisher_id");
                        boolean interstital_ad = Boolean.parseBoolean(object.getString("interstital_ad"));
                        String interstital_ad_id = object.getString("interstital_ad_id");
                        String interstital_ad_click = object.getString("interstital_ad_click");
                        boolean banner_ad = Boolean.parseBoolean(object.getString("banner_ad"));
                        String banner_ad_id = object.getString("banner_ad_id");
                        boolean rewarded_video_ads = Boolean.parseBoolean(object.getString("rewarded_video_ads"));
                        String rewarded_video_ads_id = object.getString("rewarded_video_ads_id");
                        String redeem_currency = object.getString("redeem_currency");
                        String redeem_points = object.getString("redeem_points");
                        String redeem_money = object.getString("redeem_money");
                        String minimum_redeem_points = object.getString("minimum_redeem_points");
                        String payment_method1 = object.getString("payment_method1");
                        String payment_method2 = object.getString("payment_method2");
                        String payment_method3 = object.getString("payment_method3");
                        String payment_method4 = object.getString("payment_method4");
                        String daily_spin_limit = object.getString("daily_spin_limit");
                        String ads_frequency_limit = object.getString("ads_frequency_limit");
                        String video_add_point = object.getString("video_add_point");
                        String app_refer_reward = object.getString("app_refer_reward");
                        String registration_reward = object.getString("registration_reward");
                        String video_ads_limit = object.getString("daily_rewarded_video_ads_limits");

                        Constant.settings = new Settings(app_name, app_logo, app_version, app_author, app_contact, app_email, app_website, app_description, app_developed_by,
                                app_faq, app_privacy_policy, publisher_id, interstital_ad_id, interstital_ad_click, banner_ad_id, rewarded_video_ads_id, redeem_currency, redeem_points,
                                redeem_money, minimum_redeem_points, payment_method1, payment_method2, payment_method3, payment_method4, interstital_ad, banner_ad, rewarded_video_ads, daily_spin_limit, ads_frequency_limit, video_add_point, app_refer_reward, registration_reward, video_ads_limit);
                        Log.d("Response-ls", ads_frequency_limit);
                    }
                    adsConsent.checkForConsent();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }


    private String GetDeviceID() {

        String deviceID = null;

//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//
//            return deviceID;
//        }

        deviceID = android.provider.Settings.Secure.getString(
                this.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

//        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        int readIMEI = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_PHONE_STATE);
//        if (deviceID == null) {
//            if (readIMEI == PackageManager.PERMISSION_GRANTED) {
//                deviceID = tm.getDeviceId().toString();
//            }
//        }
        return deviceID;
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            LayoutInflater dialogue_layout = LayoutInflater.from(MainActivity.this);
            final View dialogueview = dialogue_layout.inflate(R.layout.alert_native_lout, null);
            main_dialogue = new Dialog(MainActivity.this);
            main_dialogue.setContentView(dialogueview);
            final Button btn_cancel, btn_claim;
            final TextView tv_alert_msg;

/*
            com.facebook.ads.AdView adView2=new com.facebook.ads.AdView(MainActivity.this,getString(R.string.facebook_banner),AdSize.BANNER_HEIGHT_50);
            RelativeLayout adcontainer=dialogueview.findViewById(R.id.banner_container);
            adcontainer.addView(adView2);
            adView2.loadAd();
*/


            btn_cancel = dialogueview.findViewById(R.id.btn_cancel);
            btn_claim = dialogueview.findViewById(R.id.btn_claim);
            tv_alert_msg = dialogueview.findViewById(R.id.tv_alert_msg);

            btn_cancel.setText("NO");
            btn_claim.setText("YES");
            tv_alert_msg.setText("Do you really want to exit ?");

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(main_dialogue.getWindow().getAttributes());
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            main_dialogue.getWindow().setAttributes(layoutParams);
            main_dialogue.setCancelable(false);

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    main_dialogue.dismiss();

                }
            });

            btn_claim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            main_dialogue.show();

        }

    }

    public void onexit() {

    }

    @Subscribe
    public void onEvent(UserBalance rewardNotify) {
        UserBalance(constant.sharedPreferences.getString(constant.profileId, "profileId"));
    }

    public void UserBalance(String UserID) {
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

                            point.setText(total_point);

                            Log.d("Response-s", total_point);
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

}
