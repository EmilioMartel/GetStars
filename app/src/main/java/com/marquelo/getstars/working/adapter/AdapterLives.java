package com.marquelo.getstars.working.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.marquelo.getstars.R;
import com.marquelo.getstars.menu_lobby_fragments.store.menu_store_fragments.lives.LivesDetalladoActivity;
import com.marquelo.getstars.working.Lives;

import java.util.ArrayList;

public class AdapterLives extends RecyclerView.Adapter<AdapterLives.ViewHolderLives> {
    private ArrayList<Lives> listaLives;


    public AdapterLives(ArrayList<Lives> listaLives){
        this.listaLives = listaLives;
    }


    @NonNull
    @Override
    public ViewHolderLives onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_lives,null,false);
        return new ViewHolderLives(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderLives holder, int position) {
        Glide.with(holder.itemView.getContext()).load(listaLives.get(position).getImg()).into(holder.foto);
        holder.nombre.setText(listaLives.get(position).getNombre());
        holder.capacidadActual.setText(listaLives.get(position).getCapacidadActual());
        holder.capacidadTotal.setText(listaLives.get(position).getCapacidadTotal());


        holder.itemView.setOnClickListener(v -> {

            //Establecemos las características
            Lives lives = new Lives();
            lives.setNombre(listaLives.get(position).getNombre());
            lives.setCreador(listaLives.get(position).getCreador());
            lives.setDescripcion(listaLives.get(position).getDescripcion());
            lives.setFechaFinal(listaLives.get(position).getFechaFinal());
            lives.setImg(listaLives.get(position).getImg());
            lives.setKey(listaLives.get(position).getKey());
            lives.setIdLives(listaLives.get(position).getIdLives());

            Intent intent = new Intent(holder.itemView.getContext(), LivesDetalladoActivity.class);
            intent.putExtra("lives", lives);

            holder.itemView.getContext().startActivity(intent);

        });
    }




    @Override
    public int getItemCount() {
        return listaLives.size();
    }

    public static class ViewHolderLives extends RecyclerView.ViewHolder {

        public ImageView foto;
        public TextView nombre,capacidadActual, capacidadTotal;
        public ViewHolderLives(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imgFamosoCV);
            nombre = itemView.findViewById(R.id.edNameFamosoCV);
            capacidadActual = itemView.findViewById(R.id.txtUserActuales);
            capacidadTotal = itemView.findViewById(R.id.capacidadTotal);

        }
    }
}
