package com.marquelo.getstars.menu_lobby_fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.marquelo.getstars.R;
import com.marquelo.getstars.working.FirmaManual;
import com.marquelo.getstars.working.adapter.AdapterFirmaManual;

import java.util.ArrayList;
import java.util.Objects;


public class Firmas_Manual_Fragment extends Fragment {

    //------------ ATRIBUTOS FIREBASE-----------//
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    //------------- ATRIBUTOS PROPIOS -----------//
    ArrayList<FirmaManual> listaFirmaManual;
    RecyclerView recyclerFirmaManual;
    View root;

    //----------------- CONSTRUCTOR SIN PARAMETROS -----------------//
    public Firmas_Manual_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaFirmaManual = new ArrayList<>();
        root = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_manual_firmas, null, false);
        //------------------------  CREAMOS NUESTRO RECYCLER VIEW --------------------------//
        recyclerFirmaManual = root.findViewById(R.id.RecyclerId);
        recyclerFirmaManual.setLayoutManager(new GridLayoutManager(this.getContext(), 4));

        llenarFamosos(inflater);

        return root;
    }


    private void llenarFamosos(LayoutInflater inflater) {

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Path
        String path = "usuarios/" + Objects.requireNonNull(user).getEmail() +"/" + "AuthMan/";

        // Reference to manual autograph
        StorageReference imgRef = storageRef.child(path);



        Task<ListResult> listResultTask = imgRef.listAll();
        listResultTask.addOnCompleteListener(task -> {
            if(task.isSuccessful()){

                for(StorageReference item: Objects.requireNonNull(listResultTask.getResult()).getItems()){
                    item.getDownloadUrl().addOnSuccessListener(uri -> {
                        FirmaManual fm2 = new FirmaManual();
                        fm2.setUrl(uri);
                        listaFirmaManual.add(fm2);
                        root = cargarVista(inflater,listaFirmaManual);
                    });

                }

            }
        });
    }

    private View cargarVista(LayoutInflater inflater, ArrayList<FirmaManual> listaFirmaManual) {
        root = inflater.inflate(R.layout.fragment_manual_firmas, null, false);

        AdapterFirmaManual adapter = new AdapterFirmaManual(listaFirmaManual);
        recyclerFirmaManual.setAdapter(adapter);

        return root;
    }
}