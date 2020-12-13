package com.marquelo.getstars.menu_lobby_fragments.profile.famoso;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.marquelo.getstars.R;

public class CrearEventFragment extends Fragment {


    public CrearEventFragment() {
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
        View root = inflater.inflate(R.layout.fragment_crear_eventos, container, false);

        Button btnCrearSorteo = root.findViewById(R.id.btnCrearSorteo);
        Button btnCrearSubasta = root.findViewById(R.id.btnCrearSubasta);


        btnCrearSorteo.setOnClickListener(v -> startActivity(new Intent(getContext(),CrearSorteoActivity.class)));

        btnCrearSubasta.setOnClickListener(v -> startActivity(new Intent(getContext(),CrearSubastasActivity.class)));


        return root;
    }
}