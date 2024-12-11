package com.example.koperasi.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.koperasi.R
import com.example.koperasi.databinding.ActivityRegistBinding
import com.example.koperasi.login.LoginActivity
import com.example.koperasi.user.UserActivity

class RegistActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegistBinding
    private lateinit var registViewModel: RegistViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        registViewModel = ViewModelProvider(this)[RegistViewModel::class.java]
        binding.btnRegist.setOnClickListener{
            val nama = binding.edtUsn.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val confirmPass = binding.edtConfirmPass.text.toString()
            registViewModel.regist(nama, email, password, confirmPass)
        }
                registViewModel.msg.observe(this){msg->
                    if (msg != null) {
                        showToast(msg)
                        val intentLogin = Intent(this, LoginActivity::class.java)
                        startActivity(intentLogin)
                    }
                }

        binding.txtLink.setOnClickListener {
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
        }
    }
    private fun showToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}