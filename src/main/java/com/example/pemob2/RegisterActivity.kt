package com.example.pemob2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Temukan tombol daftar dan sudah memiliki akun
        val btnRegisterSubmit = findViewById<Button>(R.id.btn_register_submit)
        val btnAlreadyHaveAccount = findViewById<Button>(R.id.btn_already_have_account)

        // Set onClickListener untuk tombol Daftar
        btnRegisterSubmit.setOnClickListener {
            // Navigasi ke BerandaActivity
            val intent = Intent(this, BerandaActivity::class.java)
            startActivity(intent)
            finish()  // Menutup RegisterActivity agar tidak kembali ke sini saat menekan tombol kembali
        }

        // Set onClickListener untuk tombol Sudah Memiliki Akun
        btnAlreadyHaveAccount.setOnClickListener {
            // Navigasi ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Menutup RegisterActivity agar tidak kembali ke sini saat menekan tombol kembali
        }
    }
}

