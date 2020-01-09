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
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiDetTrans;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSS;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Main.MainActivity;
import com.zhuo.dean.siksa_mobile.Start.Model.DetailJasa;
import com.zhuo.dean.siksa_mobile.Start.Model.DetailTransaksi;
import com.zhuo.dean.siksa_mobile.Start.Model.HistoryKeluar;
import com.zhuo.dean.siksa_mobile.Start.Model.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransaksiService extends AppCompatActivity {

    private List<DetailTransaksi> detailTransaksiList = new ArrayList<>();
    private List<DetailJasa> detailJasaList = new ArrayList<>();
    private DetailJasaAdapter adapter;
    private RecyclerView recyclerView, recyclerViewNew;
    private RecyclerView.LayoutManager layoutManager;
    private int kodeTrans;
    private List<Service> serviceList = new ArrayList<>();
    private List<Service> kodeService = new ArrayList<>();
    private List<HistoryKeluar> kodeSparepart = new ArrayList<>();
    private int kode, harga;
    private String nama;
    private int totalservice=0;
    private TableLayout tableLayout;
    private int kodeDetTr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_transaksi_service);

        kodeTrans = getIntent().getIntExtra("kodeTrans",0);
        getDataTransaksi(kodeTrans);

        adapter = new DetailJasaAdapter(this, detailJasaList);
        adapter.notifyDataSetChanged();
        recyclerView = (RecyclerView) findViewById(R.id.TrServiceView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        tableLayout = findViewById(R.id.TrDetNewService);

        Button addButton = findViewById(R.id.TrDetAddService);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeServiceDialog();
            }
        });

        Button done = findViewById(R.id.TrDetServiceDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetail(kodeTrans);
                Intent intent = new Intent(TransaksiService.this, TransaksiByCustomer.class);
                startActivity(intent);
            }
        });
    }

    private void getDetailJasa(int kodedetailtransaksi) {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);
        Call<List<DetailJasa>> listCall  = apiDetTrans.getDetJsByDetTr(kodedetailtransaksi);
        listCall.enqueue(new Callback<List<DetailJasa>>() {
            @Override
            public void onResponse(Call<List<DetailJasa>> call, Response<List<DetailJasa>> response) {
                try {
                    adapter.notifyDataSetChanged();
                    if(response.body() != null){
                        for(int i=0; i<response.body().size(); i++){
                            detailJasaList.add(response.body().get(i));
                        }
                        Log.d("service", "onResponse: size: "+detailJasaList.size());
                    }else {
                        Log.d("service", "onResponse: "+response.code());
                    }
                    adapter = new DetailJasaAdapter(TransaksiService.this,detailJasaList);
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    Log.d("Error response", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<DetailJasa>> call, Throwable t) {
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
                            getDetailJasa(detailTransaksiList.get(i).getKODEDETAILTRANSAKSI());
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

    private void makeServiceDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(TransaksiService.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_service, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        final AutoCompleteTextView autoService = dialogView.findViewById(R.id.TrAddService);
        final Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiSS apiSS = retrofit.create(ApiSS.class);
        Call<List<Service>> sparepartCall = apiSS.getService();
        sparepartCall.enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                try {
                    if(response.body() != null){
                        Log.d("serviceCall", "onResponse: size:  "+response.body().size());
                        for(int i=0; i<response.body().size(); i++){
                            serviceList.add(response.body().get(i));
                        }
                    } else {
                        Log.d("serviceCall", "onResponse: "+response.code());
                    }

                    serviceAdapter serviceArrayAdapter = new serviceAdapter(TransaksiService.this, R.layout.row_service, serviceList);
                    autoService.setThreshold(1);
                    autoService.setAdapter(serviceArrayAdapter);
                    autoService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Service service = (Service) parent.getItemAtPosition(position);
                            kode = service.getKODEJASA();
                            nama = service.getNAMASERVICE();
                            harga = service.getHARGASERVICE();
                            Log.d("service", "onClick: "+kode+" "+harga);
                        }
                    });
                } catch (Exception e) {
                    Log.d("Error service", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                Toast.makeText(TransaksiService.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(TransaksiService.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(TransaksiService.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                kodeService.add(new Service(kode, nama, harga));
                Log.d("service", "onClick: "+kode+" "+harga+" get");
                totalservice += harga;
                setNewList(kodeService);
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

    private void setNewList(List<Service> kodeService) {
        for (int i=0;i<kodeService.size();i++){
            View tableRow = LayoutInflater.from(TransaksiService.this).inflate(R.layout.tr_add_item,null,false);
            TextView id = tableRow.findViewById(R.id.textId);
            id.setText(String.valueOf(i+1));
            TextView nama = tableRow.findViewById(R.id.textNama);
            TextView harga = tableRow.findViewById(R.id.textHarga);

            Service service = kodeService.get(i);
            nama.setText(service.getNAMASERVICE());
            harga.setText(String.valueOf(service.getHARGASERVICE()));
            tableLayout.addView(tableRow);
        }
    }

    private void saveDetail(int kodeTr){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);

        if (kodeService.size()>0){
            Call<String> detSpCall = apiDetTrans.addDetTr(kodeTr, totalservice);
            detSpCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 500) {
                        Toast.makeText(TransaksiService.this, "Failed To Save detail", Toast.LENGTH_SHORT).show();
                        try {
                            Log.d("500", "onResponse: detTrSparepart " + response.message() + "\n" + response.body()+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == 409) {
                        Toast.makeText(TransaksiService.this, "detTr Already Exist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TransaksiService.this, "detTr Success", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(TransaksiService.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    if (t instanceof IOException) {
                        Toast.makeText(TransaksiService.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(TransaksiService.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
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
                        saveJasa(kodeDetTr);
                    } else {
                        kodeDetTr = 0;
                        Log.d("kodeDetTr", "onResponse: "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(TransaksiService.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TransaksiService.this, MainActivity.class);
                    if (t instanceof IOException) {
                        Toast.makeText(TransaksiService.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(TransaksiService.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });
        }
    }

    private void saveJasa(int kodeDetTr){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);
        for (int i=0; i<kodeService.size();i++){
            Service service = kodeService.get(i);
            Call<String> detailJasaCall = apiDetTrans.addDetailJasa(kodeDetTr, service.getKODEJASA());
            detailJasaCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 500) {
                        Toast.makeText(TransaksiService.this, "Failed To Save history", Toast.LENGTH_SHORT).show();
                        try {
                            Log.d("500", "onResponse: detailJasa " + response.message() + "\n" + response.body()+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == 409) {
                        Toast.makeText(TransaksiService.this, "detailJasa Already Exist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TransaksiService.this, "detailJasa Success", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(TransaksiService.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    if (t instanceof IOException) {
                        Toast.makeText(TransaksiService.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(TransaksiService.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });
        }
    }
}
