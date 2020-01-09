package com.zhuo.dean.siksa_mobile.Start.Transactional;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiCabang;
import com.zhuo.dean.siksa_mobile.Start.API.ApiCustomer;
import com.zhuo.dean.siksa_mobile.Start.API.ApiDetTrans;
import com.zhuo.dean.siksa_mobile.Start.API.ApiPegawai;
import com.zhuo.dean.siksa_mobile.Start.API.ApiTransaksi;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Main.MainActivity;
import com.zhuo.dean.siksa_mobile.Start.Model.Cabang;
import com.zhuo.dean.siksa_mobile.Start.Model.Customer;
import com.zhuo.dean.siksa_mobile.Start.Model.DetailJasa;
import com.zhuo.dean.siksa_mobile.Start.Model.DetailTransaksi;
import com.zhuo.dean.siksa_mobile.Start.Model.HistoryKeluar;
import com.zhuo.dean.siksa_mobile.Start.Model.Kendaraan;
import com.zhuo.dean.siksa_mobile.Start.Model.PegawaiDAO;
import com.zhuo.dean.siksa_mobile.Start.Model.Transaksi;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransaksiDetail extends AppCompatActivity {

    Transaksi transaksi;

    TextView trPenjualan, trTanggal, trCustomer, trKendaraan, trCabang, trCS, trKasir, trLunas, trDiskon, trTotal, trBayar;
    Button editBtn, serviceBtn, sparepartBtn;
    int kodeTrans, total, diskon, bayar;
    String kodePenjualan, tanggal, customer, kendaraan, cabang, cs, kasir, pelunasan;
    String pegawai;
    int kodeCust;
    List<DetailTransaksi> detailTransaksiList;
    private android.app.AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_detail);

        getSupportActionBar().setTitle("Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setAtribut();

        android.app.AlertDialog.Builder builder = Helper.loadDialog(TransaksiDetail.this);
        dialog = builder.create();
        dialog.show();

        kodeTrans = getIntent().getIntExtra("id",1);
        kodeCust = getIntent().getIntExtra("customer",1);
        customer = getIntent().getStringExtra("namaCus");
        kendaraan = getIntent().getStringExtra("kendaraan");
        cabang = getIntent().getStringExtra("cabang");
        cs = getIntent().getStringExtra("cs");
        kasir = getIntent().getStringExtra("kasir");

        setDetail(kodeTrans, customer, kendaraan, cabang, cs, kasir);
        dialog.dismiss();

        Button deleteButton = findViewById(R.id.TrDetDel);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteTran(kodeTrans);
            }
        });

        serviceBtn = (Button) findViewById(R.id.TrDetService);
        serviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaksiDetail.this, TransaksiService.class);
                intent.putExtra("kodeTrans",kodeTrans);
                intent.putExtra("kendaraan",kendaraan);
                intent.putExtra("cs",cs);
                intent.putExtra("kasir",kasir);
                intent.putExtra("id",transaksi.getKODETRANSAKSI());
                intent.putExtra("customer",customer);
                intent.putExtra("namaCus", customer);
                startActivity(intent);
            }
        });

        sparepartBtn = (Button) findViewById(R.id.TrDetSparepart);
        sparepartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaksiDetail.this, TransaksiSparepart.class);
                intent.putExtra("kodeTrans",kodeTrans);
                intent.putExtra("kendaraan",kendaraan);
                intent.putExtra("cs",cs);
                intent.putExtra("kasir",kasir);
                intent.putExtra("id",transaksi.getKODETRANSAKSI());
                intent.putExtra("customer",customer);
                intent.putExtra("namaCus", customer);
                startActivity(intent);
            }
        });
    }

    private void onDeleteTran(int kodeTrans) {
        dialog.show();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);
        Call<List<DetailTransaksi>> objectCall = apiDetTrans.getByTrans(kodeTrans);
        objectCall.enqueue(new Callback<List<DetailTransaksi>>() {
            @Override
            public void onResponse(Call<List<DetailTransaksi>> call, Response<List<DetailTransaksi>> response) {
                if (response.body() != null){
                    for (int i=0; i<response.body().size(); i++){
                        int kodeDetail = response.body().get(i).getKODEDETAILTRANSAKSI();
                        deleteHistory(kodeDetail);
                        deleteDetailJasa(kodeDetail);
                        deleteDetailTransaksi(kodeDetail);
                    }
                    Log.d("Detail Transaksi list", "onResponse: Success deleting");
                }else{
                    Log.d("Detail Transaksi list", "onResponse: Failed deleting");
                }
            }

            @Override
            public void onFailure(Call<List<DetailTransaksi>> call, Throwable t) {
                Log.d("Detail Transaksi list", "onFailure: "+t.getMessage());
            }
        });

        ApiTransaksi apiTransaksi = retrofit.create(ApiTransaksi.class);
        Call<String> stringCall = apiTransaksi.deleteTransaksi(kodeTrans);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 404){
                    Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_SHORT).show();
                }else if (response.code() == 500) {
                    Toast.makeText(getApplicationContext(), "Error Deleting", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(TransaksiDetail.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                Log.d("delete transaksi", "onFailure: " + t.getMessage());
            }
        });
        dialog.dismiss();
    }

    private void deleteDetailTransaksi(int kodeDetail) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);
        Call<String> objectCall = apiDetTrans.deleteDetTr(kodeDetail);
        objectCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200){
                    Log.d("Detail Transaksi", "onResponse: Success");
                }else {
                    Log.d("Detail Transaksi", "onResponse: Failed");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Detail Transaksi", "onFailure: "+t.getMessage());
            }
        });
    }

    private void deleteDetailJasa(int kodeDetail) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);
        Call<String> objectCall = apiDetTrans.deleteDetJasa(kodeDetail);
        objectCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200){
                    Log.d("Detail Jasa", "onResponse: Success");
                }else {
                    Log.d("Detail Jasa", "onResponse: Failed");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Detail Jasa", "onFailure: "+t.getMessage());
            }
        });
    }

    private void deleteHistory(int kodeDetail){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);
        Call<String> objectCall = apiDetTrans.deleteHistoryKeluar(kodeDetail);
        objectCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200){
                    Log.d("History Keluar", "onResponse: Success");
                }else {
                    Log.d("History Keluar", "onResponse: Failed");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("History Keluar", "onFailure: "+t.getMessage());
            }
        });
    }

    private void setDetail(int kodeTrans, final String customer, final String kendaraan, final String cabang, final String cs, final String kasir) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        ApiTransaksi apiTransaksi = retrofit.create(ApiTransaksi.class);
        Call<Transaksi> objectCall = apiTransaksi.getTransaksiById(kodeTrans);
        objectCall.enqueue(new Callback<Transaksi>() {
            @Override
            public void onResponse(Call<Transaksi> call, Response<Transaksi> response) {
                int status = response.code();
                if (status == 404) {
                    Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_SHORT).show();
                }else if (status == 500){
                    Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                }else{
                    transaksi = response.body();
                    Log.d("tr", "onResponse: "+response.toString());
                    Log.d("tr", "onResponse: spare " + transaksi.getKODEPENJUALAN());
                    Toast.makeText(getApplicationContext(), "Transaksi", Toast.LENGTH_SHORT).show();
                    kodePenjualan = transaksi.getKODEPENJUALAN();
                    tanggal = transaksi.getTANGGALTRANSAKSI();
                    if (transaksi.getKODELUNAS() == 1)
                        pelunasan = "Lunas";
                    else
                        pelunasan = "Belum Lunas";
                    diskon = transaksi.getDISKON();

                    trPenjualan.setText(kodePenjualan);
                    trTanggal.setText(tanggal);
                    trCustomer.setText(customer);
                    trKendaraan.setText(kendaraan);
                    trCabang.setText(cabang);
                    trCS.setText(cs);
                    trKasir.setText(kasir);
                    trLunas.setText(pelunasan);
                    trDiskon.setText(String.valueOf(diskon));
                }
            }

            @Override
            public void onFailure(Call<Transaksi> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                Log.d("transaksi", "onFailure: "+ t.getMessage());
            }
        });

        ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);
        Call<List<DetailTransaksi>> detailTransaksiCall = apiDetTrans.getByTrans(kodeTrans);
        detailTransaksiCall.enqueue(new Callback<List<DetailTransaksi>>() {
            @Override
            public void onResponse(Call<List<DetailTransaksi>> call, Response<List<DetailTransaksi>> response) {
                if (response.body()!=null){
                    for (int i=0; i<response.body().size(); i++){
                        DetailTransaksi detail = response.body().get(i);
                        total = Integer.parseInt(trTotal.getText().toString());
                        total += detail.getTOTALBAYAR();
                        trTotal.setText(String.valueOf(total));
                    }
                    diskon = Integer.parseInt(trDiskon.getText().toString());
                    bayar = total - diskon;
                    trBayar.setText(String.valueOf(bayar));
                } else if (response.code() == 404) {
                    Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 500) {
                    Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DetailTransaksi>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                Log.d("dettrans", "onFailure: "+t.getMessage());
            }
        });
    }

//    private String getKendaraan(final int kodeKendaraan) {
//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl(Helper.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create());
//
//        Retrofit retrofit = builder.build();
//
//        ApiCustomer apiCustomer = retrofit.create(ApiCustomer.class);
//        Call<Kendaraan> objectCall = apiCustomer.getKendaraanById(kodeKendaraan);
//        objectCall.enqueue(new Callback<Kendaraan>() {
//            @Override
//            public void onResponse(Call<Kendaraan> call, Response<Kendaraan> response) {
//                try {
//                    if (response.code() == 200) {
//                        Kendaraan kendaraan1 = response.body();
//                        kendaraan = kendaraan1.getPLATKENDARAAN();
//                    } else {
//                        Log.d("kendaraan", "onResponse: " + response.code() + kodeKendaraan);
//                    }
//                }catch (NumberFormatException num){
//                    Log.d("kendaraan", "onResponse: "+ num.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Kendaraan> call, Throwable t) {
//                Log.d("Kendaraan", "onFailure: get nama kendaraan " + t.getMessage());
//            }
//        });
//
//        return kendaraan;
//    }
//
//    private String getCabang(final int kodecabang) {
//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl(Helper.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create());
//
//        Retrofit retrofit = builder.build();
//
//        ApiCabang apiCabang = retrofit.create(ApiCabang.class);
//        Call<Cabang> objectCall = apiCabang.getCabangById(kodecabang);
//        objectCall.enqueue(new Callback<Cabang>() {
//            @Override
//            public void onResponse(Call<Cabang> call, Response<Cabang> response) {
//                if (response.code() == 200) {
//                    Cabang cabangget = response.body();
//                    cabang = cabangget.getNAMACABANG();
//                }else {
//                    Log.d("cabang", "onResponse: " + response.code() + kodecabang);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Cabang> call, Throwable t) {
//                Log.d("cabang", "onFailure: get nama cabang " + t.getMessage());
//            }
//        });
//
//        return cabang;
//    }
//
//    private String getPegawai(final int kodePegawai){
//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl(Helper.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create());
//
//        Retrofit retrofit = builder.build();
//
//        ApiPegawai apiPegawai = retrofit.create(ApiPegawai.class);
//        Call<PegawaiDAO> objectCall = apiPegawai.getPegawaiById(kodePegawai);
//        objectCall.enqueue(new Callback<PegawaiDAO>() {
//            @Override
//            public void onResponse(Call<PegawaiDAO> call, Response<PegawaiDAO> response) {
//                if (response.code() == 200) {
//                    PegawaiDAO pegawaiGet = response.body();
//                    pegawai = pegawaiGet.getNAMAPEGAWAI();
//                }else {
//                    Log.d("pegawai", "onResponse: "+response.code()+kodePegawai);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PegawaiDAO> call, Throwable t) {
//                Log.d("pegawai", "onFailure: get nama pegawai " + t.getMessage());
//            }
//        });
//
//        return pegawai;
//    }

    private void setAtribut() {
        trPenjualan = (TextView) findViewById(R.id.trDetPenjualan);
        trTanggal = (TextView) findViewById(R.id.TrDetTanggal);
        trCustomer = (TextView) findViewById(R.id.TrDetCustomer);
        trKendaraan= (TextView) findViewById(R.id.TrDetKendaraan);
        trCabang = (TextView) findViewById(R.id.TrDetCabang);
        trCS = (TextView) findViewById(R.id.TrDetCS);
        trKasir = (TextView) findViewById(R.id.TrDetKasir);
        trLunas = (TextView) findViewById(R.id.TrDetLunas);
        trDiskon = (TextView) findViewById(R.id.TrDetDiskon);
        trTotal = (TextView) findViewById(R.id.TrDetTotal);
        trBayar = (TextView) findViewById(R.id.trDetBayar);
    }
}
