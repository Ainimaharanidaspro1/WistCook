package com.example.pemob2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Temukan tombol login dan register
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnRegister = findViewById<Button>(R.id.btn_register)
        val tvForgotPassword = findViewById<TextView>(R.id.tv_forgot_password)

        // Set onClickListener untuk tombol Masuk
        btnLogin.setOnClickListener {
            // Navigasi ke BerandaActivity
            val intent = Intent(this, BerandaActivity::class.java)
            startActivity(intent)
        }

        // Set onClickListener untuk tombol Buat Akun
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
}

