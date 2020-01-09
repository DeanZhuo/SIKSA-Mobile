package com.zhuo.dean.siksa_mobile.Start.API;

import com.zhuo.dean.siksa_mobile.Start.Model.StatusService;
import com.zhuo.dean.siksa_mobile.Start.Model.Transaksi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiTransaksi {
    @GET("status")
    Call<List<StatusService>> getStatus();

    @GET("status/{id}")
    Call<StatusService> getStatusById(@Path("id") int id);

    @POST("transaksi")
    @FormUrlEncoded
    Call<String> postTransaksi(@Field("KODE_CUST") int KODE_CUST,
                               @Field("KODE_STATUS") int KODE_STATUS,
                               @Field("KODE_CABANG") int KODE_CABANG,
                               @Field("KODE_KENDARAAN") int KODE_KENDARAAN,
                               @Field("CS") int KODE_CS,
                               @Field("TANGGAL TRANSAKSI") String TANGGAL_TRANSAKSI,
                               @Field("KODE_LUNAS") int KODE_LUNAS,
                               @Field("KODE_PENJUALAN") String KODE_PENJUALAN,
                               @Field("DISKON") int DISKON);

    @GET("transaksi")
    Call<List<Transaksi>> getTransaksi();

    @GET("transaksi/{id}")
    Call<Transaksi> getTransaksiById(@Path("id") int kodeTransaksi);

    @GET("transByCus/{id}")
    Call<List<Transaksi>> getTransaksiByCust(@Path("id") int kodeCustomer);

    @PUT("transaksi/{id}")
    @FormUrlEncoded
    Call<String> updateTransaksi(@Path  ("id") int KODE_TRANSAKSI,
                                 @Field ("KODE_PENJUALAN") String KODE_PENJUALAN,
                                 @Field ("KODE_STATUS") int KODE_STATUS,
                                 @Field ("DISKON") int DISKON);

    @GET("transaksiMax")
    Call<String> getMaxTransaksi();

    @DELETE("transaksi/{id}")
    Call<String> deleteTransaksi(@Path("id") int kodeTrans);
}
