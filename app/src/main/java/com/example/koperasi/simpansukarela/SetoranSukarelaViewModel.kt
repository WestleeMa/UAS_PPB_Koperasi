package com.example.koperasi.simpansukarela

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.koperasi.API.ApiConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetoranSukarelaViewModel: ViewModel() {
    private val _msg = MutableLiveData<String>()
    val msg : MutableLiveData<String> = _msg

    private val _isError = MutableLiveData<Boolean>()
    val isError : MutableLiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading

    private val _jumlahakhir = MutableLiveData<Int>()
    val jumlahakhir : MutableLiveData<Int> = _jumlahakhir

    fun bayarSukarela(idanggota: String, jumlah: Int){
        _isLoading.value = true
        if(jumlah < 10000){
            _isError.value = true
            _msg.value = "Jumlah minimal Rp.10.000,-"
        }else{
            _isError.value = false
            _jumlahakhir.value = jumlah
        }
        val client = ApiConfig.getApiService().simpanan(idanggota, "Sukarela", jumlahakhir.value)
        client.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    Log.d("SetoranWajibViewModel", "berhasil")
                    _isError.value = false
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d("SetoranWajibViewModel", responseBody)
                        _msg.value = "Berhasil"
                    }
                }else{
                    if(_isError.value == null){
                        _isError.value = true
                    }
                    _msg.value = (response.errorBody() as ResponseBody).string()
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