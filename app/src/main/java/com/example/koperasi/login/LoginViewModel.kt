package com.example.koperasi.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.koperasi.API.ApiConfig
import com.example.koperasi.API.response.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val _msg = MutableLiveData<String>()
    val msg : MutableLiveData<String> = _msg

    private val _isError = MutableLiveData<Boolean>()
    val isError : MutableLiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading

    private val _idUser = MutableLiveData<String?>()
    val idUser : MutableLiveData<String?> = _idUser

    private val _nama = MutableLiveData<String?>()
    val nama : MutableLiveData<String?> = _nama

    private val _role = MutableLiveData<String?>()
    val role : MutableLiveData<String?> = _role

    fun login(email: String, password: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object:Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if(responseBody != null){
                        _idUser.value = responseBody.idanggota.toString()
                        _nama.value = responseBody.nama
                        _role.value = responseBody.role
                        _isError.value = false
                    }else{
                        if(_isError.value == null){
                            _isError.value = true
                        }
                        _msg.value = "error: response is empty"
                    }
                }else{
                    if(_isError.value == null){
                        _isError.value = true
                    }
                    _msg.value = (response.errorBody() as ResponseBody).string()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _msg.value = t.message
            }

        })
    }
}