package com.example.koperasi.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.koperasi.API.ApiConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistViewModel : ViewModel() {
    private val _msg = MutableLiveData<String?>()
    val msg : MutableLiveData<String?> = _msg

    private val _isError = MutableLiveData<Boolean>()
    val isError : MutableLiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading

    fun regist(nama: String, email: String, password: String, confirmpass: String){
        _isLoading.value = true
        if(password == confirmpass){
            val client = ApiConfig.getApiService().regist(nama, email, password)
            client.enqueue(object: Callback<ResponseBody>{
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("aman", response.toString())
                    if(response.isSuccessful){
                        _isLoading.value = false
                        _isError.value = false
                        val responseBody = response.body()
                        if(responseBody !== null){
                            _msg.value = "Berhasil mendaftar, silahkan Login"
                        }
                    }else{
                        if(_isError.value == null){
                            _isError.value = true
                        }
                        _msg.value = (response.errorBody() as ResponseBody).string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("error", t.message.toString())
                    _isLoading.value = false
                    _msg.value = t.message
                }
            })
        }else{
            _isError.value = true
            _isLoading.value = false
            _msg.value = "Password tidak sesuai"
        }
    }
}