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
import com.zhuo.dean.siksa_mobile.Start.Model.Sparepart;

import java.util.ArrayList;
import java.util.List;

public class sparepartAdapter extends ArrayAdapter<Sparepart> {
    Context context;
    int resourceId;
    List<Sparepart> sparepartList, tempSparepart, suggestList;

    public sparepartAdapter(Context context, int resourceId, List<Sparepart> sparepartList){
        super(context, resourceId, sparepartList);
        this.context = context;
        this.resourceId = resourceId;
        this.sparepartList = sparepartList;
        tempSparepart = new ArrayList<>(sparepartList);
        suggestList = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);
        }
        Sparepart sparepart = sparepartList.get(position);
        if (sparepart != null){
            TextView labelName = (TextView) view.findViewById(R.id.sparepartLabel);
            labelName.setText(sparepart.getnAMASPAREPART());
        }
        return view;
    }

    @Nullable
    @Override
    public Sparepart getItem(int position) {
        return sparepartList.get(position);
    }

    @Override
    public int getCount() {
        return sparepartList.size();
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
            String str = ((Sparepart) resultValue).getnAMASPAREPART();
            return str;
        }

        protected FilterResults performFiltering(CharSequence constraint){
            if (constraint != null){
                suggestList.clear();
                for (Sparepart sparepart : tempSparepart){
                    if (sparepart.getnAMASPAREPART().toLowerCase().contains(constraint.toString().toLowerCase())){
                        suggestList.add(sparepart);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestList;
                filterResults.count = suggestList.size();
                return filterResults;
            } else return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Sparepart> filterList = (ArrayList<Sparepart>) results.values;
            if (results != null && results.count > 0){
                clear();
                for (Sparepart sparepart : filterList){
                    add(sparepart);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
