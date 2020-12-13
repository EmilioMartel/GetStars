package com.marquelo.getstars.pdf;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.marquelo.getstars.R;

import java.io.InputStream;

public class LeerTermsConditions extends AppCompatActivity {
    private PDFView myPdf;
    private ProgressBar progressBar;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setTitle("Terms & Conditions");
        setContentView(R.layout.activity_leer_terms_conditions);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        myPdf = findViewById(R.id.pdfViewConditions);
        progressBar = findViewById(R.id.progressBar);

        // Path
        StorageReference storageRef = storage.getReference();
        String path = "terminosDeUso/Terms.pdf";
        StorageReference pdfRef = storageRef.child(path);

        pdfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                AsyncTask<String, Void, InputStream> o = new RecibirPDFStream(myPdf,progressBar).execute(uri.toString());
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}