package com.zhuo.dean.siksa_mobile.Start.Cabang;

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
import com.zhuo.dean.siksa_mobile.Start.API.ApiCabang;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Model.Cabang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CabangTampil extends AppCompatActivity {

    private AlertDialog dialog;
    private List<Cabang> cabangList = new ArrayList<>();
    private CabangAdapter adapter;
    private RecyclerView listView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabang_tampil);

        getSupportActionBar().setTitle("Show");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder builder = Helper.loadDialog(CabangTampil.this);
        dialog = builder.create();

        Button showAdd = (Button) findViewById(R.id.CaShowAdd);
        showAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CabangTampil.this, CabangTambah.class);
                startActivity(intent);
            }
        });

        adapter = new CabangAdapter(CabangTampil.this, cabangList);
        listView = (RecyclerView) findViewById(R.id.CaShowList);
        listView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        layoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        setList();
        listView.setAdapter(adapter);

        EditText editText = (EditText) findViewById(R.id.CaShowSearchText);
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
        ArrayList<Cabang> filter = new ArrayList<>();

        for (Cabang cabang : cabangList){
            if (cabang.getNAMACABANG().toLowerCase().contains(search.toLowerCase())){
                filter.add(cabang);
            }
        }
        adapter.filterList(filter);
    }

    private void setList() {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiCabang apiCabang = retrofit.create(ApiCabang.class);
        Call<List<Cabang>> jsonObjectCall = apiCabang.getCabang();

        jsonObjectCall.enqueue(new Callback<List<Cabang>>() {
            @Override
            public void onResponse(Call<List<Cabang>> call, Response<List<Cabang>> response) {
                adapter.notifyDataSetChanged();
                if(response.body() != null){
                    for(int i=0; i<response.body().size(); i++){
                        cabangList.add(response.body().get(i));
                    }
                }
                adapter = new CabangAdapter(CabangTampil.this, cabangList);
                listView.setAdapter(adapter);
                Toast.makeText(CabangTampil.this, "Success", Toast.LENGTH_SHORT).show();
                Log.d("su", "onResponse: " +response.body().get(1).getNAMACABANG());
            }

            @Override
            public void onFailure(Call<List<Cabang>> call, Throwable t) {
                Toast.makeText(CabangTampil.this, "Network Connection Error", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
                    Toast.makeText(CabangTampil.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(CabangTampil.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        dialog.dismiss();
    }
}
