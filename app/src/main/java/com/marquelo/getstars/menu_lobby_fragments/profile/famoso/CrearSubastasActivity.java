package com.marquelo.getstars.menu_lobby_fragments.profile.famoso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.marquelo.getstars.R;
import com.marquelo.getstars.menu_lobby_fragments.store.menu_store_fragments.sorteos.SorteoCreadoActivity;
import com.marquelo.getstars.working.Subastas;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CrearSubastasActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Button btnFechaLimite, btnSetPrecio, btnCargarImagen, btnCrearSubasta;
    private EditText txtFechaLimite, txtSetPrecio, txtNameSubasta, txtDescription;
    private int dia, mes, anio;
    private ImageView imageView;
    private Uri urlPhoto;
    private ProgressBar progressBar;

    private static final int REQUEST_PERMISSION_CODE = 100;
    private static final int REQUEST_IMAGE_GALLERY = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_subastas);

        setTitle("Crear Subasta");

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        iniciarElementos();
        crearSubasta();

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void iniciarElementos(){
        btnFechaLimite = findViewById(R.id.btnFechaLimiteSubasta);
        btnFechaLimite.setOnClickListener(this);
        txtFechaLimite = findViewById(R.id.txtFechaLimiteSubasta);
        txtFechaLimite.setEnabled(false);
        btnCargarImagen = findViewById(R.id.btnCargarImagenSubasta);
        btnCargarImagen.setOnClickListener(this);
        txtSetPrecio = findViewById(R.id.txtSetPrecio);
        txtSetPrecio.setEnabled(false);
        btnSetPrecio = findViewById(R.id.btnSetPrecio);
        btnSetPrecio.setOnClickListener(this);
        imageView = findViewById(R.id.imgFotoSubasta);
        btnCrearSubasta = findViewById(R.id.btnCrearSubasta);
        txtNameSubasta = findViewById(R.id.nombreSubasta);
        txtDescription = findViewById(R.id.txtDescriptionSubasta);
        progressBar = findViewById(R.id.progressBar6);
    }

    private void crearSubasta(){
        btnCrearSubasta.setOnClickListener(v -> {
            try{
                String name = txtNameSubasta.getText().toString().trim();
                String description = txtDescription.getText().toString().trim();
                String fecha = txtFechaLimite.getText().toString().trim();
                String precioSubasta = txtSetPrecio.getText().toString();
                if(name.isEmpty() &&description.isEmpty() && fecha.isEmpty() && urlPhoto == null){
                    Toast.makeText(getApplicationContext(),"Todos los campos están vacíos. Rellenelos por favor.",Toast.LENGTH_SHORT).show();
                }else {
                    if(!name.isEmpty()){
                        if(!description.isEmpty()){
                            if(!fecha.isEmpty()){
                                if(!precioSubasta.isEmpty()){
                                    if(urlPhoto != null){
                                        progressBar.setVisibility(View.VISIBLE);

                                        //--------------------- CAMBIAR RUTA POR LOS famosos, EMAIL POR .GETUID(), Y nombre -> Nombre
                                        db.collection("usuarios").document(Objects.requireNonNull(user.getEmail())).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                String nombreCreador = documentSnapshot.getString("nombre");

                                                Subastas subastas = new Subastas();
                                                subastas.setNombre(name);
                                                subastas.setInfo(description);
                                                subastas.setFechaFinal(fecha);
                                                subastas.setPrecio(Double.parseDouble(precioSubasta));
                                                subastas.setCreador(nombreCreador);
                                                subastas.setKey(documentSnapshot.getId());

                                                Map<String,Object> map = new HashMap<>();
                                                map.put("nombre",subastas.getNombre());
                                                map.put("descripcion",subastas.getInfo());
                                                map.put("fechaFinal",subastas.getFechaFinal());
                                                map.put("precio",subastas.getPrecio());
                                                map.put("dueño",subastas.getCreador());
                                                map.put("key",subastas.getKey());

                                                String ultimoParticipante = "";
                                                map.put("ultimoParticipante",ultimoParticipante);

                                                db.collection("subastas").document().set(map);

                                                //--------------------CARGAMOS IMG ----------------//
                                                StorageReference storageRef = storage.getReference();
                                                String nombre = subastas.getNombre().replace(" ", "");
                                                nombre = nombre.trim();

                                                // Path
                                                String path = "creadores/" + subastas.getCreador() +"/subastas/";

                                                StorageReference imgRef = storageRef.child(path).child(nombre+".jpg");


                                                UploadTask uploadTask = imgRef.putFile(urlPhoto);

                                                // Register observers to listen for when the download is done or if it fails
                                                uploadTask.addOnFailureListener(exception -> {
                                                    // Handle unsuccessful uploads
                                                }).addOnSuccessListener(taskSnapshot -> {
                                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                                    // ...
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    startActivity(new Intent(getApplicationContext(), SorteoCreadoActivity.class));
                                                });
                                            }
                                        });

                                    }else {
                                        Toast.makeText(getApplicationContext(),"Debes cargar una imagen de la subasta",Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(getApplicationContext(),"Introduzca un precio",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"Ingrese una fecha de duración",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"Ingrese una descripción",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Ingrese un nombre",Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception ignored){

            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v == btnFechaLimite){
            final Calendar calendar = Calendar.getInstance();
            dia = calendar.get(Calendar.DAY_OF_MONTH);
            mes = calendar.get(Calendar.MONTH);
            anio = calendar.get(Calendar.YEAR);
            String fechaActual = dia+"/"+(mes+1)+"/"+anio;
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    if((dayOfMonth >= dia && (month+1) >= (mes+1) && year>=anio) ||(dayOfMonth <= dia && (month+1) <= (mes+1) && year>anio) || (dayOfMonth>= dia && (month+1) <=mes && year>=anio)){
                        String fechaPropuesta = dayOfMonth+"/"+(month+1)+"/"+year;
                        txtFechaLimite.setText(fechaPropuesta);
                    }else{
                        Toast.makeText(getApplicationContext(),"FECHA INCORRECTA. Inserte una nueva",Toast.LENGTH_SHORT).show();
                    }

                }
            }
                    ,dia,mes,anio);
            datePickerDialog.show();
        }

        if(v == btnCargarImagen){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ActivityCompat.checkSelfPermission(CrearSubastasActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    openGallery();
                }else{
                    ActivityCompat.requestPermissions(CrearSubastasActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION_CODE);
                }
            }else{
                openGallery();
            }
        }

        if(v == btnSetPrecio){
            AlertDialog.Builder myDialog = new AlertDialog.Builder(CrearSubastasActivity.this);
            myDialog.setTitle("Introduzca la cantidad de euros");

            final EditText euros = new EditText(CrearSubastasActivity.this);
            euros.setInputType(InputType.TYPE_CLASS_NUMBER);
            myDialog.setView(euros);

            //Guardamos la firma manual con nuestro nombre propio
            myDialog.setPositiveButton("Siguiente", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder myDialog2 = new AlertDialog.Builder(CrearSubastasActivity.this);
                    myDialog2.setTitle("Introduzca la cantidad de centimos");

                    final EditText centimos = new EditText(CrearSubastasActivity.this);
                    centimos.setInputType(InputType.TYPE_CLASS_NUMBER);
                    myDialog2.setView(centimos);

                    //Guardamos la firma manual con nuestro nombre propio
                    myDialog2.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(centimos.getText().length() >2){
                                Toast.makeText(getApplicationContext(),"Error en la cantidad de céntimos",Toast.LENGTH_SHORT).show();
                            }else {
                                String precio = euros.getText().toString() + "." + centimos.getText().toString();
                                txtSetPrecio.setText(precio);
                            }

                        }
                    });

                    //Cancelamos y salimos del dialog
                    myDialog2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    myDialog2.show();
                }
            });

            //Cancelamos y salimos del dialog
            myDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            myDialog.show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION_CODE){
            if(permissions.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                openGallery();
            }else{
                Toast.makeText(this, "Necesitas habilitar los permisos",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_IMAGE_GALLERY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_GALLERY){
            if(resultCode == Activity.RESULT_OK && data!=null){
                urlPhoto = data.getData();
                System.out.println(urlPhoto);
                imageView.setImageURI(urlPhoto);
            }else{
                Toast.makeText(this,"No se obtuvo la imagen",Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}