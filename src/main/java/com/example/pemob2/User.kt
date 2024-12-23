package com.example.wistcookapp

data class User(
    val name: String = "", // Nama pengguna
    val email: String = "", // Email pengguna
    val photoUrl: String? = "" // URL foto pengguna (jika ada)
)
