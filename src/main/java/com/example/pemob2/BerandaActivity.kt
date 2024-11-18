package com.example.pemob2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast

class BerandaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)

        // Temukan ImageView dan tambahkan listener klik
        val imageCari = findViewById<ImageView>(R.id.imageCari)
        imageCari.setOnClickListener {
            // Membuat intent untuk membuka CAriActivity
            val intent = Intent(this, CariActivity::class.java)
            startActivity(intent)
        }

        // Temukan ImageView dengan id imagePosting
        val imagePosting = findViewById<ImageView>(R.id.imagePosting)
        imagePosting.setOnClickListener {
            val intent = Intent(this, PostingActivity::class.java)
            startActivity(intent)
        }

        // Temukan ImageView dengan id imageFavorit
        val imageFavorit = findViewById<ImageView>(R.id.imageFavorit)
        imageFavorit.setOnClickListener {
            val intent = Intent(this, FavoritActivity::class.java)
            startActivity(intent)
        }

        // Temukan ImageView dengan id imageAkun
        val imageAkun = findViewById<ImageView>(R.id.imageAkun)
        imageAkun.setOnClickListener {
            val intent = Intent(this, AkunActivity::class.java)
            startActivity(intent)
        }

        // Inisialisasi tombol-tombol kategori
        val buttonSemua = findViewById<Button>(R.id.buttonSemua)
        val buttonNusantara = findViewById<Button>(R.id.buttonNusantara)
        val buttonSarapan = findViewById<Button>(R.id.buttonSarapan)
        val buttonKue = findViewById<Button>(R.id.buttonKue)
        val buttonPudding = findViewById<Button>(R.id.buttonPudding)
        val buttonDiet = findViewById<Button>(R.id.buttonDiet)

        // Set onClickListener untuk masing-masing tombol
        buttonSemua.setOnClickListener {
            Toast.makeText(this, "Menampilkan Semua Resep", Toast.LENGTH_SHORT).show()
            // Tambahkan logika yang sesuai untuk menampilkan resep kategori "Semua"
            val intent = Intent(this, KatSemuaActivity::class.java)
            startActivity(intent)
        }

        buttonNusantara.setOnClickListener {
            Toast.makeText(this, "Menampilkan Resep Nusantara", Toast.LENGTH_SHORT).show()
            // Tambahkan logika yang sesuai untuk menampilkan resep kategori "Nusantara"
            val intent = Intent(this, KatNusantaraActivity::class.java)
            startActivity(intent)
        }

        buttonSarapan.setOnClickListener {
            Toast.makeText(this, "Menampilkan Resep Sarapan", Toast.LENGTH_SHORT).show()
            // Tambahkan logika yang sesuai untuk menampilkan resep kategori "Sarapan"
            val intent = Intent(this, KatSarapanActivity::class.java)
            startActivity(intent)
        }

        buttonKue.setOnClickListener {
            Toast.makeText(this, "Menampilkan Resep Kue", Toast.LENGTH_SHORT).show()
            // Tambahkan logika yang sesuai untuk menampilkan resep kategori "Kue"
            val intent = Intent(this, KatKueActivity::class.java)
            startActivity(intent)
        }

        buttonPudding.setOnClickListener {
            Toast.makeText(this, "Menampilkan Resep Pudding", Toast.LENGTH_SHORT).show()
            // Tambahkan logika yang sesuai untuk menampilkan resep kategori "Pudding"
            val intent = Intent(this, KatPuddingActivity::class.java)
            startActivity(intent)
        }

        buttonDiet.setOnClickListener {
            Toast.makeText(this, "Menampilkan Resep Menu Diet", Toast.LENGTH_SHORT).show()
            // Tambahkan logika yang sesuai untuk menampilkan resep kategori "Menu Diet"
            val intent = Intent(this, KatMenuDietActivity::class.java)
            startActivity(intent)
        }


    }
}
