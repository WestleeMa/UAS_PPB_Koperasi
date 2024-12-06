package com.example.koperasi.preference

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PreferenceViewModel(private val preferences: OperasiPreference): ViewModel() {
    fun getID(): LiveData<String> {
        return preferences.getID().asLiveData()
    }
    fun getNama(): LiveData<String> {
        return preferences.getNama().asLiveData()
    }
    fun getRole(): LiveData<String> {
        return preferences.getRole().asLiveData()
    }
    fun setID(id:String){
        viewModelScope.launch {
            preferences.setID(id)
        }
    }
    fun setNama(nama:String){
        viewModelScope.launch {
            preferences.setNama(nama)
        }
    }
    fun setRole(role:String){
        viewModelScope.launch {
            preferences.setRole(role)
        }
    }
}