package com.marquelo.getstars.working.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.marquelo.getstars.R;
import com.marquelo.getstars.ui.FotosCompradasActivity;
import com.marquelo.getstars.working.FirmaManual;
import com.marquelo.getstars.working.ItemCarrito;

import java.util.ArrayList;

public class AdapterPhotoSale extends RecyclerView.Adapter<AdapterPhotoSale.ViewHolderFotoCompras> {
    private ArrayList<ItemCarrito> lista;

    public AdapterPhotoSale(ArrayList<ItemCarrito> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolderFotoCompras onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_firma_manual,null,false);
        return new AdapterPhotoSale.ViewHolderFotoCompras(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFotoCompras holder, int position) {
        Glide.with(holder.itemView.getContext()).load(lista.get(position).getImagen()).into(holder.imageView);
        holder.itemView.setOnClickListener(v -> {

            FirmaManual fm = new FirmaManual();
            final ItemCarrito item = lista.get(position);
            fm.setUrl(Uri.parse(item.getImagen()));
            fm.setNombre("Fotos Compradas");
            fm.setId(item.getMotivo());

            Intent intent  = new Intent(holder.itemView.getContext(), FotosCompradasActivity.class);
            intent.putExtra("fotos", fm);



            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolderFotoCompras extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolderFotoCompras(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.idImagen);
        }
    }
}
