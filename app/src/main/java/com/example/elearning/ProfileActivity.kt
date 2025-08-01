package com.example.elearning

import android.annotation.SuppressLint
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

    private fun setupBottomNavigation() {
        TODO("Not yet implemented")
    }

    @SuppressLint("WrongViewCast")
    private fun setupMenuItems() {
        // Edit profil
        val editProfile = findViewById<ImageView>(R.id.edit_profile)
        editProfile.setOnClickListener {
            Toast.makeText(this, "Edit Profil diklik", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke halaman edit profil
        }

        // Menu Sertifikat Saya
        val certificatesMenu = findViewById<LinearLayout>(R.id.sertifikat)
        certificatesMenu.setOnClickListener {
            Toast.makeText(this, "Sertifikat Saya diklik", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke halaman sertifikat
        }

        // Menu Logout
        val logoutMenu = findViewById<LinearLayout>(R.id.keluar)
        logoutMenu.setOnClickListener {
            val logout = null
            logout
        }

        fun logout() {
            // Proses logout dari Firebase
            auth.signOut()
            Toast.makeText(this, "Berhasil logout", Toast.LENGTH_SHORT).show()

            // Navigasi kembali ke halaman login dan bersihkan task
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        fun setupBottomNavigation() {
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
}
