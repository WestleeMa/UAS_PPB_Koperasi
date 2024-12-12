package com.example.koperasi.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.koperasi.R
import com.example.koperasi.admin.AdminActivity
import com.example.koperasi.register.RegistActivity
import com.example.koperasi.databinding.ActivityLoginBinding
import com.example.koperasi.preference.OperasiPreference
import com.example.koperasi.preference.PreferenceViewModel
import com.example.koperasi.preference.ViewModelFactory
import com.example.koperasi.preference.dataStore
import com.example.koperasi.user.UserActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.txtLink.setOnClickListener {
            val intentRegist = Intent(this, RegistActivity::class.java)
            startActivity(intentRegist)
        }
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        val pref = OperasiPreference.getInstance(application.dataStore)
        val preferenceViewModel = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]

        binding.btnLogin.setOnClickListener{
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            loginViewModel.login(email, password)
        }
        loginViewModel.isError.observe(this){error->
            if(!error){
                loginViewModel.idUser.observe(this){idUser->
                    if (idUser != null) {
                        preferenceViewModel.setID(idUser)
                    }
                }
                loginViewModel.nama.observe(this){nama->
                    if (nama != null) {
                        preferenceViewModel.setNama(nama)
                    }
                }
                loginViewModel.role.observe(this){role->
                    if (role != null) {
                        preferenceViewModel.setRole(role)
                        if(role == "pengurus"){
                            val intentAdmin = Intent(this, AdminActivity::class.java)
                            startActivity(intentAdmin)
                        }else if(role == "anggota"){
                            val intentUser = Intent(this, UserActivity::class.java)
                            startActivity(intentUser)
                        }
                    }
                }
            }else{
                loginViewModel.msg.observe(this){msg->
                    showToast(msg)
                }
            }
        }
        preferenceViewModel.getRole().observe(this){role ->
            Log.d("dataStore", role.toString())
            if(role !== null){
                if(role == "pengurus"){
                    val intentAdmin = Intent(this, AdminActivity::class.java)
                    startActivity(intentAdmin)
                }else if(role == "anggota"){
                    val intentUser = Intent(this, UserActivity::class.java)
                    startActivity(intentUser)
                }
            }
        }
    }

    private fun showToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}