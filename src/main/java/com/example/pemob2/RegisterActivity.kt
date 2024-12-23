package com.example.wistcookapp

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inisialisasi FirebaseAuth dan Firebase Realtime Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://wistcook-b7705-default-rtdb.asia-southeast1.firebasedatabase.app/")

        val etNama = findViewById<EditText>(R.id.et_nama_register)
        val etEmail = findViewById<EditText>(R.id.et_email_register)
        val etPassword = findViewById<EditText>(R.id.et_password_register)
        val btnRegisterSubmit = findViewById<Button>(R.id.btn_register_submit)
        val btnAlreadyHaveAccount = findViewById<Button>(R.id.btn_already_have_account)

        // Logika Tombol Daftar
        btnRegisterSubmit.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || nama.isEmpty()) {
                Toast.makeText(this, "Nama, Email, dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                // Registrasi menggunakan Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            userId?.let {
                                // Membuat data pengguna menggunakan data class User
                                val user = User(name = nama, email = email)

                                // Simpan data pengguna ke Firebase Realtime Database
                                val userRef = database.reference.child("users").child(it)
                                userRef.setValue(user)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()

                                        // Pindah ke halaman LoginActivity setelah sukses
                                        val intent = Intent(this, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Gagal menyimpan data pengguna: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            // Menampilkan pesan error jika registrasi gagal
                            Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        // Logika Tombol Sudah Memiliki Akun
        btnAlreadyHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
