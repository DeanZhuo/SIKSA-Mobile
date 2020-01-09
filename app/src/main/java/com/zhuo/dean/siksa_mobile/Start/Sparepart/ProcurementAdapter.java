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
import com.zhuo.dean.siksa_mobile.Start.Model.Pengadaan;

import java.util.ArrayList;
import java.util.List;

public class ProcurementAdapter extends RecyclerView.Adapter<ProcurementAdapter.ViewHolder> {
    private Context context;
    private List<Pengadaan> pengadaanList;
    private int kodeSup;
    private String namaSup;

    public ProcurementAdapter(Context context, List<Pengadaan> pengadaanList, int kodeSup, String supplier) {
        this.context = context;
        this.pengadaanList = pengadaanList;
        this.kodeSup = kodeSup;
        this.namaSup = supplier;
    }

    @NonNull
    @Override
    public ProcurementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.procurement_list_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProcurementAdapter.ViewHolder viewHolder, int i) {
        try {
            final Pengadaan pengadaan = pengadaanList.get(i);
            viewHolder.tvNama.setText("Kode Pengadaan: "+pengadaan.getKODEPENGADAAN());
            viewHolder.tvTanggal.setText("Tanggal: "+pengadaan.getTANGGALPENGADAAN());
            viewHolder.tvPembayaran.setText("Pembayaran: Rp "+pengadaan.getPEMBAYARANPEMESANAN());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProcurementDetail.class);
                    intent.putExtra("supplier", kodeSup);
                    intent.putExtra("namaSupplier", namaSup);
                    intent.putExtra("idPda", pengadaan.getKODEPENGADAAN());
                    intent.putExtra("tanggal", pengadaan.getTANGGALPENGADAAN());
                    intent.putExtra("pembayaran", pengadaan.getPEMBAYARANPEMESANAN());
                    context.startActivity(intent);
                }
            });
        } catch (Exception e){
            Log.d("pengadaan", "onBindViewHolder: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if (pengadaanList!=null)
            return pengadaanList.size();
        else return 0;
    }

    public void filterList(ArrayList<Pengadaan> filter) {
        pengadaanList = filter;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvNama, tvTanggal, tvPembayaran;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.SpProShowNo);
            tvTanggal = itemView.findViewById(R.id.SpProShowTanggal);
            tvPembayaran = itemView.findViewById(R.id.SpProSHowPembayaran);
        }
    }
}
