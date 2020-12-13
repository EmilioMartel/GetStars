package com.marquelo.getstars.menu_lobby_fragments.search.category;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.marquelo.getstars.R;
import com.marquelo.getstars.working.Famoso;
import com.marquelo.getstars.working.adapter.AdapterCategory;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryFiltradaFragment extends Fragment {
    private View root;
    //------------ ATRIBUTOS FIREBASE-----------//
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //------------- ATRIBUTOS PROPIOS -----------//
    ArrayList<Famoso> listaFamoso;
    RecyclerView recyclerCategory;
    private String category;

    public CategoryFiltradaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaFamoso = new ArrayList<>();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                category = bundle.getString("categoria");


            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_category_filtrada, container, false);

        recyclerCategory = root.findViewById(R.id.recyclerCategory);
        recyclerCategory.setLayoutManager(new GridLayoutManager(this.getContext(),1));

        llenarFamosos(inflater);
        AdapterCategory adapter = new AdapterCategory(listaFamoso);
        if(recyclerCategory.getAdapter() != null){
            //De esta manera sabes si tu RecyclerView está vacío
            recyclerCategory.getAdapter().getItemCount();

        }
        recyclerCategory.setAdapter(adapter);
        return root;
    }

    private void llenarFamosos(LayoutInflater inflater) {
        /*------------------------- CARGAMOS INFO (TODO MENOS IMG)------------*/
        Task<QuerySnapshot> userRef = db.collection("famosos").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            if(category.equals(document.getString("cat"))){
                                Famoso famoso = new Famoso();
                                famoso.setName(document.getString("Nombre"));
                                famoso.setDescription(document.getString("Descripcion"));
                                famoso.setCategory(document.getString("cat"));

                                famoso.setKey(document.getId());
                                //System.out.println(famoso.getKey());

                                //--------------------CARGAMOS IMG ----------------//
                                StorageReference storageRef = storage.getReference();

                                // Path
                                String path = "creadores/" + famoso.getKey() + "/profileImage.jpg" ;

                                StorageReference imgRef = storageRef.child(path);

                                Task<Uri> listResultTask = imgRef.getDownloadUrl();
                                listResultTask.addOnSuccessListener(uri -> {
                                    famoso.setImg(uri);

                                    listaFamoso.add(famoso);
                                    root = cargarVista(inflater,listaFamoso);
                                });
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al cargar la búsqueda",Toast.LENGTH_SHORT).show());
    }

    private View cargarVista(LayoutInflater inflater, ArrayList<Famoso> listaFamoso) {
        root = inflater.inflate(R.layout.fragment_category_filtrada, null, false);
        AdapterCategory adapter = new AdapterCategory(listaFamoso);
        recyclerCategory.setAdapter(adapter);

        return root;
    }
}