package com.zhuo.dean.siksa_mobile.Start.Main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.zhuo.dean.siksa_mobile.Start.API.ApiPegawai;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PasswordChange extends AppCompatActivity {

    Button bSave, bCancel;
    EditText edOld, edNew, edConfirm;
    String username;
    int KPid;
    int Status;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        getSupportActionBar().setTitle("Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences(Helper.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        KPid = sharedPreferences.getInt("id",1);

        setAtribut();
        bSave = (Button) findViewById(R.id.changeSave);
        bSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickLSave();
                }
            });
        bCancel = (Button) findViewById(R.id.changeCancel);
        bCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(PasswordChange.this, MainActivity.class);
                    startActivity(intent);
                }
            });
    }

    private void onClickLSave() {
        if(edOld.getText().toString().isEmpty() || edNew.getText().toString().isEmpty() || edConfirm.getText().toString().isEmpty()){
            Toast.makeText(this, "Field Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        }else {
            if (!edConfirm.getText().toString().equals(edNew.getText().toString())) {
                Toast.makeText(PasswordChange.this, "Password tidak sama!", Toast.LENGTH_SHORT).show();
            } else {

                Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).
                        addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();
                ApiPegawai apiClient = retrofit.create(ApiPegawai.class);

                //CALL API dari PHP
                Call<String> usersDAOCall = apiClient.passChange(KPid, username, edOld.getText().toString(), edNew.getText().toString());
                usersDAOCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        try {

                            Status = response.code();
                            if (Status == 404) {
                                Toast.makeText(PasswordChange.this, "Not Found " + Status, Toast.LENGTH_SHORT).show();
                                Log.d("404", "onResponse: " + username + " " + edOld.getText().toString());
                                Log.d("404", "onResponse: " + response.message() + "\nBody: " + response.body());
                            } else if (Status == 200) {
                                Toast.makeText(PasswordChange.this, "Success " + Status, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PasswordChange.this, "Failed " + Status, Toast.LENGTH_SHORT).show();
                            }
                            intent = new Intent(PasswordChange.this, MainActivity.class);
                            startActivity(intent);
                        }catch (Exception e){
                            Log.d("res", "onResponse: "+e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(PasswordChange.this, Status, Toast.LENGTH_SHORT).show();
                        intent = new Intent(PasswordChange.this, MainActivity.class);
                        startActivity(intent);
                        Log.d("error", t.getMessage(), t);
                    }
                });
            }
        }
    }

    private void setAtribut() {
        edOld = (EditText) findViewById(R.id.changeOld);
        edNew = (EditText) findViewById(R.id.changeNew);
        edConfirm = (EditText) findViewById(R.id.changeConfirm);
    }
}

