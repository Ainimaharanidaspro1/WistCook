package com.example.wistcookapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class TentangAplikasiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tentang_aplikasi)

        // Ambil referensi dari ImageView
        val backArrow: ImageView = findViewById(R.id.backArrow)

        // Tambahkan onClickListener
        backArrow.setOnClickListener {
            onBackPressed() // Kembali ke halaman sebelumnya
        }

    }
}
