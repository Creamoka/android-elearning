package com.example.elearning

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BerandaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)

        // Ambil nama user dari LoginActivity
        val namaUser = intent.getStringExtra("NAMA_USER") ?: "Pengguna"

        // Tampilkan di TextView selamat datang
        val txtSelamatDatang = findViewById<TextView>(R.id.txt_selamat_datang)
        txtSelamatDatang.text = "Selamat Datang $namaUser"

        // Navigasi bottom bar
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navChat = findViewById<ImageView>(R.id.nav_chat)
        val navProfile = findViewById<ImageView>(R.id.nav_profile)

        navHome.setOnClickListener {
            // Sudah di home
        }

        navChat.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}
