package com.example.elearning

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Load data user dari database saat pertama kali dibuka
        loadUserProfile()

        // Atur item menu profile
        setupMenuItems()

        // Atur navigasi bawah
        setupBottomNavigation()
    }

    // ⬇️ Tambahkan ini di LUAR onCreate
    override fun onResume() {
        super.onResume()
        loadUserProfile() // Ini akan berjalan saat balik dari edit profile
    }

    private fun loadUserProfile() {
        val user = auth.currentUser
        val uid = user?.uid ?: return

        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users").child(uid)

        userRef.get().addOnSuccessListener { snapshot ->
            val name = snapshot.child("name").getValue(String::class.java)
            val email = snapshot.child("email").getValue(String::class.java)

            val nameTextView = findViewById<TextView>(R.id.nama_pengguna)
            val emailTextView = findViewById<TextView>(R.id.email_pengguna)

            nameTextView.text = name
            emailTextView.text = email

        }.addOnFailureListener {
            Toast.makeText(this, "Gagal memuat profil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupMenuItems() {
        val editProfile = findViewById<ImageView>(R.id.edit_profile)
        editProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        val certificatesMenu = findViewById<TextView>(R.id.sertifikat)
        certificatesMenu.setOnClickListener {
            Toast.makeText(this, "Sertifikat Saya diklik", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke halaman sertifikat
        }

        val logoutMenu = findViewById<TextView>(R.id.keluar)
        logoutMenu.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        auth.signOut()
        Toast.makeText(this, "Berhasil logout", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setupBottomNavigation() {
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navChat = findViewById<ImageView>(R.id.nav_chat)
        val navProfile = findViewById<ImageView>(R.id.nav_profile)

        navHome.setOnClickListener {
            startActivity(Intent(this, BerandaActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        navChat.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        navProfile.setOnClickListener {
            // Sudah di halaman ini
        }
    }
}
