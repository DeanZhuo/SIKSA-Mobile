package com.zhuo.dean.siksa_mobile.Start.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiPegawai;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Model.PegawaiDAO;
import com.zhuo.dean.siksa_mobile.Start.Sparepart.SparepartProcurement;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    Button bLogin;
    EditText edUsername, edPassword;
    AlertDialog dialog;
    int Status;

    private NotificationManager notifManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        setAtribut();
        AlertDialog.Builder builder = Helper.loadDialog(LoginActivity.this);
        dialog = builder.create();


        bLogin = (Button) findViewById(R.id.logButton);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogin();
            }
        });
    }

    private void onClickLogin() {
        try {
            dialog.show();
            if (edUsername.getText().toString().isEmpty() || edPassword.getText().toString().isEmpty()) {
                Toast.makeText(this, "Field Cannot be Empty", Toast.LENGTH_SHORT).show();
            } else {
                Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();
                ApiPegawai apiClient = retrofit.create(ApiPegawai.class);

                //CALL API dari PHP
                Call<PegawaiDAO> usersDAOCall = apiClient.logUser(edUsername.getText().toString(),
                        edPassword.getText().toString());
                usersDAOCall.enqueue(new Callback<PegawaiDAO>() {
                    @Override
                    public void onResponse(Call<PegawaiDAO> call, Response<PegawaiDAO> response) {
                        try {
                            dialog.dismiss();
                            Status = response.code();
                            if (Status == 404) {
                                Toast.makeText(LoginActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                            } else if (Status == 200) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                PegawaiDAO pegawai = response.body();
                                Helper.loginSession(LoginActivity.this, pegawai);
                                Toast.makeText(LoginActivity.this, "Log " + Status, Toast.LENGTH_SHORT).show();
                                Helper.showUser(LoginActivity.this);
                                createNotification("Spareparts Getting Low", LoginActivity.this);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Failed " + Status + " try again", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Log.d("login", "onResponse: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<PegawaiDAO> call, Throwable t) {
                        dialog.dismiss();
                        if (t instanceof IOException) {
                            Toast.makeText(LoginActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                            // logging probably not necessary
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                            Log.d("error", t.getMessage(), t);
                            // todo log to some central bug tracking service
                        }
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Error", e.getMessage());
        }
    }

        public void createNotification(String aMessage, Context context) {
            final int NOTIFY_ID = 0; // ID of notification
            String id = context.getString(R.string.default_notification_channel_id); // default_channel_id
            String title = context.getString(R.string.default_notification_channel_title); // Default Channel
            Intent intent;
            PendingIntent pendingIntent;
            NotificationCompat.Builder builder;
            if (notifManager == null) {
                notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = notifManager.getNotificationChannel(id);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(id, title, importance);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    notifManager.createNotificationChannel(mChannel);
                }
                builder = new NotificationCompat.Builder(context, id);
                intent = new Intent(context, SparepartProcurement.class);
                intent.putExtra("min",20);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                builder.setContentTitle(aMessage)                            // required
                        .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                        .setContentText("Get Your Spareparts Now!") // required
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setTicker(aMessage)
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            }
            else {
                builder = new NotificationCompat.Builder(context, id);
                intent = new Intent(context, SparepartProcurement.class);
                intent.putExtra("min",20);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                builder.setContentTitle(aMessage)                            // required
                        .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                        .setContentText("Get Your Spareparts Now!") // required
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setTicker(aMessage)
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                        .setPriority(Notification.PRIORITY_HIGH);
            }
            Notification notification = builder.build();
            notifManager.notify(NOTIFY_ID, notification);
        }

    private void setAtribut() {
        edUsername = (EditText) findViewById(R.id.logUser);
        edUsername.setText("philPur");
        edPassword = (EditText) findViewById(R.id.logPass);
        edPassword.setText("test");
    }
}
