package com.marquelo.getstars.menu_lobby_fragments.store.menu_store_fragments.lives;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.marquelo.getstars.R;
import com.marquelo.getstars.ui.ItemCarritoAdded;
import com.marquelo.getstars.working.ItemCarrito;
import com.marquelo.getstars.working.Lives;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class LivesDetalladoActivity extends AppCompatActivity {
    private ImageView imageView;
    private Lives lives;
    private TextView nombre;
    private TextView descripcion;
    private TextView mensaje;
    private TextView precio;
    private Button btnParticipar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_lives_detallado);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        iniciarElementos();
        iniciarValores();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void iniciarElementos(){
        lives = requireNonNull(getIntent().getExtras()).getParcelable("lives");
        imageView = findViewById(R.id.imageViewLives);
        nombre = findViewById(R.id.txtNameLives);
        mensaje = findViewById(R.id.mensajeLives);
        descripcion = findViewById(R.id.txtDescripcionLives);
        btnParticipar = findViewById(R.id.btnReservarPlazaLives);
        precio = findViewById(R.id.precioLive);
    }

    private void iniciarValores(){
        if (lives != null){
            Glide.with(this).load(lives.getImg()).into(imageView);
            nombre.setText(lives.getNombre());
            setTitle(lives.getNombre());
            descripcion.setText(lives.getDescripcion());
        }

        db.collection("famosos").document(lives.getKey()).get().addOnSuccessListener(documentSnapshot -> {
            Map<String,Object> prod = (Map<String, Object>) documentSnapshot.get("prod");
            assert prod != null;
            double precioLive = (double) prod.get("live");
            precio.setText(String.valueOf(precioLive));
        });

        reservarPlaza();
    }


    private void reservarPlaza(){
        btnParticipar.setOnClickListener(v -> {

            ItemCarrito articulos = new ItemCarrito();
            articulos.setNombre("Live");
            articulos.setKey(lives.getIdLives());
            articulos.setObservaciones(mensaje.getText().toString());
            articulos.setCreador(lives.getCreador());
            articulos.setImagen(lives.getImg());
            articulos.setPrecio(Double.parseDouble(precio.getText().toString()));

            Map<String,Object> mapa = new HashMap<>();

            mapa.put("nombre",articulos.getNombre());
            mapa.put("creador",articulos.getCreador());
            mapa.put("observaciones",articulos.getObservaciones());
            mapa.put("precio",articulos.getPrecio());
            mapa.put("img",articulos.getImagen());
            mapa.put("key",articulos.getKey());



            // Add a new document with a generated ID
            db.collection("usuarios")
                    .document(Objects.requireNonNull(user.getEmail()))
                    .collection("Carrito").add(mapa)
                    .addOnSuccessListener(documentReference ->
                            startActivity(new Intent(getApplicationContext(), ItemCarritoAdded.class)));
        });
    }
}