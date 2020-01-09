package com.zhuo.dean.siksa_mobile.Start.Transactional;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.Model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder>{
    private Context context;
    private List<Customer> customerList;

    public CustomerAdapter(Context context, List<Customer> customerList) {
        this.context = context;
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaksi_customer_item, viewGroup, false);
        final MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        final Customer customer = customerList.get(i);
        myViewHolder.cusName.setText("Nama: "+customer.getnAMACUST());
        myViewHolder.cusTelp.setText("No Telp: "+customer.getnOTELPCUST());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransaksiTampil.class);
                intent.putExtra("customer",customer.getkODECUST());
                intent.putExtra("nameCus",customer.getnAMACUST());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public void filterList(ArrayList<Customer> filter){
        customerList = filter;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cusName, cusTelp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cusName = itemView.findViewById(R.id.CusName);
            cusTelp = itemView.findViewById(R.id.CusNoTelp);
        }
    }
}
