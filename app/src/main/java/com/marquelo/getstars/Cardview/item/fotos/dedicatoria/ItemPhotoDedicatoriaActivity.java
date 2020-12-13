package com.marquelo.getstars.Cardview.item.fotos.dedicatoria;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.marquelo.getstars.R;
import com.marquelo.getstars.ui.ItemCarritoAdded;
import com.marquelo.getstars.working.Famoso;
import com.marquelo.getstars.working.ItemCarrito;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemPhotoDedicatoriaActivity extends AppCompatActivity {
    private Famoso famoso;
    private ImageView img;
    private RadioButton rbtnDedicatoria;
    private ImageButton carrito;
    private Uri uri;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_item_foto_motivo_dedicacion);

        setTitle("Foto con dedicatoria");

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        iniciarVistas();
        iniciarValores();

        goCarrito();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void iniciarVistas(){
        famoso = Objects.requireNonNull(getIntent().getExtras()).getParcelable("famoso");
        img = findViewById(R.id.imageView);
        carrito = findViewById(R.id.btnCarrito);
        rbtnDedicatoria = findViewById(R.id.rbtnDedicatoria);
    }

    private void iniciarValores(){
        if(famoso != null){
            Glide.with(this).load(famoso.getImg()).into(img);
            uri = famoso.getImg();
        }
    }

    public void goCarrito(){

        carrito.setOnClickListener(v -> {
            String res="";

            if(rbtnDedicatoria.isChecked()){
                res = "Dedicatoria";
                addCarrito(res);
            }else{
                Toast.makeText(this,"Seleccione una categoría",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCarrito(String res){
        // Create a new user with a first and last name

        Map<String,Object> mapa = new HashMap<>();

        ItemCarrito articulos = new ItemCarrito();
        articulos.setNombre("Foto dedicada");
        articulos.setMotivo(res);
        articulos.setObservaciones(res);
        articulos.setPrecio(4.99);
        articulos.setImagen(uri.toString());
        articulos.setCreador(famoso.getName());
        articulos.setKey(famoso.getKey());

        mapa.put("nombre",articulos.getNombre());
        mapa.put("creador",articulos.getCreador());
        mapa.put("observaciones",articulos.getObservaciones());
        mapa.put("precio",articulos.getPrecio());
        mapa.put("img",articulos.getImagen());
        mapa.put("key",articulos.getKey());


        // Add a new document with a generated ID
        db.collection("usuarios")
                .document(Objects.requireNonNull(usuario.getEmail()))
                .collection("Carrito").add(mapa)
                .addOnSuccessListener(documentReference ->
                startActivity(new Intent(getApplicationContext(), ItemCarritoAdded.class)));
    }
}