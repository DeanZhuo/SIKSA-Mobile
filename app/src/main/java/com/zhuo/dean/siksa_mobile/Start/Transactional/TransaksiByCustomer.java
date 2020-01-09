package com.zhuo.dean.siksa_mobile.Start.Transactional;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
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
import com.zhuo.dean.siksa_mobile.Start.API.ApiCustomer;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Model.Customer;
import com.zhuo.dean.siksa_mobile.Start.Model.Pengadaan;
import com.zhuo.dean.siksa_mobile.Start.Sparepart.SparepartProcurement;
import com.zhuo.dean.siksa_mobile.Start.Sparepart.SparepartTampil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransaksiByCustomer extends AppCompatActivity {
    private List<Customer> customerList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_by_customer);

        getSupportActionBar().setTitle("Show");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final AlertDialog.Builder builder = Helper.loadDialog(TransaksiByCustomer.this);
        dialog = builder.create();
        dialog.show();

        adapter = new CustomerAdapter(this, customerList);
        recyclerView = (RecyclerView) findViewById(R.id.CustomerList);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setList();
        recyclerView.setAdapter(adapter);

        EditText editText = (EditText) findViewById(R.id.TrCusText);
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
        ArrayList<Customer> filter = new ArrayList<>();

        for (Customer customer : customerList){
            if (customer.getnAMACUST().toLowerCase().contains(search.toLowerCase())){
                filter.add(customer);
            }
        }
        adapter.filterList(filter);
    }

    private void setList() {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiCustomer apiSupplier = retrofit.create(ApiCustomer.class);
        Call<List<Customer>> jsonObjectCall = apiSupplier.getCustomer();

        jsonObjectCall.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                adapter.notifyDataSetChanged();
                if(response.body() != null){
                    for(int i=0; i<response.body().size(); i++){
                        customerList.add(response.body().get(i));
                    }
                }
                adapter = new CustomerAdapter(TransaksiByCustomer.this, customerList);
                recyclerView.setAdapter(adapter);
                Toast.makeText(TransaksiByCustomer.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                Toast.makeText(TransaksiByCustomer.this, "Network Connection Error", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
                    Toast.makeText(TransaksiByCustomer.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(TransaksiByCustomer.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });

        dialog.dismiss();
    }
}
