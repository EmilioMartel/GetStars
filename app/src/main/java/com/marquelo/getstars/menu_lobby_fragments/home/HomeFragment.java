package com.marquelo.getstars.menu_lobby_fragments.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.marquelo.getstars.R;
import com.marquelo.getstars.ui.Carrito;
import com.marquelo.getstars.working.Famoso;
import com.marquelo.getstars.working.adapter.AdapterCardViewHome;


import java.util.ArrayList;
import java.util.Objects;


public class HomeFragment extends Fragment {
    //------------ ATRIBUTOS FIREBASE-----------//
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //------------- ATRIBUTOS PROPIOS -----------//
    ArrayList<Famoso> listaFamoso;
    RecyclerView recyclerHome;
    View root;
    MenuItem menuItem;
    private TextView badgeCounter;
    private int pendingItems = 0;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pendingItems = countItem();
        listaFamoso = new ArrayList<>();
        root = null;
        setHasOptionsMenu(true);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false);


        recyclerHome = root.findViewById(R.id.recyclerViewHome);
        recyclerHome.setLayoutManager(new GridLayoutManager(this.getContext(),1));

        //llenarImg(inflater);
        llenarFamosos(inflater,container);
        AdapterCardViewHome adapter = new AdapterCardViewHome(listaFamoso, getContext());
        recyclerHome.setAdapter(adapter);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.carrito_menu, menu);
        menuItem = menu.findItem(R.id.carrito_item);
        badgeCounter = root.findViewById(R.id.badge_counter);

        countItem();
        super.onCreateOptionsMenu(menu, inflater);
    }

    private int countItem(){
        Task<QuerySnapshot> itemRef = db.collection("usuarios")
                .document(Objects.requireNonNull(user.getEmail()))
                .collection("Carrito").get()
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()){
                        pendingItems = Objects.requireNonNull(task.getResult()).size();
                        System.out.println(pendingItems);
                        cargarMenu(pendingItems);
                    }
                });
        return pendingItems;
    }

    private void cargarMenu(int pendingItems) {
        if(pendingItems == 0) System.out.println(".");//menuItem.setActionView(null);
        else{
            menuItem.setActionView(R.layout.notification_badge);
            View view = menuItem.getActionView();
            badgeCounter = view.findViewById(R.id.badge_counter);
            badgeCounter.setText(String.valueOf(pendingItems));
            view.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), Carrito.class);
                startActivity(intent);
            });

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.carrito_item) {
            Intent intent = new Intent(getContext(), Carrito.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void llenarFamosos(LayoutInflater inflater, ViewGroup container) {
        /*------------------------- CARGAMOS INFO (TODO MENOS IMG)------------*/
        Task<QuerySnapshot> userRef = db.collection("famosos").get()
        .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int i = 0;
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    
                    Famoso famoso = new Famoso();
                    famoso.setName(document.getString("Nombre"));
                    famoso.setDescription(document.getString("Descripcion"));
                    famoso.setCategory(document.getString("cat"));
                    famoso.setPosition(i);
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
        AdapterCardViewHome adapter = new AdapterCardViewHome(listaFamoso,getContext());
        recyclerHome.setAdapter(adapter);

        return root;
    }

}