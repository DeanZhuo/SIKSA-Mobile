package com.zhuo.dean.siksa_mobile.Start.Main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Cabang.CabangTampil;
import com.zhuo.dean.siksa_mobile.Start.Report.ReportKeluar;
import com.zhuo.dean.siksa_mobile.Start.Report.ReportMasuk;
import com.zhuo.dean.siksa_mobile.Start.Sparepart.SparepartProcurement;
import com.zhuo.dean.siksa_mobile.Start.Sparepart.SparepartTampil;
import com.zhuo.dean.siksa_mobile.Start.Supplier.SupplierTampil;
import com.zhuo.dean.siksa_mobile.Start.Transactional.TransaksiByCustomer;

public class MainActivity extends AppCompatActivity {

    Button transactional, history, procurement, cabang, sparepart, supplier, changePasswword, logout;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transactional = (Button) findViewById(R.id.mBtnTransactional);
        transactional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, TransaksiByCustomer.class);
                startActivity(intent);
            }
        });

        history = (Button) findViewById(R.id.mBtnHistory);
        final CharSequence[] items={"Barang Masuk","Barang Keluar"};
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("History Report")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0){
                                    Toast.makeText(MainActivity.this, "History Barang Masuk", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, ReportMasuk.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "History Barang Keluar", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, ReportKeluar.class);
                                    startActivity(intent);
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        procurement = (Button) findViewById(R.id.mBtnProcurement);
        procurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SparepartProcurement.class);
                startActivity(intent);
            }
        });

        cabang = (Button) findViewById(R.id.mBtnCabang);
        cabang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CabangTampil.class);
                startActivity(intent);
            }
        });

        sparepart = (Button) findViewById(R.id.mBtnSparepart);
        sparepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, SparepartTampil.class);
                startActivity(intent);
            }
        });

        supplier = (Button) findViewById(R.id.mBtnSupplier);
        supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, SupplierTampil.class);
                startActivity(intent);
            }
        });

        changePasswword = (Button) findViewById(R.id.mBtnPasswordChange);
        changePasswword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, PasswordChange.class);
                startActivity(intent);
            }
        });

        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Log Out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences sharedPreferences = getSharedPreferences(Helper.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                intent = new Intent(MainActivity.this, StartActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
