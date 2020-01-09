package com.zhuo.dean.siksa_mobile.Start.API;

import com.google.gson.JsonObject;
import com.zhuo.dean.siksa_mobile.Start.Model.Sparepart;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiSparepart {
    @GET("sparepart/")
    Call<List<Sparepart>> getSpareparts();

    @POST("sparepart/")
    @FormUrlEncoded
    Call<String> addSparepart (@Field("KODE_BARANG") String kode_barang,
                               @Field("TIPE_SPAREPART") String tipe,
                               @Field("NAMA_SPAREPART") String nama,
                               @Field("HARGA_JUAL") float harga_jual,
                               @Field("HARGA_BELI") float harga_beli,
                               @Field("MERK_SPAREPART") String merk,
                               @Field("KODE_PELETAKAN") String kode_peletakan,
                               @Field("JUMLAH_STOK") int stok,
                               @Field("GAMBAR") String gambar);

    @GET("minSparepart/{numberOf}")
    Call<List<Sparepart>> getMinstok (@Path("numberOf") int numberOfStock);

    @GET("sparepart/{KODE_SPAREPART}")
    Call<Sparepart> detailSparepart (@Path("KODE_SPAREPART") int KSparepart);

    @PUT("sparepart/{KODE_SPAREPART}")
    @FormUrlEncoded
    Call<String> updateSparepart (@Path("KODE_SPAREPART") int KSparepart,
                                  @Field("HARGA_JUAL") int harga_jual,
                                  @Field("HARGA_BELI") int harga_beli,
                                  @Field("KODE_PELETAKAN") String kode_peletakan);

    @DELETE("sparepart/{KODE_SPAREPART}")
    Call<String> deleteSparepart (@Path("KODE_SPAREPART") int KSparepart);
}
