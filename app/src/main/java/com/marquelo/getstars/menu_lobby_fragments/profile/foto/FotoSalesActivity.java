package com.marquelo.getstars.menu_lobby_fragments.profile.foto;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.marquelo.getstars.R;
import com.marquelo.getstars.working.ItemCarrito;
import com.marquelo.getstars.working.adapter.AdapterPhotoSale;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class FotoSalesActivity extends AppCompatActivity {
    //------------ ATRIBUTOS FIREBASE-----------//
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //------------- ATRIBUTOS PROPIOS -----------//
    ArrayList<ItemCarrito> lista;
    RecyclerView recyclerFotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_foto_compras);
        setTitle("Fotos");

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        iniciarElementos();

        llenarFotos();
    }



    private void llenarFotos() {
        db.collection("usuarios").document(Objects.requireNonNull(user.getEmail())).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> compras = (Map<String, Object>) documentSnapshot.get("compras");

                ArrayList<String> fotoFirma = (ArrayList<String>) compras.get("autFot");
                ArrayList<String> foto = (ArrayList<String>) compras.get("fot");
                ArrayList<String> fotoDedicada = (ArrayList<String>) compras.get("fotDed");

                assert fotoFirma != null;
                for(String i : fotoFirma){
                    ItemCarrito itemCarrito = new ItemCarrito();
                    itemCarrito.setImagen(i);
                    lista.add(itemCarrito);
                    AdapterPhotoSale adapter = new AdapterPhotoSale(lista);
                    recyclerFotos.setAdapter(adapter);
                }
                assert foto != null;
                for(String i : foto){
                    ItemCarrito itemCarrito = new ItemCarrito();
                    itemCarrito.setImagen(i);
                    lista.add(itemCarrito);
                    AdapterPhotoSale adapter = new AdapterPhotoSale(lista);
                    recyclerFotos.setAdapter(adapter);
                }
                assert fotoDedicada != null;
                for(String i : fotoDedicada){
                    ItemCarrito itemCarrito = new ItemCarrito();
                    itemCarrito.setImagen(i);
                    lista.add(itemCarrito);
                    AdapterPhotoSale adapter = new AdapterPhotoSale(lista);
                    recyclerFotos.setAdapter(adapter);
                }


            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void iniciarElementos(){
        lista = new ArrayList<>();
        recyclerFotos = findViewById(R.id.recyclerFotoCompras);
        recyclerFotos.setLayoutManager(new GridLayoutManager(this.getApplicationContext(),4));
    }
}