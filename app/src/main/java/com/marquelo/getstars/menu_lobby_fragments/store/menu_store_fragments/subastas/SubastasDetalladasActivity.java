package com.marquelo.getstars.menu_lobby_fragments.store.menu_store_fragments.subastas;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.marquelo.getstars.Cardview.CardViewFamososDetallada;
import com.marquelo.getstars.R;
import com.marquelo.getstars.menu_lobby_fragments.profile.famoso.CrearSubastasActivity;
import com.marquelo.getstars.working.Subastas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SubastasDetalladasActivity extends AppCompatActivity {
    private Subastas subastas;
    private ImageView imageView;
    private TextView name, creador, description, valor;
    private Button btnPujar, btnVerCreador;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_subastas_detalladas);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        iniciarVistas();
        iniciarValores();
        pujar();
        verCreador();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void iniciarVistas(){
        subastas = Objects.requireNonNull(getIntent().getExtras()).getParcelable("subastas");
        imageView = findViewById(R.id.imgSubasta);
        name = findViewById(R.id.nombreSubasta);
        creador = findViewById(R.id.txtCreador);
        description = findViewById(R.id.descriptionSubasta);
        valor = findViewById(R.id.txtValor);
        btnPujar = findViewById(R.id.btnPujar);
        btnVerCreador = findViewById(R.id.btnVerCreador);
    }

    private void iniciarValores() {
        if (subastas != null){
            Glide.with(this).load(subastas.getImg()).into(imageView);
            name.setText(subastas.getNombre());
            creador.setText(subastas.getCreador());
            setTitle(subastas.getNombre());
            description.setText(subastas.getInfo());
            valor.setText(String.valueOf(subastas.getPrecio()));

            System.out.println(subastas.getCreador()+"----------------------");
        }
    }
    private void verCreador(){
        btnVerCreador.setOnClickListener(v -> {
            Intent intent = new Intent(this, CardViewFamososDetallada.class);
            intent.putExtra("subasta", subastas);
            startActivity(intent);
        });
    }

    private void pujar(){
        btnPujar.setOnClickListener(v -> {
            AlertDialog.Builder myDialog = new AlertDialog.Builder(SubastasDetalladasActivity.this);
            myDialog.setTitle("Introduzca la cantidad de euros");

            final EditText euros = new EditText(SubastasDetalladasActivity.this);
            euros.setInputType(InputType.TYPE_CLASS_NUMBER);
            myDialog.setView(euros);

            //Guardamos la firma manual con nuestro nombre propio
            myDialog.setPositiveButton("Siguiente", (dialog, which) -> {
                AlertDialog.Builder myDialog2 = new AlertDialog.Builder(SubastasDetalladasActivity.this);
                myDialog2.setTitle("Introduzca la cantidad de centimos");

                final EditText centimos = new EditText(SubastasDetalladasActivity.this);
                centimos.setInputType(InputType.TYPE_CLASS_NUMBER);
                myDialog2.setView(centimos);

                //Guardamos la firma manual con nuestro nombre propio
                myDialog2.setPositiveButton("Guardar", (dialog12, which12) -> {

                    if(centimos.getText().length() >2){
                        Toast.makeText(getApplicationContext(),"Error en la cantidad de céntimos",Toast.LENGTH_SHORT).show();
                    }else {
                        String precio = euros.getText().toString() + "." + centimos.getText().toString();
                        double precioPujarDouble = Double.parseDouble(precio);
                        double precioActualDouble = Double.parseDouble((String) valor.getText());

                        System.out.println("precio actual: " + precioActualDouble + "\n"+ "precio a pujar: " + precioPujarDouble);

                        if(precioPujarDouble> precioActualDouble){
                            db.collection("subastas").document(subastas.getIdSubasta()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    valor.setText(precio);

                                    Map<String,Object> map = new HashMap<>();
                                    map.put("precio",Double.parseDouble(precio));
                                    map.put("ultimoParticipante",user.getEmail());
                                    db.collection("subastas").document(subastas.getIdSubasta()).set(map, SetOptions.merge());
                                    Toast.makeText(getApplicationContext(),"funciona",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else if(precioPujarDouble <= precioActualDouble){
                            Toast.makeText(getApplicationContext(),"Cantidad introducida es igual o menor al valor actual",Toast.LENGTH_SHORT).show();
                        }

                    }

                });

                //Cancelamos y salimos del dialog
                myDialog2.setNegativeButton("Cancelar", (dialog1, which1) -> dialog1.cancel());
                myDialog2.show();
            });

            //Cancelamos y salimos del dialog
            myDialog.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            myDialog.show();
        });

    }
}