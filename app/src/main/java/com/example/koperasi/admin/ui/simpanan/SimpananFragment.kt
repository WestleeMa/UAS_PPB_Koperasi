package com.example.koperasi.admin.ui.simpanan

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
import com.example.koperasi.databinding.FragmentSimpananBinding
import java.text.NumberFormat
import java.util.Locale

class SimpananFragment : Fragment() {

    private var _binding: FragmentSimpananBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimpananBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val simpananViewModel = ViewModelProvider(this).get(SimpananViewModel::class.java)
        simpananViewModel.getSimpanPinjam()

        simpananViewModel.isError.observe(viewLifecycleOwner) { err ->
            if (err) {
                simpananViewModel.msg.observe(viewLifecycleOwner) { msg ->
                    Log.e("UserHomeFragment", "Error: $msg")
                }
            } else {
                simpananViewModel.simpanan.observe(viewLifecycleOwner) { simpanan ->
                    binding.textSimpanan.text = formatRupiah(simpanan.toInt())
                }
                simpananViewModel.listSimpanPinjam.observe(viewLifecycleOwner) { list ->
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

    private fun showRecyclerList(list: List<ListItem>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val listSimpananAdapter = SimpananCardAdapter { idsimpan ->
            showDeleteConfirmationDialog(idsimpan)
        }
        listSimpananAdapter.submitList(list)
        binding.recyclerView.adapter = listSimpananAdapter
    }

    private fun showDeleteConfirmationDialog(idanggota: Int?) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Hapus Data Simpanan")
            .setMessage("Apakah Anda yakin ingin menghapus data simpanan ini?")
            .setPositiveButton("Hapus") { _, _ ->
                val simpananViewModel = ViewModelProvider(this).get(SimpananViewModel::class.java)
                simpananViewModel.deleteSimpanan(idanggota, "simpan")
                simpananViewModel.msg.observe(viewLifecycleOwner) { msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    simpananViewModel.getSimpanPinjam()
                }
            }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
