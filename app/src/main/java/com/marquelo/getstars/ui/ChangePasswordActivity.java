package com.marquelo.getstars.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marquelo.getstars.R;
public class ChangePasswordActivity extends AppCompatActivity {
    private Button btnCambiarEmail;
    private TextView emailActual;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setTitle("Cambiar Contraseña");

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        iniciarElementos();
        setUp();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void iniciarElementos(){
        btnCambiarEmail = findViewById(R.id.btnCambiarPassword);
        emailActual = findViewById(R.id.txtEmailtoChangePassword);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
    }

    private void setUp(){
        btnCambiarEmail.setOnClickListener(v ->{

            if(emailActual.getText().toString().isEmpty()){
                Toast.makeText(this,"Ingrese un email válido",Toast.LENGTH_SHORT).show();
            }else{
                progressDialog.setTitle("Espere por favor...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                resetPassword();
            }
        });
    }

    private void resetPassword(){
        mAuth.setLanguageCode("en");
        mAuth.sendPasswordResetEmail(emailActual.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Email enviado al correo aportado",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"No se pudo enviar el correo de reestablecer contraseña",Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }
}