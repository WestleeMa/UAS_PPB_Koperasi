package com.example.koperasi.API

import com.example.koperasi.API.response.ListItem
import com.example.koperasi.API.response.LoginResponse
import com.example.koperasi.API.response.SimpanPinjamResponse
import com.example.koperasi.API.response.WorldResponse
import okhttp3.ResponseBody
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

    @FormUrlEncoded
    @POST("/regist")
    fun regist(
        @Field("nama")nama:String,
        @Field("email")email:String,
        @Field("password")password:String
    ):Call<ResponseBody>

    @FormUrlEncoded
    @POST("/simpanpinjam")
    fun getSimpanPinjam(
        @Field("idanggota")idanggota:String,
        @Field("tbl")tbl:String
    ):Call<SimpanPinjamResponse>

    @FormUrlEncoded
    @POST("/simpan")
    fun simpanan(
        @Field("idanggota")idanggota:String,
        @Field("kategori")kategori:String,
        @Field("jumlah") jumlah: String? = null
    ):Call<String>

    @FormUrlEncoded
    @POST("/pinjam")
    fun pinjaman(
        @Field("idanggota")idanggota:String,
        @Field("jumlah") jumlah: String
    ):Call<ResponseBody>
}