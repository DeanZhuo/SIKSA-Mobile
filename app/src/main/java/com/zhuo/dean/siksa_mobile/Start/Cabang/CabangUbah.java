package com.zhuo.dean.siksa_mobile.Start.Cabang;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiCabang;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Main.MainActivity;
import com.zhuo.dean.siksa_mobile.Start.Model.Cabang;
import com.zhuo.dean.siksa_mobile.Start.Supplier.SupplierDetail;
import com.zhuo.dean.siksa_mobile.Start.Supplier.SupplierUbah;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CabangUbah extends AppCompatActivity {

    EditText dNama, dAlamat, dNTelp;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabang_ubah);

        getSupportActionBar().setTitle("Update");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder builder = Helper.loadDialog(CabangUbah.this);
        dialog = builder.create();
        dialog.show();

        final int kCabang = getIntent().getIntExtra("id",0);

        setDetail(kCabang);
        dialog.dismiss();

        Button upSave = (Button) findViewById(R.id.CaUpSave);
        upSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveCabang(kCabang);
            }
        });

        Button upCancel = (Button) findViewById(R.id.CaUpCancel);
        upCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CabangUbah.this, CabangDetail.class);
                intent.putExtra("id",kCabang);
                startActivity(intent);
            }
        });
    }

    private void setDetail(int kCabang) {
        dNama = findViewById(R.id.CaUpNama);
        dAlamat = findViewById(R.id.CaUpAlamat);
        dNTelp = findViewById(R.id.CaUpNoTelp);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        ApiCabang apiCabang = retrofit.create(ApiCabang.class);
        Call<Cabang> objectCall = apiCabang.getCabangById(kCabang);
        objectCall.enqueue(new Callback<Cabang>() {
            @Override
            public void onResponse(Call<Cabang> call, Response<Cabang> response) {
                if (response.code() == 404){
                    Toast.makeText(CabangUbah.this, "Not Found", Toast.LENGTH_SHORT).show();
                }else if (response.code() == 500){
                    Toast.makeText(CabangUbah.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                }else{
                    Cabang cabang = response.body();
                    Toast.makeText(CabangUbah.this, "Cabang", Toast.LENGTH_SHORT).show();

                    dNama.setText(cabang.getNAMACABANG());
                    dAlamat.setText(cabang.getALAMATCABANG());
                    dNTelp.setText(cabang.getNOTELPCABANG());
                }
            }

            @Override
            public void onFailure(Call<Cabang> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
                    Toast.makeText(CabangUbah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(CabangUbah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
    }

    private void onSaveCabang(final int kCabang) {
        if (dNama.getText().toString().isEmpty() || dAlamat.getText().toString().isEmpty() || dNTelp.getText().toString().isEmpty()){
            Toast.makeText(this, "Field Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else {
            dialog.show();
            Retrofit.Builder builder=new Retrofit.Builder()
                    .baseUrl(Helper.BASE_URL).
                            addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit=builder.build();
            ApiCabang apiCabang = retrofit.create(ApiCabang.class);

            Call<String> objectCall = apiCabang.updateCabang(kCabang,dNama.getText().toString(),dAlamat.getText().toString(),dNTelp.getText().toString());
            objectCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    int Status = response.code();
                    if (Status == 500) {
                        Toast.makeText(CabangUbah.this, "Failed To Save", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CabangUbah.this, CabangTampil.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CabangUbah.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CabangUbah.this, CabangTampil.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(CabangUbah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CabangUbah.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            dialog.dismiss();
        }
    }
}
