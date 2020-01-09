package com.zhuo.dean.siksa_mobile.Start.Supplier;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.Model.Supplier;

import java.util.ArrayList;
import java.util.List;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.MyViewHolder> {
    private Context context;
    private List<Supplier> supplierList;

    public SupplierAdapter(Context context, List<Supplier> supplierList) {
        this.context = context;
        this.supplierList = supplierList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.supplier_list_item, viewGroup, false);
        final MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        try {
            final Supplier supplier = supplierList.get(i);
            myViewHolder.suTvNSupplier.setText("Nama Supplier: "+supplier.getNAMASUPPLIER());
            myViewHolder.suTvAlamat.setText("Alamat Supplier: "+supplier.getALAMATSUPPLIER());
            myViewHolder.suTvNSales.setText("Nama Sales: "+supplier.getNAMASALES());
            myViewHolder.suTvNTSales.setText("No Telp Sales: "+supplier.getNOTELPSALES());
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SupplierDetail.class);
                    intent.putExtra("id", supplier.getKODESUPPLIER());
                    context.startActivity(intent);
                }
            });
        } catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return supplierList.size();
    }

    public void filterList(ArrayList<Supplier> filter) {
        supplierList = filter;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView suTvNSupplier, suTvAlamat, suTvNSales, suTvNTSales;

        public MyViewHolder(View itemView) {
            super(itemView);
            suTvNSupplier = itemView.findViewById(R.id.suShowNamaSupplier);
            suTvAlamat = itemView.findViewById(R.id.suShowAlamat);
            suTvNSales = itemView.findViewById(R.id.suShowNamaSales);
            suTvNTSales = itemView.findViewById(R.id.suShowNoTelpSales);
        }
    }
}
