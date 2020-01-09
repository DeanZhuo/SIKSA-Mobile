package com.zhuo.dean.siksa_mobile.Start.API;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.Model.PegawaiDAO;


public class Helper {
//    public static final String BASE_URL = "http://192.168.19.140/9029/api/";
    public static final String BASE_URL = "http://192.168.1.16:8000/api/";
//    public static final String BASE_URL_IMAGE = "http://192.168.19.140/9029/Pictures/";
    public static final String BASE_URL_IMAGE = "http://192.168.1.16:8000/Pictures/";
    public static String LOGIN_PREFERENCE = "USER";

    public static void loginSession (Context context, PegawaiDAO pegawai){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id",pegawai.getKODEPEGAWAI());
        editor.putString("username",pegawai.getUSERNAME());
        editor.putInt("role",pegawai.getKODEROLE());
        editor.commit();
    }

    public static void showUser (Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_PREFERENCE, context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", 0);
        String username = sharedPreferences.getString("username","");
        int role = sharedPreferences.getInt("role",0);

        Log.d("share", "showUser: "+ id + " " + username + " " + role);
    }

    public static AlertDialog.Builder loadDialog (Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage("Loading... Please wait...");

        return builder;
    }
}
