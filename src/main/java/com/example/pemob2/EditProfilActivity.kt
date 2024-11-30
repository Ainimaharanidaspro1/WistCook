package com.example.pengaturanakun

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class EditProfilActivity : AppCompatActivity() {

    private lateinit var btnGaleriFoto: Button
    private lateinit var btnAmbilFoto: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil) // Ganti dengan layout yang sesuai

        val backButton = findViewById<ImageView>(R.id.backButton)

        // Handle back button click to return to the previous activity
        backButton.setOnClickListener {
            finish() // Close this activity and go back
        }

        // Inisialisasi tombol
        btnGaleriFoto = findViewById(R.id.btnGaleriFoto)
        btnAmbilFoto = findViewById(R.id.btnAmbilFoto)

        // Listener untuk tombol Galeri Foto
        btnGaleriFoto.setOnClickListener {
            // Arahkan ke EditProfilActivity
            val intent = Intent(this, EditProfilActivity::class.java)
            startActivity(intent)
        }

        // Listener untuk tombol Ambil Foto
        btnAmbilFoto.setOnClickListener {
            // Arahkan ke EditProfilActivity
            val intent = Intent(this, EditProfilActivity::class.java)
            startActivity(intent)
        }

    }
}
