package com.marquelo.getstars


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.marquelo.getstars.ui.LobbyActivity
import com.marquelo.getstars.ui.LoginActivity


class MainActivity : AppCompatActivity() {

    private val authUser: FirebaseAuth by lazy { FirebaseAuth.getInstance() }


    override fun onCreate(savedInstanceState: Bundle?) {
        //SPLASH SCREEN CON EL LOGOTIPO
        Thread.sleep(2000)
        setTheme(R.style.AppTheme)

        //COMIENZO DEL PROGRAMA
        super.onCreate(savedInstanceState)
        goTo()

    }


    fun goTo() {
        if (authUser.currentUser != null) {
            val home = Intent(this, LobbyActivity::class.java)
            startActivity(home)
            finish()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

}