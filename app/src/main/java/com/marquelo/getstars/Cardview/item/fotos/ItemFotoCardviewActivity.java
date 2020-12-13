package com.marquelo.getstars.Cardview.item.fotos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.marquelo.getstars.Cardview.item.fotos.basica.FotoFirmaActivity;
import com.marquelo.getstars.Cardview.item.fotos.dedicatoria.ItemPhotoDedicatoriaActivity;
import com.marquelo.getstars.Cardview.item.fotos.personalizada.ItemPhotoCustomActivity;
import com.marquelo.getstars.R;
import com.marquelo.getstars.working.Famoso;

import java.util.Objects;

public class ItemFotoCardviewActivity extends AppCompatActivity {
    //------------ ATRIBUTOS FIREBASE-----------/
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    //------------ ATRIBUTOS PROPIOS ---------------//
    private Famoso famoso;
    private Button btnFotoFirma;
    private Button btnFotoDedicatoria;
    private Button btnFotoPersonalizada;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_item_foto_cardview);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Fotos");

        iniciarVistas();
        iniciarValores();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void iniciarVistas(){
        famoso = Objects.requireNonNull(getIntent().getExtras()).getParcelable("fotos");
        imageView = findViewById(R.id.imgFotoFirma);
        btnFotoFirma = findViewById(R.id.btnFotoFirma);
        btnFotoDedicatoria = findViewById(R.id.btnFotoDedicatoria);
        btnFotoPersonalizada = findViewById(R.id.btnFotoPersonalizada);
    }

    private void iniciarValores() {
        if(famoso != null){
            //--------------------CARGAMOS IMG ----------------//
            StorageReference storageRef = storage.getReference();

            // Path
            String path = "creadores/" + famoso.getKey() + "/autFot.jpg" ;

            StorageReference imgRef = storageRef.child(path);

            Task<Uri> listResultTask = imgRef.getDownloadUrl();
            listResultTask.addOnSuccessListener(uri -> {
                famoso.setImg(uri);
                Glide.with(getApplicationContext()).load(uri).into(imageView);
            });
        }

        btnFotoFirma.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FotoFirmaActivity.class);
            intent.putExtra("famoso",famoso);
            startActivity(intent);
        });

        btnFotoDedicatoria.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ItemPhotoDedicatoriaActivity.class);
            intent.putExtra("famoso",famoso);
            startActivity(intent);
        });

        btnFotoPersonalizada.setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(), ItemPhotoCustomActivity.class);
            intent.putExtra("famoso",famoso);
            startActivity(intent);
        });




    }
}