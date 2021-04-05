package com.example.sangdewa.simpankepetugas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sangdewa.R;
import com.example.sangdewa.simpankepetugas.item.ItemKecamatan;

import java.util.ArrayList;

public class AdapterKecamatan extends ArrayAdapter<ItemKecamatan> {
    // View lookup cache
    private static class ViewHolder {
        TextView id_kec;
        TextView nama_kec;
    }

    public AdapterKecamatan(Context context, ArrayList<ItemKecamatan> users) {
        super(context, R.layout.item_kec, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ItemKecamatan kec = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_kec, parent, false);
            viewHolder.id_kec = (TextView) convertView.findViewById(R.id.tvId);
            viewHolder.nama_kec = (TextView) convertView.findViewById(R.id.tvNama);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.id_kec.setText(kec.id_kec);
        viewHolder.nama_kec.setText(kec.nama_kec);
        // Return the completed view to render on screen
        return convertView;
    }
}