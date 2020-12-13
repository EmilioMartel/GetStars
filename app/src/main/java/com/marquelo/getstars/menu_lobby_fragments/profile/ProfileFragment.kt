package com.marquelo.getstars.menu_lobby_fragments.profile;

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.marquelo.getstars.R
import com.marquelo.getstars.R.layout.fragment_profile
import com.marquelo.getstars.menu_lobby_fragments.profile.famoso.CrearEventFragment
import com.marquelo.getstars.ui.SettingsActivity

import com.marquelo.getstars.working.adapter.ViewPagerAdapter
import com.marquelo.getstars.working.adapter.ViewPagerFamousAdapter
import java.util.*


class ProfileFragment : Fragment() {
    //ATRIBUTOS
    lateinit var tabLayout: TabLayout
    lateinit var firmasViewFragment:ViewPager2
    val adapter by lazy { ViewPagerAdapter(this) }
    val adapterFamous by lazy { ViewPagerFamousAdapter(this) }
    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Objects.requireNonNull((requireActivity() as AppCompatActivity).supportActionBar)!!.show()
        Objects.requireNonNull((requireActivity() as AppCompatActivity).supportActionBar)!!.setDisplayHomeAsUpEnabled(false)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(fragment_profile, container, false)
        getAllUserDocuments(root)
        if (user != null) {
            db.collection("usuarios").document(user.email.toString()).get().addOnSuccessListener {

                val isStar = it.getBoolean("isStar")

                if(isStar!!){

                    // ----------------- NAVEGACIÓN ENTRE LOS MENUS PRINCIPALES ------------------//
                    firmasViewFragment = root.findViewById(R.id.firmasViewFragmentProfile)
                    firmasViewFragment.adapter = adapterFamous

                    tabLayout = root.findViewById(R.id.tab_layout_profile)
                    val tabLayoutMediator = TabLayoutMediator(
                        tabLayout, firmasViewFragment
                    ) { tab, position ->
                        when (position) {
                            0 -> {
                                CrearEventFragment()
                                tab.text = "Crear Evento"
                            }

                            1 -> {
                                Firmas_Manual_Fragment()
                                tab.text = "Firma Manual"
                            }
                            2 -> {
                                ItemSalesFragment()
                                tab.text = "Compras"
                            }
                        }
                    }

                    tabLayoutMediator.attach()
                }else{

                    // ----------------- NAVEGACIÓN ENTRE LOS MENUS PRINCIPALES ------------------//
                    firmasViewFragment = root.findViewById(R.id.firmasViewFragmentProfile)
                    firmasViewFragment.adapter = adapter

                    tabLayout = root.findViewById(R.id.tab_layout_profile)
                    val tabLayoutMediator = TabLayoutMediator(
                        tabLayout, firmasViewFragment
                    ) { tab, position ->
                        when (position) {
                            0 -> {
                                Firmas_Manual_Fragment()
                                tab.text = "Firma Manual"
                            }
                            1 -> {
                                ItemSalesFragment()
                                tab.text = "Compras"
                            }
                        }
                    }
                    tabLayoutMediator.attach()
                }
            }
        }
        //-------------------------------------------------------------------------------//
        return root
    }

    //---------------- INFLAMOS EL MENU --------------------//
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings_item) {
            val intentLoadNewActivity = Intent(activity, SettingsActivity::class.java)
            startActivity(intentLoadNewActivity)
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    //------------------- OBTENEMOS LA INFO DEL USUARIO (FOTO, NOMBRE, ETC) ------------------//
   fun getAllUserDocuments(root: View) {
        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val myname = root.findViewById<TextView>(R.id.username_title)
        val myEmail = root.findViewById<TextView>(R.id.txtEmail)
        //val myPhotoUrl = root.findViewById<ImageView>(R.id.photoProfile)

        //Si logeamos con email buscamos el name en la bd
        db.collection("usuarios").document(user?.email.toString()).get().addOnSuccessListener { documento->
            if(documento.exists()){
                val nombre = documento.getString("nombre")
                val email = user?.email
                myname.text = "$nombre"
                myEmail.text = "$email"
            }
        }
      /*  //En caso contrario cargarmos el user.name del proveedor correspondiente
        user!!.let {
            for (profile in it.providerData) {
                val photoUrl = profile.photoUrl
                myname.text = user.displayName
                val myEmail = root.findViewById<TextView>(R.id.txtEmail)
                myEmail.text = user.email
                Picasso.get().load(photoUrl).into(myPhotoUrl)

            }
        }*/

    }
}
