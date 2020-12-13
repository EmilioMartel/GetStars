package com.marquelo.getstars.working;

import android.app.Application;
import android.util.Log;

import com.stripe.android.PaymentConfiguration;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_QepzQuDrjs6aE8xnKrpFaVs200mW5WR4Pn"
        );
        Log.i("TAG", "onCreate API");
    }
}