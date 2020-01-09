package com.zhuo.dean.siksa_mobile.Start.Transactional;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiDetTrans;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Model.DetailJasa;
import com.zhuo.dean.siksa_mobile.Start.Model.Service;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailJasaAdapter extends RecyclerView.Adapter<DetailJasaAdapter.ViewHolder>{
    private Context context;
    private List<DetailJasa> detailJasaList;
    private String namaService;
    private int hargaService;

    public DetailJasaAdapter(Context context, List<DetailJasa> detailJasaList) {
        this.context = context;
        this.detailJasaList = detailJasaList;
    }

    @NonNull
    @Override
    public DetailJasaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaksi_service_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailJasaAdapter.ViewHolder viewHolder, int i) {
        try {
            final DetailJasa detailJasa = detailJasaList.get(i);

            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();

            ApiDetTrans apiDetTrans = retrofit.create(ApiDetTrans.class);
            Call<Service> callService = apiDetTrans.getJasaById(detailJasa.getKODESERVICE());
            callService.enqueue(new Callback<Service>() {
                @Override
                public void onResponse(Call<Service> call, Response<Service> response) {
                    if (response.body()!=null){
                        namaService = response.body().getNAMASERVICE();
                        hargaService = response.body().getHARGASERVICE();
                        viewHolder.nama.setText("Service "+namaService);
                        viewHolder.harga.setText("Rp "+String.valueOf(hargaService));
                    }else{
                        namaService = "Ringan";
                        hargaService = 0;
                        Log.d("fail", "onResponse: call service response: "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<Service> call, Throwable t) {
                    Log.d("failure", "onResponse: call service response: "+t.getMessage());
                }
            });

        } catch (Exception e){
            Log.d("error bind jasa", "onBindViewHolder: "+e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if (detailJasaList != null)
            return detailJasaList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, harga;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.TrSvJenis);
            harga = itemView.findViewById(R.id.TrSvHarga);
        }
    }
}
