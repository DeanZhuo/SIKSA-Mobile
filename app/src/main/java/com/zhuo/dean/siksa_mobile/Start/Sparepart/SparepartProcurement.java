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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSparepart;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Model.Sparepart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SparepartProcurement extends AppCompatActivity {

    private List<Sparepart> sparepartList = new ArrayList<>();
    private RecyclerView listView;
    private SparepartAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sparepart_procurement);

        getSupportActionBar().setTitle("Show");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder builder = Helper.loadDialog(SparepartProcurement.this);
        dialog = builder.create();

        adapter = new SparepartAdapter(this, sparepartList);
        listView = (RecyclerView) findViewById(R.id.SpProView);
        listView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        layoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);

        final EditText edSearch = findViewById(R.id.SpProNumber);

        ImageButton btnSearch = findViewById(R.id.SpProSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                int number = Integer.parseInt(edSearch.getText().toString());
                if (edSearch.getText().toString().isEmpty()){
                    dialog.dismiss();
                    Toast.makeText(SparepartProcurement.this, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else {
                    sparepartList.clear();
                    setList(number);
                }
            }
        });


        Button showList = (Button) findViewById(R.id.SpProShowList);
        showList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SparepartProcurement.this, ProcurementSuppliers.class);
                startActivity(intent);
            }
        });

        int minStock = getIntent().getIntExtra("min",0);
        edSearch.setText(String.valueOf(minStock));
        setList(minStock);
    }

    private void setList(int number) {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiSparepart apiSparepart = retrofit.create(ApiSparepart.class);
        Call<List<Sparepart>> jsonObjectCall = apiSparepart.getMinstok(number);

        jsonObjectCall.enqueue(new Callback<List<Sparepart>>() {
            @Override
            public void onResponse(Call<List<Sparepart>> call, Response<List<Sparepart>> response) {
                try {
                    adapter.notifyDataSetChanged();
                    if(response.body() != null){
                        for(int i=0; i<response.body().size(); i++){
                            sparepartList.add(response.body().get(i));
                        }
                    }

                    adapter = new SparepartAdapter(SparepartProcurement.this, sparepartList);
                    listView.setAdapter(adapter);
                    Toast.makeText(SparepartProcurement.this, "Success", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(SparepartProcurement.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Error", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Sparepart>> call, Throwable t) {
                Toast.makeText(SparepartProcurement.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(SparepartProcurement.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(SparepartProcurement.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        dialog.dismiss();
    }
}

