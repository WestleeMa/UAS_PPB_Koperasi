package com.example.koperasi.admin.ui.pinjaman

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.koperasi.API.ApiConfig
import com.example.koperasi.API.response.ListItem
import com.example.koperasi.API.response.SimpanPinjamResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PinjamanViewModel : ViewModel() {
    private val _listSimpanPinjam = MutableLiveData<List<ListItem>?>()
    val listSimpanPinjam: MutableLiveData<List<ListItem>?> = _listSimpanPinjam

    private val _msg = MutableLiveData<String>()
    val msg : MutableLiveData<String> = _msg

    private val _isError = MutableLiveData<Boolean>()
    val isError : MutableLiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading

    private val _pinjaman = MutableLiveData<String>()
    val pinjaman : MutableLiveData<String> = _pinjaman

    fun getSimpanPinjam() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSimpanPinjam(null, "pinjam")
        client.enqueue(object : Callback<SimpanPinjamResponse> {
            override fun onResponse(
                call: Call<SimpanPinjamResponse>,
                response: Response<SimpanPinjamResponse>
            ) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if(responseBody != null){
                        _pinjaman.value = responseBody.total.toString()
                        _listSimpanPinjam.value = responseBody.list as List<ListItem>?
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

            override fun onFailure(call: Call<SimpanPinjamResponse>, t: Throwable) {
                _msg.value = t.message.toString()
            }

        })
    }
    fun deletePinjaman(idpinjam: Int?, tabel: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().deleteSimpanPinjam(idpinjam, tabel)
        client.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        msg.value = "Berhasil menghapus data Pinjaman"
                    }
                }else{
                    if(_isError.value == null){
                        _isError.value = true
                    }
                    _msg.value = (response.errorBody() as ResponseBody).string()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Error Delete Pinjaman View Model", t.toString())
                _isLoading.value = false
                _isError.value = true
                _msg.value = t.message
            }
        })
    }
    fun pelunasan(idpinjam: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().pelunasan(idpinjam)
        client.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    _isError.value = false
                    val responseBody = response.body()
                    if(responseBody !== null){
                        _msg.value = "Pelunasan Berhasil"
                    }
                }else{
                    _isError.value = true
                    _msg.value = (response.errorBody() as ResponseBody).string()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _msg.value = t.message.toString()
            }

        })
    }
}