package com.zhuo.dean.siksa_mobile.Start.Transactional;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiCabang;
import com.zhuo.dean.siksa_mobile.Start.API.ApiCustomer;
import com.zhuo.dean.siksa_mobile.Start.API.ApiPegawai;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Model.Cabang;
import com.zhuo.dean.siksa_mobile.Start.Model.Kendaraan;
import com.zhuo.dean.siksa_mobile.Start.Model.PegawaiDAO;
import com.zhuo.dean.siksa_mobile.Start.Model.Transaksi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransaksiTampilAdapter extends RecyclerView.Adapter<TransaksiTampilAdapter.MyViewHolder> {
    private Context context;
    private List<Transaksi> transaksiList;
    private int customer;
    private String namaCus, namaKendaraan, namaCabang, namaCS, namaKasir;
    Intent intent;

    public TransaksiTampilAdapter(Context context, List<Transaksi> transaksiList, int kodeCust, String namaCus) {
        this.context = context;
        this.transaksiList = transaksiList;
        this.customer = kodeCust;
        this.namaCus = namaCus;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaksi_list_item, viewGroup, false);
        final MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiTampilAdapter.MyViewHolder myViewHolder, final int i) {
        try {
            intent = new Intent(context,TransaksiDetail.class);
            final Transaksi transaksi = transaksiList.get(i);
            myViewHolder.trTvPenjualan.setText("Kode Penjualan: "+transaksi.getKODEPENJUALAN());
            myViewHolder.trTvTanggal.setText("Tanggal: "+transaksi.getTANGGALTRANSAKSI());

            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();

            ApiCustomer apiCustomer = retrofit.create(ApiCustomer.class);
            Call<Kendaraan> kendaraanCall = apiCustomer.getKendaraanById(transaksi.getKODEKENDARAAN());
            kendaraanCall.enqueue(new Callback<Kendaraan>() {
                @Override
                public void onResponse(Call<Kendaraan> call, Response<Kendaraan> response) {
                    if (response.body()!=null){
                        namaKendaraan = response.body().getPLATKENDARAAN();
                        intent.putExtra("kendaraan",namaKendaraan);
                        Log.d("kendaraan", "onResponse: " +namaKendaraan);
                    }else{
                        namaKendaraan = "AB000XY";
                        Log.d("kendaraan", "onResponse: " +response.code());
                    }
                }

                @Override
                public void onFailure(Call<Kendaraan> call, Throwable t) {
                    Log.d("kendaraan", "onFailure: "+t.getMessage());
                }
            });

            ApiCabang apiCabang = retrofit.create(ApiCabang.class);
            Call<Cabang> cabangCall = apiCabang.getCabangById(transaksi.getKODECABANG());
            cabangCall.enqueue(new Callback<Cabang>() {
                @Override
                public void onResponse(Call<Cabang> call, Response<Cabang> response) {
                    if (response.code() == 200){
                        namaCabang = response.body().getNAMACABANG();
                        intent.putExtra("cabang",namaCabang);
                        Log.d("cabang", "onResponse: " +namaCabang);
                    }else{
                        namaCabang = "Babarsari 2";
                        Log.d("cabang", "onResponse: " +response.code());
                    }
                }

                @Override
                public void onFailure(Call<Cabang> call, Throwable t) {
                    Log.d("cabang", "onFailure: "+t.getMessage());
                }
            });

            ApiPegawai apiPegawai = retrofit.create(ApiPegawai.class);
            Call<PegawaiDAO> pegawaiDAOCall = apiPegawai.getPegawaiById(transaksi.getCS());
            pegawaiDAOCall.enqueue(new Callback<PegawaiDAO>() {
                @Override
                public void onResponse(Call<PegawaiDAO> call, Response<PegawaiDAO> response) {
                    if (response.code() == 200){
                        namaCS = response.body().getNAMAPEGAWAI();
                        intent.putExtra("cs",namaCS);
                        Log.d("cs", "onResponse: " +namaCS);
                    }else{
                        namaCS = "Customer Service";
                        Log.d("cs", "onResponse: " +response.code());
                    }
                }

                @Override
                public void onFailure(Call<PegawaiDAO> call, Throwable t) {
                    Log.d("cs", "onFailure: "+t.getMessage());
                }
            });

            if (transaksi.getKASIR()!=null) {
                Call<PegawaiDAO> pegawaiCall = apiPegawai.getPegawaiById(transaksi.getKASIR());
                pegawaiCall.enqueue(new Callback<PegawaiDAO>() {
                    @Override
                    public void onResponse(Call<PegawaiDAO> call, Response<PegawaiDAO> response) {
                        if (response.body() != null) {
                            namaKasir = response.body().getNAMAPEGAWAI();
                            intent.putExtra("kasir", namaKasir);
                            Log.d("kasir", "onResponse: " + namaKasir);
                        } else {
                            namaCS = "Cashier";
                            Log.d("kasir", "onResponse: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<PegawaiDAO> call, Throwable t) {
                        Log.d("kasir", "onFailure: " + t.getMessage());
                    }
                });
            }else{
                namaKasir = "Belum melakukan pembayaran";
                intent.putExtra("kasir", namaKasir);
            }

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.putExtra("id",transaksi.getKODETRANSAKSI());
                    intent.putExtra("customer",customer);
                    intent.putExtra("namaCus", namaCus);
                    context.startActivity(intent);
                }
            });
        }catch (Exception e){
            Log.d("Error bind transaksi", "onBindViewHolder: "+e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return transaksiList.size();
    }

    public void filterList(ArrayList<Transaksi> filter){
        transaksiList = filter;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView trTvPenjualan, trTvTanggal;

        public MyViewHolder(View view){
            super(view);
            trTvPenjualan = view.findViewById(R.id.TrShowPenjualan);
            trTvTanggal = view.findViewById(R.id.TrShowTanggal);
        }
    }
}
