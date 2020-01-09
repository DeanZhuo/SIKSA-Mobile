package com.zhuo.dean.siksa_mobile.Start.Sparepart;

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
import com.zhuo.dean.siksa_mobile.Start.Model.DetailPengadaan;

import java.util.List;

public class ProcurementDetailAdapter extends RecyclerView.Adapter<ProcurementDetailAdapter.ViewHolder> {
    private Context context;
    private List<DetailPengadaan> detailPengadaanList;

    public ProcurementDetailAdapter(Context context, List<DetailPengadaan> detailPengadaanList) {
        this.context = context;
        this.detailPengadaanList = detailPengadaanList;
    }

    @NonNull
    @Override
    public ProcurementDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.prodet_list_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProcurementDetailAdapter.ViewHolder viewHolder, int i) {
        try {
            final DetailPengadaan pengadaan = detailPengadaanList.get(i);
            viewHolder.tvNama.setText(pengadaan.getNAMABARANG());
            viewHolder.tvJumlah.setText(String.valueOf(pengadaan.getJUMLAHPESANAN()));
            viewHolder.tvTotal.setText(": Rp "+pengadaan.getJUMLAHPESANAN()*pengadaan.getHARGASATUAN());
        } catch (Exception e){
            Log.d("pengadaan", "onBindViewHolder: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if (detailPengadaanList!=null)
            return detailPengadaanList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvNama, tvJumlah, tvTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.SpProDetNama);
            tvJumlah = itemView.findViewById(R.id.SpProDetJumlah);
            tvTotal = itemView.findViewById(R.id.SpProDetTotal);
        }
    }
}
