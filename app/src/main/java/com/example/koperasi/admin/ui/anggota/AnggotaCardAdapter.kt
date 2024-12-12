package com.example.koperasi.admin.ui.anggota

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.koperasi.API.response.AnggotaResponseItem
import com.example.koperasi.databinding.ItemCardAnggotaBinding
import com.example.koperasi.preference.OperasiPreference
import com.example.koperasi.preference.PreferenceViewModel
import com.example.koperasi.preference.ViewModelFactory
import com.example.koperasi.preference.dataStore
import java.text.NumberFormat
import java.util.Locale

class AnggotaCardAdapter(
    private val onEditClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : ListAdapter<AnggotaResponseItem, AnggotaCardAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(val binding: ItemCardAnggotaBinding): RecyclerView.ViewHolder(binding.root) {
        fun formatRupiah(number: Int): String {
            val localeID = Locale("in", "ID")
            val formatter = NumberFormat.getCurrencyInstance(localeID)
            val formattedString = formatter.format(number).replace("Rp", "Rp.").replace(",00", "")
            return "$formattedString,-"
        }

        fun bind(formr: AnggotaResponseItem, onEditClick: (Int) -> Unit, onDeleteClick: (Int) -> Unit) {
            binding.txtRole.text = formr.role
            binding.txtTglGabung.text = formr.tglgabung?.split("T")?.get(0) ?: "Tanggal Tidak Tersedia"
            binding.txtSimpanan.text = formr.simpanan?.let { formatRupiah(it) }
            binding.txtNama.text = formr.nama
            binding.txtEmail.text = formr.email
            binding.txtIdAnggota.text = "ID Anggota: ${formr.idanggota.toString()}"

            // Set click listeners
            binding.btnEditAnggota.setOnClickListener {
                formr.idanggota?.let { onEditClick(it) }
            }
            binding.btnDeleteAnggota.setOnClickListener {
                formr.idanggota?.let { onDeleteClick(it) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemCardAnggotaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val formr = getItem(position)
        holder.bind(formr, onEditClick, onDeleteClick)
    }

    companion object {
        const val TAG = "anggotaadapter"
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<AnggotaResponseItem>() {
            override fun areItemsTheSame(
                oldItem: AnggotaResponseItem,
                newItem: AnggotaResponseItem
            ): Boolean {
                return oldItem.idanggota == newItem.idanggota
            }

            override fun areContentsTheSame(
                oldItem: AnggotaResponseItem,
                newItem: AnggotaResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
