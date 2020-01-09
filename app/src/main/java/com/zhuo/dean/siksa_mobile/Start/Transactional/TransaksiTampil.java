package com.zhuo.dean.siksa_mobile.Start.Transactional;

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
import com.zhuo.dean.siksa_mobile.Start.API.ApiTransaksi;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Model.Transaksi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransaksiTampil extends AppCompatActivity {

    private List<Transaksi> transaksiList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TransaksiTampilAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private int kodeCust;
    private String namaCus;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_tampil);

        getSupportActionBar().setTitle("Show");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder builder = Helper.loadDialog(TransaksiTampil.this);
        dialog = builder.create();
        dialog.show();

        kodeCust = getIntent().getIntExtra("customer",1);
        namaCus = getIntent().getStringExtra("nameCus");

        Button showAdd = (Button) findViewById(R.id.TrShowAdd);
        showAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaksiTampil.this, TransaksiTambah.class);
                intent.putExtra("customer",kodeCust);
                startActivity(intent);
            }
        });

        adapter = new TransaksiTampilAdapter(this, transaksiList, kodeCust, namaCus);
        recyclerView = (RecyclerView) findViewById(R.id.TrShowList);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setList(kodeCust);
        recyclerView.setAdapter(adapter);

        EditText editText = (EditText) findViewById(R.id.TrShowSearchText);
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
        ArrayList<Transaksi> filter = new ArrayList<>();

        for (Transaksi transaksi : transaksiList){
            if (transaksi.getKODEPENJUALAN().toLowerCase().contains(search.toLowerCase())){
                filter.add(transaksi);
            }
        }
        adapter.filterList(filter);
    }

    private void setList(final int kodeCust) {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiTransaksi apiTransaksi = retrofit.create(ApiTransaksi.class);
        Call<List<Transaksi>> jsonObjectCall = apiTransaksi.getTransaksiByCust(kodeCust);

        jsonObjectCall.enqueue(new Callback<List<Transaksi>>() {
            @Override
            public void onResponse(Call<List<Transaksi>> call, Response<List<Transaksi>> response) {
                try {
                    adapter.notifyDataSetChanged();
                    if(response.body() != null){
                        for(int i=0; i<response.body().size(); i++){
                            transaksiList.add(response.body().get(i));
                        }
                    }

                    adapter = new TransaksiTampilAdapter(TransaksiTampil.this, transaksiList, kodeCust, namaCus);
                    recyclerView.setAdapter(adapter);
                    Toast.makeText(TransaksiTampil.this, "Success", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(TransaksiTampil.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Error", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Transaksi>> call, Throwable t) {
                Toast.makeText(TransaksiTampil.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(TransaksiTampil.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(TransaksiTampil.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        dialog.dismiss();
    }
}
