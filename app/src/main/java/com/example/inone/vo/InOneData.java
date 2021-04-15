package com.example.inone.vo;

import com.google.gson.annotations.SerializedName;

public class InOneData {
    //종가
    @SerializedName("last")
    private String last;

    public String getLast() {
        return last;
    }

    //이름
    /*@SerializedName("currency")
    private String currency;

    @SerializedName("first")
    private String first;

    @SerializedName("low")
    private String low;

    @SerializedName("high")
    private String high;

    @SerializedName("volume")
    private String volume;

    @SerializedName("yesterday_first")
    private String yesterday_first;

    @SerializedName("yesterday_low")
    private String yesterday_low;

    @SerializedName("yesterday_high")
    private String yesterday_high;

    @SerializedName("yesterday_last")
    private String yesterday_last;

    @SerializedName("yesterday_volume")
    private String yesterday_volume;

    public String getCurrency() {
        return currency;
    }

    public String getFirst() {
        return first;
    }

    public String getLow() {
        return low;
    }

    public String getHigh() {
        return high;
    }

    public String getVolume() {
        return volume;
    }

    public String getYesterday_first() {
        return yesterday_first;
    }

    public String getYesterday_low() {
        return yesterday_low;
    }

    public String getYesterday_high() {
        return yesterday_high;
    }

    public String getYesterday_last() {
        return yesterday_last;
    }

    public String getYesterday_volume() {
        return yesterday_volume;
    }*/
}