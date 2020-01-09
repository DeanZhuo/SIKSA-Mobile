package com.zhuo.dean.siksa_mobile.Start.Transactional;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.Model.Kendaraan;

import java.util.ArrayList;
import java.util.List;

public class kendaraanAdapter extends ArrayAdapter<Kendaraan> {
    private Context context;
    private int textViewResourceId;
    List<Kendaraan> kendaraanList, tempKendaraan, suggestionKendaraan;

    public kendaraanAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Kendaraan> kendaraanList) {
        super(context, textViewResourceId, kendaraanList);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.kendaraanList = kendaraanList;
        tempKendaraan = new ArrayList<>(kendaraanList);
        suggestionKendaraan = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(textViewResourceId, parent, false);
        }
        Kendaraan kendaraan = kendaraanList.get(position);
        if (kendaraan != null){
            TextView labelPlat = (TextView) view.findViewById(R.id.kendaraanLabel);
            labelPlat.setText(kendaraan.getPLATKENDARAAN());
        }
        return view;
    }

    @Nullable
    @Override
    public Kendaraan getItem(int position) {
        return kendaraanList.get(position);
    }

    @Override
    public int getCount() {
        return kendaraanList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return platFilter;
    }

    Filter platFilter = new Filter() {
        public CharSequence convertResultToString(Object resultValue){
            String str = ((Kendaraan) resultValue).getPLATKENDARAAN();
            return str;
        }

        protected FilterResults performFiltering(CharSequence constraint){
            if (constraint != null){
                suggestionKendaraan.clear();
                for (Kendaraan kendaraan : tempKendaraan){
                    if (kendaraan.getPLATKENDARAAN().toLowerCase().contains(constraint.toString().toLowerCase())){
                        suggestionKendaraan.add(kendaraan);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestionKendaraan;
                filterResults.count = suggestionKendaraan.size();
                return filterResults;
            } else return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Kendaraan> filterList = (ArrayList<Kendaraan>) results.values;
            if (results != null && results.count > 0){
                clear();
                for (Kendaraan kendaraan : filterList){
                    add(kendaraan);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
