package com.zhuo.dean.siksa_mobile.Start.Transactional;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiDetTrans;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSS;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSparepart;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Main.MainActivity;
import com.zhuo.dean.siksa_mobile.Start.Model.DetailTransaksi;
import com.zhuo.dean.siksa_mobile.Start.Model.HistoryKeluar;
import com.zhuo.dean.siksa_mobile.Start.Model.Service;
import com.zhuo.dean.siksa_mobile.Start.Model.Sparepart;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransaksiSparepart extends AppCompatActivity {

    private int kodeTrans;
    private barangKeluarAdapter adapter;
    private List<HistoryKeluar> historyKeluarList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<DetailTransaksi> detailTransaksiList = new ArrayList<>();
    private List<Sparepart> sparepartList = new ArrayList<>();
    private int kode, harga;
    private ArrayList<HistoryKeluar> kodeSparepart = new ArrayList<>();
    private int totalsparepart=0;
    private TableLayout tableLayout;
    private int kodeDetTr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_transaksi_sparepart);

        kodeTrans = getIntent().getIntExtra("kodeTrans",0);
        getDataTransaksi(kodeTrans);

        adapter = new barangKeluarAdapter(TransaksiSparepart.this, historyKeluarList);
        adapter.notifyDataSetChanged();
        recyclerView = (RecyclerView) findViewById(R.id.TrSparepartView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        tableLayout = findViewById(R.id.TrDetNewSparepart);

        Button addButton = findViewById(R.id.TrDetAddSparepart);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSparepartDialog();
            }
        });

        Button done = findViewById(R.id.TrDetSparepartDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetail(kodeTrans);
                Intent intent = new Intent(TransaksiSparepart.this,TransaksiByCustomer.class);
                startActivity(intent);
            }
        });
    }

    private void getHistoryKeluar(int kodedetailtransaksi) {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);
        Call<List<HistoryKeluar>> listCall  = apiDetTrans.getKeluarByDetTr(kodedetailtransaksi);
        listCall.enqueue(new Callback<List<HistoryKeluar>>() {
            @Override
            public void onResponse(Call<List<HistoryKeluar>> call, Response<List<HistoryKeluar>> response) {
                try {
                    adapter.notifyDataSetChanged();
                    if(response.body() != null){
                        for(int i=0; i<response.body().size(); i++){
                            historyKeluarList.add(response.body().get(i));
                        }
                        Log.d("sparepart", "onResponse: size: "+historyKeluarList.size());
                    }else {
                        Log.d("sparepart", "onResponse: "+response.code());
                    }
                    adapter = new barangKeluarAdapter(TransaksiSparepart.this,historyKeluarList);
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    Log.d("Error response", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<HistoryKeluar>> call, Throwable t) {
                if (t instanceof IOException) {
                    Log.d("error", t.getMessage(), t);
                    // logging probably not necessary
                }
                else {
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
    }

    private void getDataTransaksi(int kodeTrans) {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);
        Call<List<DetailTransaksi>> listCall  = apiDetTrans.getByTrans(kodeTrans);
        listCall.enqueue(new Callback<List<DetailTransaksi>>() {
            @Override
            public void onResponse(Call<List<DetailTransaksi>> call, Response<List<DetailTransaksi>> response) {
                try {
                    if(response.body() != null){
                        for(int i=0; i<response.body().size(); i++){
                            detailTransaksiList.add(response.body().get(i));
                            Log.d("dataTrans", "onResponse: Transaksi"+detailTransaksiList.get(i).getKODEDETAILTRANSAKSI()+detailTransaksiList.size());
                            getHistoryKeluar(detailTransaksiList.get(i).getKODEDETAILTRANSAKSI());
                        }
                    }
                } catch (Exception e) {
                    Log.d("Error response detail", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<DetailTransaksi>> call, Throwable t) {
                if (t instanceof IOException) {
                    Log.d("error", t.getMessage(), t);
                    // logging probably not necessary
                }
                else {
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
    }

    private void makeSparepartDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(TransaksiSparepart.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_sparepart, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        final AutoCompleteTextView autoSparepart = dialogView.findViewById(R.id.TrAddSparepart);
        final Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiSS apiSS = retrofit.create(ApiSS.class);
        Call<List<Sparepart>> sparepartCall = apiSS.getSparepart();
        sparepartCall.enqueue(new Callback<List<Sparepart>>() {
            @Override
            public void onResponse(Call<List<Sparepart>> call, Response<List<Sparepart>> response) {
                try {
                    if(response.body() != null){
                        Log.d("sparepartCall", "onResponse: size:  "+response.body().size());
                        for(int i=0; i<response.body().size(); i++){
                            sparepartList.add(response.body().get(i));
                        }
                    } else {
                        Log.d("sparepartCall", "onResponse: "+response.code());
                    }

                    sparepartAdapter sparepartArrayAdapter = new sparepartAdapter(TransaksiSparepart.this, R.layout.row_sparepart, sparepartList);
                    autoSparepart.setThreshold(1);
                    autoSparepart.setAdapter(sparepartArrayAdapter);
                    autoSparepart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Sparepart sparepart = (Sparepart) parent.getItemAtPosition(position);
                            kode = sparepart.getkODESPAREPART();
                            harga = sparepart.gethARGAJUAL();
                            Log.d("sparepart", "onClick: "+kode+" "+harga);
                        }
                    });
                } catch (Exception e) {
                    Log.d("Error sparepart", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Sparepart>> call, Throwable t) {
                Toast.makeText(TransaksiSparepart.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(TransaksiSparepart.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(TransaksiSparepart.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        final EditText jumlahSparepart = dialogView.findViewById(R.id.TrAddJumlah);
        dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int jumlah = Integer.parseInt(jumlahSparepart.getText().toString());
                kodeSparepart.add(new HistoryKeluar(kode, jumlah,harga*jumlah));
                Log.d("sparepart", "onClick: "+kode+" "+harga+" "+jumlah+" get");
                totalsparepart += harga*jumlah;
                setNewList(kodeSparepart);
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void setNewList(List<HistoryKeluar> kodeSparepart) {
        for (int i=0;i<kodeSparepart.size();i++){
            View tableRow = LayoutInflater.from(TransaksiSparepart.this).inflate(R.layout.tr_add_item,null,false);
            TextView id = tableRow.findViewById(R.id.textId);
            id.setText(String.valueOf(i+1));
            final TextView nama = tableRow.findViewById(R.id.textNama);
            TextView harga = tableRow.findViewById(R.id.textHarga);

            HistoryKeluar sparepart = kodeSparepart.get(i);

            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            ApiSparepart apiSparepart = retrofit.create(ApiSparepart.class);
            Call<Sparepart> sparepartCall = apiSparepart.detailSparepart(sparepart.getKODESPAREPART());
            sparepartCall.enqueue(new Callback<Sparepart>() {
                @Override
                public void onResponse(Call<Sparepart> call, Response<Sparepart> response) {
                    if (response.body()!=null){
                        nama.setText(response.body().getnAMASPAREPART());
                    } else {
                        Log.d("list", "onResponse: fail "+response.code());
                        nama.setText("Test Sparepart");
                    }
                }

                @Override
                public void onFailure(Call<Sparepart> call, Throwable t) {
                    if (t instanceof IOException) {
                        Toast.makeText(TransaksiSparepart.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(TransaksiSparepart.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });

            harga.setText(String.valueOf(sparepart.getHARGABARANGKELUAR()));
            tableLayout.addView(tableRow);
        }
    }

    private void saveDetail(int kodeTr){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);

        if (kodeSparepart.size()>0){
            Call<String> detSpCall = apiDetTrans.addDetTr(kodeTr, totalsparepart);
            detSpCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 500) {
                        Toast.makeText(TransaksiSparepart.this, "Failed To Save detail", Toast.LENGTH_SHORT).show();
                        try {
                            Log.d("500", "onResponse: detTrSparepart " + response.message() + "\n" + response.body()+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == 409) {
                        Toast.makeText(TransaksiSparepart.this, "detTr Already Exist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TransaksiSparepart.this, "detTr Success", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(TransaksiSparepart.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    if (t instanceof IOException) {
                        Toast.makeText(TransaksiSparepart.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(TransaksiSparepart.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });

            Call<String> detTrId = apiDetTrans.getMaxDetTr();
            detTrId.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.body()!=null){
                        kodeDetTr = Integer.parseInt(response.body());
                        Log.d("kodeDetTr", "onResponse: "+response.body());
                        saveHistory(kodeDetTr);
                    } else {
                        kodeDetTr = 0;
                        Log.d("kodeDetTr", "onResponse: "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(TransaksiSparepart.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TransaksiSparepart.this, MainActivity.class);
                    if (t instanceof IOException) {
                        Toast.makeText(TransaksiSparepart.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(TransaksiSparepart.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });
        }
    }

    private void saveHistory(int kodeDetTr){
        String tanggal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);

        for (int i=0; i<kodeSparepart.size();i++){
            HistoryKeluar historyKeluar = kodeSparepart.get(i);
            Call<String> historyKeluarCall =apiDetTrans.addHistoryKeluar(kodeDetTr, historyKeluar.getKODESPAREPART(), tanggal, historyKeluar.getSTOKBARANGKELUAR(), historyKeluar.getHARGABARANGKELUAR());
            historyKeluarCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 500) {
                        Toast.makeText(TransaksiSparepart.this, "Failed To Save history", Toast.LENGTH_SHORT).show();
                        try {
                            Log.d("500", "onResponse: historyKeluar " + response.message() + "\n" + response.body()+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == 409) {
                        Toast.makeText(TransaksiSparepart.this, "historyKeluar Already Exist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TransaksiSparepart.this, "historyKeluar Success", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(TransaksiSparepart.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    if (t instanceof IOException) {
                        Toast.makeText(TransaksiSparepart.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(TransaksiSparepart.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });
        }
    }
}
