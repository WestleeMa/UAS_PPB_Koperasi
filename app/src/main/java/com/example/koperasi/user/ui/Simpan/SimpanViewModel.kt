package com.example.koperasi.user.ui.Simpan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SimpanViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is simpanan Fragment"
    }
    val text: LiveData<String> = _text
}