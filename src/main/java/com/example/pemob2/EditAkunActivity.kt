package com.example.wistcookapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditAkunActivity : AppCompatActivity() {

    private lateinit var etNamaPengguna: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSave: Button
    private lateinit var btnBack: ImageView
    private lateinit var tvProfileName: TextView
    private lateinit var profileImageView: ImageView
    private lateinit var userRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_akun)

        // Inisialisasi variabel UI
        etNamaPengguna = findViewById(R.id.namaPenggunaInput)
        etEmail = findViewById(R.id.emailInput)
        etPassword = findViewById(R.id.et_password) // Input untuk password
        btnSave = findViewById(R.id.saveButton)
        btnBack = findViewById(R.id.backButton)
        tvProfileName = findViewById(R.id.profileName)
        profileImageView = findViewById(R.id.profile_image) // ImageView profil
        auth = FirebaseAuth.getInstance()

        // Ambil userId dari Firebase Authentication
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        // Inisialisasi referensi Firebase Database
        userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)

        // Memuat data pengguna dari database
        userRef.get().addOnSuccessListener { snapshot ->
            val user = snapshot.getValue(User::class.java)
            user?.let {
                tvProfileName.text = it.name
                etNamaPengguna.setText(it.name)
                etEmail.setText(it.email)

                // Cek apakah pengguna memiliki foto profil
                if (!it.photoUrl.isNullOrEmpty()) {
                    try {
                        val imageBytes = Base64.decode(it.photoUrl, Base64.DEFAULT)
                        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        profileImageView.setImageBitmap(decodedImage)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        profileImageView.setImageResource(R.drawable.ic_account) // Gunakan gambar default
                    }
                } else {
                    profileImageView.setImageResource(R.drawable.ic_account) // Gambar default
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal memuat data pengguna", Toast.LENGTH_SHORT).show()
        }

        // Simpan perubahan
        btnSave.setOnClickListener {
            val namaPengguna = etNamaPengguna.text.toString().trim()
            val emailBaru = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (namaPengguna.isEmpty() || emailBaru.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nama, email, dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = auth.currentUser
            if (user != null) {
                val credential = EmailAuthProvider.getCredential(user.email!!, password)

                // Reauthenticate sebelum memperbarui email
                user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        Log.d("EditAkunActivity", "Reauthenticate berhasil")

                        // Update email di Firebase Authentication
                        user.updateEmail(emailBaru).addOnCompleteListener { emailTask ->
                            if (emailTask.isSuccessful) {
                                Log.d("EditAkunActivity", "Email berhasil diperbarui di Authentication")

                                // Kirim email verifikasi setelah update email
                                user.sendEmailVerification()
                                    .addOnCompleteListener { verifyTask ->
                                        if (verifyTask.isSuccessful) {
                                            Log.d("EditAkunActivity", "Email verifikasi berhasil dikirim.")
                                            Toast.makeText(this, "Periksa email untuk verifikasi", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Log.e("EditAkunActivity", "Gagal mengirim email verifikasi: ${verifyTask.exception?.message}")
                                            Toast.makeText(this, "Gagal mengirim email verifikasi", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                // Perbarui data di Realtime Database
                                val userUpdates = hashMapOf<String, Any>(
                                    "name" to namaPengguna,
                                    "email" to emailBaru
                                )

                                userRef.updateChildren(userUpdates).addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        Log.d("EditAkunActivity", "Data berhasil diperbarui di Realtime Database")
                                        Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Log.e("EditAkunActivity", "Gagal memperbarui data di Realtime Database: ${dbTask.exception?.message}")
                                        Toast.makeText(this, "Gagal memperbarui data di Realtime Database", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Log.e("EditAkunActivity", "Gagal memperbarui email di Authentication: ${emailTask.exception?.message}")
                                Toast.makeText(this, "Gagal memperbarui email di Firebase Authentication", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Log.e("EditAkunActivity", "Gagal reauthenticate: ${reauthTask.exception?.message}")
                        Toast.makeText(this, "Gagal reauthenticate. Periksa password Anda", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Kembali ke halaman sebelumnya
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    // Go to EditProfileActivity when the profile text is clicked
    fun goToEditProfile(view: android.view.View) {
        val intent = Intent(this, EditProfilActivity::class.java)
        startActivity(intent)
    }
}
