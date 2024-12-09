package com.example.koperasi.user.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.koperasi.API.response.SimpanPinjamResponseItem
import com.example.koperasi.databinding.ItemCardBinding

class CardAdapter(private var data: List<SimpanPinjamResponseItem?>) :
    RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SimpanPinjamResponseItem) {
            binding.txtKategori.text = item.kategori ?: "Tidak ada kategori"
            binding.txtTanggal.text = item.tglsetor ?: "Tidak ada tanggal"
            binding.txtJumlah.text = "Rp. ${item.jumlah?.toString() ?: "0"},-"
            binding.txtKeterangan.text = item.keterangan ?: "Tidak ada keterangan"
        }
    }
    fun updateData(newData: List<SimpanPinjamResponseItem?>) {
        data = newData
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data[position]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = data.size
}
