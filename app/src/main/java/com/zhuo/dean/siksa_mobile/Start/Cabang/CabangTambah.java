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

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CabangTambah extends AppCompatActivity {

    EditText eNama, eAlamat, eNTelp;
    String nama, alamat, nTelp;
    Button eSimpan;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabang_tambah);

        getSupportActionBar().setTitle("Add");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder builder = Helper.loadDialog(CabangTambah.this);
        dialog = builder.create();

        eNama = findViewById(R.id.CaAddNama);
        eAlamat = findViewById(R.id.CaAddAlamat);
        eNTelp = findViewById(R.id.CaAddNoTelp);

        eSimpan = (Button) findViewById(R.id.CaAddButton);
        eSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSave();
            }
        });
    }

    private void onClickSave() {
        if (eNama.getText().toString().isEmpty() || eAlamat.getText().toString().isEmpty() || eNTelp.getText().toString().isEmpty()){
            Toast.makeText(this, "Field Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else {
            dialog.show();
            try {
                nama = eNama.getText().toString();
                alamat = eAlamat.getText().toString();
                nTelp = eNTelp.getText().toString();

                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(Helper.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();
                ApiCabang apiCabang = retrofit.create(ApiCabang.class);

                Call<String> objectCall = apiCabang.addCabang(nama, alamat, nTelp);
                objectCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        int Status = response.code();
                        if (Status == 500) {
                            Toast.makeText(CabangTambah.this, "Failed To Save", Toast.LENGTH_SHORT).show();
                            Log.d("500", "onResponse: " + response.message() + "\n" + response.body());
                            Intent intent = new Intent(CabangTambah.this, CabangTampil.class);
                            startActivity(intent);
                        } else if (Status == 409) {
                            Toast.makeText(CabangTambah.this, "Already Exist", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CabangTambah.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CabangTambah.this, CabangTampil.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(CabangTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CabangTambah.this, CabangTampil.class);
                        if (t instanceof IOException) {
                            Toast.makeText(CabangTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                            // logging probably not necessary
                        }
                        else {
                            Toast.makeText(CabangTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
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
}
