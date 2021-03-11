package com.example.sangdewa.listpetugas;

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
import com.example.sangdewa.config.Server;
import com.example.sangdewa.simpankepetugas.SimpanKePetugas;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPetugas extends RecyclerView.Adapter<AdapterPetugas.HolderData> {
    private List<ModelData> mItems;
    private Context context;

    public AdapterPetugas(Context context, List<ModelData> items) {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public AdapterPetugas.HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.listpetugas, parent, false);
        AdapterPetugas.HolderData holderData = new AdapterPetugas.HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(AdapterPetugas.HolderData holder, int position) {
        ModelData md = mItems.get(position);
        holder.nama.setText(md.getNama());
        holder.alamat.setText(md.getPhone());

        if (md.getFoto()==""||md.getFoto().equals("")){
            Picasso.with(context).load(R.drawable.profile01).into(holder.foto);
        }else{
            Picasso.with(context).load(Server.URL+"assets/foto/"+md.getFoto()).into(holder.foto);
        }


        holder.md = md;


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    class HolderData extends RecyclerView.ViewHolder {
        TextView nama,alamat;
        ModelData md;

        CardView klik;
        CircleImageView foto;


        public HolderData(View view) {
            super(view);

            nama=view.findViewById(R.id.nama);
            alamat=view.findViewById(R.id.alamat);
            foto=view.findViewById(R.id.foto);
            klik= view.findViewById(R.id.klik);


            klik.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent update = new Intent(context, SimpanKePetugas.class);
                    update.putExtra("update", 1);
                    update.putExtra("idpetugas", md.getIdpetugas());

                    context.startActivity(update);
                    ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


                }
            });

        }






    }
}
