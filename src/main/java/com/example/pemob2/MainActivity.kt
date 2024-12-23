package com.example.wistcookapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Menginisialisasi FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Cek status login pengguna
        if (auth.currentUser != null) {
            // Pengguna sudah login, langsung pindah ke BerandaActivity
            val intent = Intent(this, Main2Activity::class.java)
            startActivity(intent)
            finish() // Menutup MainActivity agar tidak bisa kembali ke halaman login
        } else {
            // Pengguna belum login, tampilkan halaman login atau register
            setupButtons()
        }

        // Mengatur window insets untuk layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupButtons() {
        // Temukan tombol masuk dan daftar
        val masukButton = findViewById<Button>(R.id.masuk_button)
        val registerButton = findViewById<Button>(R.id.register_button)

        // Set onClickListener untuk tombol Masuk
        masukButton.setOnClickListener {
            // Navigasi ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Set onClickListener untuk tombol Daftar
        registerButton.setOnClickListener {
            // Navigasi ke RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
