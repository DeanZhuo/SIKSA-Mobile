package com.zhuo.dean.siksa_mobile.Start.Report;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiDetTrans;
import com.zhuo.dean.siksa_mobile.Start.API.ApiPengadaan;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSparepart;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Main.MainActivity;
import com.zhuo.dean.siksa_mobile.Start.Model.HistoryKeluar;
import com.zhuo.dean.siksa_mobile.Start.Model.HistoryMasuk;
import com.zhuo.dean.siksa_mobile.Start.Model.Sparepart;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReportKeluar extends AppCompatActivity {
    private TableLayout tableLayout;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_keluar);

        tableLayout = findViewById(R.id.historyKeluarTable);

        AlertDialog.Builder dialogbuilder = Helper.loadDialog(ReportKeluar.this);
        dialog = dialogbuilder.create();
        dialog.show();


        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);
        Call<List<HistoryKeluar>> historyCall = apiDetTrans.getHistoryKeluar();
        historyCall.enqueue(new Callback<List<HistoryKeluar>>() {
            @Override
            public void onResponse(Call<List<HistoryKeluar>> call, Response<List<HistoryKeluar>> response) {
                if (response.body()!=null){
                    Log.d("onCreate", "onResponse: list get");
                    setTableLayout(response.body());
                } else {
                    Log.d("onCreate", "onResponse: fail "+response.code());
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<HistoryKeluar>> call, Throwable t) {
                dialog.dismiss();
                Intent intent = new Intent(ReportKeluar.this, MainActivity.class);
                startActivity(intent);

                if (t instanceof IOException) {
                    Toast.makeText(ReportKeluar.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(ReportKeluar.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        dialog.dismiss();
    }

    void setTableLayout(List<HistoryKeluar> historyKeluarList){

        for (int i=0;i<historyKeluarList.size();i++){
            View tableRow = LayoutInflater.from(ReportKeluar.this).inflate(R.layout.history_keluar_item,null,false);
            TextView tanggal = tableRow.findViewById(R.id.hkTanggal);
            final TextView barang = tableRow.findViewById(R.id.hkBarang);
            TextView jumlah = tableRow.findViewById(R.id.hkJumlah);
            TextView satuan = tableRow.findViewById(R.id.hkSatuan);
            TextView harga = tableRow.findViewById(R.id.hkHarga);

            HistoryKeluar historyKeluar = historyKeluarList.get(i);

            tanggal.setText(historyKeluar.getTANGGALKELUAR());

            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            ApiSparepart apiSparepart = retrofit.create(ApiSparepart.class);
            Call<Sparepart> sparepartCall = apiSparepart.detailSparepart(historyKeluar.getKODESPAREPART());
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
                        Toast.makeText(ReportKeluar.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(ReportKeluar.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });

            jumlah.setText(String.valueOf(historyKeluar.getSTOKBARANGKELUAR()));
            satuan.setText(String.valueOf((historyKeluar.getHARGABARANGKELUAR())/(historyKeluar.getSTOKBARANGKELUAR())));
            harga.setText(String.valueOf(historyKeluar.getHARGABARANGKELUAR()));

            tableLayout.addView(tableRow);
        }
    }
}
