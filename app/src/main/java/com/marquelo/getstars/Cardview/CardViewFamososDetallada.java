package com.marquelo.getstars.Cardview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.marquelo.getstars.Cardview.item.autografos.ItemFirmaCVActivity;
import com.marquelo.getstars.Cardview.item.fotos.ItemFotoCardviewActivity;
import com.marquelo.getstars.R;
import com.marquelo.getstars.working.Famoso;
import com.marquelo.getstars.working.Subastas;

import java.util.Objects;

public class CardViewFamososDetallada extends AppCompatActivity {
    //ATRIBUTOS
    private ImageView imageView;
    private TextView name;
    private TextView category;
    private TextView description;
    private Famoso famoso, famosoBySubasta;
    private Button btn_foto, btn_firma;
    private Subastas subastas;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_card_view_famosos_detallada);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        //COMIENZO DEL PROGRAMA
        iniciarVistas();
        iniciarValores();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void iniciarVistas(){
        famoso = Objects.requireNonNull(getIntent().getExtras()).getParcelable("famoso");
        subastas = Objects.requireNonNull(getIntent().getExtras()).getParcelable("subasta");
        imageView = findViewById(R.id.imgCVFamosoDetallada);
        name = findViewById(R.id.txtCVNameFamosoDetallada);
        category = findViewById(R.id.etCVCategoryFamoso);
        description = findViewById(R.id.txtCVDescriptionFamosoDetallada);
        btn_foto = findViewById(R.id.btn_fotos);
        btn_firma = findViewById(R.id.btn_autografo);

        famosoBySubasta = new Famoso();
    }

    private void iniciarValores(){
        if (famoso != null){
            Glide.with(this).load(famoso.getImg()).into(imageView);
            name.setText(famoso.getName());
            setTitle(famoso.getName());
            category.setText(famoso.getCategory());
            description.setText(famoso.getDescription());

            btn_firma.setOnClickListener(v -> {
                Intent intent  = new Intent(getApplicationContext(), ItemFirmaCVActivity.class);
                intent.putExtra("firma",famoso);
                startActivity(intent);
            });

            btn_foto.setOnClickListener(v -> {
                Intent intent  = new Intent(getApplicationContext(), ItemFotoCardviewActivity.class);
                intent.putExtra("fotos",famoso);
                startActivity(intent);
            });
        }else{

        }

        if(subastas != null){
            cargarDatos();
            btn_firma.setOnClickListener(v -> {
                Intent intent  = new Intent(getApplicationContext(), ItemFirmaCVActivity.class);
                intent.putExtra("firma",famosoBySubasta);
                startActivity(intent);
            });

            btn_foto.setOnClickListener(v -> {
                Intent intent  = new Intent(getApplicationContext(), ItemFotoCardviewActivity.class);
                intent.putExtra("fotos",famosoBySubasta);
                startActivity(intent);
            });
        }
    }

    private void cargarDatos(){
        db.collection("famosos").document(subastas.getCreador()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    famosoBySubasta.setName((String) Objects.requireNonNull(task.getResult()).get("Nombre"));
                    famosoBySubasta.setCategory((String) task.getResult().get("cat"));
                    famosoBySubasta.setDescription((String) task.getResult().get("Descripcion"));
                    famosoBySubasta.setKey(subastas.getCreador());

                    name.setText(famosoBySubasta.getName());
                    setTitle(famosoBySubasta.getName());
                    category.setText(famosoBySubasta.getCategory());
                    description.setText(famosoBySubasta.getDescription());

                    //--------------------CARGAMOS IMG ----------------//
                    StorageReference storageRef = storage.getReference();

                    // Path
                    String path = "creadores/" + subastas.getCreador() + "/profileImage.jpg" ;

                    StorageReference imgRef = storageRef.child(path);

                    Task<Uri> listResultTask = imgRef.getDownloadUrl();
                    listResultTask.addOnSuccessListener(uri -> {
                        famosoBySubasta.setImg(uri);
                        Glide.with(getApplicationContext()).load(famosoBySubasta.getImg()).into(imageView);
                    });
                }
            }
        });



    }
}