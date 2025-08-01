package com.example.elearning

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Atur tombol kembali (back)
        setupBackButton()

        // Atur navigasi bawah (bottom navigation)
        setupBottomNavigation()

        // Atur klik item chat (belum diimplementasikan)
        setupChatItems()
    }

    private fun setupBackButton() {
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            // Tutup halaman ini dan kembali ke halaman sebelumnya
            finish()
        }
    }

    private fun setupBottomNavigation() {
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navChat = findViewById<ImageView>(R.id.nav_chat)
        val navProfile = findViewById<ImageView>(R.id.nav_profile)

        // Navigasi ke halaman Beranda
        navHome.setOnClickListener {
            startActivity(Intent(this, BerandaActivity::class.java))
            overridePendingTransition(0, 0) // Menghilangkan animasi transisi
            finish()
        }

        // Navigasi ke halaman Chat (saat ini sudah di halaman ini)
        navChat.setOnClickListener {
            // Sudah berada di halaman chat, tidak melakukan apa-apa
        }

        // Navigasi ke halaman Profil
        navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            overridePendingTransition(0, 0) // Menghilangkan animasi transisi
            finish()
        }
    }

    private fun setupChatItems() {
        //buat chat di menu chat, sekarang masih static
    }
}
