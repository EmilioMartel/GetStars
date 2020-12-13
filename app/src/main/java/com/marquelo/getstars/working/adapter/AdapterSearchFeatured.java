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
import com.marquelo.getstars.Cardview.CardViewFamososDetallada;
import com.marquelo.getstars.R;
import com.marquelo.getstars.working.Famoso;

import java.util.ArrayList;

public class AdapterSearchFeatured extends RecyclerView.Adapter<AdapterSearchFeatured.ViewHolderFeatured> {
    private ArrayList<Famoso> listaFamoso;


    public AdapterSearchFeatured(ArrayList<Famoso> listaFamoso) { this.listaFamoso = listaFamoso;}

    @NonNull
    @Override
    public ViewHolderFeatured onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_famosos,null,false);
        return new ViewHolderFeatured(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFeatured holder, int position) {
        //Establecemos Foto
        Glide.with(holder.itemView.getContext()).load(listaFamoso.get(position).getImg()).into(holder.foto);

        //Establecemos nombre
        holder.nombre.setText(listaFamoso.get(position).getName());


        holder.itemView.setOnClickListener(v -> {
            //Navigation.findNavController(v).navigate(R.id.cvFamososDetallada);

            //Establecemos las características
            Famoso famoso = new Famoso();

            famoso.setImg(listaFamoso.get(position).getImg());
            famoso.setName(listaFamoso.get(position).getName());
            famoso.setCategory(listaFamoso.get(position).getCategory());
            famoso.setDescription(listaFamoso.get(position).getDescription());
            famoso.setKey(listaFamoso.get(position).getKey());

            Intent intent  = new Intent(holder.itemView.getContext(), CardViewFamososDetallada.class);
            intent.putExtra("famoso", famoso);

            holder.itemView.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return listaFamoso.size();
    }

    public class ViewHolderFeatured extends RecyclerView.ViewHolder {
        public ImageView foto;
        public TextView nombre;
        public TextView categoria;
        public TextView descripcion;
        public ViewHolderFeatured(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imgFamosoCV);
            nombre = itemView.findViewById(R.id.edNameFamosoCV);
            //categoria = itemView.findViewById(R.id.edCategoryFamosoCv);
            //descripcion = itemView.findViewById(R.id.edDescriptionFamosoCv);
        }
    }
}
