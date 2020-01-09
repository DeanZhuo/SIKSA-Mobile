package com.zhuo.dean.siksa_mobile.Start.Sparepart;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiPengadaan;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Model.DetailPengadaan;
import com.zhuo.dean.siksa_mobile.Start.Model.Pengadaan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProcurementDetail extends AppCompatActivity {

    TextView tvnama, tvtanggal, tvpembayaran;
    private AlertDialog dialog;
    private List<DetailPengadaan> detailPdaList = new ArrayList<>();
    private ProcurementDetailAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String namaSup;
    private int kodeSupp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurement_detail);

        AlertDialog.Builder builder = Helper.loadDialog(ProcurementDetail.this);
        dialog = builder.create();

        kodeSupp = getIntent().getIntExtra("supplier",0);
        namaSup = getIntent().getStringExtra("namaSupplier");
        final int kodePda = getIntent().getIntExtra("idPda",0);
        final String tanggal = getIntent().getStringExtra("tanggal");
        final int pembayaran = getIntent().getIntExtra("pembayaran",0);


        Button delete = findViewById(R.id.SpProDetDel);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDelete(kodePda, kodeSupp);
            }
        });

        Button edit = findViewById(R.id.SpProDetEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProcurementDetail.this,ProcurementUbah.class);
                intent.putExtra("supplier", namaSup);
                intent.putExtra("idPda",kodePda);
                intent.putExtra("namaSupplier", namaSup);
                intent.putExtra("tanggal",tanggal);
                intent.putExtra("pembayaran",pembayaran);
                startActivity(intent);
            }
        });

        tvnama = findViewById(R.id.SpProDetSup);
        tvtanggal = findViewById(R.id.SpProDetTanggal);
        tvpembayaran = findViewById(R.id.SpProDetBayar);

        adapter = new ProcurementDetailAdapter(this, detailPdaList);
        recyclerView = (RecyclerView) findViewById(R.id.SpProDetView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setDetail(namaSup,tanggal,pembayaran,kodePda);
        recyclerView.setAdapter(adapter);
    }

    private void setDetail(String namaSup, String tanggal, int pembayaran, int kodePda) {
        tvnama.setText(namaSup);
        tvtanggal.setText(tanggal);
        tvpembayaran.setText("Rp "+pembayaran);

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiPengadaan apiPengadaan = retrofit.create(ApiPengadaan.class);
        Call<List<DetailPengadaan>> listCall = apiPengadaan.getDetailByPengadaan(kodePda);
        listCall.enqueue(new Callback<List<DetailPengadaan>>() {
            @Override
            public void onResponse(Call<List<DetailPengadaan>> call, Response<List<DetailPengadaan>> response) {
                try {
                    if(response.body() != null){
                        for(int i=0; i<response.body().size(); i++){
                            detailPdaList.add(response.body().get(i));
                        }
                        Log.d("detail", "onResponse: size: "+response.body().size());
                    }

                    adapter.notifyDataSetChanged();
                    adapter = new ProcurementDetailAdapter(ProcurementDetail.this, detailPdaList);
                    recyclerView.setAdapter(adapter);
                    Toast.makeText(ProcurementDetail.this, "Success", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(ProcurementDetail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Error detail list", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<DetailPengadaan>> call, Throwable t) {
                Toast.makeText(ProcurementDetail.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(ProcurementDetail.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(ProcurementDetail.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
    }

    private void onClickDelete(final int kodePda, final int kodeSupp) {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiPengadaan apiPengadaan = retrofit.create(ApiPengadaan.class);
        Call<List<DetailPengadaan>> detCall = apiPengadaan.getDetailByPengadaan(kodePda);
        detCall.enqueue(new Callback<List<DetailPengadaan>>() {
            @Override
            public void onResponse(Call<List<DetailPengadaan>> call, Response<List<DetailPengadaan>> response) {
                Log.d("Detail List", "onResponse: "+response.body().size());
                if (response.body().size()!=0){
                    for (int i=0; i<response.body().size(); i++){
                        int kodeDetail = response.body().get(i).getKODEDETAILPENGADAAN();
                        deleteDetail(kodeDetail);
                    }
                    Log.d("Detail list", "onResponse: Success deleting");
                }else{
                    Log.d("Detail list", "onResponse: Failed deleting");
                }
            }

            @Override
            public void onFailure(Call<List<DetailPengadaan>> call, Throwable t) {
                Toast.makeText(ProcurementDetail.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(ProcurementDetail.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(ProcurementDetail.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });

        Call<String> pdaCall = apiPengadaan.deletePengadaan(kodePda);
        pdaCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    Log.d("del Pda", "onResponse: "+response.code());
                } else{
                    try {
                        Log.d("del Pda", "onResponse: "+response.errorBody().string());
                    } catch (IOException e) {
                        Log.d("del pda catch", "onResponse: "+e.getMessage());
                    }
                }

                Intent intent = new Intent(ProcurementDetail.this, ProcurementSuppliers.class);
                intent.putExtra("supplier", kodeSupp);
                intent.putExtra("namaSupplier", namaSup);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("delPda", "onFailure: "+t.getMessage());
            }
        });
    }

    private void deleteDetail(final int kodeDetail) {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiPengadaan apiPengadaan = retrofit.create(ApiPengadaan.class);
        Call<String> detcall = apiPengadaan.deleteDetPengadaan(kodeDetail);
        detcall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    Log.d("detDel", "onResponse: deleted "+kodeDetail);
                } else{
                    Log.d("detDel", "onResponse: error deleting "+response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("delDet", "onFailure: " + t.getMessage());
            }
        });
    }
}
