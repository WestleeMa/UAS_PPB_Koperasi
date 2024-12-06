package com.example.koperasi.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit

val Context.dataStore:DataStore<Preferences> by preferencesDataStore(name = "Koperasi")

class OperasiPreference private constructor(private val dataStore: DataStore<Preferences>){
    private val ID = stringPreferencesKey("id")
    private val NAMA = stringPreferencesKey("nama")
    private val ROLE = stringPreferencesKey("role")

    fun getID(): Flow<String>{
        return dataStore.data.map { preferences->
            preferences[ID]?:"id"
        }
    }
    fun getNama(): Flow<String>{
        return dataStore.data.map { preferences->
            preferences[NAMA]?:"nama"
        }
    }
    fun getRole(): Flow<String>{
        return dataStore.data.map { preferences->
            preferences[ROLE]?:"role"
        }
    }

    suspend fun setID(id:String){
        dataStore.edit { preferences->
            preferences[ID] = id
        }
    }
    suspend fun setNama(nama:String){
        dataStore.edit { preferences->
            preferences[NAMA] = nama
        }
    }
    suspend fun setRole(role:String){
        dataStore.edit { preferences->
            preferences[ROLE] = role
        }
    }
    companion object{
        @Volatile
        private var INSTANCE: OperasiPreference? = null

        fun getInstance(dataStore:DataStore<Preferences>):OperasiPreference{
            return INSTANCE ?: synchronized(this){
                val instance = OperasiPreference(dataStore)
                INSTANCE= instance
                instance
            }
        }
    }
}