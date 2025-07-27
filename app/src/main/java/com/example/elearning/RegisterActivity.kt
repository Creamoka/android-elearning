package com.example.elearning

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var registerBtn: Button
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.elearning.R.layout.activity_register) // ✅ Gunakan R dari package kamu

        // Inisialisasi Firebase
        auth = FirebaseAuth.getInstance()

        // Inisialisasi view
        emailInput = findViewById(com.example.elearning.R.id.emailEditText)
        passwordInput = findViewById(com.example.elearning.R.id.password)
        registerBtn = findViewById(com.example.elearning.R.id.confirmPassword)

        registerBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val pass = passwordInput.text.toString().trim()

            if (email.isNotEmpty() && pass.length >= 6) {
                auth.createUserWithEmailAndPassword(email, pass)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Akun berhasil dibuat", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Registrasi gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Email atau password tidak valid", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
