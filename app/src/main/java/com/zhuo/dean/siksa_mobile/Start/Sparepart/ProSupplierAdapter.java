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
import com.zhuo.dean.siksa_mobile.Start.Model.Supplier;

import java.util.ArrayList;
import java.util.List;

public class ProSupplierAdapter  extends RecyclerView.Adapter<ProSupplierAdapter.ViewHolder> {
    private Context context;
    private List<Supplier> supplierList;

    public ProSupplierAdapter(Context context, List<Supplier> supplierList) {
        this.context = context;
        this.supplierList = supplierList;
    }


    @NonNull
    @Override
    public ProSupplierAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.supplier_list_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProSupplierAdapter.ViewHolder viewHolder, int i) {
        try {
            final Supplier supplier = supplierList.get(i);
            viewHolder.suTvNSupplier.setText("Nama Supplier: "+supplier.getNAMASUPPLIER());
            viewHolder.suTvAlamat.setText("Alamat Supplier: "+supplier.getALAMATSUPPLIER());
            viewHolder.suTvNSales.setText("Nama Sales: "+supplier.getNAMASALES());
            viewHolder.suTvNTSales.setText("No Telp Sales: "+supplier.getNOTELPSALES());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProcurementList.class);
                    intent.putExtra("supplier", supplier.getKODESUPPLIER());
                    intent.putExtra("namaSupplier", supplier.getNAMASUPPLIER());
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView suTvNSupplier, suTvAlamat, suTvNSales, suTvNTSales;

        public ViewHolder(View itemView) {
            super(itemView);
            suTvNSupplier = itemView.findViewById(R.id.suShowNamaSupplier);
            suTvAlamat = itemView.findViewById(R.id.suShowAlamat);
            suTvNSales = itemView.findViewById(R.id.suShowNamaSales);
            suTvNTSales = itemView.findViewById(R.id.suShowNoTelpSales);
        }
    }
}
