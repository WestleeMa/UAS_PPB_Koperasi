package com.example.koperasi.API

import com.example.koperasi.API.response.LoginResponse
import com.example.koperasi.API.response.WorldResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/")
    fun getWorld():Call<WorldResponse>

    @FormUrlEncoded
    @POST("/login")
    fun login(
        @Field("email")email:String,
        @Field("password")password:String
    ):Call<LoginResponse>
}