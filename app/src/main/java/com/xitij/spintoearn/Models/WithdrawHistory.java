package com.xitij.spintoearn.Models;

public class WithdrawHistory {

    private String Description;
    private String Date;
    private String Coin;
    private String Status;


    public String getDescription() {
        return Description;
    }

    public String getDate() {
        return Date;
    }

    public String getCoin() {
        return Coin;
    }

    public String getStatus() {
        return Status;
    }


    public WithdrawHistory(String description, String date, String coin, String status) {
        Description = description;
        Date = date;
        Coin = coin;
        Status = status;
    }
}
