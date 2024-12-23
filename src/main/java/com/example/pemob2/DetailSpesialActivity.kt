package com.example.wistcookapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailSpesialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_spesial)

        val imageView = findViewById<ImageView>(R.id.imageView19)

        // Menangani klik pada ImageView
        imageView.setOnClickListener {
            // Kembali ke halaman sebelumnya
            onBackPressed()  // Fungsi ini akan mengarahkan ke aktivitas sebelumnya dalam stack
        }

        // Get the data passed through the intent, using default values if null
        val foodName = intent.getStringExtra("food_name") ?: "No Name"
        val foodDescription = intent.getStringExtra("food_description") ?: "No Description"
        val foodImageResId = intent.getIntExtra("food_image", 0)
        val foodIngredients = intent.getStringExtra("food_ingredients") ?: "No Ingredients"
        val foodSteps = intent.getStringExtra("food_steps") ?: "No Steps"

        // Find the views and set the data
        findViewById<TextView>(R.id.foodName).text = foodName
        findViewById<TextView>(R.id.foodDescription).text = foodDescription

        // Only set the image if the resource ID is valid
        if (foodImageResId != 0) {
            findViewById<ImageView>(R.id.foodImage).setImageResource(foodImageResId)
        } else {
            // Optional: Use a placeholder image if resource ID is invalid
            findViewById<ImageView>(R.id.foodImage).setImageResource(R.drawable.bolupisang)
        }

        findViewById<TextView>(R.id.ingredients).text = foodIngredients
        findViewById<TextView>(R.id.steps).text = foodSteps

        // Get the share icon view
        val shareIcon = findViewById<ImageView>(R.id.imageView21)

        // Set a click listener on the share icon
        shareIcon.setOnClickListener {
            shareFoodDetails(foodName, foodDescription, foodIngredients, foodSteps)
        }
    }

    private fun shareFoodDetails(name: String?, description: String?, ingredients: String?, steps: String?) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Lihat Resep Ini:\n\n$name\n\n$description\n")
            type = "text/plain"
        }

        // Verify that the intent resolves to an app
        if (shareIntent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }
}
