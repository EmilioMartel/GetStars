package com.marquelo.getstars.working.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.marquelo.getstars.R;
import com.marquelo.getstars.working.ItemCarrito;

import java.util.ArrayList;

public class AdapterCarrito extends RecyclerView.Adapter<AdapterCarrito.ViewHolderCarrito> {
    ArrayList<ItemCarrito> listaCarrito;


    public AdapterCarrito(ArrayList<ItemCarrito> listaCarrito){
        this.listaCarrito = listaCarrito;
    }

    @NonNull
    @Override
    public AdapterCarrito.ViewHolderCarrito onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_carrito,null,false);
        return new ViewHolderCarrito(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCarrito.ViewHolderCarrito holder, int position) {
        //Establecemos Foto
        Glide.with(holder.itemView.getContext()).load(listaCarrito.get(position).getImagen()).into(holder.foto);
        //holder.foto.setImageResource(R.drawable.goku);
        //holder.foto.setScaleType(ImageView.ScaleType.FIT_CENTER);


        //Establecemos nombre
        holder.nombre.setText(listaCarrito.get(position).getNombre());

        //Establecemos creador
        holder.categoria.setText(listaCarrito.get(position).getMotivo());

        //Establecemos precio
        holder.precio.setText(listaCarrito.get(position).getPrecio().toString());

    }

    @Override
    public int getItemCount() {
        return listaCarrito.size();
    }

    public static class ViewHolderCarrito extends RecyclerView.ViewHolder {
        public ImageView foto;
        public TextView nombre, categoria,precio;
        public RelativeLayout viewF, viewB;

        public ViewHolderCarrito(@NonNull View itemView) {
            super(itemView);
             foto = itemView.findViewById(R.id.imgProducto);
             nombre = itemView.findViewById(R.id.nameItem);
             categoria = itemView.findViewById(R.id.autorItem);
             precio = itemView.findViewById(R.id.itemPrecio);
             viewF = itemView.findViewById(R.id.rl);
             viewB = itemView.findViewById(R.id.view_background);

        }
    }

    public void removeItem(int position){
        listaCarrito.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(ItemCarrito itemCarrito, int position){
        listaCarrito.add(position,itemCarrito);
        notifyItemInserted(position);
    }

}
