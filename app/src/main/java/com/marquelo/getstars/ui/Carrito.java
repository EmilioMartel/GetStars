package com.marquelo.getstars.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.marquelo.getstars.R;
import com.marquelo.getstars.working.ItemCarrito;
import com.marquelo.getstars.working.adapter.AdapterCarrito;
import com.marquelo.getstars.working.callbacks.MyItemTouchHelperCallback;
import com.marquelo.getstars.working.interfaces.CallBackItemTouch;

import java.util.ArrayList;
import java.util.Objects;

public class Carrito extends AppCompatActivity implements CallBackItemTouch {
    private static final String TAG ="TAG: " ;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //------------- ATRIBUTOS PROPIOS -----------//
    private RecyclerView recyclerCarrito;
    private AdapterCarrito adapter;
    private ConstraintLayout layout;
    private ArrayList<ItemCarrito> itemCarritoList;
    private TextView precio;
    private Button btnPagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_carrito);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Carrito");
        iniciarValores();
        llenarItemCarrito();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.go_home, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.itemHome) {
            startActivity(new Intent(this,LobbyActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void iniciarValores(){
        itemCarritoList = new ArrayList<>();
        recyclerCarrito = findViewById(R.id.racyclerCarrito);
        layout = findViewById(R.id.layout_carrito);
        precio = findViewById(R.id.txtValor);
        btnPagar = findViewById(R.id.btnPagar);

    }

    private void llenarItemCarrito() {
        db.collection("usuarios")
                .document(Objects.requireNonNull(user.getEmail()))
                .collection("Carrito").get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                            ItemCarrito item = new ItemCarrito();
                            item.setNombre(document.getString("nombre"));
                            item.setMotivo(document.getString("creador"));
                            item.setImagen(document.getString("img"));
                            item.setPrecio(document.getDouble("precio"));
                            item.setKey(document.getId());

                            itemCarritoList.add(item);
                            cargarVista(itemCarritoList);
                        }
                    }
                });

        btnPagar.setOnClickListener(v -> {
            if(precio.getText().equals("0.0")){
                Toast.makeText(this,"Lista vacía, debes agregar productos",Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(this, CheckoutActivityJava.class);
                intent.putExtra("carrito",itemCarritoList);
                intent.putExtra("precioTotal", Double.parseDouble(precio.getText().toString()));
                startActivity(intent);
            }
        });
    }

    private void calculaPrecioTotal(){
        double res = 0.0;
        for(ItemCarrito item : itemCarritoList){
            res += item.getPrecio();
        }
        precio.setText(String.valueOf(res));
    }

    private void cargarVista(ArrayList<ItemCarrito> e){
        adapter = new AdapterCarrito(e);
        recyclerCarrito.setLayoutManager(new LinearLayoutManager(this));
        recyclerCarrito.setAdapter(adapter);
        calculaPrecioTotal();
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerCarrito);
    }


    @Override
    public void itemTouchMode(int oldPosition, int newPosition) {
        itemCarritoList.add(newPosition, itemCarritoList.remove(oldPosition));
        adapter.notifyItemMoved(oldPosition, newPosition);
    }


    private void eliminarItemDB(String key){
        db.collection("usuarios")
                .document(Objects.requireNonNull(user.getEmail()))
                .collection("Carrito").document(key)
                .delete().addOnCompleteListener(task -> {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                }).addOnFailureListener(e ->
                        Log.d(TAG, "DocumentSnapshot successfully deleted!"));



    }

    private void reAgregarItemDB(ItemCarrito deletedItem, String key){
        db.collection("usuarios")
                .document(Objects.requireNonNull(user.getEmail()))
                .collection("Carrito").document(key).set(deletedItem).addOnCompleteListener(task -> {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                }).addOnFailureListener(e ->
                        Log.d(TAG, "DocumentSnapshot successfully deleted!"));

    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int position) {

        String nombre = itemCarritoList.get(viewHolder.getAdapterPosition()).getNombre();
        final ItemCarrito deletedItem = itemCarritoList.get(viewHolder.getAdapterPosition());

        final int deletedIndex = viewHolder.getAdapterPosition();
        final String deletedKey = itemCarritoList.get(viewHolder.getAdapterPosition()).getKey();

        adapter.removeItem(viewHolder.getAdapterPosition());
        itemCarritoList.remove(deletedItem);

        eliminarItemDB(deletedKey);
        calculaPrecioTotal();

        Snackbar snackbar = Snackbar.make(layout, nombre + "-> Eliminado", Snackbar.LENGTH_LONG);
        snackbar.setAction("CANCELAR/UNDO", v -> {
            adapter.restoreItem(deletedItem,deletedIndex);
            reAgregarItemDB(deletedItem,deletedKey);
            calculaPrecioTotal();
        });

        snackbar.setActionTextColor(Color.GREEN);
        snackbar.show();
    }
}