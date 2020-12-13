package com.marquelo.getstars.menu_lobby_fragments.store.menu_store_fragments.sorteos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import com.marquelo.getstars.R;
import com.marquelo.getstars.menu_lobby_fragments.profile.famoso.CrearSorteoActivity;
import com.marquelo.getstars.ui.LobbyActivity;

public class SorteoCreadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_sorteo_creado);

        Button btnVolver = findViewById(R.id.button5);
        Button btnGoHome = findViewById(R.id.button6);

        btnVolver.setOnClickListener(v -> {
            startActivity(new Intent(this, CrearSorteoActivity.class));
        });

        btnGoHome.setOnClickListener(v -> {
            startActivity(new Intent(this, LobbyActivity.class));
        });
    }
}