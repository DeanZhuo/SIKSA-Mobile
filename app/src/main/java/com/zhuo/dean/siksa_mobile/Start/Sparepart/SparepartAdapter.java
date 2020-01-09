package com.zhuo.dean.siksa_mobile.Start.Sparepart;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Model.Sparepart;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SparepartAdapter extends RecyclerView.Adapter<SparepartAdapter.MyViewHolder> {
    private Context context;
    private List<Sparepart> sparepartList;

    public SparepartAdapter(Context context, List<Sparepart> spareparts){
        this.context = context;
        this.sparepartList = spareparts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.sparepart_list_item, viewGroup, false);
        final MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        try {
            final Sparepart sparepart = sparepartList.get(i);
            myViewHolder.spTvBarang.setText("Kode Barang"+sparepart.getkODEBARANG());
            myViewHolder.spTvNama.setText("Nama Sparepart: "+sparepart.getnAMASPAREPART());
            myViewHolder.spTvStok.setText("Jumlah stok: "+sparepart.getjUMLAHSTOK());
            myViewHolder.spTvPeletakan.setText("Kode Peletakan: "+sparepart.getkODEPELETAKAN());
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,SparepartDetail.class);
                    intent.putExtra("id",sparepart.getkODESPAREPART());
                    context.startActivity(intent);
                }
            });

            class loadImage extends AsyncTask<Integer, Void, Integer> {
                Drawable drawable;

                @Override
                protected Integer doInBackground(Integer... integers) {
                    String link = Helper.BASE_URL_IMAGE + sparepart.getgAMBAR();
                    drawable = LoadImageFromApi(link);
                    return 0;
                }

                @Override
                protected void onPostExecute(Integer integer) {
                    super.onPostExecute(integer);
                    if (drawable != null){
                        myViewHolder.imgView.setImageDrawable(drawable);
                    } else {
                        myViewHolder.imgView.setImageResource(R.drawable.logo);
                    }
                }
            }
            new loadImage().execute();
        } catch (Exception e){
            Log.d("Error bind", e.getMessage());
        }
    }

    private Drawable LoadImageFromApi(String link) {
        try {
            InputStream inputStream = (InputStream) new URL(link).getContent();
            Drawable drawable = Drawable.createFromStream(inputStream, "src name");
            return drawable;
        } catch (Exception e) {
            System.out.println("Exec = "+e.getMessage());
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return sparepartList.size();
    }

    public void filterList(ArrayList<Sparepart> filter) {
        sparepartList = filter;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView spTvBarang, spTvNama, spTvStok, spTvPeletakan;
        public ImageView imgView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            spTvBarang = itemView.findViewById(R.id.spShowKodeBarang);
            spTvNama = itemView.findViewById(R.id.spShowNamaBarang);
            spTvStok = itemView.findViewById(R.id.spShowStok);
            spTvPeletakan = itemView.findViewById(R.id.spShowKodePeletakan);
            imgView = itemView.findViewById(R.id.spShowGambar);
        }
    }
}
