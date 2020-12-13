package com.marquelo.getstars.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.marquelo.getstars.R;
import com.marquelo.getstars.working.CodigoRandom;
import com.marquelo.getstars.working.ItemCarrito;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardMultilineWidget;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class CheckoutActivityJava extends AppCompatActivity {
    // 10.0.2.2 is the Android emulator's alias to localhost
    //private static final String BACKEND_URL = "http://10.0.2.2:4242/";
    private static final String BACKEND_URL = "https://stripe-payment-android.herokuapp.com/";
    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;
    private static ArrayList<ItemCarrito> itemCarritoList;
    private Double precio;
    private EditText nombre, direccion, ciudad, telefono, email;
    public static String txtNombre, txtCiudad, txtTelefono, txtDireccion, txtEmail;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_checkout_java);
        setTitle("Finalizar compra");

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        iniciarValores();

        // Configure the SDK with your Stripe publishable key so it can make requests to Stripe
        stripe = new Stripe(
                getApplicationContext(),
                "pk_live_51HWnFdIx9ogp6GGIPAVTETO6ySGOGbcqMnCgx0a0fQHiS2NbUH5vnYm4Q9tVwrx8gTgN7J1UArcULTrgx29HgKDe00m8imEjMI"
        );
        startCheckout();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private void iniciarValores(){
        itemCarritoList = (ArrayList<ItemCarrito>) Objects.requireNonNull(getIntent().getExtras()).get("carrito");

        precio = Objects.requireNonNull(getIntent().getExtras()).getDouble("precioTotal");
        nombre = findViewById(R.id.nombreCompleto);
        ciudad = findViewById(R.id.txtCiudad);
        direccion = findViewById(R.id.direccion);
        telefono = findViewById(R.id.txtTelefono);
        email = findViewById(R.id.txtEmail);

    }
    private void startCheckout() {
        // Create a PaymentIntent by calling the server's endpoint.
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");


        /*String json = "{"
                + "\"currency\":\"usd\","
                + "\"items\":["
                + "{\"id\":\"photo_subscription\"}"
                + "]"
                + "}";*/

        double amount=precio*100;
        Map<String,Object> payMap=new HashMap<>();
        Map<String,Object> itemMap=new HashMap<>();
        List<Map<String,Object>> itemList =new ArrayList<>();
        payMap.put("currency","eur");
        ArrayList<String>listaProductos = new ArrayList<>();
        for(ItemCarrito i : itemCarritoList){
            listaProductos.add(i.getNombre());
        }
        itemMap.put("id",listaProductos);
        itemMap.put("amount",amount);
        itemList.add(itemMap);
        payMap.put("items",itemList);
        String json = new Gson().toJson(payMap);


        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(BACKEND_URL + "create-payment-intent")
                .post(body)
                .build();
        httpClient.newCall(request)
                .enqueue(new PayCallback(this));

        // Hook up the pay button to the card widget and stripe instance
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {

            try{
                txtNombre = nombre.getText().toString();
                txtCiudad = ciudad.getText().toString();
                txtTelefono = telefono.getText().toString();
                txtDireccion = direccion.getText().toString();
                txtEmail = email.getText().toString();

                if(!txtNombre.isEmpty()){
                    if(!txtDireccion.isEmpty()){
                        if(!txtCiudad.isEmpty()){
                            if(!txtTelefono.isEmpty()){
                                if(!txtEmail.isEmpty()){

                                    CardMultilineWidget cardInputWidget = findViewById(R.id.cardInputWidget);
                                    PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
                                    if (params != null) {
                                        ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                                                .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                                        stripe.confirmPayment(this, confirmParams);
                                    }
                                }else {
                                    Toast.makeText(this,"Rellene el email", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(this,"Rellene la Ciudad", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(this,"Rellene la Ciudad", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this,"Rellene la dirección", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Rellene el nombre", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    private static final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        private static final String TAG = "";
        @NonNull private final WeakReference<CheckoutActivityJava> activityRef;
        private FirebaseFirestore db = FirebaseFirestore.getInstance();
        private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        PaymentResultCallback(@NonNull CheckoutActivityJava activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                Map <String,String> map = new HashMap<>();
                map.put("nombre",txtNombre);
                map.put("direccion",txtDireccion);
                map.put("ciudad",txtCiudad);
                map.put("nTelefono",txtTelefono);
                map.put("email",txtEmail);

                db.collection("usuarios").document(Objects.requireNonNull(user.getEmail())).collection("Address").document().set(map);

                db.collection("usuarios").document(Objects.requireNonNull(user.getEmail())).collection("Carrito").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                String nombre = (String) document.get("nombre");

                                assert nombre != null;
                                if(nombre.equals("Foto firma")){
                                    db.collection("usuarios").document(Objects.requireNonNull(user.getEmail())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                Map<String,Object> compras = (Map<String, Object>) task.getResult().get("compras");
                                                assert compras != null;
                                                ArrayList<String> autFot =  (ArrayList<String>) compras.get("autFot");
                                                assert autFot != null;
                                                autFot.add((String) document.get("img"));
                                                compras.put("autFot",autFot);
                                                Map<String,Object> map1 = new HashMap<>();
                                                map1.put("compras",compras);
                                                db.collection("usuarios").document(Objects.requireNonNull(user.getEmail())).set(map1, SetOptions.merge());
                                            }
                                        }
                                    });

                                    db.collection("famosos").document((String) Objects.requireNonNull(document.get("key"))).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                Map <String,Object> ventas = (Map<String, Object>) task.getResult().get("ventas");
                                                assert ventas != null;
                                                Double nVentas = (Double) ventas.get("autFot");
                                                nVentas = nVentas+1;


                                                Map<String,Object> aux = new HashMap<>();
                                                aux.put("autFot",(double)nVentas);
                                                Map<String,Object> ventas1 = new HashMap<>();
                                                ventas1.put("ventas", aux);
                                                db.collection("famosos").document((String) Objects.requireNonNull(document.get("key"))).set(ventas1,SetOptions.merge());
                                            }
                                        }
                                    });
                                }


                                if(nombre.equals("Firma dedicada") || nombre.equals("Foto dedicada") || nombre.equals("Foto Personalizada") || nombre.equals("Live") ){
                                    Map<String,Object> revision = new HashMap<>();
                                    revision.put("email", user.getEmail());
                                    revision.put("key", document.get("key"));

                                    revision.put("mensaje", document.get("observaciones"));
                                    revision.put("articulo", document.get("nombre"));


                                    CodigoRandom codigoRandom = new CodigoRandom();
                                    String s = codigoRandom.generarCodigoRandom(20);

                                    db.collection("revision").document(s).set(revision);
                                    db.collection("usuarios").document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                ArrayList<String> revisionesPendientes = (ArrayList<String>) task.getResult().get("revisionesPendientes");
                                                assert revisionesPendientes != null;
                                                revisionesPendientes.add(s);

                                                Map<String,Object> map = new HashMap<>();
                                                map.put("revisionesPendientes",revisionesPendientes);
                                                db.collection("usuarios").document(user.getEmail()).set(map,SetOptions.merge());
                                            }
                                        }
                                    });

                                    if(nombre.equals("Firma dedicada")){
                                        db.collection("famosos").document((String) Objects.requireNonNull(revision.get("key"))).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    Map <String,Object> ventas = (Map<String, Object>) task.getResult().get("ventas");
                                                    assert ventas != null;
                                                    Double nVentas = (Double) ventas.get("autDed");
                                                    nVentas = nVentas+1;


                                                    Map<String,Object> aux = new HashMap<>();
                                                    aux.put("autDed",(double)nVentas);
                                                    Map<String,Object> ventas1 = new HashMap<>();
                                                    ventas1.put("ventas",aux);
                                                    db.collection("famosos").document((String) Objects.requireNonNull(revision.get("key"))).set(ventas1,SetOptions.merge());
                                                }
                                            }
                                        });
                                    }

                                    //TIENES QUE IMPLEMENTAR EL CONTADOR PARA LOS PRODUCTOS RESTANTES
                                    if(nombre.equals("Foto dedicada")){
                                        db.collection("famosos").document((String) Objects.requireNonNull(revision.get("key"))).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    Map <String,Object> ventas = (Map<String, Object>) task.getResult().get("ventas");
                                                    assert ventas != null;
                                                    Double nVentas = (Double) ventas.get("fotDed");
                                                    nVentas = nVentas+1;


                                                    Map<String,Object> aux = new HashMap<>();
                                                    aux.put("fotDed",(double)nVentas);
                                                    Map<String,Object> ventas1 = new HashMap<>();
                                                    ventas1.put("ventas",aux);
                                                    db.collection("famosos").document((String) Objects.requireNonNull(revision.get("key"))).set(ventas1,SetOptions.merge());
                                                }
                                            }
                                        });
                                    }
                                    if(nombre.equals("Foto Personalizada")){
                                        db.collection("famosos").document((String) Objects.requireNonNull(revision.get("key"))).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    Map <String,Object> ventas = (Map<String, Object>) task.getResult().get("ventas");
                                                    assert ventas != null;
                                                    Double nVentas = (Double) ventas.get("fotCus");
                                                    nVentas = nVentas+1;


                                                    Map<String,Object> aux = new HashMap<>();
                                                    aux.put("fotCus",(double)nVentas);
                                                    Map<String,Object> ventas1 = new HashMap<>();

                                                    ventas1.put("ventas",aux);
                                                    db.collection("famosos").document((String) Objects.requireNonNull(revision.get("key"))).set(ventas1,SetOptions.merge());
                                                }
                                            }
                                        });
                                    }

                                    if(nombre.equals("Live")){
                                        db.collection("famosos").document((String) Objects.requireNonNull(revision.get("key"))).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    Map <String,Object> ventas = (Map<String, Object>) task.getResult().get("ventas");
                                                    assert ventas != null;
                                                    Double nVentas = (Double) ventas.get("live");
                                                    nVentas = nVentas+1;


                                                    Map<String,Object> aux = new HashMap<>();
                                                    aux.put("live",(double)nVentas);
                                                    Map<String,Object> ventas1 = new HashMap<>();
                                                    ventas1.put("ventas",aux);
                                                    db.collection("famosos").document((String) Objects.requireNonNull(revision.get("key"))).set(ventas1,SetOptions.merge());
                                                }
                                            }
                                        });
                                        db.collection("lives").document((String) Objects.requireNonNull(revision.get("key"))).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    ArrayList<Object> lista = (ArrayList<Object>) task.getResult().get("listaParticipantes");

                                                    Map<String,String> map = new HashMap<>();
                                                    map.put(user.getEmail(),document.getString("observaciones"));
                                                    assert lista != null;
                                                    lista.add(map);

                                                    Map<String, Object> aux = new HashMap<>();
                                                    aux.put("listaParticipantes",lista);

                                                    db.collection("lives").document((String) Objects.requireNonNull(revision.get("key")) ).set(aux,SetOptions.merge());
                                                }
                                            }
                                        });
                                    }
                                }
                            }

                        }
                    }
                });
                 db.collection("usuarios").document(user.getEmail()).collection("Carrito").get().addOnSuccessListener(queryDocumentSnapshots -> {
                     for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                        String key = document.getId();
                         db.collection("usuarios").document(user.getEmail()).collection("Carrito").document(key).delete();
                     }
                 });

                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                activity.displayAlert(
                        "Payment completed",
                        gson.toJson(paymentIntent)

                );
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }
        @Override
        public void onError(@NonNull Exception e) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }

    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok",  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(),LobbyActivity.class));
            }
        });
        builder.create().show();

    }

    private static final class PayCallback implements Callback {
        @NonNull private final WeakReference<CheckoutActivityJava> activityRef;
        PayCallback(@NonNull CheckoutActivityJava activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error: " + e.toString(), Toast.LENGTH_LONG
                    ).show()
            );
        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG
                        ).show()
                );
            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }

    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");
    }
}