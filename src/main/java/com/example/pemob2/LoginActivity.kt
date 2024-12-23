package com.example.wistcookapp

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var tvForgotPassword: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi UI
        etEmail = findViewById(R.id.et_email_login)
        etPassword = findViewById(R.id.et_password_login)
        btnLogin = findViewById(R.id.btn_login)
        btnRegister = findViewById(R.id.btn_register)
        tvForgotPassword = findViewById(R.id.tv_forgot_password)

        // Inisialisasi FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Set OnClickListener untuk tombol login
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validasi input
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Kata Sandi harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cek kredensial di Firebase Authentication
            loginUser(email, password)
        }

        // Set OnClickListener untuk tombol Buat Akun
        btnRegister.setOnClickListener {
            // Navigasi ke RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Navigasi ke ForgotPasswordActivity saat TextView Lupa Kata Sandi diklik
        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        // Memverifikasi login menggunakan Firebase Authentication
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login berhasil, pindah ke halaman BerandaActivity
                    val intent = Intent(this@LoginActivity, BerandaActivity::class.java)
                    startActivity(intent)
                    finish()  // Menutup LoginActivity agar pengguna tidak bisa kembali
                } else {
                    // Gagal login
                    Toast.makeText(this@LoginActivity, "Kata sandi salah atau pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
