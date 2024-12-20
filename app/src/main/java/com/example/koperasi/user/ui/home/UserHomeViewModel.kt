package com.example.koperasi.user.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.koperasi.API.ApiConfig
import com.example.koperasi.API.response.ListItem
import com.example.koperasi.API.response.SimpanPinjamResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserHomeViewModel : ViewModel() {
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

    fun getSimpanPinjam(idanggota:String, tbl:String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSimpanPinjam(idanggota, tbl)
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
                _msg.value = t.message.toString()
            }

        })
    }
}