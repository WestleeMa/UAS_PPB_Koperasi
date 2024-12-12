package com.example.koperasi.admin.ui.pinjaman

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.koperasi.API.response.ListItem
import com.example.koperasi.databinding.ItemCardPinjamanAdminBinding
import java.text.NumberFormat
import java.util.Locale

class PinjamanCardAdapter(
    private val onDeleteClick: (Int?) -> Unit,
    private val onPelunasanClick: (Int?) -> Unit
) : ListAdapter<ListItem, PinjamanCardAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(val binding: ItemCardPinjamanAdminBinding) : RecyclerView.ViewHolder(binding.root) {
        fun formatRupiah(number: Int): String {
            val localeID = Locale("in", "ID")
            val formatter = NumberFormat.getCurrencyInstance(localeID)
            val formattedString = formatter.format(number).replace("Rp", "Rp.").replace(",00", "")
            return "$formattedString,-"
        }

        fun bind(formr: ListItem, onDeleteClick: (Int?) -> Unit, onPelunasanClick: (Int?) -> Unit) {
            binding.txtStatus.text = formr.status
            binding.txtTanggal.text = formr.tglpinjam?.split("T")?.get(0) ?: "Tanggal Tidak Tersedia"
            binding.txtJumlah.text = formr.jumlah?.let { formatRupiah(it) }
            binding.txtNamaUser.text = "Nama Anggota: ${formr.nama}"
            binding.txtIDUser.text = "ID Anggota: ${formr.idanggota}"

            binding.btnDelete.setOnClickListener {
                onDeleteClick(formr.idpinjam)
            }
            binding.btnPelunasan.setOnClickListener {
                onPelunasanClick(formr.idpinjam)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemCardPinjamanAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val formr = getItem(position)
        holder.bind(formr, onDeleteClick, onPelunasanClick)
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
