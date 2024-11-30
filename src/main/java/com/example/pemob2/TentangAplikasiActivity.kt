package com.example.pengaturanakun

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class TentangAplikasiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tentang_aplikasi)

        // "Tingkat Lanjut" button click listener
        val tingkatLanjutButton = findViewById<Button>(R.id.btn_tingkat_lanjut)
        tingkatLanjutButton.setOnClickListener {
            val intent = Intent(this, TingkatLanjutActivity::class.java)
            startActivity(intent)
        }

        // Back button click listener
        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            finish() // Returns to the previous activity (AccountActivity)
        }
    }
}
