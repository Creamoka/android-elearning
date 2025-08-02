package com.example.elearning

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class BerandaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth // Objek untuk autentikasi Firebase
    private lateinit var txtSelamatDatang: TextView // TextView untuk ucapan selamat datang

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Ambil referensi ke TextView selamat datang
        txtSelamatDatang = findViewById<TextView>(R.id.welcome)

        // Load nama pengguna dari Firebase
        loadUserName()

        // Atur navigasi bawah (bottom bar)
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navChat = findViewById<ImageView>(R.id.nav_chat)
        val navProfile = findViewById<ImageView>(R.id.nav_profile)

        navHome.setOnClickListener {
            // Sudah berada di halaman beranda, tidak melakukan apa-apa
        }

        navChat.setOnClickListener {
            // Pindah ke halaman Chat
            startActivity(Intent(this, ChatActivity::class.java))
        }

        navProfile.setOnClickListener {
            // Pindah ke halaman Profil
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun loadUserName() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Coba ambil nama tampilan pengguna (display name)
            val userName = currentUser.displayName

            if (!userName.isNullOrEmpty()) {
                // Kalau nama tampilan ada, gunakan itu
                txtSelamatDatang.text = "Selamat Datang $userName"
                Log.d("BERANDA", "Menggunakan display name: $userName")
            } else {
                // Kalau tidak ada, ambil nama dari email
                val email = currentUser.email
                if (!email.isNullOrEmpty()) {
                    val nameFromEmail = email.substringBefore("@")
                    txtSelamatDatang.text = "Selamat Datang $nameFromEmail"
                    Log.d("BERANDA", "Menggunakan nama dari email: $nameFromEmail")
                } else {
                    // Kalau tidak ada email juga, fallback ke default
                    txtSelamatDatang.text = "Selamat Datang Pengguna"
                    Log.d("BERANDA", "Tidak ada nama atau email ditemukan")
                }
            }
        } else {
            // Pengguna belum login
            txtSelamatDatang.text = "Selamat Datang Pengguna"
            Log.d("BERANDA", "Tidak ada pengguna yang login")
        }
    }
}
