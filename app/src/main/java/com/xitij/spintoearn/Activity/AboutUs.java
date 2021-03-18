package com.xitij.spintoearn.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAd;
import com.xitij.spintoearn.R;
import com.xitij.spintoearn.Util.Constant;

public class AboutUs extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView textView_version;
    private TextView textView_appName,textView_email,textView_company;
    private WebView webView;
    private Constant constant;
    private InterstitialAd interstitialAd_f_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        interstitialAd_f_back=new com.facebook.ads.InterstitialAd(this,getString(R.string.facebook_inter));
        interstitialAd_f_back.loadAd();


        com.facebook.ads.AdView adView1= new com.facebook.ads.AdView(AboutUs.this,getString(R.string.facebook_banner), AdSize.BANNER_HEIGHT_50);
        RelativeLayout adcontainer=findViewById(R.id.banner_container);
        adcontainer.addView(adView1);
        adView1.loadAd();

        toolbar =(Toolbar)findViewById(R.id.toolbarAbout);
        textView_appName=(TextView)findViewById(R.id.textAppName);
        textView_version=(TextView)findViewById(R.id.textVersion);
        textView_company=(TextView)findViewById(R.id.textView_app_author_about_us);
        textView_email=(TextView)findViewById(R.id.textView_app_email_about_us);
        webView=(WebView)findViewById(R.id.webView_about_us);
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
        if (Constant.settings!=null){
            textView_appName.setText(Constant.settings.getApp_name());
            textView_version.setText(Constant.settings.getApp_version());
            textView_company.setText(Constant.settings.getApp_author());
            textView_email.setText(Constant.settings.getApp_email());
            webView.setBackgroundColor(Color.TRANSPARENT);
            webView.setFocusableInTouchMode(false);
            webView.setFocusable(false);
            webView.getSettings().setDefaultTextEncodingName("UTF-8");
            String mimeType = "text/html";
            String encoding = "utf-8";
            String htmlText = Constant.settings.getApp_description();

            String text = "<html><head>"
                    + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/worksans_regular.otf\")}body{font-family: MyFont;color: #8b8b8b;}"
                    + "</style></head>"
                    + "<body>"
                    + htmlText
                    + "</body></html>";
            webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);

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
