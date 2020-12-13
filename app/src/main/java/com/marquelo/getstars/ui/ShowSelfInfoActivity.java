package com.marquelo.getstars.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.marquelo.getstars.R;

import java.util.Objects;

public class ShowSelfInfoActivity extends AppCompatActivity {
    private TextView nombre, email, fecha, genero;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_datos_personales);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setTitle("Datos personales");

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        iniciarElemento();
        setup();
    }

    private void iniciarElemento(){
        nombre = findViewById(R.id.txtNombre);
        email = findViewById(R.id.txtMostrarEmail);
        fecha = findViewById(R.id.txtFechaNacimiento);
        genero = findViewById(R.id.txtGenero);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void setup(){
        db.collection("usuarios").document(Objects.requireNonNull(user.getEmail())).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                nombre.setText(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).get("nombre")).toString());
                email.setText(user.getEmail());
                fecha.setText(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).get("fecha")).toString());
                genero.setText(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).get("sexo")).toString());

            }
        });
    }
}