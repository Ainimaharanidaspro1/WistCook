package com.example.wistcookapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class DetailResepActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_resep)

        val imageView = findViewById<ImageView>(R.id.imageView19)

        // Menangani klik pada ImageView
        imageView.setOnClickListener {
            // Kembali ke halaman sebelumnya
            onBackPressed()  // Fungsi ini akan mengarahkan ke aktivitas sebelumnya dalam stack
        }

        // Inisialisasi View
        val titleRecipe = findViewById<TextView>(R.id.titleRecipe)
        val descriptionRecipe = findViewById<TextView>(R.id.descriptionRecipe)
        val ingredientsRecipe = findViewById<TextView>(R.id.ingredientsRecipe)
        val stepsRecipe = findViewById<TextView>(R.id.stepsRecipe)
        val imageRecipe = findViewById<ImageView>(R.id.imageRecipe)
        val shareIcon = findViewById<ImageView>(R.id.imageView21)  // Icon Share

        // Ambil data resep dari Intent
        val recipeName = intent.getStringExtra("recipe_name")
        val recipeDescription = intent.getStringExtra("recipe_description")
        val recipeIngredients = intent.getStringExtra("recipe_ingredients")
        val recipeSteps = intent.getStringExtra("recipe_steps")
        val recipeImage = intent.getStringExtra("recipe_image")

        // Set data ke UI
        titleRecipe.text = recipeName ?: "Nama resep tidak tersedia"
        descriptionRecipe.text = recipeDescription ?: "Deskripsi tidak tersedia"
        ingredientsRecipe.text = recipeIngredients ?: "Bahan-bahan tidak tersedia"
        stepsRecipe.text = recipeSteps ?: "Langkah-langkah tidak tersedia"

        // Decode gambar dari Base64 dan tampilkan
        recipeImage?.let {
            val decodedImage = Base64.decode(it, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size)
            imageRecipe.setImageBitmap(bitmap)
        }

        // Tombol Share Resep
        shareIcon.setOnClickListener {
            shareRecipe(recipeName, recipeDescription, recipeImage)
        }
    }

    // Fungsi Share Resep
    private fun shareRecipe(name: String?, description: String?, imageBase64: String?) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"

        // Membuat pesan yang hanya berisi instruksi untuk cek resep di WistCook dan kemudian menampilkan nama dan deskripsi
        val shareBody = """
        Ayo cek resep ini di Wistcook!

        $name

        $description
    """.trimIndent()

        // Setel subjek dan teks yang akan dibagikan
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Bagikan Resep: $name")
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)

        // Mulai aktivitas berbagi
        startActivity(Intent.createChooser(shareIntent, "Bagikan Resep via"))
    }

}
