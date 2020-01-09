package com.zhuo.dean.siksa_mobile.Start.Supplier;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSupplier;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Main.MainActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupplierTambah extends AppCompatActivity {

    EditText eNSupplier, eAlamatS, eNSales, eNTSales;
    String NSupplier, AlamatS, NSales, NTSales;
    Button eSimpan;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_tambah);

        getSupportActionBar().setTitle("Add");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder builder = Helper.loadDialog(SupplierTambah.this);
        dialog = builder.create();

        setAtribut();

        eSimpan = (Button) findViewById(R.id.SuAddButton);
        eSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSave();
            }
        });
    }

    private void onClickSave() {
        if (eNSupplier.getText().toString().isEmpty() || eAlamatS.getText().toString().isEmpty() || eNSales.getText().toString().isEmpty() || eNTSales.getText().toString().isEmpty()){
            Toast.makeText(this, "Field Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else {
            dialog.show();
            try {
                NSupplier = eNSupplier.getText().toString();
                AlamatS = eAlamatS.getText().toString();
                NSales = eNSales.getText().toString();
                NTSales = eNTSales.getText().toString();

                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(Helper.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();
                ApiSupplier apiSupplier = retrofit.create(ApiSupplier.class);

                Call<String> objectCall = apiSupplier.addSupplier(NSupplier, AlamatS, NSales, NTSales);
                objectCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d("supplier", "onResponse: "+response.body());
                        try {
                            if (response.errorBody().string() != null) {
                                Log.d("tambah supplier", "onResponse: " + response.errorBody().string());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int Status = response.code();
                        if (Status == 500) {
                            Toast.makeText(SupplierTambah.this, "Failed To Save", Toast.LENGTH_SHORT).show();
                            Log.d("500", "onResponse: " + response.message() + "\n" + response.body());
                            Intent intent = new Intent(SupplierTambah.this, MainActivity.class);
                            startActivity(intent);
                        } else if (Status == 409) {
                            Toast.makeText(SupplierTambah.this, "Already Exist", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SupplierTambah.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SupplierTambah.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(SupplierTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SupplierTambah.this, MainActivity.class);
                        if (t instanceof IOException) {
                            Toast.makeText(SupplierTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                            // logging probably not necessary
                        }
                        else {
                            Toast.makeText(SupplierTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                            Log.d("error", t.getMessage(), t);
                            // todo log to some central bug tracking service
                        }
                        startActivity(intent);
                    }
                });
            }catch (Exception e){
                Log.d("add", "onClickSave: " + e.getMessage());
            }
            dialog.dismiss();
        }
    }

    private void setAtribut() {
        eNSupplier = (EditText) findViewById(R.id.SuAddSupplier);
        eAlamatS = (EditText) findViewById(R.id.SuAddAlamat);
        eNSales = (EditText) findViewById(R.id.SuAddSales);
        eNTSales = (EditText) findViewById(R.id.SuAddNoTelp);
    }
}
