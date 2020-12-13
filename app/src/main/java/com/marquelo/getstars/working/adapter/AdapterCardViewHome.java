package com.marquelo.getstars.working.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.marquelo.getstars.R;
import com.marquelo.getstars.Cardview.CardViewFamososDetallada;
import com.marquelo.getstars.working.Famoso;

import java.util.ArrayList;

public class AdapterCardViewHome extends RecyclerView.Adapter<AdapterCardViewHome.ViewHolderHome> {
    //Atributos
    ArrayList<Famoso>listaFamosos;
    Context context;

    public AdapterCardViewHome(ArrayList<Famoso> listaFamosos, Context context){
        this.listaFamosos = listaFamosos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_famosos,null,false);

        return new ViewHolderHome(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderHome holder, int position) {
        //Establecemos Foto
        Glide.with(holder.itemView.getContext()).load(listaFamosos.get(position).getImg()).into(holder.foto);

        //Establecemos nombre
        holder.nombre.setText(listaFamosos.get(position).getName());

        holder.itemView.setOnClickListener(v -> {

            //Establecemos las características
            Famoso famoso = new Famoso();
            famoso.setImg(listaFamosos.get(position).getImg());
            famoso.setName(listaFamosos.get(position).getName());
            famoso.setCategory(listaFamosos.get(position).getCategory());
            famoso.setDescription(listaFamosos.get(position).getDescription());
            famoso.setKey(listaFamosos.get(position).getKey());
            System.out.println(famoso.getKey());

            Intent intent  = new Intent(holder.itemView.getContext(), CardViewFamososDetallada.class);
            intent.putExtra("famoso", famoso);

            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaFamosos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return listaFamosos.get(position).getViewType();
    }

    public static class ViewHolderHome extends RecyclerView.ViewHolder {
        public ImageView foto;
        public TextView nombre;

        public ViewHolderHome(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imgFamosoCV);
            nombre = itemView.findViewById(R.id.edNameFamosoCV);
           // categoria = itemView.findViewById(R.id.edCategoryFamosoCv);
           // descripcion = itemView.findViewById(R.id.edDescriptionFamosoCv);
        }
    }

    public static class ViewHolderAdMob extends RecyclerView.ViewHolder {
        public AdView mAdView;
        public ViewHolderAdMob(View view) {
            super(view);
            mAdView = (AdView) view.findViewById(R.id.adViewBannerItem);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            mAdView.loadAd(adRequest);
        }
    }
}
