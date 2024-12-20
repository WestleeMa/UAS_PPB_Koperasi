package com.example.koperasi.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.koperasi.R
import com.example.koperasi.databinding.ActivityUserBinding
import com.example.koperasi.login.LoginActivity
import com.example.koperasi.preference.OperasiPreference
import com.example.koperasi.preference.PreferenceViewModel
import com.example.koperasi.preference.ViewModelFactory
import com.example.koperasi.preference.dataStore

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_user)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_simpan, R.id.navigation_pinjam
            )
        )
        navView.setupWithNavController(navController)

        val pref = OperasiPreference.getInstance(application.dataStore)
        val preferenceViewModel = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]

        preferenceViewModel.getID().observe(this){id ->
            Log.d("id", id.toString())
            if(id == null){
                val intentMain = Intent(this, LoginActivity::class.java)
                startActivity(intentMain)
            }
        }
    }
}