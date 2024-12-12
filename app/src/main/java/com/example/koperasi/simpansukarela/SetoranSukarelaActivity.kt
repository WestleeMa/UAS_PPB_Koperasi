package com.example.koperasi.simpansukarela

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.koperasi.R
import com.example.koperasi.databinding.ActivitySetoranSukarelaBinding
import com.example.koperasi.databinding.ActivitySetoranWajibBinding
import com.example.koperasi.preference.OperasiPreference
import com.example.koperasi.preference.PreferenceViewModel
import com.example.koperasi.preference.ViewModelFactory
import com.example.koperasi.preference.dataStore
import com.example.koperasi.simpanwajib.SetoranWajibViewModel
import com.example.koperasi.user.UserActivity
import okhttp3.internal.format
import java.lang.Integer.parseInt
import java.text.NumberFormat
import java.util.Locale

class SetoranSukarelaActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetoranSukarelaBinding
    private lateinit var setoranSukarelaViewModel: SetoranSukarelaViewModel
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("Notifikasi tersedia", "Notifications permission granted")
            } else {
                Log.e("Notifikasi tidak tersedia", "Notifications permission rejected")
            }
        }

    private fun formatRupiah(number: Int): String {
        val localeID = Locale("in", "ID") // Locale untuk Indonesia
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        val formattedString = formatter.format(number).replace("Rp", "Rp.").replace(",00", "") // Format dan sesuaikan
        return "$formattedString,-"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySetoranSukarelaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setoranSukarelaViewModel = ViewModelProvider(this)[SetoranSukarelaViewModel::class.java]
        val intentMain = Intent(this, UserActivity::class.java)
        val pref = OperasiPreference.getInstance(application.dataStore)
        val preferenceViewModel = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]

        binding.btnSimpan.setOnClickListener {
            preferenceViewModel.getID().observe(this) { id ->
                if (id != null) {
                    setoranSukarelaViewModel.bayarSukarela(id, parseInt(binding.jumlahSukarela.text.toString()))
                    setoranSukarelaViewModel.isError.observe(this){err ->
                        if(err == true && setoranSukarelaViewModel.msg.value != null && setoranSukarelaViewModel.msg.value != "timeout"){
                            Log.d("Setoran Sukarela", err.toString())
                            sendNotification("Gagal", setoranSukarelaViewModel.msg.value.toString())
                        }else{
                            Log.d("Setoran Sukarela", "berhasil")
                            sendNotification("Terima Kasih", "Berhasil membayar setoran sukarela sebanyak ${formatRupiah(parseInt(binding.jumlahSukarela.text.toString()))}")
                            startActivity(intentMain)
                        }
                    }
                }
            }
        }
        binding.btnCancel.setOnClickListener{
            startActivity(intentMain)
        }
    }

    private fun sendNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSubText(getString(R.string.notification_subtext))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "dicoding channel"
    }
}