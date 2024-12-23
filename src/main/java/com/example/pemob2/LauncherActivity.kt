package com.example.wistcookapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cek status login dari SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        // Arahkan ke Main2Activity jika sudah login, atau MainActivity jika belum
        val intent = if (isLoggedIn) {
            Intent(this, Main2Activity::class.java)  // Pengguna sudah login
        } else {
            Intent(this, MainActivity::class.java)  // Pengguna belum login
        }

        startActivity(intent)

        // Menutup LauncherActivity agar tidak bisa kembali ke halaman ini
        finish()
    }
}
