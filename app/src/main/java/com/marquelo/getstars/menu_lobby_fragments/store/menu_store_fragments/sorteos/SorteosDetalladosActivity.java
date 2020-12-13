package com.marquelo.getstars.menu_lobby_fragments.store.menu_store_fragments.sorteos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.marquelo.getstars.R;
import com.marquelo.getstars.working.Sorteos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.*;

public class SorteosDetalladosActivity extends AppCompatActivity {
    private Sorteos sorteos;
    private ImageView imageView;
    private TextView nombre,descripcion,fechaLimite,participacion;
    private Button participar,noParticipar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_sorteos_detallados);

        iniciarElementos();
        iniciarValores();
        comprobarParticipacionUsuario();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }




    private void iniciarElementos(){
        sorteos = requireNonNull(getIntent().getExtras()).getParcelable("sorteos");
        imageView = findViewById(R.id.imageView);
        nombre = findViewById(R.id.txtNameSorteo);
        descripcion = findViewById(R.id.txtDescripcionSorteo);
        fechaLimite = findViewById(R.id.txtFechaSorteo);
        participacion = findViewById(R.id.txtParticipacion);
        participar = findViewById(R.id.btnParticipar);
        noParticipar = findViewById(R.id.btnNoParticipar);
    }

    private void iniciarValores(){
        if (sorteos != null){
            Glide.with(this).load(sorteos.getImg()).into(imageView);
            nombre.setText(sorteos.getNombre());
            setTitle(sorteos.getNombre());
            descripcion.setText(sorteos.getDescripcion());
            fechaLimite.setText(sorteos.getFechaFinal());
        }

        participar.setOnClickListener(v -> {
            participar.setVisibility(View.INVISIBLE);
            noParticipar.setVisibility(View.VISIBLE);
            participacion.setVisibility(View.VISIBLE);
            agregarUsuario();
        });

        noParticipar.setOnClickListener(v -> {
            noParticipar.setVisibility(View.INVISIBLE);
            participacion.setVisibility(View.INVISIBLE);
            participar.setVisibility(View.VISIBLE);
            eliminarUsuario();
        });
    }

    private void eliminarUsuario() {
        Map<String,ArrayList<String>> map = new HashMap<>();
        //map.put("participante",sorteos.getKey());
        /*------------------------- CARGAMOS INFO (TODO MENOS IMG)------------*/
        db.collection("sorteos").document(sorteos.getIdSorteo()).get().addOnSuccessListener(documentSnapshot -> {

            ArrayList<String> listParticipantes = (ArrayList<String>) requireNonNull(documentSnapshot.getData()).get("participantes");

            assert listParticipantes != null;
            if(listParticipantes.contains(user.getEmail())){
                listParticipantes.remove(user.getEmail());
                map.put("participantes",listParticipantes);
                db.collection("sorteos").document(sorteos.getIdSorteo()).set(map, SetOptions.merge());
            }
        });
    }

    private void agregarUsuario() {
        Map<String,ArrayList<String>> map = new HashMap<>();
        //map.put("participante",sorteos.getKey());
        /*------------------------- CARGAMOS INFO (TODO MENOS IMG)------------*/
        db.collection("sorteos").document(sorteos.getIdSorteo()
        ).get().addOnSuccessListener(documentSnapshot -> {

            ArrayList<String> listParticipantes = (ArrayList<String>) requireNonNull(documentSnapshot.getData()).get("participantes");

            assert listParticipantes != null;
            if(listParticipantes.contains(user.getEmail())){
                System.out.println("ya estas dentro bro");
            }else{
                listParticipantes.add(user.getEmail());

                map.put("participantes",listParticipantes);
                db.collection("sorteos").document(sorteos.getIdSorteo()).set(map, SetOptions.merge());
            }

        });

    }
    private void comprobarParticipacionUsuario(){
        db.collection("sorteos").document(sorteos.getIdSorteo()).get().addOnSuccessListener(documentSnapshot -> {

            ArrayList<String> listParticipantes = (ArrayList<String>) requireNonNull(documentSnapshot.getData()).get("participantes");

            assert listParticipantes != null;
            if(listParticipantes.contains(user.getEmail())){
                System.out.println("ya estas dentro bro");
                noParticipar.setVisibility(View.VISIBLE);
                participacion.setVisibility(View.VISIBLE);
                participar.setVisibility(View.INVISIBLE);
            }else{
                noParticipar.setVisibility(View.INVISIBLE);
                participacion.setVisibility(View.INVISIBLE);
                participar.setVisibility(View.VISIBLE);
            }

        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}