package com.xitij.spintoearn.Activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.xitij.spintoearn.Models.Settings;
import com.xitij.spintoearn.R;
import com.xitij.spintoearn.Util.Constant;
import com.xitij.spintoearn.Util.Ex;
import com.xitij.spintoearn.Util.RestAPI;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.refactor.library.SmoothCheckBox;
import cz.msebera.android.httpclient.Header;

import static com.xitij.spintoearn.Util.Ex.isValidMail;

public class Login extends AppCompatActivity {
    private Button btnReg, btnLogin, btnGuest;
    private EditText txtEmail, txtPassword;
    private String email, password, deviceid;
    private ProgressDialog progressDialog;
    private TextView tvForgatpass;
    SmoothCheckBox smoothCheckBox;


    private Constant constant;

    private String GetDeviceID() {
        String deviceID = null;

//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            deviceID = android.provider.Settings.Secure.getString(
//                    this.getContentResolver(),
//                    android.provider.Settings.Secure.ANDROID_ID);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        com.facebook.ads.AdView adView1 = new com.facebook.ads.AdView(Login.this, getString(R.string.facebook_banner), AdSize.BANNER_HEIGHT_50);
        RelativeLayout adcontainer = findViewById(R.id.banner_container);
        adcontainer.addView(adView1);
        adView1.loadAd();

        //smoothCheckBox=findViewById(R.id.checkbox_login_activity);
        btnReg = (Button) findViewById(R.id.btnRegistation);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnGuest = findViewById(R.id.btnGuest);

        progressDialog = new ProgressDialog(Login.this);
        txtEmail = (EditText) findViewById(R.id.editTextEmail);
        txtPassword = (EditText) findViewById(R.id.editTextPassword);
        tvForgatpass = (TextView) findViewById(R.id.textViewForgetPassword);
        constant = new Constant(Login.this);
        Constant.DeviceID = GetDeviceID();

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, Guest.class);
                startActivity(intent);

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = txtEmail.getText().toString();
                password = txtPassword.getText().toString();
                Validate_form();
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Register.class);
                startActivity(intent);
            }
        });
        tvForgatpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Login.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_forgetpassword);
                dialog.getWindow().setLayout(ViewPager.LayoutParams.FILL_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
                final EditText editText_forgetPassword = dialog.findViewById(R.id.editText_forget_password);
                Button buttonForgetPassword = dialog.findViewById(R.id.button_forgetPassword);
                buttonForgetPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String stringForgetPassword = editText_forgetPassword.getText().toString();
                        editText_forgetPassword.setError(null);
                        if (!isValidMail(stringForgetPassword) || stringForgetPassword.isEmpty()) {
                            editText_forgetPassword.requestFocus();
                            editText_forgetPassword.setError(getResources().getString(R.string.please_enter_email));
                        } else {
                            if (Ex.isConnectionEnable(Login.this)) {
                                forgetPassword(stringForgetPassword);
                            } else {
                                Toast.makeText(Login.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    public void Validate_form() {
        txtEmail.setError(null);
        txtPassword.setError(null);

        if (!isValidMail(email) || email.isEmpty()) {
            txtEmail.requestFocus();
            txtEmail.setError(getResources().getString(R.string.EmailEditText_error));
        } else if (password.equals("") || password.isEmpty()) {
            txtPassword.requestFocus();
            txtPassword.setError(getResources().getString(R.string.PasswordEditTest_error));
        } else {
            txtEmail.clearFocus();
            txtPassword.clearFocus();

            txtEmail.setText("");
            txtPassword.setText("");

            if (Ex.isConnectionEnable(Login.this)) {
                login(email, password);
            } else {
                Toast.makeText(this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //login using email and password
    public void login(final String sendEmail, final String sendPassword) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        String login = RestAPI.API_Login + "&email=" + sendEmail + "&password=" + sendPassword;

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
                            progressDialog.dismiss();
                            String user_id = object.getString("user_id");
                            String name = object.getString("name");
                            String sendEmail = object.getString("email");
                            String userPhone = object.getString("phone");
                            constant.sharedEditor.putBoolean(constant.isLogin, true);
                            constant.sharedEditor.putString(constant.profileId, user_id);
                            constant.sharedEditor.putString(constant.userName, name);
                            constant.sharedEditor.putString(constant.userEmail, sendEmail);
                            constant.sharedEditor.putString(constant.userPhone, userPhone);
                            constant.sharedEditor.commit();
                            LoadSettings();
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);

                        } else {
                            progressDialog.dismiss();
                            // Ex.okAlertBox(getResources().getString(R.string.login_failed_message));
                            Toast.makeText(Login.this, object.getString("msg"), Toast.LENGTH_LONG).show();
                        }

                    }

                    progressDialog.dismiss();

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
    public void onBackPressed() {
        finish();
    }

    public void forgetPassword(String sendEmail_forget_password) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        String forgetPassword_url = RestAPI.API_Forgot_Pass + "&email=" + sendEmail_forget_password;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(forgetPassword_url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant.AppSid);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String msg = object.getString("msg");
                        String success = object.getString("success");

                        if (success.equals("1")) {
                            Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    }

                    progressDialog.dismiss();

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

}