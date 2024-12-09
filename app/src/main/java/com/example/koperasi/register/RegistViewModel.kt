package com.example.koperasi.register

import androidx.lifecycle.MutableLiveData

class RegistViewModel {
    private val _msg = MutableLiveData<String>()
    val msg : MutableLiveData<String> = _msg

    private val _isError = MutableLiveData<Boolean>()
    val isError : MutableLiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading

    private val _email = MutableLiveData<String?>()
    val email : MutableLiveData<String?> = _email

    private val _nama = MutableLiveData<String?>()
    val nama : MutableLiveData<String?> = _nama

    private val _password = MutableLiveData<String?>()
    val password : MutableLiveData<String?> = _password

}