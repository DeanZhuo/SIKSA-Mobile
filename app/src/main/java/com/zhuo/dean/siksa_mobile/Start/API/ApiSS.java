package com.zhuo.dean.siksa_mobile.Start.API;

import com.zhuo.dean.siksa_mobile.Start.Model.Service;
import com.zhuo.dean.siksa_mobile.Start.Model.Sparepart;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiSS {
    @GET ("jasa")
    Call<List<Service>> getService();

    @GET ("sparepart")
    Call<List<Sparepart>> getSparepart();
}
