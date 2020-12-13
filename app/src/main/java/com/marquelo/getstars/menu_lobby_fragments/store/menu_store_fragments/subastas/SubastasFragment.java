package com.marquelo.getstars.menu_lobby_fragments.store.menu_store_fragments.subastas;

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
import com.marquelo.getstars.working.Subastas;
import com.marquelo.getstars.working.adapter.AdapterSubastas;

import java.util.ArrayList;
import java.util.Objects;


public class SubastasFragment extends Fragment {
    //------------ ATRIBUTOS FIREBASE-----------//
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //------------- ATRIBUTOS PROPIOS -----------//
    ArrayList<Subastas> listaSubasta;
    RecyclerView recyclerSubastas;
    View root;

    public SubastasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaSubasta = new ArrayList<>();
        root = null;
        recyclerSubastas = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_subastas, container, false);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerSubastas = root.findViewById(R.id.recyclerSubastas);
        recyclerSubastas.setLayoutManager(new GridLayoutManager(this.getContext(),1));

        llenarSubastas(inflater);
        AdapterSubastas adapter = new AdapterSubastas(listaSubasta);

        recyclerSubastas.setAdapter(adapter);

        return root;
    }

    private void llenarSubastas(LayoutInflater inflater) {
        /*------------------------- CARGAMOS INFO (TODO MENOS IMG)------------*/
        Task<QuerySnapshot> subastasRef = db.collection("subastas").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Subastas subastas = new Subastas();
                            subastas.setNombre(document.getString("nombre"));
                            subastas.setInfo(document.getString("descripcion"));
                            subastas.setFechaFinal(document.getString("fechaFinal"));
                            subastas.setCreador(document.getString("dueño"));
                            subastas.setIdSubasta(document.getId());
                            subastas.setPrecio(document.getDouble("precio"));
                            subastas.setUltimoParticipante(document.getString("ultimoParticipante"));

                            //--------------------CARGAMOS IMG ----------------//
                            StorageReference storageRef = storage.getReference();

                            String nombre = subastas.getNombre().replace(" ", "").trim();
                            // Path
                            String path = "creadores/" + subastas.getCreador() +"/subastas/"+nombre +".jpg";

                            StorageReference imgRef = storageRef.child(path);



                            Task<Uri> listResultTask = imgRef.getDownloadUrl();
                            listResultTask.addOnSuccessListener(uri -> {
                                subastas.setImg(uri.toString());

                                listaSubasta.add(subastas);
                                root = cargarVista(inflater,listaSubasta);
                            }).addOnFailureListener(e -> {
                                Toast.makeText(getContext(),"No se pudo obtener la imagen",Toast.LENGTH_SHORT).show();
                            });

                        }

                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(),"No se pudo obtener los datos de la subasta",Toast.LENGTH_SHORT).show();
                });

    }

    private View cargarVista(LayoutInflater inflater, ArrayList<Subastas> listaSubasta) {
        root = inflater.inflate(R.layout.fragment_subastas, null, false);
        AdapterSubastas adapter = new AdapterSubastas(listaSubasta);
        recyclerSubastas.setAdapter(adapter);

        return root;
    }
}