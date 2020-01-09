package com.zhuo.dean.siksa_mobile.Start.Sparepart;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiPengadaan;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSparepart;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSupplier;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Model.DetailPengadaan;
import com.zhuo.dean.siksa_mobile.Start.Model.Sparepart;
import com.zhuo.dean.siksa_mobile.Start.Model.Supplier;
import com.zhuo.dean.siksa_mobile.Start.Transactional.sparepartAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProcurementTambah extends AppCompatActivity {

    EditText eTanggal, jumlahSparepart;
    Spinner eSupplier;
    String tanggal;
    int kSupplier;
    int total = 0;
    Button eSimpan, eSparepart;
    private AlertDialog loadDialog;

    android.app.AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    int hargaSp, kodePr;
    String namaSp;

    private List<Supplier> supplierList = new ArrayList<>();
    private List<Sparepart> sparepartList = new ArrayList<>();
    private List<DetailPengadaan> prSparepart = new ArrayList<>();
    private ArrayAdapter<Supplier> supplierArrayAdapter;
    private sparepartAdapter sparepartArrayAdapter;
    private AutoCompleteTextView autoSparepart;
    private ProcurementUbahAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurement_tambah);

        getSupportActionBar().setTitle("Add");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder build = Helper.loadDialog(this);
        loadDialog = build.create();

        eTanggal = findViewById(R.id.SpProAddTanggal);
        eSupplier = findViewById(R.id.SpProAddSupplier);

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiSupplier apiSupplier = retrofit.create(ApiSupplier.class);
        Call<List<Supplier>> supplierListCall = apiSupplier.getSupplier();

        supplierListCall.enqueue(new Callback<List<Supplier>>() {
            @Override
            public void onResponse(Call<List<Supplier>> call, Response<List<Supplier>> response) {
                try {
                    if(response.body() != null){
                        Log.d("supplierCall", "onResponse: "+response.body().size());
                        for(int i=0; i<response.body().size(); i++){
                            supplierList.add(response.body().get(i));
                        }
                    } else  {
                        Log.d("supplierCall", "onResponse: "+response.code());
                    }

                    supplierArrayAdapter = new ArrayAdapter<Supplier>(ProcurementTambah.this, android.R.layout.simple_spinner_item, supplierList);
                    supplierArrayAdapter.notifyDataSetChanged();
                    supplierArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    eSupplier.setAdapter(supplierArrayAdapter);
                    eSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Supplier supplier = (Supplier) parent.getSelectedItem();
                            kSupplier = supplier.getKODESUPPLIER();
                            Log.d("aaSupplier", "onItemSelected: "+kSupplier);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            kSupplier = 0;
                            Toast.makeText(ProcurementTambah.this, "Supplier not selected", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    Log.d("Error supplier", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Supplier>> call, Throwable t) {
                Toast.makeText(ProcurementTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(ProcurementTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(ProcurementTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        adapter = new ProcurementUbahAdapter(this, prSparepart);
        recyclerView = (RecyclerView) findViewById(R.id.SpProAddNewView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setDetail();
        recyclerView.setAdapter(adapter);

        eSparepart = (Button) findViewById(R.id.SpProAddSparepart);
        eSparepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSparepartDialog();
            }
        });

        eSimpan = (Button) findViewById(R.id.SpProSave);
        eSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSave();
                getPengadaanDetail();
            }
        });
    }

    private void makeSparepartDialog() {
        dialog = new AlertDialog.Builder(ProcurementTambah.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.layout_sparepart, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        autoSparepart = dialogView.findViewById(R.id.TrAddSparepart);
        final Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiSparepart apiSS = retrofit.create(ApiSparepart.class);
        Call<List<Sparepart>> sparepartCall = apiSS.getSpareparts();
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

                    sparepartArrayAdapter = new sparepartAdapter(ProcurementTambah.this, R.layout.row_sparepart, sparepartList);
                    autoSparepart.setThreshold(1);
                    autoSparepart.setAdapter(sparepartArrayAdapter);
                    autoSparepart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Sparepart sparepart = (Sparepart) parent.getItemAtPosition(position);
                            namaSp = sparepart.getnAMASPAREPART();
                            hargaSp = sparepart.gethARGAJUAL();
                            Log.d("sparepart", "onClick: "+namaSp+" "+hargaSp);
                        }
                    });
                } catch (Exception e) {
                    Log.d("Error sparepart", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Sparepart>> call, Throwable t) {
                Toast.makeText(ProcurementTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(ProcurementTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(ProcurementTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        jumlahSparepart = dialogView.findViewById(R.id.TrAddJumlah);
        dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int jumlah = Integer.parseInt(jumlahSparepart.getText().toString());
                prSparepart.add(new DetailPengadaan(namaSp, jumlah, hargaSp));
                Log.d("sparepart", "onClick: "+namaSp+" "+hargaSp+" "+jumlah+" get");
                total += hargaSp*jumlah;
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

    private void onClickSave() {
        tanggal = eTanggal.getText().toString();

            loadDialog.show();
            try {
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(Helper.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();

                ApiPengadaan apiPengadaan = retrofit.create(ApiPengadaan.class);
                Call<String> pengadaanCall = apiPengadaan.addPengadaan(kSupplier, 1, tanggal, total);
                pengadaanCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        int Status = response.code();
                        if (Status == 500) {
                            Toast.makeText(ProcurementTambah.this, "Failed To Save", Toast.LENGTH_SHORT).show();
                            try {
                                Log.d("500", "onResponse: Pengadaan" + response.message() + "\n" + response.body()+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (Status == 409) {
                            Toast.makeText(ProcurementTambah.this, "Pengadaan Already Exist", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProcurementTambah.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(ProcurementTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                        if (t instanceof IOException) {
                            Toast.makeText(ProcurementTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                            // logging probably not necessary
                        }
                        else {
                            Toast.makeText(ProcurementTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                            Log.d("error", t.getMessage(), t);
                            // todo log to some central bug tracking service
                        }
                    }
                });

            } catch (Exception e){
                Log.d("add", "onClickSave: " + e.getMessage());
            }
            loadDialog.dismiss();
            Intent intent = new Intent(ProcurementTambah.this, ProcurementSuppliers.class);
            startActivity(intent);

    }

    private void getPengadaanDetail() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiPengadaan apiPengadaan = retrofit.create(ApiPengadaan.class);
        Call<String> pengadaanid = apiPengadaan.getMaxPengadaan();
        pengadaanid.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body()!=null){
                    kodePr = Integer.parseInt(response.body());
                    Log.d("kodePr", "onResponse: "+kodePr);
                    saveDetail(kodePr);
                } else {
                    kodePr = 0;
                    Log.d("kodePr", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ProcurementTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(ProcurementTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(ProcurementTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });

    }

    private void  saveDetail(int kodePr){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiPengadaan apiPengadaan = retrofit.create(ApiPengadaan.class);

        for (int i = 0; i<prSparepart.size(); i++){
            DetailPengadaan detailPengadaan = prSparepart.get(i);
            Call<String> detailcall = apiPengadaan.addDetailPengadaan(kodePr, detailPengadaan.getNAMABARANG(), detailPengadaan.getJUMLAHPESANAN(), detailPengadaan.getHARGASATUAN());
            detailcall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 500) {
                        Toast.makeText(ProcurementTambah.this, "Failed To Save detail", Toast.LENGTH_SHORT).show();
                        try {
                            Log.d("500", "onResponse: detail " + response.message() + "\n" + response.body()+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == 409) {
                        Toast.makeText(ProcurementTambah.this, "detail Already Exist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProcurementTambah.this, "detail Success", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(ProcurementTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    if (t instanceof IOException) {
                        Toast.makeText(ProcurementTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(ProcurementTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });
        }
    }

    private void setDetail() {
        loadDialog.show();
        adapter = new ProcurementUbahAdapter(ProcurementTambah.this, prSparepart);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        Toast.makeText(ProcurementTambah.this, "Success", Toast.LENGTH_SHORT).show();
        loadDialog.dismiss();
    }
}
