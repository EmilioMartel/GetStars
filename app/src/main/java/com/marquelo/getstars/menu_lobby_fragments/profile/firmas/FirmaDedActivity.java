package com.marquelo.getstars.menu_lobby_fragments.profile.firmas;

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
import com.marquelo.getstars.working.adapter.AdapterFirmaDed;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class FirmaDedActivity extends AppCompatActivity {
    //------------ ATRIBUTOS FIREBASE-----------//
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //------------- ATRIBUTOS PROPIOS -----------//
    ArrayList<ItemCarrito> lista;
    RecyclerView recyclerFirmaDedicada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_firma_dedicada);

        setTitle("Autógrafos");

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        iniciarElementos();

        llenarFirmas();
    }

    private void llenarFirmas() {
        db.collection("usuarios").document(Objects.requireNonNull(user.getEmail())).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> compras = (Map<String, Object>) documentSnapshot.get("compras");

                ArrayList<String> firma = (ArrayList<String>) compras.get("aut");

                assert firma != null;
                for(String i : firma){
                    ItemCarrito itemCarrito = new ItemCarrito();
                    itemCarrito.setImagen(i);
                    lista.add(itemCarrito);
                    AdapterFirmaDed adapter = new AdapterFirmaDed(lista);
                    recyclerFirmaDedicada.setAdapter(adapter);
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
        recyclerFirmaDedicada = findViewById(R.id.recyclerFirmaDedicada);
        recyclerFirmaDedicada.setLayoutManager(new GridLayoutManager(this.getApplicationContext(),3));
    }
}