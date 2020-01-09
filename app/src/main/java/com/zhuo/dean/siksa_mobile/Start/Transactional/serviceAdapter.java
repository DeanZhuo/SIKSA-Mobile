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
import com.zhuo.dean.siksa_mobile.Start.Model.Service;

import java.util.ArrayList;
import java.util.List;

public class serviceAdapter extends ArrayAdapter<Service> {
    Context context;
    int resourceId;
    List<Service> serviceList, tempService, suggestList;

    public serviceAdapter(Context context, int resourceId, List<Service> serviceList){
        super(context, resourceId, serviceList);
        this.context = context;
        this.resourceId = resourceId;
        this.serviceList = serviceList;
        tempService = new ArrayList<>(serviceList);
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
        Service service = serviceList.get(position);
        if (service != null){
            TextView labelName = (TextView) view.findViewById(R.id.serviceLabel);
            labelName.setText(service.getNAMASERVICE());
        }
        return view;
    }

    @Nullable
    @Override
    public Service getItem(int position) {
        return serviceList.get(position);
    }

    @Override
    public int getCount() {
        return serviceList.size();
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
            String str = ((Service) resultValue).getNAMASERVICE();
            return str;
        }

        protected FilterResults performFiltering(CharSequence constraint){
            if (constraint != null){
                suggestList.clear();
                for (Service service : tempService){
                    if (service.getNAMASERVICE().toLowerCase().contains(constraint.toString().toLowerCase())){
                        suggestList.add(service);
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
            List<Service> filterList = (ArrayList<Service>) results.values;
            if (results != null && results.count > 0){
                clear();
                for (Service service : filterList){
                    add(service);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
