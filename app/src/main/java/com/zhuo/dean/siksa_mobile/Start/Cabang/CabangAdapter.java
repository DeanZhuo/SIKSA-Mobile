package com.zhuo.dean.siksa_mobile.Start.Cabang;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.Model.Cabang;

import java.util.ArrayList;
import java.util.List;

public class CabangAdapter extends RecyclerView.Adapter<CabangAdapter.ViewHolder> {
    private Context context;
    private List<Cabang> cabangList;

    public CabangAdapter(Context context, List<Cabang> cabangList) {
        this.context = context;
        this.cabangList = cabangList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cabang_list_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Cabang cabang = cabangList.get(i);
         viewHolder.nama.setText("Cabang "+cabang.getNAMACABANG());
         viewHolder.alamat.setText(cabang.getALAMATCABANG());
         viewHolder.noTelp.setText("No Telp "+cabang.getNOTELPCABANG());
         viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(context, CabangDetail.class);
                 intent.putExtra("id",cabang.getKODECABANG());
                 context.startActivity(intent);
             }
         });
    }

    @Override
    public int getItemCount() {
        if (cabangList != null)
            return cabangList.size();
        else
            return 0;
    }

    public void filterList(ArrayList<Cabang> filter) {
        cabangList = filter;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, alamat, noTelp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.tvNama);
            alamat = itemView.findViewById(R.id.tvAlamat);
            noTelp = itemView.findViewById(R.id.tvNoTelp);
        }
    }
}
