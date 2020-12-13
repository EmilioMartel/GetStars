package com.marquelo.getstars.menu_lobby_fragments.search.news;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.marquelo.getstars.R;
import com.marquelo.getstars.working.Famoso;
import com.marquelo.getstars.working.adapter.AdapterSearchNews;

import java.util.ArrayList;
import java.util.Objects;


public class NewsFragment extends Fragment {
    //------------ ATRIBUTOS FIREBASE-----------//
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //------------- ATRIBUTOS PROPIOS -----------//
    ArrayList<Famoso> listaFamoso;
    RecyclerView recyclerNews;
    View root;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaFamoso = new ArrayList<>();
        root = null;
        recyclerNews = null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerNews = root.findViewById(R.id.recyclerViewNews);
        recyclerNews.setLayoutManager(new GridLayoutManager(this.getContext(),1));

        llenarFamosos(inflater);
        AdapterSearchNews adapter = new AdapterSearchNews(listaFamoso);

        recyclerNews.setAdapter(adapter);

        return root;
    }

    private void llenarFamosos(LayoutInflater inflater) {
        /*------------------------- CARGAMOS INFO (TODO MENOS IMG)------------*/
        Task<QuerySnapshot> userRef = db.collection("famosos").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
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
                });
    }

    private View cargarVista(LayoutInflater inflater, ArrayList<Famoso> listaFamoso) {
        root = inflater.inflate(R.layout.fragment_home, null, false);
        AdapterSearchNews adapter = new AdapterSearchNews(listaFamoso);
        recyclerNews.setAdapter(adapter);

        return root;
    }
}