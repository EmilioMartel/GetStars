package com.marquelo.getstars.menu_lobby_fragments.store.menu_store_fragments.subastas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import com.marquelo.getstars.R;
import com.marquelo.getstars.menu_lobby_fragments.profile.famoso.CrearSorteoActivity;
import com.marquelo.getstars.menu_lobby_fragments.profile.famoso.CrearSubastasActivity;
import com.marquelo.getstars.ui.LobbyActivity;

public class SubastaCreadaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_subasta_creada);

        Button btnVolver = findViewById(R.id.btnVolverFirma);
        Button btnGoHome = findViewById(R.id.btnVolverInicio2);

        btnVolver.setOnClickListener(v -> {
            startActivity(new Intent(this, CrearSubastasActivity.class));
        });

        btnGoHome.setOnClickListener(v -> {
            startActivity(new Intent(this, LobbyActivity.class));
        });
    }
}