package com.example.halaman_tutorial

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TutorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        // Atur RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recommendationRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Gunakan Adapter dengan data rekomendasi resep
        val adapter = RecommendationAdapter(this, getRecommendationData())
        recyclerView.adapter = adapter
    }

    // Fungsi untuk mendapatkan data rekomendasi
    private fun getRecommendationData(): MutableList<Recipe> {
        return mutableListOf(
            Recipe("Seblak Pedas", "Kuah pedas menggugah selera", R.drawable.puding_coklat),
            Recipe("Sate Ayam", "Sate ayam dengan bumbu kacang spesial", R.drawable.puding_coklat),
            Recipe("Rendang", "Rendang asli Minang yang kaya rempah", R.drawable.puding_coklat),
            Recipe("Nasi Goreng", "Nasi goreng kampung yang lezat", R.drawable.puding_coklat),
            Recipe("Bakso", "Bakso sapi dengan kuah hangat", R.drawable.puding_coklat)
        )
    }
}
