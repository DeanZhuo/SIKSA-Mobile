package com.zhuo.dean.siksa_mobile.Start.Transactional;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSparepart;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Model.HistoryKeluar;
import com.zhuo.dean.siksa_mobile.Start.Model.Sparepart;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class barangKeluarAdapter extends RecyclerView.Adapter<barangKeluarAdapter.ViewHolder> {
    private Context context;
    private List<HistoryKeluar> historyKeluarList;
    private String namaSp;

    public barangKeluarAdapter(Context context, List<HistoryKeluar> historyKeluarList) {
        this.context = context;
        this.historyKeluarList = historyKeluarList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaksi_sparepart_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        try {
            final HistoryKeluar historyKeluar = historyKeluarList.get(i);

            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Helper.BASE_URL).addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();

            ApiSparepart apiSparepart = retrofit.create(ApiSparepart.class);
            Call<Sparepart> sparepartCall = apiSparepart.detailSparepart(historyKeluar.getKODESPAREPART());
            sparepartCall.enqueue(new Callback<Sparepart>() {
                @Override
                public void onResponse(Call<Sparepart> call, Response<Sparepart> response) {
                    if (response.code() == 200){
                        namaSp = response.body().getnAMASPAREPART();
                        viewHolder.tvnama.setText(namaSp);
                        Log.d("sparepart", "onResponse: " +response.body().getnAMASPAREPART());
                    }else{
                        namaSp = "Lamp";
                        viewHolder.tvnama.setText("Nama: "+namaSp);
                        Log.d("sparepart", "onResponse: " +response.code());
                    }
                }

                @Override
                public void onFailure(Call<Sparepart> call, Throwable t) {
                    Log.d("sparepart", "onFailure: "+t.getMessage());
                }
            });

            viewHolder.tvjumlah.setText(String.valueOf(historyKeluar.getSTOKBARANGKELUAR()));
            viewHolder.tvharga.setText("Rp "+historyKeluar.getHARGABARANGKELUAR());
        }catch (Exception e){
            Log.d("error bind keluar", "onBindViewHolder: "+e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if (historyKeluarList!=null)
            return historyKeluarList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvnama, tvjumlah, tvharga;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvnama = itemView.findViewById(R.id.TrSpNama);
            tvjumlah = itemView.findViewById(R.id.TrSpJumlah);
            tvharga = itemView.findViewById(R.id.TrSpHarga);
        }
    }
}
