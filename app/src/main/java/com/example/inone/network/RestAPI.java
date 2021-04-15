package com.example.inone.network;
import com.example.inone.vo.InOneData;
import com.example.inone.vo.InOneDataAll;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestAPI {

    @GET("ticker/?currency=all")
    Call<InOneDataAll> getAll();

    @GET("ticker/?currency=klay")
    Call<InOneData> getKlay();

    @GET("ticker/?currency=ksp")
    Call<InOneData> getKsp();

}
