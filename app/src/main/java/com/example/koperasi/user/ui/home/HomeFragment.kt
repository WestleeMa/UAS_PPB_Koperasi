package com.example.koperasi.user.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.koperasi.API.response.ListItem
import com.example.koperasi.databinding.FragmentHomeBinding
import com.example.koperasi.preference.OperasiPreference
import com.example.koperasi.preference.PreferenceViewModel
import com.example.koperasi.preference.ViewModelFactory
import com.example.koperasi.preference.dataStore
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAdapter: HomeCardAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
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

        // Observasi ID anggota
        preferenceViewModel.getID().observe(viewLifecycleOwner) { id ->
            homeViewModel.getSimpanPinjam(id, "simpan") // Panggil API
        }

        homeViewModel.isError.observe(viewLifecycleOwner) { err ->
            if (err){
                homeViewModel.msg.observe(viewLifecycleOwner) { msg ->
                    Log.e("HomeFragment", "Error: $msg")
                }

            }else{
                homeViewModel.simpanan.observe(viewLifecycleOwner) { simpanan ->
                    binding.textSimpanan.text = formatRupiah(simpanan.toInt())
                }
                homeViewModel.listSimpanPinjam.observe(viewLifecycleOwner) { list ->
                    if (list != null) {
                        showRecyclerList(list)
                        Log.d("HomeFragment", "Total Item: ${binding.recyclerView.adapter?.itemCount}")
                    }
                }
            }
        }

    }
    private fun formatRupiah(number: Int): String {
        val localeID = Locale("in", "ID") // Locale untuk Indonesia
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        val formattedString = formatter.format(number).replace("Rp", "Rp.").replace(",00", "") // Format dan sesuaikan
        return "$formattedString,-"
    }
    private fun showRecyclerList(list: List<ListItem>){
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val listFomUserAdapter = HomeCardAdapter()
        listFomUserAdapter.submitList(list)
        binding.recyclerView.adapter =listFomUserAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
