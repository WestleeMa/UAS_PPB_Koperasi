package com.example.koperasi.API.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("idanggota")
	val idanggota: Int? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("simpanan")
	val simpanan: Int? = null
)
