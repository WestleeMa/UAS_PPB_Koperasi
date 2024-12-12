package com.example.koperasi.admin.ui.anggota

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.koperasi.API.response.AnggotaResponseItem
import com.example.koperasi.databinding.DialogEditAnggotaBinding
import com.example.koperasi.databinding.FragmentAnggotaBinding
import com.example.koperasi.login.LoginActivity
import com.example.koperasi.preference.OperasiPreference
import com.example.koperasi.preference.PreferenceViewModel
import com.example.koperasi.preference.ViewModelFactory
import com.example.koperasi.preference.dataStore
import com.example.koperasi.register.RegistActivity
import com.example.koperasi.user.UserActivity

class AnggotaFragment : Fragment() {

    private var _binding: FragmentAnggotaBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnggotaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val anggotaViewModel =
            ViewModelProvider(this).get(AnggotaViewModel::class.java)
        val pref = OperasiPreference.getInstance(requireContext().dataStore)
        val preferenceViewModel = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]

        binding.btnAddUser.setOnClickListener{
            val intentRegister = Intent(requireContext(), RegistActivity::class.java)
            startActivity(intentRegister)
        }
        binding.btnUserHome.setOnClickListener{
            val intentHome = Intent(requireContext(), UserActivity::class.java)
            startActivity(intentHome)
        }

        preferenceViewModel.getNama().observe(viewLifecycleOwner) {
            binding.txtNama.text = "Selamat Datang, Admin $it"
        }

        binding.btnLogOut.setOnClickListener{
            preferenceViewModel.clearPreferences()
            val intentLogin = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intentLogin)
        }

        preferenceViewModel.getID().observe(viewLifecycleOwner) { id ->
            if (id != null) {
                anggotaViewModel.getAnggota()
            }
        }

        anggotaViewModel.isError.observe(viewLifecycleOwner) { err ->
            if (err){
                anggotaViewModel.msg.observe(viewLifecycleOwner) { msg ->
                    Log.e("AnggotaFragment", "Error: $msg")
                }

            }else{
                anggotaViewModel.listAnggota.observe(viewLifecycleOwner) { list ->
                    if (list != null) {
                        showRecyclerList(list)
                    }
                }
            }
        }

    }

    private fun showEditDialog(idanggota: Int) {
        val anggotaViewModel =
            ViewModelProvider(this).get(AnggotaViewModel::class.java)
        val anggota = anggotaViewModel.listAnggota.value?.find { it.idanggota == idanggota }

        if (anggota != null) {
            val dialogBinding = DialogEditAnggotaBinding.inflate(LayoutInflater.from(requireContext()))

            dialogBinding.edtNama.setText(anggota.nama)
            dialogBinding.edtEmail.setText(anggota.email)
            dialogBinding.edtSimpanan.setText(anggota.simpanan?.toString() ?: "")

            val roles = listOf("anggota", "pengurus")
            val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, roles)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialogBinding.spinnerRole.adapter = spinnerAdapter

            val roleIndex = roles.indexOf(anggota.role)
            if (roleIndex != -1) {
                dialogBinding.spinnerRole.setSelection(roleIndex)
            }

            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Edit Anggota")
                .setView(dialogBinding.root)
                .setNegativeButton("Batal", null)
                .create()

            dialogBinding.btnSave.setOnClickListener {
                val idanggota = anggota.idanggota
                val nama = dialogBinding.edtNama.text.toString()
                val email = dialogBinding.edtEmail.text.toString()
                val role = dialogBinding.spinnerRole.selectedItem.toString()
                val simpanan = dialogBinding.edtSimpanan.text.toString().toIntOrNull()
                val password = dialogBinding.edtPassword.text.toString()


                if (idanggota != null && simpanan != null) {
                    anggotaViewModel.editAnggota(idanggota, nama, simpanan, role, email, password)
                    anggotaViewModel.msg.observe(viewLifecycleOwner){ msg->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        anggotaViewModel.getAnggota()
                    }
                }
                dialog.dismiss()
            }

            dialog.show()
        } else {
            Log.e("AnggotaFragment", "Anggota dengan ID $idanggota tidak ditemukan")
        }
    }
    private fun showDeleteConfirmationDialog(idanggota: Int) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Hapus Anggota")
            .setMessage("Apakah Anda yakin ingin menghapus anggota dengan ID $idanggota?")
            .setPositiveButton("Hapus") { _, _ ->
                val anggotaViewModel =
                    ViewModelProvider(this).get(AnggotaViewModel::class.java)
                anggotaViewModel.deleteAnggota(idanggota)
                anggotaViewModel.msg.observe(viewLifecycleOwner) { msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    anggotaViewModel.getAnggota()
                }
            }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()
    }

    private fun showRecyclerList(list: List<AnggotaResponseItem>) {
        binding.recyclerViewAnggota.layoutManager = LinearLayoutManager(requireContext())

        val listFomUserAdapter = AnggotaCardAdapter(
            onEditClick = { idanggota ->
                showEditDialog(idanggota)
            },
            onDeleteClick = { idanggota ->
                showDeleteConfirmationDialog(idanggota)
            }
        )

        listFomUserAdapter.submitList(list)
        binding.recyclerViewAnggota.adapter = listFomUserAdapter
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}