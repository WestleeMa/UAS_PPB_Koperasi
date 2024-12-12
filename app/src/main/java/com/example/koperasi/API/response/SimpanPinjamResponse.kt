package com.example.koperasi.API.response

import com.google.gson.annotations.SerializedName

data class SimpanPinjamResponse(

	@field:SerializedName("Total")
	val total: Int? = null,

	@field:SerializedName("List")
	val list: List<ListItem?>? = null
)

data class ListItem(

	@field:SerializedName("idanggota")
	val idanggota: Int? = null,

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("idsimpan")
	val idsimpan: Int? = null,

	@field:SerializedName("idpinjam")
	val idpinjam: Int? = null,

	@field:SerializedName("jumlah")
	val jumlah: Int? = null,

	@field:SerializedName("tglsetor")
	val tglsetor: String? = null,

	@field:SerializedName("tglpinjam")
	val tglpinjam: String? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,
)
