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
import com.zhuo.dean.siksa_mobile.Start.API.ApiSupplier;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Model.Supplier;
import com.zhuo.dean.siksa_mobile.Start.Supplier.SupplierAdapter;
import com.zhuo.dean.siksa_mobile.Start.Supplier.SupplierTambah;
import com.zhuo.dean.siksa_mobile.Start.Supplier.SupplierTampil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProcurementSuppliers extends AppCompatActivity {

    private List<Supplier> supplierList = new ArrayList<>();
    private RecyclerView listView;
    private RecyclerView.LayoutManager layoutManager;
    private ProSupplierAdapter adapter;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_tampil);

        getSupportActionBar().setTitle("Show");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        android.app.AlertDialog.Builder builder = Helper.loadDialog(ProcurementSuppliers.this);
        dialog = builder.create();

        Button showAdd = (Button) findViewById(R.id.suShowAdd);
        showAdd.setVisibility(View.INVISIBLE);

        adapter = new ProSupplierAdapter(this, supplierList);
        listView = (RecyclerView) findViewById(R.id.TrShowList);
        listView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        layoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        setList();
        listView.setAdapter(adapter);

        EditText editText = (EditText) findViewById(R.id.suShowSearchText);
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
        ArrayList<Supplier> filter = new ArrayList<>();

        for (Supplier supplier : supplierList){
            if (supplier.getNAMASUPPLIER().toLowerCase().contains(search.toLowerCase())){
                filter.add(supplier);
            }
        }
        adapter.filterList(filter);
    }

    private void setList() {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiSupplier apiSupplier = retrofit.create(ApiSupplier.class);
        Call<List<Supplier>> jsonObjectCall = apiSupplier.getSupplier();

        jsonObjectCall.enqueue(new Callback<List<Supplier>>() {
            @Override
            public void onResponse(Call<List<Supplier>> call, Response<List<Supplier>> response) {
                adapter.notifyDataSetChanged();
                if(response.body() != null){
                    for(int i=0; i<response.body().size(); i++){
                        supplierList.add(response.body().get(i));
                    }
                } else {
                    try {
                        Log.d("pengadaan", "onResponse: "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                adapter = new ProSupplierAdapter(ProcurementSuppliers.this, supplierList);
                listView.setAdapter(adapter);
                Toast.makeText(ProcurementSuppliers.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Supplier>> call, Throwable t) {
                Toast.makeText(ProcurementSuppliers.this, "Network Connection Error", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
                    Toast.makeText(ProcurementSuppliers.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(ProcurementSuppliers.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        dialog.dismiss();
    }
}
