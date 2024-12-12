package com.example.koperasi.admin.ui.simpanan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.koperasi.API.response.ListItem
import com.example.koperasi.databinding.ItemCardSimpananBinding
import java.text.NumberFormat
import java.util.Locale

class SimpananCardAdapter(
    private val onDeleteClick: (Int?) -> Unit // Tambahkan callback untuk delete
) : ListAdapter<ListItem, SimpananCardAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(val binding: ItemCardSimpananBinding) : RecyclerView.ViewHolder(binding.root) {
        fun formatRupiah(number: Int): String {
            val localeID = Locale("in", "ID")
            val formatter = NumberFormat.getCurrencyInstance(localeID)
            val formattedString = formatter.format(number).replace("Rp", "Rp.").replace(",00", "")
            return "$formattedString,-"
        }

        fun bind(formr: ListItem, onDeleteClick: (Int?) -> Unit) {
            binding.txtKategori.text = formr.kategori
            binding.txtTanggal.text = formr.tglsetor?.split("T")?.get(0) ?: "Tanggal Tidak Tersedia"
            binding.txtJumlah.text = formr.jumlah?.let { formatRupiah(it) }
            binding.txtKeterangan.text = formr.keterangan
            binding.txtNamaUser.text = "Nama Anggota: ${formr.nama}"
            binding.txtIDUser.text = "ID Anggota: ${formr.idanggota}"

            binding.btnDelete.setOnClickListener {
                onDeleteClick(formr.idsimpan)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemCardSimpananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val formr = getItem(position)
        holder.bind(formr, onDeleteClick) // Kirim callback delete ke bind
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                return oldItem.idanggota == newItem.idanggota
            }

            override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
