package com.marquelo.getstars.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.marquelo.getstars.R;

public class SettingsActivity extends AppCompatActivity {
    private Button cerrarSesion;
    private TextView verDatos, cambiarPassword, contactanos, eliminarCuenta;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_settings);

        setTitle("Ajustes");
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        iniciarElementos();

        setUp();

    }

    private void iniciarElementos(){
        cerrarSesion = findViewById(R.id.btn_logout);
        verDatos = findViewById(R.id.verDatosPersonales);
        cambiarPassword = findViewById(R.id.cambiarPassword);
        contactanos = findViewById(R.id.contactUs);
        eliminarCuenta = findViewById(R.id.eliminarCuenta);
        progressBar = findViewById(R.id.progressBar4);
    }

    private void setUp(){
        verDatos.setOnClickListener(v -> startActivity(new Intent(this, ShowSelfInfoActivity.class)));

        cambiarPassword.setOnClickListener(v -> startActivity(new Intent(this, ChangePasswordActivity.class)));

        cerrarSesion.setOnClickListener(v->{
            progressBar.setVisibility(View.INVISIBLE);
            logOut();
        } );

        contactanos.setOnClickListener(v -> startActivity(new Intent(this, ContactUsActivity.class)));

        eliminarCuenta.setOnClickListener(v -> startActivity(new Intent(this, DeleteAccountActivity.class)));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void logOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(this,"¡Hasta pronto!", Toast.LENGTH_LONG).show();
    }
}