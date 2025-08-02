package com.example.elearning

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var deleteAccountButton: Button

    private val user = FirebaseAuth.getInstance().currentUser
    private val uid = user?.uid
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        nameEditText = findViewById(R.id.editnama)
        emailEditText = findViewById(R.id.editEmail)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
        deleteAccountButton = findViewById(R.id.deleteAccountButton)

        // Ambil data user dari database
        if (uid != null) {
            dbRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("name").getValue(String::class.java)
                    val email = snapshot.child("email").getValue(String::class.java)

                    nameEditText.setText(name)
                    emailEditText.setText(email)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@EditProfileActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Simpan perubahan
        saveButton.setOnClickListener {
            val newName = nameEditText.text.toString().trim()
            val newEmail = emailEditText.text.toString().trim()

            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Nama dan email tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedData = mapOf(
                "name" to newName,
                "email" to newEmail
            )

            if (uid != null) {
                dbRef.child(uid).updateChildren(updatedData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()

                        // Balik ke ProfileActivity secara eksplisit
                        val intent = Intent(this, ProfileActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // Batal edit
        cancelButton.setOnClickListener {
            finish()
        }

        // Hapus akun
        deleteAccountButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus Akun")
                .setMessage("Apakah kamu yakin ingin menghapus akunmu secara permanen?")
                .setPositiveButton("YA") { _, _ ->
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    val user = FirebaseAuth.getInstance().currentUser

                    if (uid != null) {
                        FirebaseDatabase.getInstance().getReference("users").child(uid).removeValue()
                    }

                    user?.delete()
                        ?.addOnSuccessListener {
                            Toast.makeText(this, "Akun berhasil dihapus", Toast.LENGTH_SHORT).show()
                            finishAffinity()
                        }
                        ?.addOnFailureListener {
                            Toast.makeText(this, "Gagal menghapus akun: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                }
                .setNegativeButton("BATAL", null)
                .show()
        }
    }
}
