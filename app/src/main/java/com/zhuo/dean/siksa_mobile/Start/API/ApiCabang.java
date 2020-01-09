package com.zhuo.dean.siksa_mobile.Start.API;

import com.zhuo.dean.siksa_mobile.Start.Model.Cabang;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiCabang {
    @GET("cabang")
    Call<List<Cabang>> getCabang();

    @GET("cabang/{id}")
    Call<Cabang> getCabangById(@Path("id") int id);

    @POST("cabang/")
    @FormUrlEncoded
    Call<String> addCabang(@Field("NAMA_CABANG") String NAMA_CABANG,
                           @Field("ALAMAT_CABANG") String ALAMAT_CABANG,
                           @Field("NO_TELP_CABANG") String NO_TELP_CABANG);

    @PUT("cabang/{id}")
    @FormUrlEncoded
    Call<String> updateCabang(@Path("id") int KODE_CABANG,
                           @Field("NAMA_CABANG") String NAMA_CABANG,
                           @Field("ALAMAT_CABANG") String ALAMAT_CABANG,
                           @Field("NO_TELP_CABANG") String NO_TELP_CABANG);

    @DELETE ("cabang/{id}")
    Call<String> deleteCabang(@Path("id") int KODE_CABANG);
}
