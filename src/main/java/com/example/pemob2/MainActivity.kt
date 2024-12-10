package com.example.halaman_tutorial

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private var isFavorite: Boolean = false // Status favorit untuk toggle ikon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi View
        ratingBar = findViewById(R.id.ratingBar)
        val buttonStartCooking: Button = findViewById(R.id.buttonStartCooking)
        val iconDelete: ImageView = findViewById(R.id.iconDelete)
        val iconFavorite: ImageView = findViewById(R.id.iconFavorite)
        val iconShare: ImageView = findViewById(R.id.iconShare)

        // Navigasi ke halaman TutorialActivity
        buttonStartCooking.setOnClickListener {
            val intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
        }

        // Konfirmasi penghapusan resep
        iconDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        // Toggle favorit
        iconFavorite.setOnClickListener {
            toggleFavorite(iconFavorite)
        }

        // Bagikan resep
        iconShare.setOnClickListener {
            shareRecipe()
        }

        // Update rating dengan listener
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            Toast.makeText(this, "Rating diberikan: $rating", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk toggle favorit
    private fun toggleFavorite(iconFavorite: ImageView) {
        isFavorite = !isFavorite
        val color = if (isFavorite) {
            ContextCompat.getColor(this, R.color.red) // Warna untuk status favorit
        } else {
            ContextCompat.getColor(this, R.color.gray) // Warna untuk status non-favorit
        }
        iconFavorite.setColorFilter(color) // Mengubah warna ikon

        val message = if (isFavorite) {
            "Resep ditambahkan ke favorit"
        } else {
            "Resep dihapus dari favorit"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Fungsi untuk membagikan resep
    private fun shareRecipe() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Coba resep Seblak di aplikasi ini!")
        }
        startActivity(Intent.createChooser(shareIntent, "Bagikan ke"))
    }

    // Dialog konfirmasi hapus
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Hapus Resep")
            .setMessage("Apakah Anda yakin ingin menghapus resep ini?")
            .setPositiveButton("Ya") { _, _ ->
                Toast.makeText(this, "Resep dihapus", Toast.LENGTH_SHORT).show()
                // Tambahkan logika penghapusan data jika diperlukan
            }
            .setNegativeButton("Tidak", null)
            .show()
    }
}
