package com.marquelo.getstars.menu_lobby_fragments.store.menu_store_fragments.sorteos;

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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.marquelo.getstars.R;
import com.marquelo.getstars.working.Sorteos;
import com.marquelo.getstars.working.adapter.AdapterSorteos;

import java.util.ArrayList;
import java.util.Objects;

public class SorteosFragment extends Fragment {
    //------------ ATRIBUTOS FIREBASE-----------//
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //------------- ATRIBUTOS PROPIOS -----------//
    ArrayList<Sorteos> listaSorteos;
    RecyclerView recyclerSorteos;
    View root;

    public SorteosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaSorteos = new ArrayList<>();
        root = null;
        recyclerSorteos = null;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_sorteos, container, false);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerSorteos = root.findViewById(R.id.recyclerSorteos);
        recyclerSorteos.setLayoutManager(new GridLayoutManager(this.getContext(),1));

        llenarSorteos(inflater);
        AdapterSorteos adapter = new AdapterSorteos(listaSorteos);

        recyclerSorteos.setAdapter(adapter);

        return root;
    }

    private void llenarSorteos(LayoutInflater inflater) {
        /*------------------------- CARGAMOS INFO (TODO MENOS IMG)------------*/
        Task<QuerySnapshot> sorteosRef = db.collection("sorteos").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Sorteos sorteos = new Sorteos();
                            sorteos.setNombre(document.getString("nombre"));
                            sorteos.setCreador(document.getString("dueño"));
                            sorteos.setIdSorteo(document.getId());
                            sorteos.setKey(document.getId());
                            sorteos.setDescripcion(document.getString("descripcion"));
                            sorteos.setFechaFinal(document.getString("fechaFinal"));



                            //--------------------CARGAMOS IMG ----------------//
                            StorageReference storageRef = storage.getReference();

                            String nombre = sorteos.getNombre().replace(" ", "");
                            // Path
                            String path = "creadores/" + sorteos.getCreador() + "/sorteos/" +nombre + ".jpg";

                            StorageReference imgRef = storageRef.child(path);

                            Task<Uri> listResultTask = imgRef.getDownloadUrl();
                            listResultTask.addOnSuccessListener(uri -> {
                                System.out.println(uri);
                                sorteos.setImg(uri.toString());
                                listaSorteos.add(sorteos);
                                root = cargarVista(inflater,listaSorteos);
                            }).addOnFailureListener(e -> {  Toast.makeText(getContext(),"No se pudo obtener la imagen",Toast.LENGTH_SHORT).show();});
                        }
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(),"No se pudo obtener los datos del sorteo",Toast.LENGTH_SHORT).show();
                });
    }

    private View cargarVista(LayoutInflater inflater, ArrayList<Sorteos> listaSorteos) {
        root = inflater.inflate(R.layout.fragment_sorteos, null, false);
        AdapterSorteos adapter = new AdapterSorteos(listaSorteos);
        recyclerSorteos.setAdapter(adapter);

        return root;
    }
}