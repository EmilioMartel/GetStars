package com.marquelo.getstars.menu_lobby_fragments.store.menu_store_fragments.lives;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.marquelo.getstars.R;
import com.marquelo.getstars.working.Lives;
import com.marquelo.getstars.working.adapter.AdapterLives;

import java.util.ArrayList;
import java.util.Objects;

public class LivesFragment extends Fragment {
    //------------ ATRIBUTOS FIREBASE-----------//
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //------------- ATRIBUTOS PROPIOS -----------//
    private ArrayList<Lives> listaLives;
    private RecyclerView recyclerLives;
    private View root;


    public LivesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaLives = new ArrayList<>();
        recyclerLives = null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_lives, container, false);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerLives = root.findViewById(R.id.recyclerLives);
        recyclerLives.setLayoutManager(new GridLayoutManager(this.getContext(),1));



        llenarLives(inflater);

        return root;
    }



    private void llenarLives(LayoutInflater inflater) {
        /*------------------------- CARGAMOS INFO (TODO MENOS IMG)------------*/
        db.collection("lives").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Lives lives = new Lives();
                            lives.setNombre(document.getString("nombre"));
                            lives.setCreador(document.getString("nombre"));
                            lives.setIdLives(document.getId());
                            lives.setKey(document.getString("dueño"));
                            lives.setDescripcion(document.getString("descripcion"));


                            double capacidadTotal = (double) document.getDouble("numeroParticipantes");
                            int capacidadTotal2 = (int) capacidadTotal;

                            ArrayList<String> listaParticipantes = (ArrayList<String>) document.get("listaParticipantes");
                            assert listaParticipantes != null;
                            int capacidadActual =listaParticipantes.size();

                            lives.setCapacidadActual(String.valueOf(capacidadActual));
                            lives.setCapacidadTotal(String.valueOf(capacidadTotal2));


                            //--------------------CARGAMOS IMG ----------------//
                            StorageReference storageRef = storage.getReference();

                            // Path
                            String path = "creadores/" + lives.getIdLives() + "/profileImage.jpg";

                            StorageReference imgRef = storageRef.child(path);

                            Task<Uri> listResultTask = imgRef.getDownloadUrl();
                            listResultTask.addOnSuccessListener(uri -> {
                                System.out.println(uri);
                                lives.setImg(uri.toString());
                                listaLives.add(lives);
                                root = cargarVista(inflater,listaLives);
                            }).addOnFailureListener(e -> {
                                Toast.makeText(getContext(),"No se pudo obtener la imagen",Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                }).addOnFailureListener(e -> {  Toast.makeText(getContext(),"No se pudo obtener los datos del Lives",Toast.LENGTH_SHORT).show();});
    }

    private View cargarVista(LayoutInflater inflater, ArrayList<Lives> listaLives) {
        root = inflater.inflate(R.layout.fragment_lives, null, false);
        AdapterLives adapter = new AdapterLives(listaLives);
        recyclerLives.setAdapter(adapter);

        return root;
    }
}