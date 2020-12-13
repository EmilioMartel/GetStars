package com.marquelo.getstars.menu_lobby_fragments.search.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.marquelo.getstars.R;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryFragment extends Fragment {
    //------------ ATRIBUTOS FIREBASE-----------//
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //------------- ATRIBUTOS PROPIOS -----------//
    private ArrayList<String> listaCategory;
    private ListView listView;
    private View root;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listaCategory = new ArrayList<>();
        root = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_category, container, false);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        listView = root.findViewById(R.id.listViewId);
        listaCategory = new ArrayList<>();

        listaCategory.add("deportes");
        listaCategory.add("cine");
        listaCategory.add("television");
        listaCategory.add("influencer");

        ArrayAdapter adapter = new ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1, listaCategory);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("categoria",parent.getItemAtPosition(position).toString());
                getParentFragmentManager().setFragmentResult("key",bundle);
                Navigation.findNavController(view).navigate(R.id.categoryFiltradaFragment);

            }
        });

        return root;
    }


}