package com.example.wistcookapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val favoriteList = mutableListOf<Spesial>()
    private lateinit var adapter: SpecialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorit)

        val backArrow: ImageView = findViewById(R.id.backArrow)

        // Set click listener untuk back arrow
        backArrow.setOnClickListener {
            // Kembali ke aktivitas sebelumnya
            onBackPressed() // Atau bisa menggunakan finish()
        }

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("favorite_prefs", Context.MODE_PRIVATE)

        // Load item favorit dari SharedPreferences
        loadFavoriteItems()

        // Setup RecyclerView untuk menampilkan favorit
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewResepFavorit)

        // Menggunakan GridLayoutManager dengan 2 kolom (bisa diubah sesuai kebutuhan)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        adapter = SpecialAdapter(favoriteList) { food ->
            // Handle klik ikon hati di adapter
            Toast.makeText(this, "${food.name} favorit diubah", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
    }

    private fun loadFavoriteItems() {
        // Ambil semua data yang disimpan di SharedPreferences
        val allFavorites = sharedPreferences.all
        Log.d("FavoritActivity", "Total item di SharedPreferences: ${allFavorites.size}")

        for ((key, value) in allFavorites) {
            if (value == true) { // Jika item favorit, tambahkan ke list
                // Mengambil semua detail yang disimpan sebelumnya dengan format kunci yang benar
                val description = sharedPreferences.getString("${key}_description", "Deskripsi default") ?: "Deskripsi default"
                val imageResId = sharedPreferences.getInt("${key}_image", R.drawable.nasi_goreng) // ID gambar default
                val ingredients = sharedPreferences.getString("${key}_ingredients", "Bahan default") ?: "Bahan default"
                val steps = sharedPreferences.getString("${key}_steps", "Langkah-langkah default") ?: "Langkah-langkah default"

                Log.d("FavoritActivity", "Memuat favorit: $key, description: $description, ingredients: $ingredients, steps: $steps")

                val food = Spesial(
                    name = key,
                    description = description,
                    imageResId = imageResId,
                    ingredients = ingredients,
                    steps = steps,
                    isFavorited = true // Tandai sebagai favorit
                )

                // Menambahkan ke list favorit
                favoriteList.add(food)
            }
        }

        // Jika tidak ada data favorit
        if (favoriteList.isEmpty()) {
            Log.d("FavoritActivity", "Tidak ada item favorit yang ditemukan")
        }
    }
}
