package com.xitij.spintoearn.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.xitij.spintoearn.Interface.AdConsentListener;
import com.xitij.spintoearn.Models.UserBalance;
import com.xitij.spintoearn.R;
import com.xitij.spintoearn.Spinner.SpinningWheelView;
import com.xitij.spintoearn.Util.AdsConsent;
import com.xitij.spintoearn.Util.BackgroundSoundService;
import com.xitij.spintoearn.Util.Constant;
import com.xitij.spintoearn.Util.Method;
import com.google.ads.consent.ConsentStatus;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class BidSpin extends AppCompatActivity implements SpinningWheelView.OnRotationListener<String>{
    private SpinningWheelView spinningWheelView;
    private Button btn1,btn2,btn3,btn4,btn5;
    Toolbar toolbar;
    public static String sel_item;
    private Constant constant;
    private TextView point,txtbidamount;
    private String Bidamount="0";
    private Method method;
    private AdsConsent adsConsent;
    private LinearLayout linearLayout;
    private boolean btnselect = false;
    ImageView btnPlay;
    private InterstitialAd interstitialAd_f;
    MediaPlayer player;
    private MediaPlayer player3;
    private MediaPlayer player4;
    private InterstitialAd interstitialAd_f_back;
    private Dialog main_dialogue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //startService(new Intent(BidSpin.this, BackgroundSoundService.class));


        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_luck);

        AssetFileDescriptor afd3 = null;
        try {
            afd3 = getAssets().openFd("win.mp3");
            player3 = new MediaPlayer();
            player3.setDataSource(afd3.getFileDescriptor(), afd3.getStartOffset(), afd3.getLength());

        }catch (Exception e){
            e.printStackTrace();
        }

        AssetFileDescriptor afd4 = null;
        try {
            afd4 = getAssets().openFd("fail.wav");
            player4 = new MediaPlayer();
            player4.setDataSource(afd4.getFileDescriptor(), afd4.getStartOffset(), afd4.getLength());

        }catch (Exception e){
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
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


        interstitialAd_f_back=new com.facebook.ads.InterstitialAd(this,getString(R.string.facebook_inter));
        interstitialAd_f_back.loadAd();

        loadfbads();

        com.facebook.ads.AdView adView1= new com.facebook.ads.AdView(BidSpin.this,getString(R.string.facebook_banner), AdSize.BANNER_HEIGHT_50);
        RelativeLayout adcontainer=findViewById(R.id.banner_container);
        adcontainer.addView(adView1);
        adView1.loadAd();

        constant=new Constant(this);
        spinningWheelView =(SpinningWheelView)findViewById(R.id.luckywheel);
        btnPlay=findViewById(R.id.btnPlay);
        btn1=(Button)findViewById(R.id.btnlucpoint1);
        btn2=(Button)findViewById(R.id.btnlucpoint2);
        btn3=(Button)findViewById(R.id.btnlucpoint3);
        btn4=(Button)findViewById(R.id.btnlucpoint4);
        btn5=(Button)findViewById(R.id.btnlucpoint5);
        point = (TextView) findViewById(R.id.point);
        txtbidamount=(TextView)findViewById(R.id.txtbidamount);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayout_luck);
        point.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(BidSpin.this, R.drawable.coins), null, null, null);
        toolbar =(Toolbar)findViewById(R.id.toolbarluck);
        toolbar.getBackground().setAlpha(0);
        toolbar.setBackgroundResource(R.color.colorPrimaryDark);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        method=new Method(this);
        txtbidamount.setText("Select your bid Coins");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
        Method.UserBalance(constant.sharedPreferences.getString(constant.profileId,"profileId"),point);

      /*  List<String> itm=new ArrayList<>();
        itm.add("40");
        itm.add("60");
        itm.add("20");
        itm.add("90");
        itm.add("80");
        itm.add("50");*/

        spinningWheelView.setItems(R.array.luckyspin);
        spinningWheelView.setOnRotationListener(this);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRestback();
                btnselect=true;
                btn1.setBackground(getResources().getDrawable(R.drawable.button_luck_select));
                Bidamount=btn1.getText().toString();
                txtbidamount.setText("Your Bid Coin "+Bidamount);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRestback();
                btnselect=true;
                btn2.setBackground(getResources().getDrawable(R.drawable.button_luck_select));
                Bidamount=btn2.getText().toString();
                txtbidamount.setText("Your Bid Coin "+Bidamount);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRestback();
                btnselect=true;
                btn3.setBackground(getResources().getDrawable(R.drawable.button_luck_select));
                Bidamount=btn3.getText().toString();
                txtbidamount.setText("Your Bid Coin "+Bidamount);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRestback();
                btnselect=true;
                btn4.setBackground(getResources().getDrawable(R.drawable.button_luck_select));
                Bidamount=btn4.getText().toString();
                txtbidamount.setText("Your Bid Coin "+Bidamount);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRestback();
                btnselect=true;
                btn5.setBackground(getResources().getDrawable(R.drawable.button_luck_select));
                Bidamount=btn5.getText().toString();
                txtbidamount.setText("Your Bid Coin "+Bidamount);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    player4.prepare();
                    player3.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                Method.UserBalance(constant.sharedPreferences.getString(constant.profileId,"profileId"),point);
                int pnt =Integer.parseInt(point.getText().toString());
                int bidam=Integer.parseInt(Bidamount);
         if(btnselect){

            if( pnt >= bidam ){


                LayoutInflater dialogue_layout=LayoutInflater.from(BidSpin.this);
                final View dialogueview = dialogue_layout.inflate(R.layout.alert_native_lout,null);
                main_dialogue=new Dialog(BidSpin.this);
                main_dialogue.setContentView(dialogueview);
                final Button btn_cancel,btn_claim;
                final TextView tv_alert_msg;



              /*  com.facebook.ads.AdView adView2=new com.facebook.ads.AdView(BidSpin.this,getString(R.string.facebook_banner),AdSize.BANNER_HEIGHT_50);
                RelativeLayout adcontainer=dialogueview.findViewById(R.id.banner_container);
                adcontainer.addView(adView2);
                adView2.loadAd();*/



                btn_cancel=dialogueview.findViewById(R.id.btn_cancel);
                btn_claim=dialogueview.findViewById(R.id.btn_claim);
                btn_cancel.setText("No");
                btn_claim.setText("Yes");

                tv_alert_msg=dialogueview.findViewById(R.id.tv_alert_msg);
               tv_alert_msg.setText("Do you really want to play ?");

                WindowManager.LayoutParams layoutParams=new WindowManager.LayoutParams();
                layoutParams.copyFrom(main_dialogue.getWindow().getAttributes());
                layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
                layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
                main_dialogue.getWindow().setAttributes(layoutParams);
                main_dialogue.setCancelable(false);

                btn_claim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        player.start();

                        String ip= Constant.PublicIP;
                        Method.BalanceUpdate(constant.sharedPreferences.getString(constant.profileId,"profileId"),"Spin bid","-"+Bidamount,ip,point,0);
                        Toast.makeText(BidSpin.this, Bidamount+" Coins Debited ", Toast.LENGTH_LONG).show();
                        spinningWheelView.rotate(60,5000,60);
                        main_dialogue.dismiss();
                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        main_dialogue.dismiss();
                    }
                });
                main_dialogue.show();


            }
            else {
                Toast.makeText(BidSpin.this, "You don't have a sufficient Balance", Toast.LENGTH_LONG).show();
                txtbidamount.setText("Select your bid amount");
                btnRestback();
            }
           }
           else{ Toast.makeText(BidSpin.this, "Please Select your bid Amount", Toast.LENGTH_LONG).show();}
            }
        });
        adsConsent=new AdsConsent(this, new AdConsentListener() {
            @Override
            public void onConsentUpdate(ConsentStatus consentStatus) {
                method.showBannerAd(linearLayout);
            }

        });
        adsConsent.checkForConsent();
    }



    private void loadfbads() {
        interstitialAd_f=new com.facebook.ads.InterstitialAd(this,getString(R.string.facebook_inter));
        interstitialAd_f.loadAd();
        interstitialAd_f.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

               loadfbads();

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                loadfbads();

            }

            @Override
            public void onError(Ad ad, AdError adError) {

                loadfbads();

            }

            @Override
            public void onAdLoaded(Ad ad) {



            }

            @Override
            public void onAdClicked(Ad ad) {

                loadfbads();

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

    }

    private void btnRestback(){
        btnselect=false;
        btn1.setBackground(getResources().getDrawable(R.drawable.button_luck));
        btn2.setBackground(getResources().getDrawable(R.drawable.button_luck));
        btn3.setBackground(getResources().getDrawable(R.drawable.button_luck));
        btn4.setBackground(getResources().getDrawable(R.drawable.button_luck));
        btn5.setBackground(getResources().getDrawable(R.drawable.button_luck));
    }

    @Override
    public void onRotation() {
        Log.d("XXXX", "On Rotation");
    }
    @Override
    public void onStopRotation(final String item) {

        if (player.isPlaying()){
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
                if(item.equals(Bidamount)){

                    player3.start();


                    LayoutInflater dialogue_layout=LayoutInflater.from(BidSpin.this);
                    final View dialogueview = dialogue_layout.inflate(R.layout.alert_native_lout,null);
                    main_dialogue=new Dialog(BidSpin.this);
                    main_dialogue.setContentView(dialogueview);
                    final Button btn_cancel,btn_claim;
                    final TextView tv_alert_msg;


                 /*   com.facebook.ads.AdView adView2=new com.facebook.ads.AdView(BidSpin.this,getString(R.string.facebook_banner),AdSize.BANNER_HEIGHT_50);
                    RelativeLayout adcontainer=dialogueview.findViewById(R.id.banner_container);
                    adcontainer.addView(adView2);
                    adView2.loadAd();
*/

                    btn_cancel=dialogueview.findViewById(R.id.btn_cancel);
                    btn_claim=dialogueview.findViewById(R.id.btn_claim);
                    btn_cancel.setText("Cancel");
                    btn_claim.setText("Claim");

                    tv_alert_msg=dialogueview.findViewById(R.id.tv_alert_msg);
                    tv_alert_msg.setText("Congratulations!! You win "+Double.parseDouble(item)*2+" coins !!");

                    WindowManager.LayoutParams layoutParams=new WindowManager.LayoutParams();
                    layoutParams.copyFrom(main_dialogue.getWindow().getAttributes());
                    layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
                    layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
                    main_dialogue.getWindow().setAttributes(layoutParams);
                    main_dialogue.setCancelable(false);

                    btn_claim.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ShowFbAds();

                            String ip= Constant.PublicIP;
                            int itm =Integer.parseInt(item)*2;
                            Method.BalanceUpdate(constant.sharedPreferences.getString(constant.profileId,"profileId"),"Win bid",Integer.toString(itm),ip,point,1);
                            Toast.makeText(BidSpin.this, Double.parseDouble(item)*2+" Coin Added", Toast.LENGTH_LONG).show();

                            txtbidamount.setText("Select your bid amount");
                            btnRestback();
                            main_dialogue.dismiss();

                        }
                    });

                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            main_dialogue.dismiss();
                        }
                    });
                    main_dialogue.show();

                }else {

                    player4.start();
                    Toast.makeText(BidSpin.this, "You Loss Try Again!", Toast.LENGTH_LONG).show();
                    txtbidamount.setText("Select your bid amount");
                    btnRestback();
                }

            }catch (Exception e){}
        } else {
            Toast.makeText(BidSpin.this, "Try Again!", Toast.LENGTH_LONG).show();
            txtbidamount.setText("Select your bid amount");
            btnRestback();
        }
    }

    private void ShowFbAds() {

        if (interstitialAd_f.isAdLoaded()){
            interstitialAd_f.show();
        }

    }

    @Override
    public void onBackPressed() {

        if(interstitialAd_f_back.isAdLoaded()){
            interstitialAd_f_back.show();
        }
        UserBalance rewardNotify = new UserBalance("100");
        EventBus.getDefault().post(rewardNotify);
        super.onBackPressed();
    }


    @Override
    protected void onResume() {

        startService(new Intent(this, BackgroundSoundService.class));

        super.onResume();
    }

    @Override
    protected void onPause() {
        stopService(new Intent(BidSpin.this, BackgroundSoundService.class));
        super.onPause();
    }

    protected void onDestroy() {
        //stop service and stop music
        stopService(new Intent(BidSpin.this, BackgroundSoundService.class));
        super.onDestroy();
    }


}

