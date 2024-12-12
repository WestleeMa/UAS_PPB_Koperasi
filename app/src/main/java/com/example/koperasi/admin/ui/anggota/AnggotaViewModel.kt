package com.example.koperasi.admin.ui.anggota

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.koperasi.API.ApiConfig
import com.example.koperasi.API.response.AnggotaResponseItem
import com.example.koperasi.API.response.AnggotaResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnggotaViewModel : ViewModel() {
    private val _listAnggota = MutableLiveData<List<AnggotaResponseItem>?>()
    val listAnggota: MutableLiveData<List<AnggotaResponseItem>?> = _listAnggota

    private val _msg = MutableLiveData<String>()
    val msg : MutableLiveData<String> = _msg

    private val _isError = MutableLiveData<Boolean>()
    val isError : MutableLiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading

    private val _simpanan = MutableLiveData<String>()
    val simpanan : MutableLiveData<String> = _simpanan

    fun getAnggota() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().anggota()
        client.enqueue(object : Callback<List<AnggotaResponseItem>> {
            override fun onResponse(
                call: Call<List<AnggotaResponseItem>>,
                response: Response<List<AnggotaResponseItem>>
            ) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if(responseBody != null){
                        _listAnggota.value = responseBody
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

            override fun onFailure(call: Call<List<AnggotaResponseItem>>, t: Throwable) {
                Log.d("gagal", t.toString())
                _msg.value = t.message.toString()
            }

        })
    }

    fun editAnggota(idanggota: Int, nama: String, simpanan: Int, role: String, email: String, password: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().editUser(idanggota, nama, simpanan, role, email, password )
        client.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        msg.value = "Berhasil mengubah data anggota"
                    }
                }else{
                    if(_isError.value == null){
                        _isError.value = true
                    }
                    _msg.value = (response.errorBody() as ResponseBody).string()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Error Anggota View Model", t.toString())
                _isLoading.value = false
                _isError.value = true
                _msg.value = t.message
            }
        })
    }

    fun deleteAnggota(idanggota: Int){
        _isLoading.value = true
        val client = ApiConfig.getApiService().deleteUser(idanggota)
        client.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        msg.value = "Berhasil menghapus anggota"
                    }
                }else{
                    if(_isError.value == null){
                        _isError.value = true
                    }
                    _msg.value = (response.errorBody() as ResponseBody).string()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Error Anggota View Model", t.toString())
                _isLoading.value = false
                _isError.value = true
                _msg.value = t.message
            }
        })
    }
}