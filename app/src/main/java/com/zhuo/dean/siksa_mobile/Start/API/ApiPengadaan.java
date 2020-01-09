package com.zhuo.dean.siksa_mobile.Start.API;

import com.zhuo.dean.siksa_mobile.Start.Model.DetailPengadaan;
import com.zhuo.dean.siksa_mobile.Start.Model.HistoryMasuk;
import com.zhuo.dean.siksa_mobile.Start.Model.Pengadaan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiPengadaan {
    @GET ("pengadaan")
    Call<List<Pengadaan>> getPengadaan();

    @POST ("pengadaan")
    @FormUrlEncoded
    Call<String> addPengadaan(@Field("KODE_SUPPLIER") int KODE_SUPPLIER,
                              @Field("KODE_PEGAWAI") int KODE_PEGAWAI,
                              @Field("TANGGAL_PENGADAAN") String TANGGAL_PENGADAAN,
                              @Field("PEMBAYARAN_PEMESANAN") int PEMBAYARAN_PEMESANAN);

    @GET ("pengadaanMax")
    Call<String> getMaxPengadaan();

    @DELETE("pengadaan/{id}")
    Call<String> deletePengadaan (@Path("id") int KODE_PENGADAAN);

    @POST ("detailPengadaan")
    @FormUrlEncoded
    Call<String> addDetailPengadaan(@Field("KODE_PENGADAAN") int KODE_PENGADAAN,
                                    @Field("NAMA_BARANG") String NAMA_BARANG,
                                    @Field("JUMLAH_PESANAN") int JUMLAH_PESANAN,
                                    @Field("HARGA_SATUAN") int HARGA_SATUAN);

    @GET ("proBySup/{id}")
    Call<List<Pengadaan>> getPengadaanBySupplier(@Path("id") int KODE_SUPPLIER);

    @GET ("detailByPengadaan/{id}")
    Call<List<DetailPengadaan>> getDetailByPengadaan(@Path("id") int KODE_PENGADAAN);

    @DELETE("detailPengadaan/{id}")
    Call<String> deleteDetPengadaan (@Path("id") int KODE_DETAIL_PENGADAAN);

    @GET ("barangMasuk")
    Call<List<HistoryMasuk>> getHistory();
}
