package com.marquelo.getstars.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.marquelo.getstars.R;

import static android.content.ContentValues.TAG;

public class DeleteAccountActivity extends AppCompatActivity {
    private EditText email, password;
    private Button btnEliminarCuenta;
    private String txtEmail;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_eliminar_cuenta);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Eliminar cuenta");

        email = findViewById(R.id.emailEliminarCuenta);
        password = findViewById(R.id.passwordEliminarCuenta);
        btnEliminarCuenta = findViewById(R.id.btnEliminarCuenta);
        txtEmail = user.getEmail();
        eliminarCuenta();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void eliminarCuenta(){
        btnEliminarCuenta.setOnClickListener(v -> {


            // Get auth credentials from the user for re-authentication. The example below shows
            // email and password credentials but there are multiple possible providers,
            // such as GoogleAuthProvider or FacebookAuthProvider.
            AuthCredential credential = EmailAuthProvider
                    .getCredential(email.getText().toString().trim(), password.getText().toString().trim());

            // Prompt the user to re-provide their sign-in credentials
            assert user != null;
            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> user.delete()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    Toast.makeText(getApplicationContext(),"Cuenta eliminada con éxito",Toast.LENGTH_SHORT).show();
                                    finish();
                                    Log.d(TAG, "User account deleted.");
                                }
                            }));


            db.collection("usuarios").document(txtEmail).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        System.out.println("----------------------- bd eliminado ----------------------");
                    }
                }
            });
        });
    }
}