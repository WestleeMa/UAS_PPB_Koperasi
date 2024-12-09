package com.example.koperasi.API.response

import com.google.gson.annotations.SerializedName

data class SimpanPinjamResponse(

	@field:SerializedName("SimpanPinjamResponse")
	val simpanPinjamResponse: List<SimpanPinjamResponseItem?>? = null
)

data class SimpanPinjamResponseItem(

	@field:SerializedName("idanggota")
	val idanggota: Int? = null,

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("idsimpan")
	val idsimpan: Int? = null,

	@field:SerializedName("jumlah")
	val jumlah: Int? = null,

	@field:SerializedName("tglsetor")
	val tglsetor: String? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null
)
