package com.example.koperasi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.koperasi.databinding.ActivityLoginBinding
import com.example.koperasi.databinding.ActivityMainBinding
import com.example.koperasi.preference.OperasiPreference
import com.example.koperasi.preference.PreferenceViewModel
import com.example.koperasi.preference.ViewModelFactory
import com.example.koperasi.preference.dataStore
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val pref = OperasiPreference.getInstance(application.dataStore)
        val preferenceViewModel = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]
        preferenceViewModel.getNama().observe(this) {
            binding.greet.text = "Selamat Datang, $it"
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    true
                }
                R.id.navigation_simpan -> {
                    true
                }
                R.id.navigation_pinjam -> {
                    true
                }
                else -> false
            }
        }
    }
    companion object {
        private const val TAG = "MainActivity"
    }
}