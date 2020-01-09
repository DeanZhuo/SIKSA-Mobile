package com.zhuo.dean.siksa_mobile.Start.Supplier;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSupplier;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Main.LoginActivity;
import com.zhuo.dean.siksa_mobile.Start.Model.Supplier;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupplierDetail extends AppCompatActivity {

    TextView dNSupplier, dAlamatS, dNSales, dNTSales;
    String NSupplier, AlamatS, NSales, NTSales;
    int KSupplier;
    Button detDelete, detEdit;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_detail);

        getSupportActionBar().setTitle("Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder builder = Helper.loadDialog(SupplierDetail.this);
        dialog = builder.create();

        KSupplier = getIntent().getIntExtra("id",0);

        setAtribut();

        detDelete = (Button) findViewById(R.id.suDetDel);
        detDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteSupplier(KSupplier);
            }
        });

        detEdit = (Button) findViewById(R.id.suDetEdit);
        detEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupplierDetail.this, SupplierUbah.class);
                intent.putExtra("id",KSupplier);
                startActivity(intent);
            }
        });

        setDetail(KSupplier);
    }


    private void setDetail(int kSupplier) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        ApiSupplier apiSupplier = retrofit.create(ApiSupplier.class);
        Call<Supplier> objectCall = apiSupplier.detailSupplier(kSupplier);
        objectCall.enqueue(new Callback<Supplier>() {
            @Override
            public void onResponse(Call<Supplier> call, Response<Supplier> response) {
                int status = response.code();
                if (status == 404){
                    Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_SHORT).show();
                }else if (status == 500){
                    Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                }else{
                    Supplier supplier = response.body();
                    Toast.makeText(getApplicationContext(), "Supplier", Toast.LENGTH_SHORT).show();
                    NSupplier = supplier.getNAMASUPPLIER();
                    AlamatS = supplier.getALAMATSUPPLIER();
                    NSales = supplier.getNAMASALES();
                    NTSales = supplier.getNOTELPSALES();

                    dNSupplier.setText(NSupplier);
                    dAlamatS.setText(AlamatS);
                    dNSales.setText(NSales);
                    dNTSales.setText(NTSales);
                }
            }

            @Override
            public void onFailure(Call<Supplier> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
                    Toast.makeText(SupplierDetail.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(SupplierDetail.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        dialog.dismiss();
    }

    private void onDeleteSupplier(int kSupplier) {
        dialog.show();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        ApiSupplier apiSupplier = retrofit.create(ApiSupplier.class);
        Call<String> objectCall = apiSupplier.deleteSupplier(kSupplier);
        objectCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.code() == 404) {
                        Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_SHORT).show();
                    } else {
                        if (response.code() == 500) {
                            Toast.makeText(getApplicationContext(), "Error Deleting", Toast.LENGTH_SHORT).show();
                            Log.d("del sup", "onResponse: " + response.errorBody().string());
                        } else {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        }
                    }
                    Intent intent = new Intent(SupplierDetail.this, SupplierTampil.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d("del sup", "onResponse: "+e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
                    Toast.makeText(SupplierDetail.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(SupplierDetail.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        dialog.dismiss();
    }

    private void setAtribut() {
        dNSupplier = (TextView) findViewById(R.id.suDetNamaSupplier);
        dAlamatS = (TextView) findViewById(R.id.suDetAlamat);
        dNSales = (TextView) findViewById(R.id.suDetNamaSales);
        dNTSales = (TextView) findViewById(R.id.suDetNoTelpSales);
    }
}
