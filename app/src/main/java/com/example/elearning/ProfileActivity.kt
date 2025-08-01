package com.example.elearning

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Atur item menu profile
        setupMenuItems()

        // Atur navigasi bawah (bottom navigation)
        setupBottomNavigation()
    }

    private fun setupMenuItems() {
        // Edit profil
        val editProfile = findViewById<ImageView>(R.id.btn_edit_profile)
        editProfile.setOnClickListener {
            Toast.makeText(this, "Edit Profil diklik", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke halaman edit profil
        }

        // Menu Sertifikat Saya
        val certificatesMenu = findViewById<LinearLayout>(R.id.menu_certificates)
        certificatesMenu.setOnClickListener {
            Toast.makeText(this, "Sertifikat Saya diklik", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke halaman sertifikat
        }

        // Menu Pusat Bantuan
        val helpMenu = findViewById<LinearLayout>(R.id.menu_help)
        helpMenu.setOnClickListener {
            Toast.makeText(this, "Pusat Bantuan diklik", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke pusat bantuan
        }

        // Menu Undang Teman
        val inviteMenu = findViewById<LinearLayout>(R.id.menu_invite)
        inviteMenu.setOnClickListener {
            Toast.makeText(this, "Undang Teman diklik", Toast.LENGTH_SHORT).show()
            // TODO: Implementasi fungsi undang teman
        }

        // Menu Logout
        val logoutMenu = findViewById<LinearLayout>(R.id.menu_logout)
        logoutMenu.setOnClickListener {
            logout()
        }

        // Kebijakan Privasi
        val privacyPolicy = findViewById<android.widget.TextView>(R.id.privacy_policy)
        privacyPolicy.setOnClickListener {
            Toast.makeText(this, "Kebijakan Privasi diklik", Toast.LENGTH_SHORT).show()
            // TODO: Buka halaman kebijakan privasi
        }

        // Syarat dan Ketentuan
        val termsConditions = findViewById<android.widget.TextView>(R.id.terms_conditions)
        termsConditions.setOnClickListener {
            Toast.makeText(this, "Syarat dan Ketentuan diklik", Toast.LENGTH_SHORT).show()
            // TODO: Buka halaman syarat dan ketentuan
        }
    }

    private fun logout() {
        // Proses logout dari Firebase
        auth.signOut()
        Toast.makeText(this, "Berhasil logout", Toast.LENGTH_SHORT).show()

        // Navigasi kembali ke halaman login dan bersihkan task
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
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

        // Navigasi ke halaman Chat
        navChat.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
            overridePendingTransition(0, 0) // Menghilangkan animasi transisi
            finish()
        }

        // Sudah berada di halaman profil, tidak melakukan apa-apa
        navProfile.setOnClickListener {
            // Tidak ada aksi
        }
    }
}
