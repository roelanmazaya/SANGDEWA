/*
 * Copyright (c) Bakulapp.com 2020
 * Dev : Moh. Lukman Sholeh
 */

package com.example.sangdewa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangdewa.R;
import com.example.sangdewa.config.ModelData;

import java.util.List;

public class AdapterDetail extends RecyclerView.Adapter<AdapterDetail.HolderData> {
    private List<ModelData> mItems;
    private Context context;
    

    public AdapterDetail(Context context, List<ModelData> items) {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public AdapterDetail.HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail, parent, false);
        AdapterDetail.HolderData holderData = new AdapterDetail.HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(AdapterDetail.HolderData holder, int position) {
        ModelData md = mItems.get(position);
        holder.nama.setText(md.getNama());
        holder.harga.setText(md.getHarga());



        holder.md = md;


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class HolderData extends RecyclerView.ViewHolder {
        TextView id, nama, harga, diskon;
        ImageView gambar;
        Button bayar;
        ModelData md;
        View viewkomplain;
        TextView quantityTextView, beli;
        CardView cardview;

        LinearLayout tambah, kurang;

        public HolderData(View view) {
            super(view);
            gambar = (ImageView) view.findViewById(R.id.gambar);
            nama =  view.findViewById(R.id.nama);
            harga =  view.findViewById(R.id.harga);


      
        }

    }


}
