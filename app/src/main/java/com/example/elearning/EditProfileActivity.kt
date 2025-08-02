package com.example.elearning

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    companion object {
        private const val TAG = "EditProfileActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        initializeViews()
        loadUserData()
        setupClickListeners()
    }

    // Inisialisasi komponen UI
    private fun initializeViews() {
        nameEditText = findViewById(R.id.editnama)
        emailEditText = findViewById(R.id.editEmail)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
        deleteAccountButton = findViewById(R.id.deleteAccountButton)

        Log.d(TAG, "Views berhasil diinisialisasi")
    }

    // Mengambil data pengguna dari database Firebase
    private fun loadUserData() {
        if (uid != null) {
            Log.d(TAG, "Memuat data pengguna dengan UID: $uid")

            dbRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val name = snapshot.child("name").getValue(String::class.java)
                        val email = snapshot.child("email").getValue(String::class.java)

                        nameEditText.setText(name ?: "")
                        emailEditText.setText(email ?: "")
                    } else {
                        Toast.makeText(this@EditProfileActivity, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@EditProfileActivity, "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Pengguna tidak terautentikasi", Toast.LENGTH_SHORT).show()
        }
    }

    // Mengatur aksi tombol
    private fun setupClickListeners() {
        saveButton.setOnClickListener {
            saveUserData()
        }

        cancelButton.setOnClickListener {
            finish()
        }

        deleteAccountButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    // Menyimpan perubahan data pengguna
    private fun saveUserData() {
        val newName = nameEditText.text.toString().trim()
        val newEmail = emailEditText.text.toString().trim()

        if (newName.isEmpty()) {
            Toast.makeText(this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        if (newEmail.isEmpty()) {
            Toast.makeText(this, "Email tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            Toast.makeText(this, "Format email tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        if (uid != null) {
            saveButton.isEnabled = false

            val updatedData = hashMapOf<String, Any>(
                "name" to newName,
                "email" to newEmail
            )

            Log.d(TAG, "Mengupdate data pengguna di Firebase untuk UID: $uid")

            dbRef.child(uid).updateChildren(updatedData)
                .addOnSuccessListener {
                    Log.d(TAG, "Profil berhasil diperbarui")
                    Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()

                    user?.updateEmail(newEmail)
                        ?.addOnSuccessListener {
                            Log.d(TAG, "Email di FirebaseAuth berhasil diupdate")
                        }
                        ?.addOnFailureListener { exception ->
                            Log.w(TAG, "Gagal update email di FirebaseAuth: ${exception.message}")
                        }

                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Gagal update profil: ${exception.message}")
                    Toast.makeText(this, "Gagal memperbarui profil: ${exception.message}", Toast.LENGTH_LONG).show()
                    saveButton.isEnabled = true
                }
        } else {
            Log.e(TAG, "Tidak bisa simpan: UID pengguna null")
            Toast.makeText(this, "Pengguna tidak terautentikasi", Toast.LENGTH_SHORT).show()
        }
    }

    // Menampilkan dialog konfirmasi penghapusan akun
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Hapus Akun")
            .setMessage("Apakah kamu yakin ingin menghapus akunmu secara permanen? Tindakan ini tidak dapat dibatalkan.")
            .setPositiveButton("YA") { _, _ ->
                deleteUserAccount()
            }
            .setNegativeButton("BATAL", null)
            .show()
    }

    // Menghapus akun dan data pengguna
    private fun deleteUserAccount() {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        val currentUser = FirebaseAuth.getInstance().currentUser

        Log.d(TAG, "Mencoba menghapus akun dengan UID: $currentUid")

        if (currentUid != null && currentUser != null) {
            FirebaseDatabase.getInstance().getReference("users").child(currentUid).removeValue()
                .addOnSuccessListener {
                    currentUser.delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "Akun berhasil dihapus")
                            Toast.makeText(this, "Akun berhasil dihapus", Toast.LENGTH_SHORT).show()
                            finishAffinity()
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Gagal menghapus akun: ${exception.message}")
                            Toast.makeText(this, "Gagal menghapus akun: ${exception.message}", Toast.LENGTH_LONG).show()
                        }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Gagal menghapus data pengguna: ${exception.message}")
                    Toast.makeText(this, "Gagal menghapus data pengguna: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Log.e(TAG, "Tidak bisa hapus akun: pengguna null")
            Toast.makeText(this, "Pengguna tidak terautentikasi", Toast.LENGTH_SHORT).show()
        }
    }
}
