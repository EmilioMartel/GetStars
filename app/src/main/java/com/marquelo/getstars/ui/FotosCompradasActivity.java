package com.marquelo.getstars.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.marquelo.getstars.R;
import com.marquelo.getstars.working.FirmaManual;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class FotosCompradasActivity extends AppCompatActivity {

    private ImageView imageView;
    private FirmaManual fm;
    private Button btnGuardar;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private OutputStream outputStream;
    private static final int REQUEST_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotos_compradas);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);


        ActivityCompat.requestPermissions(FotosCompradasActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(FotosCompradasActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        iniciarVistas();
        iniciarValores();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void iniciarVistas(){
        imageView = findViewById(R.id.imgFotoComprada);
        fm = Objects.requireNonNull(getIntent().getExtras()).getParcelable("fotos");
        btnGuardar = findViewById(R.id.btnGuardarFoto);
        setTitle(fm.getNombre());
    }

    private void iniciarValores(){
        if (fm != null){
            Glide.with(this).load(fm.getUrl()).into(imageView);
        }

        btnGuardar.setOnClickListener(v -> {
            comprobarSdk();
        });

    }

    private void guardarImagen2(){
        String ruta = fm.getUrl().getLastPathSegment();
        assert ruta != null;

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Path
        String path = fm.getUrl().getLastPathSegment();

        // Reference to manual autograph
        StorageReference imgRef = storageRef.child(path);


        Task<ListResult> listResultTask = imgRef.listAll();
        listResultTask.addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                final long ONE_MEGABYTE = 1024 * 1024;
                imgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {

                    Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    int count = 0;
                    File sdDirectory = Environment.getExternalStorageDirectory();
                    File subDirectory = new File(sdDirectory.toString() + "/Pictures/GetStars");

                    if (subDirectory.exists()) {

                        File[] existing = subDirectory.listFiles();
                        for (File file : existing) {
                            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                                count++;
                            }
                        }

                    } else {
                        subDirectory.mkdir();
                    }

                    if (subDirectory.exists()) {
                        File image = new File(subDirectory, "/GetStars_" + count + ".png");
                        FileOutputStream fileOutputStream;

                        try {
                            fileOutputStream = new FileOutputStream(image);

                            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                            fileOutputStream.flush();
                            fileOutputStream.close();

                            Toast.makeText(getApplicationContext(), "Imagen almacenada con éxito!", Toast.LENGTH_SHORT).show();

                        } catch (IOException ignored) {

                        }
                    }
                }).addOnFailureListener(exception -> {
                    // Handle any errors
                });
            }
        });
    }


    private void comprobarSdk() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(FotosCompradasActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                guardarImagen2();
                Log.i("Mensaje", "Se tiene permiso para leer!");
            }else{
                ActivityCompat.requestPermissions(FotosCompradasActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION_CODE);
            }
        }else{
            guardarImagen2();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION_CODE){
            if(permissions.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                guardarImagen2();
                Log.i("Mensaje", "Se tiene permiso para leer!");
            }else{
                Toast.makeText(this, "Necesitas habilitar los permisos",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}