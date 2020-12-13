package com.marquelo.getstars.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.marquelo.getstars.R;

public class ItemCarritoAdded extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_item_carrito_added);


        Button btnCarrito = findViewById(R.id.btnOtraFirma);
        Button btnHome = findViewById(R.id.btnVolverInicio);

        btnCarrito.setOnClickListener(v -> {
            startActivity(new Intent(this,Carrito.class));
        });

        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(this, LobbyActivity.class));
        });
    }


}