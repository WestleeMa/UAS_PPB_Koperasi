package com.example.koperasi.user.ui.home

import android.content.Intent
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
import com.example.koperasi.admin.AdminActivity
import com.example.koperasi.databinding.FragmentUserhomeBinding
import com.example.koperasi.login.LoginActivity
import com.example.koperasi.preference.OperasiPreference
import com.example.koperasi.preference.PreferenceViewModel
import com.example.koperasi.preference.ViewModelFactory
import com.example.koperasi.preference.dataStore
import java.text.NumberFormat
import java.util.Locale

class UserHomeFragment : Fragment() {

    private var _binding: FragmentUserhomeBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAdapter: UserHomeCardAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserhomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userHomeViewModel =
            ViewModelProvider(this).get(UserHomeViewModel::class.java)

        val pref = OperasiPreference.getInstance(requireContext().dataStore)
        val preferenceViewModel = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]

        preferenceViewModel.getNama().observe(viewLifecycleOwner) {
            binding.textHome.text = "Selamat Datang, $it"
        }

        preferenceViewModel.getRole().observe(viewLifecycleOwner) {
            if (it == "pengurus") {
                binding.btnPageAdmin.visibility = View.VISIBLE
            }else {
                binding.btnPageAdmin.visibility = View.GONE
            }
        }
        binding.btnPageAdmin.setOnClickListener{
            val intentAdmin = Intent(requireContext(), AdminActivity::class.java)
            startActivity(intentAdmin)
        }
        binding.btnLogout.setOnClickListener{
            preferenceViewModel.clearPreferences()
            val intentLogin = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intentLogin)
        }

        preferenceViewModel.getID().observe(viewLifecycleOwner) { id ->
            if (id != null) {
                userHomeViewModel.getSimpanPinjam(id, "simpan")
            }
        }

        userHomeViewModel.isError.observe(viewLifecycleOwner) { err ->
            if (err){
                userHomeViewModel.msg.observe(viewLifecycleOwner) { msg ->
                    Log.e("UserHomeFragment", "Error: $msg")
                }

            }else{
                userHomeViewModel.simpanan.observe(viewLifecycleOwner) { simpanan ->
                    binding.textSimpanan.text = formatRupiah(simpanan.toInt())
                }
                userHomeViewModel.listSimpanPinjam.observe(viewLifecycleOwner) { list ->
                    if (list != null) {
                        showRecyclerList(list)
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
        val listFomUserAdapter = UserHomeCardAdapter()
        listFomUserAdapter.submitList(list)
        binding.recyclerView.adapter =listFomUserAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
