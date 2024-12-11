package com.example.koperasi.user.ui.Pinjam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.koperasi.API.response.ListItem
import com.example.koperasi.databinding.ItemCardPinjamanBinding
import java.text.NumberFormat
import java.util.Locale

class PinjamCardAdapter(private val onButtonClick: (Int) -> Unit) : ListAdapter<ListItem, PinjamCardAdapter.ListViewHolder>(DIFF_CALLBACK) {
    class ListViewHolder(val binding: ItemCardPinjamanBinding): RecyclerView.ViewHolder(binding.root){
        fun formatRupiah(number: Int): String {
            val localeID = Locale("in", "ID")
            val formatter = NumberFormat.getCurrencyInstance(localeID)
            val formattedString = formatter.format(number).replace("Rp", "Rp.").replace(",00", "") // Format dan sesuaikan
            return "$formattedString,-"
        }
        fun bind(formr: ListItem, onButtonClick: (Int) -> Unit){
            binding.txtStatus.text = formr.status
            binding.txtTanggal.text = formr.tglpinjam?.split("T")?.get(0) ?: "Tanggal Tidak Tersedia"
            binding.txtJumlah.text = formr.jumlah?.let { formatRupiah(it) }

            binding.btnBayarPinjaman.setOnClickListener {
                formr.idanggota?.let { id ->
                    onButtonClick(id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemCardPinjamanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val formr = getItem(position)
        holder.bind(formr, onButtonClick)
    }

    companion object{
        const val TAG = "pinjamcardadapter"
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
