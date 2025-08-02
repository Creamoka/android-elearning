package com.example.elearning

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
    private lateinit var loginLink: TextView
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        nameInput = findViewById(R.id.nameEditText)
        emailInput = findViewById(R.id.emailEditText)
        passwordInput = findViewById(R.id.password)
        confirmPasswordInput = findViewById(R.id.confirmPassword)
        showPasswordIcon = findViewById(R.id.showPasswordIcon)
        showConfirmPasswordIcon = findViewById(R.id.showConfirmPasswordIcon)
        registerBtn = findViewById(R.id.daftar)
        loginLink = findViewById(R.id.logins)

        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        showPasswordIcon.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(passwordInput, isPasswordVisible)
        }

        showConfirmPasswordIcon.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            togglePasswordVisibility(confirmPasswordInput, isConfirmPasswordVisible)
        }

        registerBtn.setOnClickListener {
            Log.d("REGISTER", "Register button clicked")

            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            Log.d("REGISTER", "Input values - Name: $name, Email: $email, Password length: ${password.length}")

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Log.d("REGISTER", "Empty fields detected")
                Toast.makeText(this, "Isi semua field terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Log.d("REGISTER", "Password too short")
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Log.d("REGISTER", "Password mismatch")
                Toast.makeText(this, "Password dan konfirmasi tidak cocok", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("REGISTER", "Starting registration process")
            Toast.makeText(this, "Sedang mendaftar...", Toast.LENGTH_SHORT).show()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val user = auth.currentUser
                    val uid = user?.uid
                    if (uid != null) {
                        val userData = mapOf("name" to name, "email" to email)
                        FirebaseDatabase.getInstance().reference
                            .child("users").child(uid).setValue(userData)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("REGISTER", "Registration failed: ${e.message}")
                }

            Handler(Looper.getMainLooper()).postDelayed({
                Toast.makeText(this, "Akun berhasil dibuat!", Toast.LENGTH_SHORT).show()

                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }, 1000)
            }, 1500)
        }
    }

    // Menampilkan atau menyembunyikan password
    private fun togglePasswordVisibility(editText: EditText, isVisible: Boolean) {
        editText.transformationMethod = if (isVisible) {
            HideReturnsTransformationMethod.getInstance()
        } else {
            PasswordTransformationMethod.getInstance()
        }
        editText.setSelection(editText.text.length)
    }
}
