package com.marquelo.getstars.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.marquelo.getstars.R
import com.marquelo.getstars.pdf.LeerPrivatePolicy
import com.marquelo.getstars.pdf.LeerTermsConditions
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SignInActivity() : AppCompatActivity() {

    //Atributos
    private val db = FirebaseFirestore.getInstance()
    private var progressBar: ProgressBar? = null

    //---------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_sign_in)

        this.title = "Registrar"

        progressBar = findViewById(R.id.progressBar2)

        crearUsuario()

        showTermsConditions()
    }



    @SuppressLint("CutPasteId", "WrongViewCast")
    fun crearUsuario(){


        val btnFecha = findViewById<Button>(R.id.btnNacimiento)
        val fecha = findViewById<EditText>(R.id.fecha_nacimiento)
        fecha.isEnabled = false

        btnFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dia = calendar[Calendar.DAY_OF_MONTH]
            val mes = calendar[Calendar.MONTH]
            val anio = calendar[Calendar.YEAR]

            val datePickerDialog = DatePickerDialog(this,
                { view, year, month, dayOfMonth ->
                    if (dayOfMonth <= dia && (month + 1) <= (mes + 1) && year <= anio ||
                        dayOfMonth > dia && (month + 1) > (mes + 1) && year < anio ||
                        dayOfMonth > dia && (month + 1) < (mes + 1) && year < anio) {
                        val fechaPropuesta = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
                        fecha.setText(fechaPropuesta)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "FECHA INCORRECTA. Inserte una nueva",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, dia, mes, anio
            )
            datePickerDialog.show()
        }


        val btnRegistrar = findViewById<Button>(R.id.btn_registrar)
        btnRegistrar.setOnClickListener {
            try{
                val nombre: String = this.sign_in_fullname.text.toString().trim()
                val switch = findViewById<SwitchMaterial>(R.id.aceptarCondiciones)

                val rbHombre = findViewById<RadioButton>(R.id.rbHombre)
                val rbMujer = findViewById<RadioButton>(R.id.rbMujer)
                val rbOtro = findViewById<RadioButton>(R.id.rbOtro)

                var genero:String

                val email = this.sign_in_email.text.toString().trim()
                val password = findViewById<AppCompatEditText>(R.id.fecha_nacimiento)
                val fechaValida = fecha.text.toString()
                val invitationCode = findViewById<EditText>(R.id.invitationCode)
                val invitationCodeValue = invitationCode.text.toString().trim()
                //-----------------------------------------------------

                db.collection("keyInvitaciones").document("key").get().addOnCompleteListener {
                    if(it.isSuccessful){
                        val list:ArrayList<String> = it.result?.get("key") as ArrayList<String>
                        for(i in list){
                            if(invitationCodeValue.isNotEmpty() && i == invitationCodeValue){
                                if(nombre.isNotEmpty()){
                                    if(email.isNotEmpty()){
                                        if(fechaValida.isNotEmpty()){
                                            if(password.text?.isNotEmpty()!! && password.text!!.length > 6){
                                                if(switch.isChecked){
                                                    if(rbHombre.isChecked) {

                                                        genero = "Hombre"
                                                        setUserData(nombre, genero, email, fechaValida, password.toString(), invitationCodeValue)
                                                    }else if(rbMujer.isChecked){
                                                        genero = "Mujer"
                                                        setUserData(
                                                            nombre,
                                                            genero,
                                                            email,
                                                            fechaValida,
                                                            password.toString(),
                                                            invitationCodeValue
                                                        )

                                                    }else if(rbOtro.isChecked){
                                                        genero = "otro"
                                                        setUserData(
                                                            nombre,
                                                            genero,
                                                            email,
                                                            fechaValida,
                                                            password.toString(),
                                                            invitationCodeValue
                                                        )
                                                    }else{
                                                        Toast.makeText(this, "Seleccione su sexo", Toast.LENGTH_SHORT).show()
                                                    }
                                                }else{
                                                    Toast.makeText(this, "Lea y acepte las condiciones", Toast.LENGTH_SHORT).show()
                                                }
                                            }else{
                                                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                                            }
                                        }else{
                                            Toast.makeText(this, "Rellene la fecha", Toast.LENGTH_SHORT).show()
                                        }
                                    }else{
                                        Toast.makeText(this, "Rellene el email", Toast.LENGTH_SHORT).show()
                                    }
                                }else{
                                    Toast.makeText(this, "Rellene el nombre", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }

            }catch (e: NumberFormatException){
                Toast.makeText(
                    this,
                    "Asegurese de rellenar todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showTermsConditions(){
        val privatePolicy = findViewById<TextView>(R.id.txtPrivatePolicy)
        val termsConditions = findViewById<TextView>(R.id.txtTermsConditions)

        privatePolicy.setOnClickListener {
            val intent = Intent(this,LeerPrivatePolicy::class.java)
            startActivity(intent)
        }

        termsConditions.setOnClickListener {
            startActivity(Intent(this,LeerTermsConditions::class.java))
        }
    }


    private fun setUserData(
        nombre: String,
        sexo: String,
        email: String,
        fecha: String,
        password: String,
        invitationCodeValue: String
    ) {
        progressBar?.visibility= VISIBLE

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {

                if (it.isSuccessful) {
                    db.collection("keyInvitaciones").document("key").get().addOnCompleteListener {
                        if(it.isSuccessful){
                            val list:ArrayList<String> = it.result?.get("key") as ArrayList<String>

                            for(i in list){
                                if(invitationCodeValue.equals(i)){
                                    list.remove(i)
                                    val map = hashMapOf(
                                        "key" to list
                                    )
                                    db.collection("keyInvitaciones").document("key")
                                        .set(map, SetOptions.merge())
                                }
                            }
                        }
                    }

                    val aut = arrayListOf<String>()
                    val autFot = arrayListOf<String>()
                    val fot = arrayListOf<String>()
                    val fotDed = arrayListOf<String>()
                    val live = arrayListOf<String>()

                    val compras = hashMapOf(
                        "aut" to aut,
                        "autFot" to autFot,
                        "fot" to fot,
                        "fotDed" to fotDed,
                        "live" to live
                    )
                    val revisionesPen = arrayListOf<String>()

                    val userHashMap = hashMapOf(
                        "nombre" to nombre,
                        "sexo" to sexo,
                        "fecha" to fecha,
                        "isStar" to false,
                        "isPro" to false,
                        "compras" to compras,
                        "revisionesPendientes" to revisionesPen
                    )

                    val usuarios = db.collection("usuarios")
                    usuarios.document(email).set(userHashMap)

                    progressBar?.visibility = GONE
                    showlobby()
                }else{
                    if(it.exception is FirebaseAuthUserCollisionException){
                        showAlert()
                    }
                }

            }
    }

    private fun showlobby(){
        startActivity(Intent(this, LobbyActivity::class.java))
        finish()
    }
    //MUESTRA ERROR AL INICIAR SESION
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error de Registro")
        builder.setMessage("El correo propuesto ya se encuentra registrado. Pruebe otro.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}