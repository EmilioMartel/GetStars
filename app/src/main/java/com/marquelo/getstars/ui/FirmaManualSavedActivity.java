package com.marquelo.getstars.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import com.marquelo.getstars.R;
import com.marquelo.getstars.menu_lobby_fragments.profile.famoso.CrearSorteoActivity;

public class FirmaManualSavedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_firma_manual_guardada);

        Button btnVolver = findViewById(R.id.btnOtraFirma);
        Button btnGoHome = findViewById(R.id.btnVolverInicio);

        btnVolver.setOnClickListener(v -> {
            startActivity(new Intent(this, AutographActivity.class));
        });

        btnGoHome.setOnClickListener(v -> {
            startActivity(new Intent(this, LobbyActivity.class));
        });
    }
}