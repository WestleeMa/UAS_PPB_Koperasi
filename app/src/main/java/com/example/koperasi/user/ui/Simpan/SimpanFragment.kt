package com.example.koperasi.user.ui.Simpan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.koperasi.databinding.FragmentSimpanBinding
import com.example.koperasi.simpanwajib.SetoranWajibActivity

class SimpanFragment : Fragment() {

    private var _binding: FragmentSimpanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val simpanViewModel =
            ViewModelProvider(this).get(SimpanViewModel::class.java)
        _binding = FragmentSimpanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnWajib.setOnClickListener {
            Log.d("SimpanFragment", "Tombol Wajib ditekan")
            val intentMain = Intent(requireContext(), SetoranWajibActivity::class.java)
            startActivity(intentMain)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}