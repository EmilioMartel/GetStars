package com.marquelo.getstars.menu_lobby_fragments.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.marquelo.getstars.R;
import com.marquelo.getstars.menu_lobby_fragments.profile.firmas.FirmaDedActivity;
import com.marquelo.getstars.menu_lobby_fragments.profile.foto.FotoSalesActivity;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class ItemSalesFragment extends Fragment {
    private TextView cFotoFirma, cFotoDedicatoria, cFotoPersonalizada, cFirma, cLive;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public ItemSalesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_compras_firmas, container, false);
        cFirma = root.findViewById(R.id.countFirmaDedicada);

        cFotoFirma = root.findViewById(R.id.countFotoAutografo);
        cFotoDedicatoria = root.findViewById(R.id.countFotoDedicatoria);
        cFotoPersonalizada = root.findViewById(R.id.countFotoPersonalizada);

        cLive = root.findViewById(R.id.countLive);

        db.collection("usuarios").document(Objects.requireNonNull(user.getEmail())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Map<String,Object> map = (Map<String, Object>) task.getResult().get("compras");
                    assert map != null;
                    ArrayList<String> aut = (ArrayList<String>) map.get("aut");
                    assert aut != null;
                    int c = aut.size();
                    cFirma.setText(String.valueOf(c));
                    ArrayList<String> autFot = (ArrayList<String>) map.get("autFot");
                    assert autFot != null;
                    c = autFot.size();
                    cFotoFirma.setText(String.valueOf(c));
                    ArrayList<String> fot = (ArrayList<String>) map.get("fot");
                    assert fot != null;
                    c = fot.size();
                    cFotoPersonalizada.setText(String.valueOf(c));
                    ArrayList<String> fotDed = (ArrayList<String>) map.get("fotDed");
                    assert fotDed != null;
                    c = fotDed.size();
                    cFotoDedicatoria.setText(String.valueOf(c));
                    ArrayList<String> live = (ArrayList<String>) map.get("live");
                    assert live != null;
                    c = live.size();
                    cLive.setText(String.valueOf(c));
                }
            }
        });
        iniciarValores(root);
        return root;
    }

    private void iniciarValores(View root){
        CardView foto = root.findViewById(R.id.cvFoto);
        CardView firma = root.findViewById(R.id.cvFirma);
        CardView live = root.findViewById(R.id.cvLive);





        foto.setOnClickListener(v -> startActivity(new Intent(getContext(), FotoSalesActivity.class)));

        firma.setOnClickListener(v -> startActivity(new Intent(getContext(), FirmaDedActivity.class)));

        live.setOnClickListener(v -> {
        });
    }
}