package com.example.koperasi.simpanwajib

import androidx.lifecycle.MutableLiveData
import com.example.koperasi.API.ApiConfig
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class SetoranWajibViewModel {
    private val _msg = MutableLiveData<String>()
    val msg : MutableLiveData<String> = _msg

    private val _isError = MutableLiveData<Boolean>()
    val isError : MutableLiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading

    fun bayarWajib(idanggota: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().simpanan(idanggota, "Wajib")
        client.enqueue(object: Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {

                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                _msg.value = t.message
            }
        })
    }
}