package com.zhuo.dean.siksa_mobile.Start.API;

import com.google.gson.JsonObject;
import com.zhuo.dean.siksa_mobile.Start.Model.PegawaiDAO;
import com.zhuo.dean.siksa_mobile.Start.Model.Role;

import java.util.List;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiPegawai {
    @POST("Login")
    @FormUrlEncoded
    Call<PegawaiDAO> logUser(@Field("USERNAME") String USERNAME,
                             @Field("PASSWORD") String PASSWORD);

    @PUT("PasswordChange/{KODE_PEGAWAI}")
    @FormUrlEncoded
    Call<String> passChange(@Path("KODE_PEGAWAI") int KODE_PEGAWAI,
                            @Field("USERNAME") String username,
                            @Field("PASSWORD") String password,
                            @Field("NEW_PASS") String new_password);

    @GET("role")
    Call<List<Role>> getRole();

    @GET("role/{id}")
    Call<Role> getRoleById(@Path("id") int id);

    @GET("pegawai")
    Call<List<PegawaiDAO>>  getPegawai();

    @GET("pegawai/{id}")
    Call<PegawaiDAO> getPegawaiById(@Path("id") int id);

    @GET("pegawaiByRole/{id}")
    Call<List<PegawaiDAO>> getPegawaiByRole(@Path("id") int role);
}
