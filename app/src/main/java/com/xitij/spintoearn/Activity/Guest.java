package com.xitij.spintoearn.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.xitij.spintoearn.R;
import com.xitij.spintoearn.Spinner.SpinningWheelView;
import com.xitij.spintoearn.Util.BackgroundSoundService;
import com.xitij.spintoearn.Util.Constant;

import java.io.IOException;

public class Guest extends AppCompatActivity implements SpinningWheelView.OnRotationListener {

    private MediaPlayer player3;

    ImageView btnSpin;
    SpinningWheelView wheelView;
    ImageView imgback;
    private MediaPlayer player;
    TextView tvCoins;
    private Dialog main_dialogue;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        startService(new Intent(this, BackgroundSoundService.class));

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_guest);

        AssetFileDescriptor afd = null;
        try {
            afd = getAssets().openFd("back.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
        player = new MediaPlayer();
        try {
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AssetFileDescriptor afd3 = null;

        try {
            afd3 = getAssets().openFd("win.mp3");
            player3 = new MediaPlayer();
            player3.setDataSource(afd3.getFileDescriptor(), afd3.getStartOffset(), afd3.getLength());

        }catch (Exception e){
            e.printStackTrace();
        }

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

        com.facebook.ads.AdView adView1= new com.facebook.ads.AdView(Guest.this,getString(R.string.facebook_banner), AdSize.BANNER_HEIGHT_50);
        RelativeLayout adcontainer=findViewById(R.id.banner_container);
        adcontainer.addView(adView1);
        adView1.loadAd();

        btnSpin=findViewById(R.id.buttonSpin);
        wheelView=findViewById(R.id.wheel);
        imgback=findViewById(R.id.img_back);
        tvCoins=findViewById(R.id.tv_coins);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        wheelView.setOnRotationListener(this);
        btnSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    player3.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                player.start();
                wheelView.rotate(60, 5000, 60);

            }
        });

    }

    @Override
    public void onRotation() {

    }

    @Override
    public void onStopRotation(final Object item) {

        player3.start();

        if (player.isPlaying()) {
            player.stop();
        }
        try {
            player.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }



        LayoutInflater dialogue_layout=LayoutInflater.from(Guest.this);
        final View dialogueview = dialogue_layout.inflate(R.layout.alert_native_lout,null);
        main_dialogue=new Dialog(Guest.this);
        main_dialogue.setContentView(dialogueview);
        final Button btn_cancel,btn_claim;
        final TextView tv_alert_msg, tv_count;

        btn_cancel=dialogueview.findViewById(R.id.btn_cancel);
        btn_claim=dialogueview.findViewById(R.id.btn_claim);
        btn_cancel.setText("Cancel");
        btn_claim.setText("Claim");

        tv_alert_msg=dialogueview.findViewById(R.id.tv_alert_msg);
        tv_count=dialogueview.findViewById(R.id.tv_count);
        tv_count.setTextSize(TypedValue.COMPLEX_UNIT_PX,100);


        WindowManager.LayoutParams layoutParams=new WindowManager.LayoutParams();
        layoutParams.copyFrom(main_dialogue.getWindow().getAttributes());
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        main_dialogue.getWindow().setAttributes(layoutParams);
        main_dialogue.setCancelable(false);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                main_dialogue.dismiss();

                if (interstitialAd.isAdLoaded()){
                    interstitialAd.show();
                }
            }
        });

        btn_claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int coins=Integer.parseInt(tvCoins.getText().toString());
                int new_coins=Integer.parseInt(item.toString());
                tvCoins.setText(String.valueOf(coins+new_coins));


                if (interstitialAd.isAdLoaded()){
                    interstitialAd.show();
                }

                main_dialogue.dismiss();

            }
        });

        main_dialogue.show();

        new CountDownTimer(Constant.Waitsecond, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btn_claim.setEnabled(false);  //BUTTON_POSITIVE is False
                tv_count.setText(""+(millisUntilFinished/1000)+"");
                tv_alert_msg.setText("Please Wait to "+ (millisUntilFinished/1000)+" seconds for add coins into wallet!!");

            }
            @Override
            public void onFinish() {
                btn_claim.setEnabled(true); //BUTTON_POSITIVE is Enable
            }
        }.start();


    }

    private void LoadSpinintFB() {
        interstitialAd=new com.facebook.ads.InterstitialAd(this,getString(R.string.facebook_inter));
        interstitialAd.loadAd();
    }

    @Override
    protected void onPause() {
        stopService(new Intent(Guest.this, BackgroundSoundService.class));
        super.onPause();
    }

    protected void onDestroy() {
        //stop service and stop music
        stopService(new Intent(Guest.this, BackgroundSoundService.class));
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        startService(new Intent(this, BackgroundSoundService.class));
        super.onResume();
    }
}
