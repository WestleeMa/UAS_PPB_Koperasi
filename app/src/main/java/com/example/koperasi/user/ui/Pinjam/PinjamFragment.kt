package com.example.koperasi.user.ui.Pinjam

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
import com.example.koperasi.databinding.FragmentPinjamBinding
import com.example.koperasi.preference.OperasiPreference
import com.example.koperasi.preference.PreferenceViewModel
import com.example.koperasi.preference.ViewModelFactory
import com.example.koperasi.preference.dataStore
import com.example.koperasi.user.ui.Pinjam.PinjamCardAdapter
import com.example.koperasi.user.ui.Pinjam.PinjamViewModel
import java.text.NumberFormat
import java.util.Locale

class PinjamFragment : Fragment() {
    private var _binding: FragmentPinjamBinding? = null
    private val binding get() = _binding!!

    private lateinit var pinjamViewModel: PinjamViewModel
    private lateinit var pinjamCardAdapter: PinjamCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinjamBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = OperasiPreference.getInstance(requireContext().dataStore)
        val preferenceViewModel = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]
        pinjamViewModel = ViewModelProvider(this).get(PinjamViewModel::class.java)
        val builder = AlertDialog.Builder(requireContext())

        preferenceViewModel.getID().observe(viewLifecycleOwner) { id ->
            id?.let {
                pinjamViewModel.getSimpanPinjam(id, "pinjam")
            }
        }

        pinjamCardAdapter = PinjamCardAdapter { id ->
            builder.setTitle("Konfirmasi")
            builder.setMessage("Lunasi Pinjaman?")
            builder.setPositiveButton("Ya") { dialog, which ->
                pinjamViewModel.pelunasan(id.toString())
                pinjamViewModel.msg.observe(viewLifecycleOwner) { msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
                preferenceViewModel.getID().observe(viewLifecycleOwner) { idanggota ->
                    idanggota?.let {
                        pinjamViewModel.getSimpanPinjam(idanggota, "pinjam")
                    }
                }
            }
            builder.setNegativeButton("Tidak") { dialog, which ->
                Toast.makeText(context, "Pelunasan dibatalkan", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            val dialog = builder.create()
            Log.d("PinjamFragment", "ID yang diklik: $id")
            dialog.show()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = pinjamCardAdapter

        binding.btnPinjam.setOnClickListener {
            val jumlah = binding.edtNominal.text.toString()


            preferenceViewModel.getID().observe(viewLifecycleOwner) { id ->
                id?.let {
                    pinjamViewModel.pinjamDana(id, jumlah)
                    pinjamViewModel.msg.observe(viewLifecycleOwner) { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }


            pinjamViewModel.msg.observe(viewLifecycleOwner) { msg ->
                Log.d("PinjamFragment", msg)
                preferenceViewModel.getID().observe(viewLifecycleOwner) { id ->
                    id?.let {
                        pinjamViewModel.getSimpanPinjam(id, "pinjam")
                    }
                }
            }
        }

        pinjamViewModel.listSimpanPinjam.observe(viewLifecycleOwner) { list ->
            list?.let {
                showRecyclerList(it)
            }
        }

        pinjamViewModel.isError.observe(viewLifecycleOwner) { err ->
            if (err) {
                pinjamViewModel.msg.observe(viewLifecycleOwner) { msg ->
                    Log.e("PinjamFragment", "Error: $msg")
                }
            }
        }
    }

    private fun showRecyclerList(list: List<ListItem>) {
        pinjamCardAdapter.submitList(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
