package com.xitij.spintoearn.Util;

import com.xitij.spintoearn.Interface.ApiServices;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAPI {



//    public static final String BASE_URL="http://spintoearn.xitijapp.com/";
    public static final String BASE_URL="http://spin.spectrapackautomation.com/";
    public static final String URL= BASE_URL +"/api_v1/api.php?";
    public static final String PURL=BASE_URL + "/api_v1/api_withdraw_request.php?";
    public static final String API_Registation=URL+ "user_register";
    public static final String API_Login=URL + "users_login";
    public static final String API_Device_Login=URL + "device_login";
    public static final String API_Login_Logs=URL + "user_logs";
    public static final String API_User_Balance=URL + "user_balance";
    public static final String API_Balance_Update=URL + "balance_update";
    public static final String API_Coin_History=URL + "user_coin_history";
    public static final String API_Coin_withdrawal_History = URL + "user_withdrawal_history";
    public static final String API_Refer_History=URL + "user_refer_history";
    public static final String API_Settings=URL + "settings";
    public static final String API_Spin_Count=URL + "user_coin_count";
    public static final String API_Video_Ads_Count=URL + "video_ads_count";
    public static final String API_Video_Ads_Count_update=URL + "video_ads_count_update";
    public static final String API_Spin_Count_Update=URL + "coin_count_update";
    public static final String API_Forgot_Pass=URL + "forgot_pass";
    public static final String API_Contact_Us=URL + "support_ticket";
    public static final String API_Payment_Request=PURL;

    public static Retrofit getRetrofitInstance(){

        return new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiServices getApiService(){
        return getRetrofitInstance().create(ApiServices.class);
    }
}
