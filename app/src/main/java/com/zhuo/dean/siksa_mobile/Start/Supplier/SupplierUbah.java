package com.zhuo.dean.siksa_mobile.Start.Supplier;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSupplier;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Main.MainActivity;
import com.zhuo.dean.siksa_mobile.Start.Model.Supplier;
import com.zhuo.dean.siksa_mobile.Start.Sparepart.SparepartUbah;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupplierUbah extends AppCompatActivity {

    EditText uNSupplier, uAlamatS, uNSales, uNTSales;
    String NSupplier, AlamatS, NSales, NTSales;
    int KSupplier;
    Button upSave, upCancel;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_ubah);

        getSupportActionBar().setTitle("Update");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder builder = Helper.loadDialog(SupplierUbah.this);
        dialog = builder.create();
        dialog.show();

        KSupplier = getIntent().getIntExtra("id",0);

        setAtribut();
        setDetail(KSupplier);

        upSave = (Button) findViewById(R.id.suUpSave);
        upSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveSupplier(KSupplier);
            }
        });

        upCancel = (Button) findViewById(R.id.suUpCancel);
        upCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupplierUbah.this, SupplierDetail.class);
                intent.putExtra("id",KSupplier);
                startActivity(intent);
            }
        });
    }

    private void onSaveSupplier(int kSupplier) {
        if (uNSupplier.getText().toString().isEmpty() || uAlamatS.getText().toString().isEmpty() || uNSales.getText().toString().isEmpty() || uNTSales.getText().toString().isEmpty()){
            Toast.makeText(this, "Field Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else {
            dialog.show();
            Retrofit.Builder builder=new Retrofit.Builder()
                    .baseUrl(Helper.BASE_URL).
                            addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit=builder.build();
            ApiSupplier apiSupplier = retrofit.create(ApiSupplier.class);

            Call<String> objectCall = apiSupplier.updateSupplier(kSupplier, uNSupplier.getText().toString(), uAlamatS.getText().toString(), uNSales.getText().toString(), uNTSales.getText().toString());
            objectCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    int Status = response.code();
                    if (Status == 500) {
                        Toast.makeText(SupplierUbah.this, "Failed To Save", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SupplierUbah.this, SupplierTampil.class);
                        intent.putExtra("id",KSupplier);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SupplierUbah.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SupplierUbah.this, SupplierTampil.class);
                        intent.putExtra("id",KSupplier);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(SupplierUbah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SupplierUbah.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            dialog.dismiss();
        }
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
                }else{
                    Supplier supplier = response.body();
                    Toast.makeText(getApplicationContext(), "Sparepart", Toast.LENGTH_SHORT).show();
                    NSupplier = supplier.getNAMASUPPLIER();
                    AlamatS = supplier.getALAMATSUPPLIER();
                    NSales = supplier.getNAMASALES();
                    NTSales = supplier.getNOTELPSALES();

                    uNSupplier.setText(NSupplier);
                    uAlamatS.setText(AlamatS);
                    uNSales.setText(NSales);
                    uNTSales.setText(NTSales);
                }
            }

            @Override
            public void onFailure(Call<Supplier> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
                    Toast.makeText(SupplierUbah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(SupplierUbah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        dialog.dismiss();
    }

    private void setAtribut() {
        uNSupplier = (EditText) findViewById(R.id.SuUpNamaSupplier);
        uAlamatS = (EditText) findViewById(R.id.SuUpAlamat);
        uNSales = (EditText) findViewById(R.id.SuUpNamaSales);
        uNTSales = (EditText) findViewById(R.id.SuUpNoTelp);
    }
}
