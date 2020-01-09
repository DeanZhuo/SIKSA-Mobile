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

public class SparepartTampil extends AppCompatActivity {

    private List<Sparepart> sparepartList = new ArrayList<>();
    private RecyclerView listView;
    private SparepartAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sparepart_tampil);

        getSupportActionBar().setTitle("Show");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder builder = Helper.loadDialog(SparepartTampil.this);
        dialog = builder.create();
        dialog.show();

        Button showAdd = (Button) findViewById(R.id.spShowAdd);
        showAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SparepartTampil.this, SparepartTambah.class);
                startActivity(intent);
            }
        });

        adapter = new SparepartAdapter(this, sparepartList);
        listView = (RecyclerView) findViewById(R.id.SpShowList);
        listView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        layoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        setList();
        listView.setAdapter(adapter);

        EditText editText = (EditText) findViewById(R.id.SpShowSearchText);
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
        ArrayList<Sparepart> filter = new ArrayList<>();

        for (Sparepart sparepart : sparepartList){
            if (sparepart.getnAMASPAREPART().toLowerCase().contains(search.toLowerCase())){
                filter.add(sparepart);
            }
        }
        adapter.filterList(filter);
    }

    private void setList() {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiSparepart apiSparepart = retrofit.create(ApiSparepart.class);
        Call<List<Sparepart>> jsonObjectCall = apiSparepart.getSpareparts();

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

                    adapter = new SparepartAdapter(SparepartTampil.this, sparepartList);
                    listView.setAdapter(adapter);
                    Toast.makeText(SparepartTampil.this, "Success", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(SparepartTampil.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Error", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Sparepart>> call, Throwable t) {
                Toast.makeText(SparepartTampil.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(SparepartTampil.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(SparepartTampil.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        dialog.dismiss();
    }
}
