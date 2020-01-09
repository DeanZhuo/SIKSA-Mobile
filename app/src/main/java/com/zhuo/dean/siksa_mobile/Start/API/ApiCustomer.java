package com.zhuo.dean.siksa_mobile.Start.API;

import com.zhuo.dean.siksa_mobile.Start.Model.Customer;
import com.zhuo.dean.siksa_mobile.Start.Model.Kendaraan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiCustomer {
    @GET("customer")
    Call<List<Customer>> getCustomer();

    @GET("customer/{id}")
    Call<Customer> getCustomerById(@Path("id") int KODE_CUSTOMER);

    @GET("kendaraan")
    Call<List<Kendaraan>> getKendaraan();

    @GET("kendaraan/{id}")
    Call<Kendaraan> getKendaraanById(@Path("id") int kodeKendaraan);

}
