package com.marquelo.getstars.working.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.marquelo.getstars.menu_lobby_fragments.profile.ItemSalesFragment
import com.marquelo.getstars.menu_lobby_fragments.profile.Firmas_Manual_Fragment
import com.marquelo.getstars.menu_lobby_fragments.profile.ProfileFragment
import com.marquelo.getstars.menu_lobby_fragments.profile.famoso.CrearEventFragment

class ViewPagerFamousAdapter(fa: ProfileFragment): FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> CrearEventFragment()
            1-> Firmas_Manual_Fragment()
            2-> ItemSalesFragment()
            else -> CrearEventFragment()
        }
    }
}