package com.marquelo.getstars.ui

 import android.app.Activity
 import android.content.Intent
 import android.os.Bundle
 import android.view.View.*
 import android.view.WindowManager
 import android.widget.ProgressBar
 import android.widget.TextView
 import android.widget.Toast
 import androidx.appcompat.app.AlertDialog
 import androidx.appcompat.app.AppCompatActivity
 import com.firebase.ui.auth.AuthUI
 import com.google.firebase.auth.FirebaseAuth
 import com.google.firebase.firestore.FirebaseFirestore
 import com.marquelo.getstars.R
 import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object{
        internal const val RC_SIGN_IN = 423
        internal const val RC_SIGN_IN_EMAIL = 424
    }
    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        );
        setContentView(R.layout.activity_login)
        progressBar = findViewById(R.id.progressBar3)

        //comienzo del programa
        setup()
    }

    private fun setup(){

        // INICIAMOS SESION
        btn_iniciar_sesion.setOnClickListener{
            val email = this.emailLogin.text.toString().trim()
            val password = PasswordLogin.text.toString().trim()

            mailLogin(email, password)
        }

        // VAMOS AL MENU DE REGISTRO
        btn_to_registro.setOnClickListener{
            showSignIn()
        }

      /*  // ----------- INICIAR SESION CON GOOGLE ------//
        btn_google_login.setOnClickListener {
            googleLogin()
        }*/

        val reestablecerPassword = findViewById<TextView>(R.id.txtReestablecerPassword)

        reestablecerPassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

    }

    //----------------- INICIO DE SESION -------------------//
    fun mailLogin(emailParam: String, passwordParam: String){

        val email = emailParam
        val password = passwordParam


        if (email.isNotEmpty() && password.isNotEmpty()) {
            progressBar?.visibility = VISIBLE
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        progressBar?.visibility = GONE
                        showLobby()
                    }else{
                        showAlert()
                    }
                }
        } else {
            progressBar?.visibility = INVISIBLE
            showAlert()
        }
    }
    // --------------------- GOOGLE -----------------------------//
    /*fun googleLogin(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )


        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build(),
            RC_SIGN_IN
        )

    }
    // ------------------------- METODOS SECUNDARIOS OVERRIDE ----------------------------------//
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try{
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                //val response = IdpResponse.fromResultIntent(data)

                if (resultCode == Activity.RESULT_OK) {
                    val user = FirebaseAuth.getInstance().currentUser
                    //Si logeamos con email buscamos el name en la bd
                    Toast.makeText(this, "Bienvenido ${user!!.displayName}", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this, LobbyActivity::class.java))
                    finish()

                }
            }
            if (requestCode == RC_SIGN_IN_EMAIL) {
                //val response = IdpResponse.fromResultIntent(data)

                if (resultCode == Activity.RESULT_OK) {
                    val user = FirebaseAuth.getInstance().currentUser

                    Toast.makeText(this, "Bienvenido ${user!!.displayName}", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this, LobbyActivity::class.java))
                    finish()

                }
            }

        }catch (e: KotlinNullPointerException){
            //Sign in failed. If response is null the user canceled the
            //sign-in flow using the back button. Otherwise check
            //response.getError().getErrorCode() and handle the error.
            Toast.makeText(
                this, "Ocurrió un error en el inicio de sesión. Intentelo de nuevo.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }*/


    //--------------------------- METODOS AUXILIARES ------------------------------//
    private fun showSignIn(){
        startActivity(Intent(this, SignInActivity::class.java))
    }

    private fun showLobby(){
        val home = Intent(this, LobbyActivity::class.java)
        startActivity(home)
        finish()
    }

    //MUESTRA ERROR AL INICIAR SESION
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}