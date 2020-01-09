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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiPengadaan;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSparepart;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Model.DetailPengadaan;
import com.zhuo.dean.siksa_mobile.Start.Model.Sparepart;
import com.zhuo.dean.siksa_mobile.Start.Transactional.sparepartAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.PUT;

public class ProcurementUbah extends AppCompatActivity {

    private AlertDialog loaddialog;
    private LayoutInflater inflater;
    private View dialogView;
    private AlertDialog.Builder dialog;
    private AutoCompleteTextView autoSparepart;
    private List<Sparepart> sparepartList = new ArrayList<>();
    private sparepartAdapter sparepartArrayAdapter;
    private String namaSp;
    private int hargaSp;
    private List<DetailPengadaan> prSparepart = new ArrayList<>();
    private int total = 0;
    private ProcurementUbahAdapter adapter;
    private List<DetailPengadaan> detailPdaList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    int kodeSupp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurement_ubah);

        AlertDialog.Builder builder = Helper.loadDialog(this);
        loaddialog = builder.create();
        loaddialog.show();

        kodeSupp = getIntent().getIntExtra("supplier",0);
        final String namaSup = getIntent().getStringExtra("namaSupplier");
        final int kodePda = getIntent().getIntExtra("idPda",0);
        final String tanggal = getIntent().getStringExtra("tanggal");
        final int pembayaran = getIntent().getIntExtra("pembayaran",0);

        TextView namaSupplier = findViewById(R.id.SpProUpNama);
        namaSupplier.setText(namaSup);
        TextView tanggalPegadaan = findViewById(R.id.SpProUpTanggal);
        tanggalPegadaan.setText(tanggal);

        Button add = findViewById(R.id.SpProAddDetail);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSparepartDialog(kodePda);
            }
        });

        Button done = findViewById(R.id.SpProUpCancel);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProcurementUbah.this,ProcurementDetail.class);
                intent.putExtra("supplier", namaSup);
                intent.putExtra("idPda",kodePda);
                intent.putExtra("namaSupplier", namaSup);
                intent.putExtra("tanggal",tanggal);
                intent.putExtra("pembayaran",pembayaran);
                startActivity(intent);
            }
        });

        adapter = new ProcurementUbahAdapter(this, detailPdaList);
        recyclerView = (RecyclerView) findViewById(R.id.SpProEditView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setDetail(kodePda);
        recyclerView.setAdapter(adapter);
    }

    private void setDetail(int kodePda) {
        loaddialog.show();
        detailPdaList.clear();
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
                    adapter = new ProcurementUbahAdapter(ProcurementUbah.this, detailPdaList);
                    recyclerView.setAdapter(adapter);
                    Toast.makeText(ProcurementUbah.this, "Success", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(ProcurementUbah.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Error detail list", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<DetailPengadaan>> call, Throwable t) {
                Toast.makeText(ProcurementUbah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(ProcurementUbah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(ProcurementUbah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        loaddialog.dismiss();
    }

    private void makeSparepartDialog(final int kodePda) {
        dialog = new AlertDialog.Builder(ProcurementUbah.this);
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

                    sparepartArrayAdapter = new sparepartAdapter(ProcurementUbah.this, R.layout.row_sparepart, sparepartList);
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
                Toast.makeText(ProcurementUbah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(ProcurementUbah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(ProcurementUbah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
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
                prSparepart.add(new DetailPengadaan(namaSp, jumlah, hargaSp));
                Log.d("sparepart", "onClick: "+namaSp+" "+hargaSp+" "+jumlah+" get");
                total += hargaSp*jumlah;
                saveDetail(kodePda);
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

    private void  saveDetail(int kodePda){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiPengadaan apiPengadaan = retrofit.create(ApiPengadaan.class);

        for (int i = 0; i<prSparepart.size(); i++){
            DetailPengadaan detailPengadaan = prSparepart.get(i);
            Call<String> detailcall = apiPengadaan.addDetailPengadaan(kodePda, detailPengadaan.getNAMABARANG(), detailPengadaan.getJUMLAHPESANAN(), detailPengadaan.getHARGASATUAN());
            detailcall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 500) {
                        Toast.makeText(ProcurementUbah.this, "Failed To Save detail", Toast.LENGTH_SHORT).show();
                        try {
                            Log.d("500", "onResponse: detail " + response.message() + "\n" + response.body()+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == 409) {
                        Toast.makeText(ProcurementUbah.this, "detail Already Exist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProcurementUbah.this, "detail Success", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(ProcurementUbah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    if (t instanceof IOException) {
                        Toast.makeText(ProcurementUbah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(ProcurementUbah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });
        }
        setDetail(kodePda);
    }

    protected void deleteDetail(int kodeDetail, int kodePda){
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiPengadaan apiPengadaan = retrofit.create(ApiPengadaan.class);
        Call<String> call = apiPengadaan.deleteDetPengadaan(kodeDetail);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code()==200){
                    Toast.makeText(ProcurementUbah.this, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProcurementUbah.this, "Fail to delete", Toast.LENGTH_SHORT).show();
                    Log.d("delDetPda", "onResponse: " +response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ProcurementUbah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(ProcurementUbah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(ProcurementUbah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });

        setDetail(kodePda);
    }
}
