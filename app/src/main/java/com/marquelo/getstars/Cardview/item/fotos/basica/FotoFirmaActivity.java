package com.marquelo.getstars.Cardview.item.fotos.basica;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.marquelo.getstars.R;
import com.marquelo.getstars.ui.ItemCarritoAdded;
import com.marquelo.getstars.working.Famoso;
import com.marquelo.getstars.working.ItemCarrito;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FotoFirmaActivity extends AppCompatActivity {
    private Famoso famoso;
    private ImageView img;
    private ImageButton btnCarrito;
    private Uri uri;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
    ItemCarrito articulos = new ItemCarrito();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_firma);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Foto Firma");

        iniciarVistas();
        iniciarValores();
        goCarrito();
    }

    private void goCarrito() {
        btnCarrito.setOnClickListener(v -> {
            addCarrito();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void iniciarVistas(){
        famoso = Objects.requireNonNull(getIntent().getExtras()).getParcelable("famoso");
        img = findViewById(R.id.imgFotoFirma);
        btnCarrito = findViewById(R.id.btnOtraFirma);

        articulos.setCreador(famoso.getName());
    }

    private void iniciarValores() {
        if(famoso != null){
            Glide.with(this).load(famoso.getImg()).into(img);
            uri = famoso.getImg();
        }
    }

    private void addCarrito(){
        Map<String,Object> mapa = new HashMap<>();

        articulos.setNombre("Foto firma");
        articulos.setPrecio(2.99);
        articulos.setImagen(uri.toString());
        articulos.setKey(famoso.getKey());

        mapa.put("nombre",articulos.getNombre());
        mapa.put("precio",articulos.getPrecio());
        mapa.put("img",articulos.getImagen());
        mapa.put("creador",articulos.getCreador());
        mapa.put("key",articulos.getKey());


        // Add a new document with a generated ID
        db.collection("usuarios")
                .document(Objects.requireNonNull(usuario.getEmail()))
                .collection("Carrito").add(mapa).addOnSuccessListener(documentReference ->
                startActivity(new Intent(getApplicationContext(), ItemCarritoAdded.class)));
    }
}