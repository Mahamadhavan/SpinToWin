package com.xitij.spintoearn.Models;

import android.os.Build;

import com.xitij.spintoearn.BuildConfig;

public class Settings {
    private String app_name,app_logo,app_version,app_author,app_contact,app_email,app_website,app_description,app_developed_by,app_faq,app_privacy_policy,publisher_id,interstital_ad_id,interstital_ad_click,banner_ad_id,rewarded_video_ads_id,redeem_currency,redeem_points,redeem_money,minimum_redeem_points,payment_method1,payment_method2,payment_method3,payment_method4,daily_spin_limit,ads_frequency_limit;
    private boolean interstital_ad=false;
    private boolean banner_ad=false;
    private boolean rewarded_video_ads=false;
    private String video_add_point,app_refer_reward,registration_reward,video_ads_limit;

    public Settings(String app_name, String app_logo, String app_version, String app_author, String app_contact, String app_email, String app_website, String app_description, String app_developed_by, String app_faq, String app_privacy_policy, String publisher_id, String interstital_ad_id, String interstital_ad_click, String banner_ad_id, String rewarded_video_ads_id, String redeem_currency, String redeem_points, String redeem_money, String minimum_redeem_points, String payment_method1, String payment_method2, String payment_method3, String payment_method4, boolean interstital_ad, boolean banner_ad, boolean rewarded_video_ads, String spin_limit, String adsfrequency_limit,
    String video_add_point,String app_refer_reward,String registration_reward,String video_ads_limit) {
        this.app_name = app_name;
        this.app_logo = app_logo;
        this.app_version = app_version;
        this.app_author = app_author;
        this.app_contact = app_contact;
        this.app_email = app_email;
        this.app_website = app_website;
        this.app_description = app_description;
        this.app_developed_by = app_developed_by;
        this.app_faq = app_faq;
        this.app_privacy_policy = app_privacy_policy;
        this.publisher_id = publisher_id;
        this.interstital_ad_id = interstital_ad_id;
        this.interstital_ad_click = interstital_ad_click;
        this.banner_ad_id = banner_ad_id;
        this.rewarded_video_ads_id = rewarded_video_ads_id;
        this.redeem_currency = redeem_currency;
        this.redeem_points = redeem_points;
        this.redeem_money = redeem_money;
        this.minimum_redeem_points = minimum_redeem_points;
        this.payment_method1 = payment_method1;
        this.payment_method2 = payment_method2;
        this.payment_method3 = payment_method3;
        this.payment_method4 = payment_method4;
        this.interstital_ad = interstital_ad;
        this.banner_ad = banner_ad;
        this.rewarded_video_ads = rewarded_video_ads;
        this.daily_spin_limit = spin_limit;
        this.ads_frequency_limit = adsfrequency_limit;
        this.video_add_point=video_add_point;
        this.app_refer_reward=app_refer_reward;
        this.registration_reward=registration_reward;
        this.video_ads_limit=video_ads_limit;

    }

    public String getVideo_ads_limit() {
        return video_ads_limit;
    }

    public void setVideo_ads_limit(String video_ads_limit) {
        this.video_ads_limit = video_ads_limit;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_logo() {
        return app_logo;
    }

    public void setApp_logo(String app_logo) {
        this.app_logo = app_logo;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getApp_author() {
        return app_author;
    }

    public void setApp_author(String app_author) {
        this.app_author = app_author;
    }

    public String getApp_contact() {
        return app_contact;
    }

    public void setApp_contact(String app_contact) {
        this.app_contact = app_contact;
    }

    public String getApp_email() {
        return app_email;
    }

    public void setApp_email(String app_email) {
        this.app_email = app_email;
    }

    public String getApp_website() {
        return app_website;
    }

    public void setApp_website(String app_website) {
        this.app_website = app_website;
    }

    public String getApp_description() {
        return app_description;
    }

    public void setApp_description(String app_description) {
        this.app_description = app_description;
    }

    public String getApp_developed_by() {
        return app_developed_by;
    }

    public void setApp_developed_by(String app_developed_by) {
        this.app_developed_by = app_developed_by;
    }

    public String getApp_faq() {
        return app_faq;
    }

    public void setApp_faq(String app_faq) {
        this.app_faq = app_faq;
    }

    public String getApp_privacy_policy() {
        return app_privacy_policy;
    }

    public void setApp_privacy_policy(String app_privacy_policy) {
        this.app_privacy_policy = app_privacy_policy;
    }

    public String getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(String publisher_id) {
        this.publisher_id = publisher_id;
    }

    public String getInterstital_ad_id() {

        if (BuildConfig.DEBUG){
            return "ca-app-pub-3940256099942544/1033173712";
        }
        return interstital_ad_id;
    }

    public void setInterstital_ad_id(String interstital_ad_id) {
        this.interstital_ad_id = interstital_ad_id;
    }

    public String getInterstital_ad_click() {
        return interstital_ad_click;
    }

    public void setInterstital_ad_click(String interstital_ad_click) {
        this.interstital_ad_click = interstital_ad_click;
    }

    public String getBanner_ad_id() {
        return banner_ad_id;
    }

    public void setBanner_ad_id(String banner_ad_id) {
        this.banner_ad_id = banner_ad_id;
    }

    public String getRewarded_video_ads_id() {

        if (BuildConfig.DEBUG){
            return "ca-app-pub-3940256099942544/5224354917";
        }

        return rewarded_video_ads_id;
    }

    public void setRewarded_video_ads_id(String rewarded_video_ads_id) {
        this.rewarded_video_ads_id = rewarded_video_ads_id;
    }

    public String getRedeem_currency() {
        return redeem_currency;
    }

    public void setRedeem_currency(String redeem_currency) {
        this.redeem_currency = redeem_currency;
    }

    public String getRedeem_points() {
        return redeem_points;
    }

    public void setRedeem_points(String redeem_points) {
        this.redeem_points = redeem_points;
    }

    public String getRedeem_money() {
        return redeem_money;
    }

    public void setRedeem_money(String redeem_money) {
        this.redeem_money = redeem_money;
    }

    public String getMinimum_redeem_points() {
        return minimum_redeem_points;
    }

    public void setMinimum_redeem_points(String minimum_redeem_points) {
        this.minimum_redeem_points = minimum_redeem_points;
    }

    public String getPayment_method1() {
        return payment_method1;
    }

    public void setPayment_method1(String payment_method1) {
        this.payment_method1 = payment_method1;
    }

    public String getPayment_method2() {
        return payment_method2;
    }

    public void setPayment_method2(String payment_method2) {
        this.payment_method2 = payment_method2;
    }

    public String getPayment_method3() {
        return payment_method3;
    }

    public void setPayment_method3(String payment_method3) {
        this.payment_method3 = payment_method3;
    }

    public String getPayment_method4() {
        return payment_method4;
    }

    public void setPayment_method4(String payment_method4) {
        this.payment_method4 = payment_method4;
    }

    public boolean isInterstital_ad() {
        return interstital_ad;
    }

    public void setInterstital_ad(boolean interstital_ad) {
        this.interstital_ad = interstital_ad;
    }

    public boolean isBanner_ad() {
        return banner_ad;
    }

    public void setBanner_ad(boolean banner_ad) {
        this.banner_ad = banner_ad;
    }

    public boolean isRewarded_video_ads() {


        return rewarded_video_ads;
    }

    public void setRewarded_video_ads(boolean rewarded_video_ads) {
        this.rewarded_video_ads = rewarded_video_ads;
    }

    public String getDaily_spin_limit() {
        return daily_spin_limit;
    }

    public void setDaily_spin_limit(String daily_spin_limit) {
        this.daily_spin_limit = daily_spin_limit;
    }

    public String getAds_frequency_limit() {
        return ads_frequency_limit;
    }

    public void setAds_frequency_limit(String ads_frequency_limit) {
        this.ads_frequency_limit = ads_frequency_limit;
    }

    public String getVideo_add_point() {
        return video_add_point;
    }

    public void setVideo_add_point(String video_add_point) {
        this.video_add_point = video_add_point;
    }

    public String getApp_refer_reward() {
        return app_refer_reward;
    }

    public void setApp_refer_reward(String app_refer_reward) {
        this.app_refer_reward = app_refer_reward;
    }

    public String getRegistration_reward() {
        return registration_reward;
    }

    public void setRegistration_reward(String registration_reward) {
        this.registration_reward = registration_reward;
    }
}
