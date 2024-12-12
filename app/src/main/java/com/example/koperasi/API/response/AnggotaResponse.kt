package com.example.koperasi.API.response

import com.google.gson.annotations.SerializedName

data class AnggotaResponse(

	@field:SerializedName("AnggotaResponse")
	val anggotaResponse: List<AnggotaResponseItem?>? = null
)

data class AnggotaResponseItem(

	@field:SerializedName("idanggota")
	val idanggota: Int? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("tglgabung")
	val tglgabung: String? = null,

	@field:SerializedName("simpanan")
	val simpanan: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)
