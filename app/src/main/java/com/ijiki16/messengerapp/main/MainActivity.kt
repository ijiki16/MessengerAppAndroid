package com.ijiki16.messengerapp.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ijiki16.messengerapp.R
import com.ijiki16.messengerapp.databinding.ActivityMainBinding
import com.ijiki16.messengerapp.main.home.view.HomeFragment
import com.ijiki16.messengerapp.main.profile.view.ProfileFragment
import com.ijiki16.messengerapp.users.view.UsersActivity
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        showFragment(HomeFragment.newInstance())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    showFragment(HomeFragment.newInstance())
                    true
                }
                R.id.profile -> {
                    showFragment(ProfileFragment.newInstance())
                    true
                }
                else -> throw Exception("Unknown item")
            }
        }

        binding.fab.setOnClickListener {
            showUsersActivity()
        }

        setContentView(binding.root)
    }

    private fun showUsersActivity() {
        val myIntent = Intent(this@MainActivity, UsersActivity::class.java)
        this@MainActivity.startActivity(myIntent)
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }

}