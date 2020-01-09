package com.zhuo.dean.siksa_mobile.Start.Transactional;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiCabang;
import com.zhuo.dean.siksa_mobile.Start.API.ApiCustomer;
import com.zhuo.dean.siksa_mobile.Start.API.ApiDetTrans;
import com.zhuo.dean.siksa_mobile.Start.API.ApiPegawai;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSS;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSparepart;
import com.zhuo.dean.siksa_mobile.Start.API.ApiTransaksi;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Main.MainActivity;
import com.zhuo.dean.siksa_mobile.Start.Model.Cabang;
import com.zhuo.dean.siksa_mobile.Start.Model.DetailTransaksi;
import com.zhuo.dean.siksa_mobile.Start.Model.HistoryKeluar;
import com.zhuo.dean.siksa_mobile.Start.Model.Kendaraan;
import com.zhuo.dean.siksa_mobile.Start.Model.PegawaiDAO;
import com.zhuo.dean.siksa_mobile.Start.Model.Service;
import com.zhuo.dean.siksa_mobile.Start.Model.Sparepart;
import com.zhuo.dean.siksa_mobile.Start.Model.Transaksi;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransaksiTambah extends AppCompatActivity {

    EditText edTanggal, jumlahSparepart;
    AutoCompleteTextView edKendaraan, autoSparepart, autoService;
    Spinner edCs, edCabang;
    String tanggal, penjualan;
    int kKendaraan, kCs, kCabang, kCust, kStatus;
    int totalservice = 0, totalsparepart = 0;
    Button eSimpan, eSparepart, eService;
    private AlertDialog loadDialog;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    int kode, harga, kodeTr, kodeDetTr;
    String nama;

    private List<Kendaraan> kendaraanList = new ArrayList<>();
    private List<Cabang> cabangList = new ArrayList<>();
    private List<PegawaiDAO> csList = new ArrayList<>();
    private List<Service> serviceList = new ArrayList<>();
    private List<Sparepart> sparepartList = new ArrayList<>();
    private List<Service> kodeService = new ArrayList<>();
    private List<HistoryKeluar> kodeSparepart = new ArrayList<>();
    private kendaraanAdapter kendaraanArrayAdapter;
    private ArrayAdapter<Cabang> cabangArrayAdapter;
    private ArrayAdapter<PegawaiDAO> pegawaiDAOArrayAdapter;
    private serviceAdapter serviceArrayAdapter;
    private sparepartAdapter sparepartArrayAdapter;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_tambah);

        getSupportActionBar().setTitle("Add");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder dialogbuilder = Helper.loadDialog(TransaksiTambah.this);
        loadDialog = dialogbuilder.create();

        kCust = getIntent().getIntExtra("customer",1);

        edTanggal = findViewById(R.id.TrAddDate);
        String date_n = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        edTanggal.setText(date_n);

        edKendaraan = findViewById(R.id.TrAddKendaraan);

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ApiCustomer apiCustomer = retrofit.create(ApiCustomer.class);
        Call<List<Kendaraan>> kendaraanCall = apiCustomer.getKendaraan();

        kendaraanCall.enqueue(new Callback<List<Kendaraan>>() {
            @Override
            public void onResponse(Call<List<Kendaraan>> call, Response<List<Kendaraan>> response) {
                try {
                    if(response.body() != null){
                        Log.d("kendaraanCall", "onResponse: size:  "+response.body().size());
                        for(int i=0; i<response.body().size(); i++){
                            kendaraanList.add(response.body().get(i));
                        }
                    } else {
                        Log.d("kendaraanCall", "onResponse: "+response.code());
                    }

                    kendaraanArrayAdapter = new kendaraanAdapter(TransaksiTambah.this, R.layout.row_kendaran, kendaraanList);
                    edKendaraan.setThreshold(1);
                    edKendaraan.setAdapter(kendaraanArrayAdapter);
                    edKendaraan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Kendaraan kendaraan = (Kendaraan) parent.getItemAtPosition(position);
                            kKendaraan = kendaraan.getKODEKENDARAAN();
                            Log.d("aaKendaraan", "onItemClick: "+kKendaraan);
                        }
                    });
                } catch (Exception e) {
                    Log.d("Error kendaraan", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Kendaraan>> call, Throwable t) {
                Toast.makeText(TransaksiTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(TransaksiTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(TransaksiTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });

        edCabang = findViewById(R.id.TrAddCabang);

        ApiCabang apiCabang = retrofit.create(ApiCabang.class);
        Call<List<Cabang>> jsonObjectCall = apiCabang.getCabang();

        jsonObjectCall.enqueue(new Callback<List<Cabang>>() {
            @Override
            public void onResponse(Call<List<Cabang>> call, Response<List<Cabang>> response) {
                try {
                    if(response.body() != null){
                        Log.d("cabangCall", "onResponse: "+response.body().size());
                        for(int i=0; i<response.body().size(); i++){
                            cabangList.add(response.body().get(i));
                        }
                    } else  {
                        Log.d("cabangCall", "onResponse: "+response.code());
                    }

                    cabangArrayAdapter = new ArrayAdapter<Cabang>(TransaksiTambah.this, android.R.layout.simple_spinner_item, cabangList);
                    cabangArrayAdapter.notifyDataSetChanged();
                    cabangArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    edCabang.setAdapter(cabangArrayAdapter);
                    edCabang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cabang cabang = (Cabang) parent.getSelectedItem();
                            kCabang = cabang.getKODECABANG();
                            Log.d("aaCabang", "onItemSelected: "+kCabang);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            kCabang = 0;
                            Toast.makeText(TransaksiTambah.this, "Cabang not selected", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    Log.d("Error cabang", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Cabang>> call, Throwable t) {
                Toast.makeText(TransaksiTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(TransaksiTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(TransaksiTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });

        edCs = findViewById(R.id.TrAddCS);

        ApiPegawai apiPegawai = retrofit.create(ApiPegawai.class);
        Call<List<PegawaiDAO>> listCall = apiPegawai.getPegawai();

        listCall.enqueue(new Callback<List<PegawaiDAO>>() {
            @Override
            public void onResponse(Call<List<PegawaiDAO>> call, Response<List<PegawaiDAO>> response) {
                try {
                    if(response.body() != null){
                        Log.d("pegawaiCall", "onResponse: size: "+response.body().size());
                        for(int i=0; i<response.body().size(); i++){
                            csList.add(response.body().get(i));
                        }
                    } else {
                        Log.d("pegawaiCall", "onResponse: "+response.code());
                    }

                    pegawaiDAOArrayAdapter = new ArrayAdapter<PegawaiDAO>(TransaksiTambah.this, android.R.layout.simple_spinner_item, csList);
                    pegawaiDAOArrayAdapter.notifyDataSetChanged();
                    pegawaiDAOArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    edCs.setAdapter(pegawaiDAOArrayAdapter);
                    edCs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            PegawaiDAO pegawai = (PegawaiDAO) parent.getSelectedItem();
                            kCs = pegawai.getKODEPEGAWAI();
                            Log.d("aaCS", "onItemSelected: "+kCs);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            kCs = 0;
                            Toast.makeText(TransaksiTambah.this, "CS not selected", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    Log.d("Error pegawai", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<PegawaiDAO>> call, Throwable t) {
                Toast.makeText(TransaksiTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(TransaksiTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(TransaksiTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });

        tableLayout = findViewById(R.id.TrAddShowTable);

        eSparepart = (Button) findViewById(R.id.TrAddTambahSparepart);
        eSparepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSparepartDialog();
            }
        });

        eService = (Button) findViewById(R.id.TrAddTambahService);
        eService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeServiceDialog();
            }
        });

        eSimpan = (Button) findViewById(R.id.TrAddSave);
        eSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMaxInc();
            }
        });
    }

    private void setTable(List<HistoryKeluar> sparepartList, List<Service> serviceList){
        tableLayout.removeAllViewsInLayout();
        int idNo = 0;
        for (int i=0;i<sparepartList.size();i++){
            View tableRow = LayoutInflater.from(TransaksiTambah.this).inflate(R.layout.tr_add_item,null,false);
            TextView id = tableRow.findViewById(R.id.textId);
            idNo+=1;
            id.setText(String.valueOf(idNo));
            final TextView nama = tableRow.findViewById(R.id.textNama);
            TextView harga = tableRow.findViewById(R.id.textHarga);

            HistoryKeluar sparepart = sparepartList.get(i);

            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            ApiSparepart apiSparepart = retrofit.create(ApiSparepart.class);
            Call<Sparepart> sparepartCall = apiSparepart.detailSparepart(sparepart.getKODESPAREPART());
            sparepartCall.enqueue(new Callback<Sparepart>() {
                @Override
                public void onResponse(Call<Sparepart> call, Response<Sparepart> response) {
                    if (response.body()!=null){
                        nama.setText(response.body().getnAMASPAREPART());
                    } else {
                        nama.setText("Empty");
                        Log.d("barang", "onResponse: "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<Sparepart> call, Throwable t) {
                    if (t instanceof IOException) {
                        Toast.makeText(TransaksiTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(TransaksiTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });

            harga.setText(String.valueOf(sparepart.getHARGABARANGKELUAR()));
            tableLayout.addView(tableRow);
        }

        for (int i=0;i<serviceList.size();i++){
            View tableRow = LayoutInflater.from(TransaksiTambah.this).inflate(R.layout.tr_add_item,null,false);
            TextView id = tableRow.findViewById(R.id.textId);
            idNo+=1;
            id.setText(String.valueOf(idNo));
            TextView nama = tableRow.findViewById(R.id.textNama);
            TextView harga = tableRow.findViewById(R.id.textHarga);

            Service service = serviceList.get(i);
            nama.setText(service.getNAMASERVICE());
            harga.setText(String.valueOf(service.getHARGASERVICE()));
            tableLayout.addView(tableRow);
        }
    }

    private void makeSparepartDialog() {
        dialog = new AlertDialog.Builder(TransaksiTambah.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.layout_sparepart, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        autoSparepart = dialogView.findViewById(R.id.TrAddSparepart);
        final Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiSS apiSS = retrofit.create(ApiSS.class);
        Call<List<Sparepart>> sparepartCall = apiSS.getSparepart();
        sparepartCall.enqueue(new Callback<List<Sparepart>>() {
            @Override
            public void onResponse(Call<List<Sparepart>> call, Response<List<Sparepart>> response) {
                try {
                    if(response.body() != null){
                        Log.d("sparepartCall", "onResponse: size:  "+response.body().size());
                        for(int i=0; i<response.body().size(); i++){
                            sparepartList.add(response.body().get(i));
                        }
                    } else {
                        Log.d("sparepartCall", "onResponse: "+response.code());
                    }

                    sparepartArrayAdapter = new sparepartAdapter(TransaksiTambah.this, R.layout.row_sparepart, sparepartList);
                    autoSparepart.setThreshold(1);
                    autoSparepart.setAdapter(sparepartArrayAdapter);
                    autoSparepart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Sparepart sparepart = (Sparepart) parent.getItemAtPosition(position);
                            kode = sparepart.getkODESPAREPART();
                            harga = sparepart.gethARGAJUAL();
                            Log.d("sparepart", "onClick: "+kode+" "+harga);
                        }
                    });
                } catch (Exception e) {
                    Log.d("Error sparepart", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Sparepart>> call, Throwable t) {
                Toast.makeText(TransaksiTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(TransaksiTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(TransaksiTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        jumlahSparepart = dialogView.findViewById(R.id.TrAddJumlah);
        dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int jumlah = Integer.parseInt(jumlahSparepart.getText().toString());
                kodeSparepart.add(new HistoryKeluar(kode, jumlah,harga*jumlah));
                Log.d("sparepart", "onClick: "+kode+" "+harga+" "+jumlah+" get");
                totalsparepart += harga*jumlah;
                setTable(kodeSparepart, kodeService);
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void makeServiceDialog() {
        dialog = new AlertDialog.Builder(TransaksiTambah.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.layout_service, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        autoService = dialogView.findViewById(R.id.TrAddService);
        final Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiSS apiSS = retrofit.create(ApiSS.class);
        Call<List<Service>> sparepartCall = apiSS.getService();
        sparepartCall.enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                try {
                    if(response.body() != null){
                        Log.d("serviceCall", "onResponse: size:  "+response.body().size());
                        for(int i=0; i<response.body().size(); i++){
                            serviceList.add(response.body().get(i));
                        }
                    } else {
                        Log.d("serviceCall", "onResponse: "+response.code());
                    }

                    serviceArrayAdapter = new serviceAdapter(TransaksiTambah.this, R.layout.row_service, serviceList);
                    autoService.setThreshold(1);
                    autoService.setAdapter(serviceArrayAdapter);
                    autoService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Service service = (Service) parent.getItemAtPosition(position);
                            kode = service.getKODEJASA();
                            nama = service.getNAMASERVICE();
                            harga = service.getHARGASERVICE();
                            Log.d("service", "onClick: "+kode+" "+harga);
                        }
                    });
                } catch (Exception e) {
                    Log.d("Error service", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                Toast.makeText(TransaksiTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                if (t instanceof IOException) {
                    Toast.makeText(TransaksiTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(TransaksiTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
        dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                kodeService.add(new Service(kode, nama, harga));
                Log.d("service", "onClick: "+kode+" "+harga+" get");
                totalservice += harga;
                setTable(kodeSparepart, kodeService);
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void onClickSave(String inc) {
        loadDialog.show();
        if (inc.isEmpty()){
            inc = "000";
        }
        tanggal = edTanggal.getText().toString();
        String date_n = new SimpleDateFormat("ddMMyy", Locale.getDefault()).format(new Date());
        if (sparepartList.size()==0 && serviceList.size()!=0){
            penjualan = "SP-"+date_n+"-"+inc;
        } else if (sparepartList.size()!=0 && serviceList.size()==0) {
            penjualan = "SV-"+date_n+"-"+inc;
        } else {
            penjualan = "SS-"+date_n+"-"+inc;
        }
        kStatus = 1;
        if (edTanggal.getText().toString().isEmpty() || kKendaraan == 0 || kCabang == 0 || kCs == 0){
            Log.d("ex", "onClickSave: "+tanggal+" "+kKendaraan+" "+kCabang+" "+kCs);
            Toast.makeText(this, "Field Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else {
            loadDialog.show();
            try {
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(Helper.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();
                ApiTransaksi apiTransaksi = retrofit.create(ApiTransaksi.class);

                Call<String> objectCall = apiTransaksi.postTransaksi(kCust, kStatus, kCabang, kKendaraan, kCs, tanggal, 0, penjualan, 0);
                objectCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        int Status = response.code();
                        if (Status == 500) {
                            Toast.makeText(TransaksiTambah.this, "Failed To Save", Toast.LENGTH_SHORT).show();
                            try {
                                Log.d("500", "onResponse: Transaksi" + response.message() + "\n" + response.body()+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (Status == 409) {
                            Toast.makeText(TransaksiTambah.this, "Transaksi Already Exist", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TransaksiTambah.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(TransaksiTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                        if (t instanceof IOException) {
                            Toast.makeText(TransaksiTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                            // logging probably not necessary
                        }
                        else {
                            Toast.makeText(TransaksiTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                            Log.d("error", t.getMessage(), t);
                            // todo log to some central bug tracking service
                        }
                    }
                });

                Call<String> transId = apiTransaksi.getMaxTransaksi();
                transId.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body()!=null){
                            kodeTr = Integer.parseInt(response.body());
                            Log.d("kodeTr", "onResponse: "+kodeTr);
                            saveDetail(kodeTr);
                        } else {
                            kodeTr = 0;
                            Log.d("kodeTr", "onResponse: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(TransaksiTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TransaksiTambah.this, MainActivity.class);
                        if (t instanceof IOException) {
                            Toast.makeText(TransaksiTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                            // logging probably not necessary
                        }
                        else {
                            Toast.makeText(TransaksiTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                            Log.d("error", t.getMessage(), t);
                            // todo log to some central bug tracking service
                        }
                    }
                });

            }catch (Exception e){
                Log.d("add", "onClickSave: " + e.getMessage());
            }
            loadDialog.dismiss();
            Intent intent = new Intent(TransaksiTambah.this, TransaksiByCustomer.class);
            startActivity(intent);
        }
    }

    private void saveDetail(int kodeTr){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);

        if (kodeSparepart.size()>0){
            Call<String> detSpCall = apiDetTrans.addDetTr(kodeTr, totalsparepart);
            detSpCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 500) {
                        Toast.makeText(TransaksiTambah.this, "Failed To Save detail", Toast.LENGTH_SHORT).show();
                        try {
                            Log.d("500", "onResponse: detTrSparepart " + response.message() + "\n" + response.body()+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == 409) {
                        Toast.makeText(TransaksiTambah.this, "detTr Already Exist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TransaksiTambah.this, "detTr Success", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(TransaksiTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    if (t instanceof IOException) {
                        Toast.makeText(TransaksiTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(TransaksiTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });

            Call<String> detTrId = apiDetTrans.getMaxDetTr();
            detTrId.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.body()!=null){
                        kodeDetTr = Integer.parseInt(response.body());
                        Log.d("kodeDetTr", "onResponse: "+response.body());
                        saveHistory(kodeDetTr);
                    } else {
                        kodeDetTr = 0;
                        Log.d("kodeDetTr", "onResponse: "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(TransaksiTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TransaksiTambah.this, MainActivity.class);
                    if (t instanceof IOException) {
                        Toast.makeText(TransaksiTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(TransaksiTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });
        }

        if (kodeService.size()>0){
            Call<String> detSpCall = apiDetTrans.addDetTr(kodeTr, totalservice);
            detSpCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 500) {
                        Toast.makeText(TransaksiTambah.this, "Failed To Save detail", Toast.LENGTH_SHORT).show();
                        try {
                            Log.d("500", "onResponse: detTrSparepart " + response.message() + "\n" + response.body()+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == 409) {
                        Toast.makeText(TransaksiTambah.this, "detTr Already Exist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TransaksiTambah.this, "detTr Success", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(TransaksiTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    if (t instanceof IOException) {
                        Toast.makeText(TransaksiTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(TransaksiTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });

            Call<String> detTrId = apiDetTrans.getMaxDetTr();
            detTrId.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.body()!=null){
                        kodeDetTr = Integer.parseInt(response.body());
                        Log.d("kodeDetTr", "onResponse: "+response.body());
                        saveJasa(kodeDetTr);
                    } else {
                        kodeDetTr = 0;
                        Log.d("kodeDetTr", "onResponse: "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(TransaksiTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TransaksiTambah.this, MainActivity.class);
                    if (t instanceof IOException) {
                        Toast.makeText(TransaksiTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(TransaksiTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });
        }
    }

    private void saveHistory(int kodeDetTr){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);

        for (int i=0; i<kodeSparepart.size();i++){
            HistoryKeluar historyKeluar = kodeSparepart.get(i);
            Call<String> historyKeluarCall =apiDetTrans.addHistoryKeluar(kodeDetTr, historyKeluar.getKODESPAREPART(), tanggal, historyKeluar.getSTOKBARANGKELUAR(), historyKeluar.getHARGABARANGKELUAR());
            historyKeluarCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 500) {
                        Toast.makeText(TransaksiTambah.this, "Failed To Save history", Toast.LENGTH_SHORT).show();
                        try {
                            Log.d("500", "onResponse: historyKeluar " + response.message() + "\n" + response.body()+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == 409) {
                        Toast.makeText(TransaksiTambah.this, "historyKeluar Already Exist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TransaksiTambah.this, "historyKeluar Success", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(TransaksiTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    if (t instanceof IOException) {
                        Toast.makeText(TransaksiTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(TransaksiTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });
        }
    }

    private void saveJasa(int kodeDetTr){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);
        for (int i=0; i<kodeService.size();i++){
            Service service = kodeService.get(i);
            Call<String> detailJasaCall = apiDetTrans.addDetailJasa(kodeDetTr, service.getKODEJASA());
            detailJasaCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 500) {
                        Toast.makeText(TransaksiTambah.this, "Failed To Save history", Toast.LENGTH_SHORT).show();
                        try {
                            Log.d("500", "onResponse: detailJasa " + response.message() + "\n" + response.body()+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == 409) {
                        Toast.makeText(TransaksiTambah.this, "detailJasa Already Exist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TransaksiTambah.this, "detailJasa Success", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(TransaksiTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                    if (t instanceof IOException) {
                        Toast.makeText(TransaksiTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(TransaksiTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                        Log.d("error", t.getMessage(), t);
                        // todo log to some central bug tracking service
                    }
                }
            });
        }
    }

    private void getMaxInc(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ApiTransaksi apiTransaksi = retrofit.create(ApiTransaksi.class);
        Call<String> transId = apiTransaksi.getMaxTransaksi();
        transId.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body()!=null){
                    kodeTr = Integer.parseInt(response.body());
                    Log.d("kodeTr", "onResponse: "+kodeTr);
                    getInc(kodeTr);

                } else {
                    kodeTr = 0;
                    Log.d("kodeTr", "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(TransaksiTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TransaksiTambah.this, MainActivity.class);
                if (t instanceof IOException) {
                    Toast.makeText(TransaksiTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(TransaksiTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                    Log.d("error", t.getMessage(), t);
                    // todo log to some central bug tracking service
                }
            }
        });
    }

    private void getInc(int kodeTr) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        ApiTransaksi apiTransaksi = retrofit.create(ApiTransaksi.class);
        Call<Transaksi> objectCall = apiTransaksi.getTransaksiById(kodeTr);
        objectCall.enqueue(new Callback<Transaksi>() {
            @Override
            public void onResponse(Call<Transaksi> call, Response<Transaksi> response) {
                int status = response.code();
                if (status == 404) {
                    Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_SHORT).show();
                } else if (status == 500) {
                    Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                } else {
                    Transaksi transaksi = response.body();
                    String inc = transaksi.getKODEPENJUALAN().substring(10);
                     onClickSave(inc);
                }
            }

            @Override
            public void onFailure (Call < Transaksi > call, Throwable t){
                Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                Log.d("transaksi tambah", "onFailure: "+ t.getMessage());
            }
        });
    }
}
