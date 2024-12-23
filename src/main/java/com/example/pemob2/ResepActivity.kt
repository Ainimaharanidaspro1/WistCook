package com.example.wistcookapp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ResepActivity : AppCompatActivity() {

    private var isFavorite = false
    private var isRated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda) // Ganti dengan layout file Anda

        val imageViewHeart: ImageView = findViewById(R.id.imageFavorit)


        imageViewHeart.setOnClickListener {
            // Toggle favorit
            isFavorite = !isFavorite
            imageViewHeart.isSelected = isFavorite
        }
    }
}
