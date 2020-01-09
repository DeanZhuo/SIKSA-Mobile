package com.zhuo.dean.siksa_mobile.Start.API;

import com.google.gson.JsonObject;
import com.zhuo.dean.siksa_mobile.Start.Model.Supplier;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiSupplier {
    @GET("supplier/")
    Call<List<Supplier>> getSupplier();

    @POST("supplier/")
    @FormUrlEncoded
    Call<String> addSupplier (@Field("NAMA_SUPPLIER") String Nama_supplier,
                              @Field("ALAMAT_SUPPLIER") String Alamat_supplier,
                              @Field("NAMA_SALES") String Nama_sales,
                              @Field("NO_TELP_SALES") String No_telp_sales);

    @GET("supplier/{KODE_SUPPLIER}")
    Call<Supplier> detailSupplier (@Path("KODE_SUPPLIER") int KSupplier);

    @GET("supplierByPengadaan/{id}")
    Call<Supplier> getSupplierByPengadaan (@Path("id") int KODE_PENGADAAN);

    @PUT("supplier/{KODE_SUPPLIER}")
    @FormUrlEncoded
    Call<String> updateSupplier (@Path("KODE_SUPPLIER") int KSupplier,
                                 @Field("NAMA_SUPPLIER") String Nama_supplier,
                                 @Field("ALAMAT_SUPPLIER") String Alamat_supplier,
                                 @Field("NAMA_SALES") String Nama_sales,
                                 @Field("NO_TELP_SALES") String No_telp_sales);

    @DELETE("supplier/{KODE_SUPPLIER}")
    Call<String> deleteSupplier (@Path("KODE_SUPPLIER") int KSupplier);
}
