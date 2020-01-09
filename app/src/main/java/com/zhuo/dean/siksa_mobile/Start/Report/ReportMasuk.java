package com.zhuo.dean.siksa_mobile.Start.Report;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiPengadaan;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSparepart;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSupplier;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Main.LoginActivity;
import com.zhuo.dean.siksa_mobile.Start.Main.MainActivity;
import com.zhuo.dean.siksa_mobile.Start.Model.HistoryMasuk;
import com.zhuo.dean.siksa_mobile.Start.Model.Sparepart;
import com.zhuo.dean.siksa_mobile.Start.Model.Supplier;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReportMasuk extends AppCompatActivity {

    private TableLayout tableLayout;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_masuk);

        tableLayout = findViewById(R.id.historyMasukTable);

        AlertDialog.Builder dialogbuilder = Helper.loadDialog(ReportMasuk.this);
        dialog = dialogbuilder.create();
        dialog.show();


        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiPengadaan apiPengadaan = retrofit.create(ApiPengadaan.class);
        Call<List<HistoryMasuk>> historyCall = apiPengadaan.getHistory();
        historyCall.enqueue(new Callback<List<HistoryMasuk>>() {
            @Override
            public void onResponse(Call<List<HistoryMasuk>> call, Response<List<HistoryMasuk>> response) {
                if (response.body()!=null){
                    Log.d("onCreate", "onResponse: list get");
                    setTableLayout(response.body());
                } else {
                    Log.d("onCreate", "onResponse: fail "+response.code());
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<HistoryMasuk>> call, Throwable t) {
                dialog.dismiss();
                Intent intent = new Intent(ReportMasuk.this, MainActivity.class);
                startActivity(intent);

                if (t instanceof IOException) {
                    Toast.makeText(ReportMasuk.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(ReportMasuk.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        dialog.dismiss();
    }

    void setTableLayout(List<HistoryMasuk> historyMasukList){
        for (int i=0;i<historyMasukList.size();i++){
            View tableRow = LayoutInflater.from(ReportMasuk.this).inflate(R.layout.history_masuk_item,null,false);
            TextView tanggal = tableRow.findViewById(R.id.hmTanggal);
            final TextView supplier = tableRow.findViewById(R.id.hmSupplier);
            final TextView barang = tableRow.findViewById(R.id.hmBarang);
            TextView jumlah = tableRow.findViewById(R.id.hmJumlah);
            TextView satuan = tableRow.findViewById(R.id.hmSatuan);
            TextView harga = tableRow.findViewById(R.id.hmHarga);

            HistoryMasuk historyMasuk = historyMasukList.get(i);

            tanggal.setText(historyMasuk.getTANGGALMASUK());

            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            ApiSupplier apiSupplier = retrofit.create(ApiSupplier.class);
            Call<Supplier> supplierCall = apiSupplier.getSupplierByPengadaan(historyMasuk.getKODEPENGADAAN());
            supplierCall.enqueue(new Callback<Supplier>() {
                @Override
                public void onResponse(Call<Supplier> call, Response<Supplier> response) {
                    if (response.body()!=null){
                        supplier.setText(response.body().getNAMASUPPLIER());
                    } else {
                        supplier.setText("Empty");
                        Log.d("supplier", "onResponse: "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<Supplier> call, Throwable t) {
                    if (t instanceof IOException) {
                        Toast.makeText(ReportMasuk.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(ReportMasuk.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });

            ApiSparepart apiSparepart = retrofit.create(ApiSparepart.class);
            Call<Sparepart> sparepartCall = apiSparepart.detailSparepart(historyMasuk.getKODESPAREPART());
            sparepartCall.enqueue(new Callback<Sparepart>() {
                @Override
                public void onResponse(Call<Sparepart> call, Response<Sparepart> response) {
                    if (response.body()!=null){
                        barang.setText(response.body().getnAMASPAREPART());
                    } else {
                        barang.setText("Empty");
                        Log.d("barang", "onResponse: "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<Sparepart> call, Throwable t) {
                    if (t instanceof IOException) {
                        Toast.makeText(ReportMasuk.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(ReportMasuk.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });

            jumlah.setText(String.valueOf(historyMasuk.getSTOKBARANGMASUK()));
            satuan.setText(String.valueOf((historyMasuk.getHARGABARANGMASUK())/(historyMasuk.getSTOKBARANGMASUK())));
            harga.setText(String.valueOf(historyMasuk.getHARGABARANGMASUK()));
            tableLayout.addView(tableRow);
        }
    }
}
