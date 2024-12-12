package com.example.koperasi.admin.ui.pinjaman

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.koperasi.API.response.ListItem
import com.example.koperasi.admin.ui.simpanan.SimpananCardAdapter
import com.example.koperasi.admin.ui.simpanan.SimpananViewModel
import com.example.koperasi.databinding.FragmentPinjamanBinding
import java.text.NumberFormat
import java.util.Locale

class PinjamanFragment : Fragment() {

    private var _binding: FragmentPinjamanBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinjamanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pinjamanViewModel = ViewModelProvider(this).get(PinjamanViewModel::class.java)
        pinjamanViewModel.getSimpanPinjam()

        pinjamanViewModel.isError.observe(viewLifecycleOwner) { err ->
            if (err) {
                pinjamanViewModel.msg.observe(viewLifecycleOwner) { msg ->
                    Log.e("UserHomeFragment", "Error: $msg")
                }
            } else {
                pinjamanViewModel.pinjaman.observe(viewLifecycleOwner) { simpanan ->
                    binding.textPinjaman.text = formatRupiah(simpanan.toInt())
                }
                pinjamanViewModel.listSimpanPinjam.observe(viewLifecycleOwner) { list ->
                    if (list != null) {
                        showRecyclerList(list)
                    }
                }
            }
        }
    }

    private fun formatRupiah(number: Int): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        val formattedString = formatter.format(number).replace("Rp", "Rp.").replace(",00", "")
        return "$formattedString,-"
    }

    private fun showPelunasanConfirmationDialog(idpinjam: Int?) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Pelunasan Pinjaman")
            .setMessage("Apakah Anda yakin ingin melunasi pinjaman ini?")
            .setPositiveButton("Lunasi") { _, _ ->
                idpinjam?.let {
                    val pinjamanViewModel = ViewModelProvider(this).get(PinjamanViewModel::class.java)
                    pinjamanViewModel.pelunasan(it.toString())
                    pinjamanViewModel.msg.observe(viewLifecycleOwner) { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        pinjamanViewModel.getSimpanPinjam()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()
    }

    private fun showDeleteConfirmationDialog(idanggota: Int?) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Hapus Data Pinjaman")
            .setMessage("Apakah Anda yakin ingin menghapus data pinjaman ini?")
            .setPositiveButton("Hapus") { _, _ ->
                val pinjamanViewModel = ViewModelProvider(this).get(PinjamanViewModel::class.java)
                pinjamanViewModel.deletePinjaman(idanggota, "pinjam")
                pinjamanViewModel.msg.observe(viewLifecycleOwner) { msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    pinjamanViewModel.getSimpanPinjam()
                }
            }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()
    }

    private fun showRecyclerList(list: List<ListItem>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val listPinjamanAdapter = PinjamanCardAdapter(
            onDeleteClick = { idpinjam ->
                showDeleteConfirmationDialog(idpinjam)
            },
            onPelunasanClick = { idpinjam ->
                showPelunasanConfirmationDialog(idpinjam)
            }
        )
        listPinjamanAdapter.submitList(list)
        binding.recyclerView.adapter = listPinjamanAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}