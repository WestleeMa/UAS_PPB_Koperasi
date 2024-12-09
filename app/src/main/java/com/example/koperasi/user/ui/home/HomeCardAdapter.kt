package com.example.koperasi.user.ui.home

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.koperasi.API.response.ListItem
import com.example.koperasi.R
import com.example.koperasi.databinding.ItemCardBinding
import java.text.NumberFormat
import java.time.ZonedDateTime
import java.util.Locale

class HomeCardAdapter : ListAdapter<ListItem, HomeCardAdapter.ListViewHolder>(DIFF_CALLBACK) {
    class ListViewHolder(val binding: ItemCardBinding):RecyclerView.ViewHolder(binding.root){
        fun formatRupiah(number: Int): String {
            val localeID = Locale("in", "ID") // Locale untuk Indonesia
            val formatter = NumberFormat.getCurrencyInstance(localeID)
            val formattedString = formatter.format(number).replace("Rp", "Rp.").replace(",00", "") // Format dan sesuaikan
            return "$formattedString,-"
        }
        fun bind(formr: ListItem){
            binding.txtKategori.text = formr.kategori
            binding.txtTanggal.text = formr.tglsetor?.split("T")?.get(0) ?: "Tanggal Tidak Tersedia"
            binding.txtJumlah.text = formr.jumlah?.let { formatRupiah(it) }
            binding.txtKeterangan.text = formr.keterangan
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeCardAdapter.ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val formr = getItem(position)
        holder.bind(formr)
    }

    companion object{
        const val TAG = "formuseradapter"
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<ListItem>(){
            override fun areItemsTheSame(
                oldItem: ListItem,
                newItem: ListItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListItem,
                newItem: ListItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
