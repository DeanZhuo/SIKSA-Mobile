package com.zhuo.dean.siksa_mobile.Start.Sparepart;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSparepart;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Main.MainActivity;
import com.zhuo.dean.siksa_mobile.Start.Model.Sparepart;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SparepartUbah extends AppCompatActivity {

    TextView uKBarang, uTipe, uNama, uMerk, uStok;
    EditText uJual, uBeli, uKPeletakan;
    int KSparepart, Jual, Beli, Stok;
    String KBarang, Tipe, Nama, Merk, KPeletakan;
    Button upSave, upCancel;
    ImageView upGambar;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sparepart_ubah);

        getSupportActionBar().setTitle("Update");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder builder = Helper.loadDialog(SparepartUbah.this);
        dialog = builder.create();
        dialog.show();

        KSparepart = getIntent().getIntExtra("id",1);

        setAtribut();
        setDetail(KSparepart);

        upSave = (Button) findViewById(R.id.spUpSave);
        upSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveSparepart(KSparepart);
            }
        });

        upCancel = (Button) findViewById(R.id.spUpCancel);
        upCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SparepartUbah.this, SparepartDetail.class);
                intent.putExtra("id",KSparepart);
                startActivity(intent);
            }
        });
    }

    private void setDetail(int kSparepart) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        ApiSparepart apiSparepart = retrofit.create(ApiSparepart.class);
        Call<Sparepart> objectCall = apiSparepart.detailSparepart(kSparepart);
        objectCall.enqueue(new Callback<Sparepart>() {
            @Override
            public void onResponse(Call<Sparepart> call, Response<Sparepart> response) {
                int status = response.code();
                if (status == 404){
                    Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_SHORT).show();
                }else{
                    Sparepart sparepart = response.body();
                    Toast.makeText(getApplicationContext(), "Sparepart", Toast.LENGTH_SHORT).show();
                    KBarang = sparepart.getkODEBARANG();
                    Tipe = sparepart.gettIPESPAREPART();
                    Nama = sparepart.getnAMASPAREPART();
                    Jual = sparepart.gethARGAJUAL();
                    Beli = sparepart.gethARGABELI();
                    Merk = sparepart.getmERKSPAREPART();
                    KPeletakan = sparepart.getkODEPELETAKAN();
                    Stok = sparepart.getjUMLAHSTOK();

                    uKBarang.setText(KBarang);
                    uTipe.setText(Tipe);
                    uNama.setText(Nama);
                    uJual.setText(String.valueOf(Jual));
                    uBeli.setText(String.valueOf(Beli));
                    uMerk.setText(Merk);
                    uKPeletakan.setText(KPeletakan);
                    uStok.setText(String.valueOf(Stok));
                }
            }

            @Override
            public void onFailure(Call<Sparepart> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.dismiss();
    }

    private void onSaveSparepart(int kSparepart) {
        if (uJual.getText().toString().isEmpty() || uBeli.getText().toString().isEmpty() || uKPeletakan.getText().toString().isEmpty()){
            Toast.makeText(this, "Field Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else {
            dialog.show();
            Retrofit.Builder builder=new Retrofit.Builder()
                    .baseUrl(Helper.BASE_URL).
                            addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit=builder.build();
            ApiSparepart apiSparepart = retrofit.create(ApiSparepart.class);

            Call<String> objectCall = apiSparepart.updateSparepart(kSparepart, Integer.parseInt(uJual.getText().toString()), Integer.parseInt(uBeli.getText().toString()), uKPeletakan.getText().toString());
            objectCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    int Status = response.code();
                    if (Status == 500) {
                        Toast.makeText(SparepartUbah.this, "Failed To Save", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SparepartUbah.this, SparepartDetail.class);
                        intent.putExtra("id",KSparepart);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SparepartUbah.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SparepartUbah.this, SparepartDetail.class);
                        intent.putExtra("id",KSparepart);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(SparepartUbah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SparepartUbah.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            dialog.dismiss();
        }
    }

    private void setAtribut() {
        uKBarang = (TextView) findViewById(R.id.spUpKBarang);
        uTipe = (TextView) findViewById(R.id.spUpTipe);
        uNama = (TextView) findViewById(R.id.spUpNama);
        uJual = (EditText) findViewById(R.id.spUpJual);
        uBeli = (EditText) findViewById(R.id.spUpBeli);
        uMerk = (TextView) findViewById(R.id.spUpMerk);
        uKPeletakan = (EditText) findViewById(R.id.spUpKPeletakan);
        uStok = (TextView) findViewById(R.id.spUpStok);

        upGambar = (ImageView) findViewById(R.id.spUpGambar);
    }


}
