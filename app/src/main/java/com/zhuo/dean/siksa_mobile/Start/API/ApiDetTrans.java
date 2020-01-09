package com.zhuo.dean.siksa_mobile.Start.API;

import com.zhuo.dean.siksa_mobile.Start.Model.DetailJasa;
import com.zhuo.dean.siksa_mobile.Start.Model.DetailTransaksi;
import com.zhuo.dean.siksa_mobile.Start.Model.HistoryKeluar;
import com.zhuo.dean.siksa_mobile.Start.Model.Service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiDetTrans {
    @GET ("detailTransaksi/{id}")
    Call<DetailTransaksi> getDetTrans(@Path("id") int DetailTransaksi);

    @GET  ("detTrByTrans/{id}")
    Call<List<DetailTransaksi>> getByTrans(@Path("id") int KodeTransaksi);

    @POST ("detailTransaksi")
    @FormUrlEncoded
    Call<String> addDetTr(@Field("KODE_TRANSAKSI") int KODE_TRANSAKSI,
                          @Field("TOTAL_BAYAR") int TOTAL_BAYAR);

    @DELETE("detailTransaksi/{id}")
    Call<String> deleteDetTr(@Path("id") int kodeDetTr);

    @GET ("detTrMax")
    Call<String> getMaxDetTr();

    @GET ("detailJasa")
    Call<List<DetailJasa>> getDetailJasa();

    @GET ("detailJasa/{id}")
    Call<List<DetailJasa>> getDetJsByDetTr(@Path("id") int kodeDetailTransaksi);

    @POST ("detailJasa")
    @FormUrlEncoded
    Call<String> addDetailJasa(@Field("KODE_DETAIL_TRANSAKSI") int KODE_DETAIL_TRANSAKSI,
                               @Field("KODE_SERVICE") int KODE_SERVICE);

    @DELETE("detailJasa/{id}")
    Call<String> deleteDetJasa(@Path("id") int kodeDetTr);

    @GET ("barangKeluar")
    Call<List<HistoryKeluar>> getHistoryKeluar();

    @GET ("barangKeluar/{id}")
    Call<List<HistoryKeluar>> getKeluarByDetTr(@Path("id") int kodeDetailTransaksi);

    @POST ("barangKeluar")
    @FormUrlEncoded
    Call<String> addHistoryKeluar(@Field("KODE_DETAIL_TRANSAKSI") int KODE_DETAIL_TRANSAKSI,
                                   @Field("KODE_SPAREPART") int KODE_SPAREPART,
                                   @Field("TANGGAL_KELUAR") String TANGGAL_KELUAR,
                                   @Field("STOK_BARANG_KELUAR") int STOK_BARANG_KELUAR,
                                   @Field("HARGA_BARANG_KELUAR") int HARGA_BARANG_KELUAR);

    @DELETE("barangKeluar/{id}")
    Call<String> deleteHistoryKeluar(@Path("id") int kodeDetTr);

    @GET("jasa/{id}")
    Call<Service> getJasaById(@Path("id") int kodeJasa);
}
