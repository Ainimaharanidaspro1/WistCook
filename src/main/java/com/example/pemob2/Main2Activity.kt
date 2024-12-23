package com.example.wistcookapp

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Main2Activity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        auth = FirebaseAuth.getInstance()

        val textView = findViewById<TextView>(R.id.main_heading) // Mengambil TextView untuk animasi
        val imageView = findViewById<ImageView>(R.id.imageView10)

        // Tambahkan animasi pada TextView dan ImageView
        addTextAnimation(textView)
        addImageAnimation(imageView)

        // Cek apakah pengguna sudah login atau belum
        if (auth.currentUser != null) {
            // Jika sudah login, langsung menuju ke BerandaActivity
            Handler().postDelayed({
                val intent = Intent(this, BerandaActivity::class.java)
                startActivity(intent)
                finish()  // Menutup Main2Activity agar pengguna tidak bisa kembali
            }, 3000)
        } else {
            // Jika belum login, kembali ke halaman login setelah animasi
            Handler().postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()  // Menutup Main2Activity agar pengguna tidak bisa kembali
            }, 3000)
        }
    }

    private fun addTextAnimation(textView: TextView) {
        // Animasi pergerakan teks dari bawah ke atas (translation)
        val translateAnimator = ObjectAnimator.ofFloat(textView, "translationY", 500f, 0f).apply {
            duration = 1500 // Durasi animasi dalam milidetik
            repeatCount = 0 // Hanya sekali
            startDelay = 500 // Menunggu sebelum mulai (untuk efek lebih dramatis)
        }

        // Animasi opacity (fade in) untuk teks
        val fadeInAnimator = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f).apply {
            duration = 1500 // Durasi animasi
            repeatCount = 0 // Hanya sekali
            startDelay = 500 // Menunggu sebelum mulai
        }

        // Animasi scaling teks (zoom in)
        val scaleAnimator = ObjectAnimator.ofFloat(textView, "scaleX", 0.8f, 1f).apply {
            duration = 1500
            repeatCount = 0
            startDelay = 500
        }

        // Jalankan animasi secara bersamaan
        translateAnimator.start()
        fadeInAnimator.start()
        scaleAnimator.start()
    }

    private fun addImageAnimation(imageView: ImageView) {
        // Animasi rotasi ringan pada gambar
        val rotateAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f).apply {
            duration = 1500 // Durasi animasi
            repeatCount = 0 // Hanya sekali
            startDelay = 500 // Menunggu sebelum mulai
        }

        // Animasi scale (zoom in) untuk gambar
        val scaleAnimator = ObjectAnimator.ofFloat(imageView, "scaleX", 0.8f, 1.2f).apply {
            duration = 1500
            repeatCount = 0
            startDelay = 500
        }

        val scaleYAnimator = ObjectAnimator.ofFloat(imageView, "scaleY", 0.8f, 1.2f).apply {
            duration = 1500
            repeatCount = 0
            startDelay = 500
        }

        // Animasi fade out untuk gambar setelah beberapa waktu
        val fadeOutAnimator = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f).apply {
            duration = 500 // Durasi animasi fade out
            startDelay = 2500 // Mulai setelah animasi selesai
        }

        // Menjalankan semua animasi bersama-sama
        rotateAnimator.start()
        scaleAnimator.start()
        scaleYAnimator.start()

        // Menjalankan fade out setelah beberapa detik
        fadeOutAnimator.start()
    }
}
