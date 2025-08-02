package com.example.elearning

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.text.method.HideReturnsTransformationMethod
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var registerBtn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var showPasswordIcon: ImageView
    private lateinit var showConfirmPasswordIcon: ImageView

    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Ambil view dari layout
        nameInput = findViewById(R.id.nameEditText)
        emailInput = findViewById(R.id.emailEditText)
        passwordInput = findViewById(R.id.password)
        confirmPasswordInput = findViewById(R.id.confirmPassword)
        showPasswordIcon = findViewById(R.id.showPasswordIcon)
        showConfirmPasswordIcon = findViewById(R.id.showConfirmPasswordIcon)
        registerBtn = findViewById(R.id.daftar)

        // Toggle password visibility
        showPasswordIcon.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(passwordInput, isPasswordVisible)
        }

        showConfirmPasswordIcon.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            togglePasswordVisibility(confirmPasswordInput, isConfirmPasswordVisible)
        }

        // Klik tombol daftar
        registerBtn.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            // Validasi input
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Isi semua field terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Password dan konfirmasi tidak cocok", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Proses registrasi akun
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val user = auth.currentUser
                    val uid = user?.uid

                    if (uid == null) {
                        Log.e("REGISTER", "UID null setelah registrasi")
                        Toast.makeText(this, "Terjadi kesalahan saat mengambil UID", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    val userData = mapOf(
                        "name" to name,
                        "email" to email
                    )

                    Log.d("REGISTER", "UID: $uid")
                    Log.d("REGISTER", "User Data: $userData")

                    val database = FirebaseDatabase.getInstance().reference
                    database.child("users").child(uid).setValue(userData)
                        .addOnSuccessListener {
                            Log.d("REGISTER", "Data berhasil disimpan ke database")
                            Toast.makeText(this, "Akun berhasil dibuat!", Toast.LENGTH_SHORT).show()

                            // Arahkan ke LoginActivity
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Log.e("REGISTER", "Gagal simpan ke database: ${e.message}")
                            Toast.makeText(this, "Gagal menyimpan data pengguna", Toast.LENGTH_LONG).show()
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("REGISTER", "Gagal daftar: ${e.message}")
                    Toast.makeText(this, "Gagal mendaftar: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun togglePasswordVisibility(editText: EditText, isVisible: Boolean) {
        editText.transformationMethod = if (isVisible) {
            HideReturnsTransformationMethod.getInstance()
        } else {
            PasswordTransformationMethod.getInstance()
        }
        editText.setSelection(editText.text.length)
    }
}
