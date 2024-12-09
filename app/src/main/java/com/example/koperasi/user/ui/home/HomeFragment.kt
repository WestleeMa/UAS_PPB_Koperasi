package com.example.koperasi.user.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.koperasi.API.response.SimpanPinjamResponse
import com.example.koperasi.API.response.SimpanPinjamResponseItem
import com.example.koperasi.R
import com.example.koperasi.databinding.FragmentHomeBinding
import com.example.koperasi.preference.OperasiPreference
import com.example.koperasi.preference.PreferenceViewModel
import com.example.koperasi.preference.ViewModelFactory
import com.example.koperasi.preference.dataStore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Inisialisasi adapter dengan data kosong
        cardAdapter = CardAdapter(emptyList())
        recyclerView.adapter = cardAdapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val pref = OperasiPreference.getInstance(requireContext().dataStore)
        val preferenceViewModel = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]

        preferenceViewModel.getNama().observe(viewLifecycleOwner) {
            binding.textHome.text = "Selamat Datang, $it"
        }

        preferenceViewModel.getID().observe(viewLifecycleOwner) { id ->
            homeViewModel.getSimpanPinjam(id, "simpan")
        }

        homeViewModel.listSimpanPinjam.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                Log.d("HomeFragment", "Data diterima: $response")
                val cardData = response
                cardAdapter.updateData(cardData)
            } else {
                Log.e("HomeFragment", "Data kosong atau null.")
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.textNotif.visibility = View.VISIBLE
                binding.textNotif.text = "Memuat data..."
                recyclerView.visibility = View.GONE
            } else {
                binding.textNotif.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        homeViewModel.isError.observe(viewLifecycleOwner) { isError ->
            if (isError) {
                val errorMsg = homeViewModel.msg.value ?: "Terjadi kesalahan, coba lagi."
                binding.textNotif.visibility = View.VISIBLE
                binding.textNotif.text = errorMsg
                recyclerView.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
