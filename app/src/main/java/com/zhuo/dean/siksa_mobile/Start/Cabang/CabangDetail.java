package com.zhuo.dean.siksa_mobile.Start.Cabang;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiCabang;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Model.Cabang;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CabangDetail extends AppCompatActivity {

    TextView dNama, dAlamat, dNTelp;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabang_detail);

        getSupportActionBar().setTitle("Add");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder builder = Helper.loadDialog(CabangDetail.this);
        dialog = builder.create();

        final int kCabang = getIntent().getIntExtra("id",0);

        dNama = findViewById(R.id.CaDetNama);
        dAlamat = findViewById(R.id.CaDetAlamat);
        dNTelp = findViewById(R.id.CaDetNoTelp);

        setDetail(kCabang);

        Button delete = findViewById(R.id.CaDetDel);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDel(kCabang);
            }
        });

        Button edit = findViewById(R.id.CaDetEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CabangDetail.this,CabangUbah.class);
                intent.putExtra("id",kCabang);
                startActivity(intent);
            }
        });
    }

    private void setDetail(int kCabang) {
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
                    Toast.makeText(CabangDetail.this, "Not Found", Toast.LENGTH_SHORT).show();
                }else if (response.code() == 500){
                    Toast.makeText(CabangDetail.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                }else{
                    Cabang cabang = response.body();
                    Toast.makeText(CabangDetail.this, "Cabang", Toast.LENGTH_SHORT).show();

                    dNama.setText(cabang.getNAMACABANG());
                    dAlamat.setText(cabang.getALAMATCABANG());
                    dNTelp.setText(cabang.getNOTELPCABANG());
                }
            }

            @Override
            public void onFailure(Call<Cabang> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
                    Toast.makeText(CabangDetail.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(CabangDetail.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
    }

    private void onClickDel(int kCabang) {
        dialog.show();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        ApiCabang apiCabang = retrofit.create(ApiCabang.class);
        Call<String> objectCall = apiCabang.deleteCabang(kCabang);
        objectCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                int status = response.code();
                if (status == 404){
                    Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_SHORT).show();
                }else{
                    if (status == 500) {
                        Toast.makeText(getApplicationContext(), "Error Deleting", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
                }
                Intent intent = new Intent(CabangDetail.this, CabangTampil.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
                    Toast.makeText(CabangDetail.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(CabangDetail.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        dialog.dismiss();
    }
}
