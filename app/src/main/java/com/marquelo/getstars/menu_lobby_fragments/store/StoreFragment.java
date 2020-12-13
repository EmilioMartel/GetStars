package com.marquelo.getstars.menu_lobby_fragments.store;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.marquelo.getstars.R;

import java.util.Objects;

public class StoreFragment extends Fragment {

    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_store, container, false);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        Button btnSubastas = root.findViewById(R.id.btnSubastas);
        btnSubastas.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.subastasFragment));

        Button btnSorteos = root.findViewById(R.id.btnSorteos);
        btnSorteos.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.sorteosFragment));

        Button btnLives = root.findViewById(R.id.btnLives);
        btnLives.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.livesFragment));

        return root;
    }
}