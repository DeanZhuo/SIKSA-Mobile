package com.zhuo.dean.siksa_mobile.Start.Sparepart;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiPengadaan;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Model.Pengadaan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProcurementList extends AppCompatActivity {

    private AlertDialog dialog;
    private ProcurementAdapter adapter;
    private List<Pengadaan> pengadaanList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String TAG = "proList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurement_list);

        getSupportActionBar().setTitle("Show");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder builder = Helper.loadDialog(ProcurementList.this);
        dialog = builder.create();

        Button addButton = (Button) findViewById(R.id.SpProListAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProcurementList.this,ProcurementTambah.class);
                startActivity(intent);
            }
        });

        int kodeSupp = getIntent().getIntExtra("supplier",0);
        String namaSup = getIntent().getStringExtra("namaSupplier");
        Log.d("", "onCreate: value: "+ kodeSupp+namaSup);

        adapter = new ProcurementAdapter(this, pengadaanList, kodeSupp, namaSup);
        recyclerView = (RecyclerView) findViewById(R.id.SpShowProView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setList(kodeSupp, namaSup);
        recyclerView.setAdapter(adapter);

        EditText editText = (EditText) findViewById(R.id.SpProSearch);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String search) {
        ArrayList<Pengadaan> filter = new ArrayList<>();

        for (Pengadaan pengadaan : pengadaanList){
            if (pengadaan.getTANGGALPENGADAAN().toLowerCase().contains(search.toLowerCase())){
                filter.add(pengadaan);
            }
        }
        adapter.filterList(filter);
    }

    private void setList(final int kodeSupp, final String namaSup) {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiPengadaan apiPengadaan = retrofit.create(ApiPengadaan.class);
        Call<List<Pengadaan>> jsonObjectCall = apiPengadaan.getPengadaanBySupplier(kodeSupp);

        jsonObjectCall.enqueue(new Callback<List<Pengadaan>>() {
            @Override
            public void onResponse(Call<List<Pengadaan>> call, Response<List<Pengadaan>> response) {
                try {
                    adapter.notifyDataSetChanged();
                    if(response.body() != null){
                        for(int i=0; i<response.body().size(); i++){
                            pengadaanList.add(response.body().get(i));
                        }
                        Log.d(TAG, "onResponse: size: "+response.body().size());
                    }

                    adapter = new ProcurementAdapter(ProcurementList.this, pengadaanList, kodeSupp, namaSup);
                    recyclerView.setAdapter(adapter);
                    Toast.makeText(ProcurementList.this, "Success", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(ProcurementList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Error", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Pengadaan>> call, Throwable t) {
                Toast.makeText(ProcurementList.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(ProcurementList.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(ProcurementList.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        dialog.dismiss();
    }
}
