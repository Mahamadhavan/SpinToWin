package com.xitij.spintoearn.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.xitij.spintoearn.Models.User;
import com.xitij.spintoearn.R;
import com.xitij.spintoearn.Util.Constant;
import com.xitij.spintoearn.Util.Ex;
import com.xitij.spintoearn.Util.RestAPI;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import cz.msebera.android.httpclient.Header;

import static com.xitij.spintoearn.Util.Constant.AppSid;

public class Register extends AppCompatActivity {

    private EditText txtName, txtEmail, txtPassword, txtRef_code, txtPhone;
    private TextView textViewLogin;


    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public void Validate_form(User user) {

        txtName.setError(null);
        txtEmail.setError(null);
        txtPassword.setError(null);
        txtRef_code.setError(null);
        if (user.getName().equals("") || user.getName().isEmpty()) {
            txtName.requestFocus();
            txtName.setError(getResources().getString(R.string.NameEditText_error));
        } else if (!isValidMail(user.getEmail()) || user.getEmail().isEmpty()) {
            txtEmail.requestFocus();
            txtEmail.setError(getResources().getString(R.string.EmailEditText_error));
        } else if (user.getPassword().equals("") || user.getPassword().isEmpty()) {
            txtPassword.requestFocus();
            txtPassword.setError(getResources().getString(R.string.PasswordEditTest_error));
        } else if (user.getPhone().equals("") || user.getPhone().isEmpty()) {
            txtPhone.requestFocus();
            txtPhone.setError(getResources().getString(R.string.PhoneeditText_error));
        } else {

            txtName.clearFocus();
            txtEmail.clearFocus();
            txtPassword.clearFocus();
            txtPhone.clearFocus();
            txtName.setText("");
            txtEmail.setText("");
            txtPassword.setText("");
            txtPhone.setText("");
            txtRef_code.setText("");

            if (Ex.isConnectionEnable(Register.this)) {
                Random generator = new Random();
                int n = generator.nextInt(9999 - 1000) + 1000;
                register(user);
            } else {
                Toast.makeText(this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String GetDeviceID(){

        String deviceID = null;

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
//            }
//        }
        return deviceID;
    }
    public String deviceid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);

        com.facebook.ads.AdView adView1= new com.facebook.ads.AdView(Register.this,getString(R.string.facebook_banner), AdSize.BANNER_HEIGHT_50);
        RelativeLayout adcontainer=findViewById(R.id.banner_container);
        adcontainer.addView(adView1);
        adView1.loadAd();

        Ex.checkAndRequestPermissions(this,this);
        txtName = (EditText) findViewById(R.id.editTextName);
        txtEmail = (EditText) findViewById(R.id.editTextEmail);
        txtPassword = (EditText) findViewById(R.id.editTextPassword);
        txtPhone = (EditText) findViewById(R.id.editTextPhoneNo);
        txtRef_code = (EditText) findViewById(R.id.editTextReferenceCode);
        textViewLogin=(TextView)findViewById(R.id.textViewLogin);
        Button btnSubmit = (Button) findViewById(R.id.buttonSubmit);
        deviceid = GetDeviceID();
        Ex.getIPaddress();
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tran = new Intent(getBaseContext(), Login.class);
                startActivity(tran);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         User user = new User(deviceid,txtName.getText().toString(), txtEmail.getText().toString(), txtPassword.getText().toString(),
                        txtPhone.getText().toString(), txtRef_code.getText().toString());
                Validate_form(user);
            }
        });
    }

    public void register(User user) {
        final ProgressDialog progressDialog = new ProgressDialog(Register.this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.setCancelable(false);


        String register = RestAPI.API_Registation +"&deviceid=" + user.getDeviceid()+ "&name=" + user.getName() + "&email=" + user.getEmail() + "&password=" + user.getPassword() + "&phone=" + user.getPhone() + "&user_refrence_code=" + user.getConfirm_code()+ "&reg_ip=" + Constant.PublicIP;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(register, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Toast.makeText(Register.this, "Registred Successfully", Toast.LENGTH_LONG).show();

                Log.d("Response-R", new String(responseBody));
                String res = new String(responseBody);

                Intent intent1=new Intent(Register.this,Login.class);
                startActivity(intent1);
                finishAffinity();

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(AppSid);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String msg = object.getString("msg");
                        String success = object.getString("success");




                        if (success.equals("1")) {

                            Intent intent=new Intent(Register.this,Login.class);
                            startActivity(intent);
                            finishAffinity();

                            progressDialog.dismiss();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);
                            alertDialogBuilder.setTitle(R.string.Congratulation);
                            alertDialogBuilder.setMessage(msg);
                            alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
                            alertDialogBuilder.setPositiveButton(Register.this.getResources().getString(R.string.ok_message),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            startActivity(new Intent(Register.this, Login.class));
                                            //Log.d("Response",msg);
                                            finishAffinity();
                                        }
                                    });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                           // Toast.makeText(Register.this, "Congratulation! Registration Successfully", Toast.LENGTH_LONG).show();

                        } else {

                            Intent intent=new Intent(Register.this,Login.class);
                            startActivity(intent);
                            finishAffinity();


                            progressDialog.dismiss();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);
                            alertDialogBuilder.setTitle(R.string.app_name);
                            alertDialogBuilder.setMessage(msg);
                            alertDialogBuilder.setPositiveButton(Register.this.getResources().getString(R.string.ok_message),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            startActivity(new Intent(Register.this, Register.class));
                                            //Log.d("Response",msg);
                                            finishAffinity();
                                        }
                                    });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                          //  Toast.makeText(Register.this, msg, Toast.LENGTH_LONG).show();

                        }
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Response-F", new String(responseBody));

                Toast.makeText(Register.this, "Failed to Register", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
