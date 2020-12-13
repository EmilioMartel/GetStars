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
import com.marquelo.getstars.menu_lobby_fragments.store.menu_store_fragments.subastas.SubastasDetalladasActivity;
import com.marquelo.getstars.working.Subastas;

import java.util.ArrayList;

public class AdapterSubastas extends RecyclerView.Adapter<AdapterSubastas.ViewHolderSubastas> {
    ArrayList<Subastas> listaSubasta;

    public AdapterSubastas(ArrayList<Subastas> listaSubasta){
        this.listaSubasta = listaSubasta;
    }


    @NonNull
    @Override
    public ViewHolderSubastas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_subastas,null,false);
        return new ViewHolderSubastas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSubastas holder, int position) {
        //Establecemos Foto
        Glide.with(holder.itemView.getContext()).load(listaSubasta.get(position).getImg()).into(holder.foto);
        //holder.foto.setImageResource(R.drawable.goku);
        //holder.foto.setScaleType(ImageView.ScaleType.FIT_CENTER);


        //Establecemos nombre
        holder.nombre.setText(listaSubasta.get(position).getNombre());
        holder.itemView.setOnClickListener(v -> {

            //Establecemos las características
            Subastas subastas = new Subastas();
            subastas.setNombre(listaSubasta.get(position).getNombre());
            subastas.setCreador(listaSubasta.get(position).getCreador());
            subastas.setInfo(listaSubasta.get(position).getInfo());
            subastas.setFechaFinal(listaSubasta.get(position).getFechaFinal());
            subastas.setImg(listaSubasta.get(position).getImg());
            subastas.setKey(listaSubasta.get(position).getKey());
            subastas.setIdSubasta(listaSubasta.get(position).getIdSubasta());
            subastas.setPrecio(listaSubasta.get(position).getPrecio());

            Intent intent  = new Intent(holder.itemView.getContext(), SubastasDetalladasActivity.class);
            intent.putExtra("subastas", subastas);

            holder.itemView.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return listaSubasta.size();
    }

    public static class ViewHolderSubastas extends RecyclerView.ViewHolder {
        public ImageView foto;
        public TextView nombre, descripcion,precio;

        public ViewHolderSubastas(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imgCardviewSubastas);
            nombre = itemView.findViewById(R.id.txtCardViewSubastas);
        }
    }
}
