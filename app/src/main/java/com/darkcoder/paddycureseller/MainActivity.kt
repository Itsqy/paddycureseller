package com.darkcoder.paddycureseller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.darkcoder.paddycureseller.databinding.ActivityMainBinding
import com.darkcoder.paddycureseller.ui.home.HomeFragment
import com.darkcoder.paddycureseller.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val homeMenu: Fragment = HomeFragment()

    val fm: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        setButtomNavWithConfigChanges()
    }

    private fun setButtomNavWithConfigChanges() {

        fm.beginTransaction().add(R.id.fragment_container, homeMenu).show(homeMenu).commit()
        binding.btnBottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.meHome -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()


                    return@setOnNavigationItemSelectedListener true
                }

                R.id.meProfile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ProfileFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }

                else -> return@setOnNavigationItemSelectedListener false
            }
        }


    }
}