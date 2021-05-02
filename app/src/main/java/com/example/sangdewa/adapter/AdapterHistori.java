/*
 * Copyright (c) Bakulapp.com 2020
 * Dev : Moh. Lukman Sholeh
 */

package com.example.sangdewa.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sangdewa.R;
import com.example.sangdewa.config.ModelData;
import com.example.sangdewa.detailtransaksi.DetailTransaksi;

import java.util.List;

public class AdapterHistori extends RecyclerView.Adapter<AdapterHistori.HolderData> {
    private List<ModelData> mItems;
    private Context context;

    public AdapterHistori(Context context, List<ModelData> items) {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public AdapterHistori.HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_histori, parent, false);
        AdapterHistori.HolderData holderData = new AdapterHistori.HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(AdapterHistori.HolderData holder, int position) {
        ModelData md = mItems.get(position);
        holder.nama.setText(md.getNama());
        if(md.getStatus().equalsIgnoreCase("0")){
            holder.status.setBackground(context.getResources().getDrawable(R.drawable.badge_warning));
            holder.status.setText("Sedang Diproses");
        }else{
            holder.status.setBackground(context.getResources().getDrawable(R.drawable.badge_success));
            holder.status.setText("Selesai");
        }
        holder.tanggal.setText(md.getTanggal());
        holder.telp.setText(md.getPhone());
        holder.md = md;


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    class HolderData extends RecyclerView.ViewHolder {
        TextView nama,tanggal,status,telp;
        ModelData md;

        CardView klik;


        public HolderData(View view) {
            super(view);

            nama=view.findViewById(R.id.nama);
            status=view.findViewById(R.id.status);
            telp=view.findViewById(R.id.telp);
            tanggal=view.findViewById(R.id.tanggal);
            klik= view.findViewById(R.id.klik);


            klik.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent update = new Intent(context, DetailTransaksi.class);
                    update.putExtra("update", 5);
                    update.putExtra("idtransaksi", md.getIdtransaksi());

                    context.startActivity(update);
                    ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


                }
            });

        }






    }
}
