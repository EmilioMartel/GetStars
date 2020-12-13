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
import com.marquelo.getstars.menu_lobby_fragments.store.menu_store_fragments.sorteos.SorteosDetalladosActivity;
import com.marquelo.getstars.working.Sorteos;

import java.util.ArrayList;

public class AdapterSorteos extends RecyclerView.Adapter<AdapterSorteos.ViewHolderSorteos> {
    private ArrayList<Sorteos> listaSorteos;

    public AdapterSorteos(ArrayList<Sorteos> listaSorteos) {
        this.listaSorteos = listaSorteos;
    }

    @NonNull
    @Override
    public ViewHolderSorteos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_sorteos,null,false);
        return new AdapterSorteos.ViewHolderSorteos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSorteos holder, int position) {

        Glide.with(holder.itemView.getContext()).load(listaSorteos.get(position).getImg()).into(holder.foto);
        holder.nombre.setText(listaSorteos.get(position).getNombre());

        holder.itemView.setOnClickListener(v -> {

            //Establecemos las características
            Sorteos sorteos = new Sorteos();
            sorteos.setNombre(listaSorteos.get(position).getNombre());
            sorteos.setCreador(listaSorteos.get(position).getCreador());
            sorteos.setDescripcion(listaSorteos.get(position).getDescripcion());
            sorteos.setFechaFinal(listaSorteos.get(position).getFechaFinal());
            sorteos.setImg(listaSorteos.get(position).getImg());
            sorteos.setKey(listaSorteos.get(position).getKey());
            sorteos.setIdSorteo(listaSorteos.get(position).getIdSorteo());

            Intent intent = new Intent(holder.itemView.getContext(), SorteosDetalladosActivity.class);
            intent.putExtra("sorteos", sorteos);

            holder.itemView.getContext().startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return listaSorteos.size();
    }

    public static class ViewHolderSorteos extends RecyclerView.ViewHolder {
        public ImageView foto;
        public TextView nombre;
        public ViewHolderSorteos(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imgCardviewSorteos);
            nombre = itemView.findViewById(R.id.txtCardViewSorteos);
        }
    }
}
