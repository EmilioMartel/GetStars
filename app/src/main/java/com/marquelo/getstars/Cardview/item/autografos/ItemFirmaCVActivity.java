package com.marquelo.getstars.Cardview.item.autografos;

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
import com.marquelo.getstars.R;
import com.marquelo.getstars.working.Famoso;

import java.util.Objects;

public class ItemFirmaCVActivity extends AppCompatActivity {
    //------------ ATRIBUTOS FIREBASE-----------/
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    //------------ ATRIBUTOS PROPIOS ---------------//
    private Famoso famoso, famosoBySubasta;
    private Button  btnPrecioFirmaDedicada;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_item_autografo_cardview);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Autógrafos");

        iniciarVistas();
        iniciarValores();
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void iniciarVistas(){
        famoso = Objects.requireNonNull(getIntent().getExtras()).getParcelable("firma");
        famosoBySubasta = Objects.requireNonNull(getIntent().getExtras()).getParcelable("firma");
        imageView = findViewById(R.id.imgFotoFirma);
        btnPrecioFirmaDedicada = findViewById(R.id.btnItemAutografo);
    }

    private void iniciarValores() {
        if(famoso != null){
            //--------------------CARGAMOS IMG ----------------//
            StorageReference storageRef = storage.getReference();

            // Path
            String path = "creadores/" + famoso.getKey() + "/aut.jpg" ;

            StorageReference imgRef = storageRef.child(path);

            Task<Uri> listResultTask = imgRef.getDownloadUrl();
            listResultTask.addOnSuccessListener(uri -> {
                famoso.setImg(uri);
                Glide.with(getApplicationContext()).load(uri).into(imageView);

            });
            btnPrecioFirmaDedicada.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), ItemFirmaDedicada.class);
                intent.putExtra("famoso",famoso);
                startActivity(intent);
            });
        }
        if(famosoBySubasta != null){
            //--------------------CARGAMOS IMG ----------------//
            StorageReference storageRef = storage.getReference();

            // Path
            String path = "creadores/" + famosoBySubasta.getKey() + "/aut.jpg" ;

            StorageReference imgRef = storageRef.child(path);

            Task<Uri> listResultTask = imgRef.getDownloadUrl();
            listResultTask.addOnSuccessListener(uri -> {
                famosoBySubasta.setImg(uri);
                Glide.with(getApplicationContext()).load(famosoBySubasta.getImg()).into(imageView);

            });
            btnPrecioFirmaDedicada.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), ItemFirmaDedicada.class);
                intent.putExtra("famoso",famosoBySubasta);
                startActivity(intent);
            });
        }



    }
}