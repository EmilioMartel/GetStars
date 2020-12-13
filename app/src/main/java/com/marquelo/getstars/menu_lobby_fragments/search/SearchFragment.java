package com.marquelo.getstars.menu_lobby_fragments.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.marquelo.getstars.R;

import java.util.Objects;


public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        //---------------------- SECCION CATEGORIA ------------------------------//
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        Button btnCategory = root.findViewById(R.id.btn_category);
        btnCategory.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.categoryFragment));

        //---------------------- SECCION DESTACADOS ------------------------//

        View btnFeatured = root.findViewById(R.id.btn_featured);
        btnFeatured.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.featuredFragment));

        //------------------------ SECCION POPULARES ---------------------//

        View btnPopular = root.findViewById(R.id.btn_popular);
        btnPopular.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.popularFragment));

        //------------------------ SECCION NOVEDADES ---------------------//

        View btnNews = root.findViewById(R.id.btn_news);
        btnNews.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.newsFragment));


        return root;
    }
}