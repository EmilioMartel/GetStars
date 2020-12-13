package com.marquelo.getstars.working.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.marquelo.getstars.R;
import com.marquelo.getstars.ui.FirmaManualDetalladaActivity;
import com.marquelo.getstars.working.FirmaManual;

import java.util.ArrayList;

public class AdapterFirmaManual extends RecyclerView.Adapter<AdapterFirmaManual.ViewHolderFirmaManual> implements View.OnClickListener {
    //ATRIBUTOS
    ArrayList<FirmaManual> listaFirmaManual;
    private View.OnClickListener listener;

    public AdapterFirmaManual(ArrayList<FirmaManual> listaFirmaManual){
        this.listaFirmaManual = listaFirmaManual;
    }

    @NonNull
    @Override
    public ViewHolderFirmaManual onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_firma_manual,null,false);
        view.setOnClickListener(this);
        return new ViewHolderFirmaManual(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFirmaManual holder, int position) {
        System.out.println("ADAPTER");

        final FirmaManual item = listaFirmaManual.get(position);
        Glide.with(holder.itemView.getContext()).load(listaFirmaManual.get(position).getUrl()).into(holder.foto);

        holder.itemView.setOnClickListener(v -> {

            FirmaManual fm = new FirmaManual();
            fm.setUrl(item.getUrl());
            fm.setNombre("Firma Manual");

            Intent intent  = new Intent(holder.itemView.getContext(), FirmaManualDetalladaActivity.class);
            intent.putExtra("firma", fm);

            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaFirmaManual.size();
    }


    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public static class ViewHolderFirmaManual extends RecyclerView.ViewHolder {
        public ImageView foto;

        public ViewHolderFirmaManual(@NonNull View itemView) {
            super(itemView);
            foto = (ImageView) itemView.findViewById(R.id.idImagen);
        }
    }
}
