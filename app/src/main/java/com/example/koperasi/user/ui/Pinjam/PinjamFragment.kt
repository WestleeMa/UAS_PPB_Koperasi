package com.example.koperasi.user.ui.Pinjam

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
        val pinjamViewModel = ViewModelProvider(this).get(PinjamViewModel::class.java)

        preferenceViewModel.getID().observe(viewLifecycleOwner) { id ->
            if (id != null) {
                pinjamViewModel.getSimpanPinjam(id, "pinjam")
            }
        }
        binding.btnPinjam.setOnClickListener{
            val jumlah = binding.edtNominal.text.toString()
            preferenceViewModel.getID().observe(viewLifecycleOwner) { id ->
                if (id != null) {
                    pinjamViewModel.pinjamDana(id, jumlah)
                }
            }

            pinjamViewModel.msg.observe(viewLifecycleOwner) {msg ->
                showToast(msg)
            }
        }
        pinjamViewModel.isError.observe(viewLifecycleOwner) { err ->
            if (err){
                pinjamViewModel.msg.observe(viewLifecycleOwner) { msg ->
                    Log.e("HomeFragment", "Error: $msg")
                }

            }else{
                pinjamViewModel.listSimpanPinjam.observe(viewLifecycleOwner) { list ->
                    if (list != null) {
                        showRecyclerList(list)
                    }
                }
            }
        }

    }

    private fun showToast(msg: String){
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showRecyclerList(list: List<ListItem>){
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val listFormAdapter = PinjamCardAdapter { id ->
            Log.d("PinjamFragment", "ID yang diklik: $id")
        }
        listFormAdapter.submitList(list)
        binding.recyclerView.adapter = listFormAdapter
    }

}