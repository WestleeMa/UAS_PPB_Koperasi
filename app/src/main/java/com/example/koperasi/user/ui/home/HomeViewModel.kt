package com.example.koperasi.user.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.koperasi.API.ApiConfig
import com.example.koperasi.API.response.SimpanPinjamResponse
import com.example.koperasi.API.response.SimpanPinjamResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _listSimpanPinjam = MutableLiveData<List<SimpanPinjamResponseItem>?>()
    val listSimpanPinjam: MutableLiveData<List<SimpanPinjamResponseItem>?> = _listSimpanPinjam
    private val _msg = MutableLiveData<String>()
    val msg : MutableLiveData<String> = _msg

    private val _isError = MutableLiveData<Boolean>()
    val isError : MutableLiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading

    fun getSimpanPinjam(idanggota:String, tbl:String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSimpanPinjam(idanggota, tbl)
        client.enqueue(object : Callback<SimpanPinjamResponse> {
            override fun onResponse(
                call: Call<SimpanPinjamResponse>,
                response: Response<SimpanPinjamResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if(responseBody != null){
                        _listSimpanPinjam.value = responseBody.simpanPinjamResponse as List<SimpanPinjamResponseItem>?
                        _isError.value = false
                    } else{
                        _isError.value = true
                        _msg.value = "Tidak ada data"
                    }
                }else{
                    _isLoading.value = false
                    _isError.value = true
                    _msg.value = response.message()
                }
            }

            override fun onFailure(call: Call<SimpanPinjamResponse>, t: Throwable) {
                _isError.value = true
                _msg.value = "gagal"
            }
        })
    }
}