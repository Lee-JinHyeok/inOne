package com.example.inone.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InOneDataAll {
    //성공여부
    /*@SerializedName("result")
    private String result;

    @SerializedName("errorCode")
    private String errorCode;
    public String getResult() {
        return result;
    }

    public String getErrorCode() {
        return errorCode;
    }*/

    //해당시간
    @SerializedName("timestamp")
    private String timestamp;


    public String getTimestamp() {
        return timestamp;
    }

    //코인 하나하나 추가하고 getter해줘야함 귀찮
    //KLAY
    @SerializedName("klay")
    private InOneData klayInfo;

    //KSP
    @SerializedName("ksp")
    private InOneData kspInfo;

    public InOneData getKlayInfo() {
        return klayInfo;
    }

    public InOneData getKspInfo() {
        return kspInfo;
    }
}
