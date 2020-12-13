package com.marquelo.getstars.ui

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.marquelo.getstars.R


class LobbyActivity : AppCompatActivity() {
    //ATRIBUTOS
    lateinit var navControllerSubMenus:NavController
    lateinit var mAdView : AdView
    //METODOS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_lobby)

        // Initialize the Mobile Ads SDK with an AdMob App ID.
        MobileAds.initialize(this, "ca-app-pub-2307684125945843~6629302622")

        val adRequest = AdRequest.Builder().build()

        mAdView = findViewById(R.id.adView)
        mAdView.loadAd(adRequest)
        setup()

        startActivity(intent)
    }

    // Called when leaving the activity
    public override fun onPause() {
        mAdView.pause()
        super.onPause()
    }

    // Called when returning to the activity
    public override fun onResume() {
        super.onResume()
        mAdView.resume()
    }

    // Called before the activity is destroyed
    public override fun onDestroy() {
        mAdView.destroy()
        super.onDestroy()
    }


    fun setup() {
        buttonNavegation()
    }

    override fun onBackPressed() {
        var count = supportFragmentManager.backStackEntryCount
        if(count==0){
            super.onBackPressed()
        }else{
            supportFragmentManager.popBackStack()
        }
    }

    private fun buttonNavegation(){
        // NAVEGACIÓN ENTRE LOS MENUS PRINCIPALES
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.itemIconTintList = null


        val navController = findNavController(R.id.fragmentPrincipal)
        navControllerSubMenus = Navigation.findNavController(this, R.id.fragmentPrincipal);

        setupActionBarWithNavController(this, navControllerSubMenus);
        bottomNavigationView.setupWithNavController(navController)

        //NAVEGACIÓN PARA IR A HACER LA FIRMA MANUAL
        val btnFirma: BottomNavigationItemView = this.findViewById(R.id.autographActivity)

        btnFirma.setOnClickListener {
            this.startActivity(Intent(this, AutographActivity::class.java))
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        return navControllerSubMenus.navigateUp()
    }

}