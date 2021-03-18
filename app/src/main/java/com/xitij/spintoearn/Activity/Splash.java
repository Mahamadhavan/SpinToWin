package com.xitij.spintoearn.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.ads.AudienceNetworkAds;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.xitij.spintoearn.Models.Settings;
import com.xitij.spintoearn.Models.User;
import com.xitij.spintoearn.R;
import com.xitij.spintoearn.Util.Constant;
import com.xitij.spintoearn.Util.Ex;
import com.xitij.spintoearn.Util.Method;
import com.xitij.spintoearn.Util.RestAPI;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    private Constant constant;


    private String GetDeviceID(){

        String deviceID = null;
//
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//
//            return deviceID;
//        }

        deviceID = android.provider.Settings.Secure.getString(
                this.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

//        TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        int readIMEI= ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_PHONE_STATE);
//        if(deviceID == null) {
//            if (readIMEI == PackageManager.PERMISSION_GRANTED) {
//                deviceID = tm.getDeviceId().toString();
//
//            }
//        }
        return deviceID;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        AudienceNetworkAds.initialize(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_splash);


        Constant.DeviceID = GetDeviceID();
        login(Constant.DeviceID);

        constant = new Constant(Splash.this);
        Constant.DeviceID = GetDeviceID();
        Ex.getIPaddress();
        if(Ex.isConnectionEnable(this) && Ex.checkAndRequestPermissions(this,this)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                       //login(Constant.DeviceID);
                }
            },SPLASH_TIME_OUT);
        }
    }
    public void login(final String deviceid) {
        String login = RestAPI.API_Device_Login + "&deviceid=" + deviceid;
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
                            String user_id = object.getString("user_id");
                            String name = object.getString("name");
                            String sendEmail = object.getString("email");
                            String userPhone = object.getString("phone");
                            String userCode = object.getString("user_code");
                            constant.sharedEditor.putBoolean(constant.isLogin, true);
                            constant.sharedEditor.putString(constant.profileId, user_id);
                            constant.sharedEditor.putString(constant.userName, name);
                            constant.sharedEditor.putString(constant.userEmail, sendEmail);
                            constant.sharedEditor.putString(constant.userPhone, userPhone);
                            constant.sharedEditor.putString(constant.userCode, userCode);
                            constant.sharedEditor.commit();
                            LoadSettings();
                            Constant.user =new User("00",name,sendEmail,"000",userPhone,userCode);
                            Method.UserLoginLogs(user_id,"Login",Constant.DeviceID);
                            Intent intent=new Intent(getBaseContext(),MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Intent inst = new Intent(Splash.this, Login.class);
                            inst.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(inst);
                            finish();
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
           AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Splash.this);
            alertDialogBuilder.setTitle("Server Maintenance");
            alertDialogBuilder.setMessage("System is Undergoing Maintenance. Please try again later.");
            alertDialogBuilder.setPositiveButton(getApplication().getResources().getString(R.string.ok_message),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
      });
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
                        String ads_frequency_limit= object.getString("ads_frequency_limit");
                        String video_add_point= object.getString("video_add_point");
                        String app_refer_reward= object.getString("app_refer_reward");
                        String registration_reward= object.getString("registration_reward");
                        String video_ads_limit= object.getString("daily_rewarded_video_ads_limits");

                        Constant.settings = new Settings(app_name, app_logo, app_version, app_author, app_contact, app_email, app_website, app_description, app_developed_by,
                                app_faq, app_privacy_policy, publisher_id, interstital_ad_id, interstital_ad_click, banner_ad_id, rewarded_video_ads_id, redeem_currency, redeem_points,
                                redeem_money, minimum_redeem_points, payment_method1, payment_method2, payment_method3, payment_method4, interstital_ad, banner_ad, rewarded_video_ads,daily_spin_limit,ads_frequency_limit,video_add_point,app_refer_reward,registration_reward,video_ads_limit);
                        Log.d("Response-ls",ads_frequency_limit );
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
