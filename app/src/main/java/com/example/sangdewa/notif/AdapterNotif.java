/*
 * Copyright (c) Bakulapp.com 2020
 * Dev : Moh. Lukman Sholeh
 */

package com.example.sangdewa.notif;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.sangdewa.R;
import com.example.sangdewa.config.ModelData;

import java.util.List;

public class AdapterNotif extends RecyclerView.Adapter<AdapterNotif.HolderData> {
    private List<ModelData> mItems;
    private Context context;
    SharedPreferences iduser;
    String ambiliduser;
    String idstok;

    public AdapterNotif(Context context, List<ModelData> items) {
        this.mItems = items;
        this.context = context;

    }

    @Override
    public AdapterNotif.HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notif, parent, false);
        AdapterNotif.HolderData holderData = new AdapterNotif.HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(AdapterNotif.HolderData holder, int position) {
        ModelData md = mItems.get(position);
        holder.judul.setText(md.getJudul());
        holder.isi.setText(md.getIsi());
        holder.tanggal.setText(md.getTanggal());

        if (md.getStatusbaca()=="N"||md.getStatusbaca().equals("N")||md.getStatusbaca()==""||md.getStatusbaca().equals("")){
            holder.background.setBackgroundResource(R.color.bacabelum);
        }else{
            holder.background.setBackgroundResource(R.color.sudahbaca);
        }


        holder.md = md;


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class HolderData extends RecyclerView.ViewHolder {
        TextView judul,isi,tanggal;
        ModelData md;
        RelativeLayout background;
        public HolderData(View view) {
            super(view);
            judul =  view.findViewById(R.id.judul);
            isi =  view.findViewById(R.id.isi);
            tanggal =  view.findViewById(R.id.tgl);
            background=(RelativeLayout)view.findViewById(R.id.background);




        }

    }


}