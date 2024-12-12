package com.example.koperasi.admin

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
import com.example.koperasi.databinding.ActivityAdminBinding
import com.example.koperasi.login.LoginActivity
import com.example.koperasi.preference.OperasiPreference
import com.example.koperasi.preference.PreferenceViewModel
import com.example.koperasi.preference.ViewModelFactory
import com.example.koperasi.preference.dataStore
import com.example.koperasi.user.UserActivity

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val pref = OperasiPreference.getInstance(application.dataStore)
        val preferenceViewModel = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]
        val navController = findNavController(R.id.nav_host_fragment_activity_admin)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_anggota, R.id.navigation_simpanan, R.id.navigation_pinjaman
            )
        )
        navView.setupWithNavController(navController)
    }
}