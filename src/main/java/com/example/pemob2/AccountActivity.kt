package com.example.pengaturanakun

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pengaturanakun.databinding.ActivityAccountBinding

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val kategoriTextView = findViewById<TextView>(R.id.btn_kategori)
        val editAccountTextView = findViewById<TextView>(R.id.btn_edit_account)
        val tentangTextView = findViewById<TextView>(R.id.btn_tentang)
        val umpanBalikTextView = findViewById<TextView>(R.id.btn_umpanbalik)
        val faqTextView = findViewById<TextView>(R.id.btn_faqq)
        val resepTextView = findViewById<TextView>(R.id.btn_resep) // Button untuk Resepmu
        val favoriteTextView = findViewById<TextView>(R.id.btn_favorite)

        // Set up dialog for kategori
        val kategoriItems = resources.getStringArray(R.array.kategori_menu)
        kategoriTextView.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pilih Kategori Menu")
            builder.setItems(kategoriItems) { _, which ->
                Toast.makeText(this, "Kategori dipilih: ${kategoriItems[which]}", Toast.LENGTH_SHORT).show()
            }
            builder.show()
        }

        // Navigate to EditAccountActivity
        editAccountTextView.setOnClickListener {
            val intent = Intent(this, EditAccountActivity::class.java)
            startActivity(intent)
        }

        // Navigate to TentangAplikasiActivity
        tentangTextView.setOnClickListener {
            val intent = Intent(this, TentangAplikasiActivity::class.java)
            startActivity(intent)
        }

        // Navigate to FeedbackActivity
        umpanBalikTextView.setOnClickListener {
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
        }

        // Navigate to FAQsActivity
        faqTextView.setOnClickListener {
            val intent = Intent(this, FaqsActivity::class.java)
            startActivity(intent)
        }

        // Navigate to ResepmuActivity
        resepTextView.setOnClickListener {
            val intent = Intent(this, ResepmuActivity::class.java)
            startActivity(intent)
        }
        // Navigate to ResepFavoritActivity
        favoriteTextView.setOnClickListener {
            val intent = Intent(this, ResepFavoritActivity::class.java)
            startActivity(intent)
        }
    }
}
