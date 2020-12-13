package com.marquelo.getstars.working.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.marquelo.getstars.R;
import com.marquelo.getstars.working.ItemCarrito;

import java.util.ArrayList;

public class AdapterFirmaDed extends RecyclerView.Adapter<AdapterFirmaDed.ViewHolderFirmaDedicada> {
    private ArrayList<ItemCarrito> lista;

    public AdapterFirmaDed(ArrayList<ItemCarrito> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolderFirmaDedicada onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_firma_manual,null,false);
        return new ViewHolderFirmaDedicada(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFirmaDedicada holder, int position) {
        Glide.with(holder.itemView.getContext()).load(lista.get(position).getImagen()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolderFirmaDedicada extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolderFirmaDedicada(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.idImagen);
        }
    }
}
