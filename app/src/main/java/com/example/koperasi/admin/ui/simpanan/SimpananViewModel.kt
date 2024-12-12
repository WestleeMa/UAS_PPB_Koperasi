package com.example.koperasi.admin.ui.simpanan

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

class SimpananViewModel : ViewModel() {
    private val _listSimpanPinjam = MutableLiveData<List<ListItem>?>()
    val listSimpanPinjam: MutableLiveData<List<ListItem>?> = _listSimpanPinjam

    private val _msg = MutableLiveData<String>()
    val msg : MutableLiveData<String> = _msg

    private val _isError = MutableLiveData<Boolean>()
    val isError : MutableLiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading

    private val _simpanan = MutableLiveData<String>()
    val simpanan : MutableLiveData<String> = _simpanan

    fun getSimpanPinjam() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSimpanPinjam(null, "simpan")
        client.enqueue(object : Callback<SimpanPinjamResponse> {
            override fun onResponse(
                call: Call<SimpanPinjamResponse>,
                response: Response<SimpanPinjamResponse>
            ) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if(responseBody != null){
                        _simpanan.value = responseBody.total.toString()
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
                Log.e("Error Simpan Pinjam View Model", t.toString())
                _isError.value = true
                _msg.value = t.message.toString()
            }

        })
    }
    fun deleteSimpanan(idsimpan: Int?, tabel: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().deleteSimpanPinjam(idsimpan, tabel)
        client.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        msg.value = "Berhasil menghapus data simpanan"
                    }
                }else{
                    if(_isError.value == null){
                        _isError.value = true
                    }
                    _msg.value = (response.errorBody() as ResponseBody).string()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Error Delete Simpanan View Model", t.toString())
                _isLoading.value = false
                _isError.value = true
                _msg.value = t.message
            }
        })
    }
}