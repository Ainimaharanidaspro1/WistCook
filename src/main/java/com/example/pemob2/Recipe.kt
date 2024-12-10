package com.example.halaman_tutorial

data class Recipe(
    val title: String,
    val description: String,
    val imageResId: Int, // ID resource gambar
    var isFavorited: Boolean = false // Status favorit
)
