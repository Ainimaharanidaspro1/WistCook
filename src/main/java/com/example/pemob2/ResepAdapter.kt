package com.example.wistcookapp

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ResepAdapter(private var recipes: List<Resep>) : RecyclerView.Adapter<ResepAdapter.ResepViewHolder>() {

    // Interface untuk mendeteksi klik pada item
    var onItemClick: ((Resep) -> Unit)? = null

    // Interface untuk menangani klik pada ikon hati
    var onHeartClick: ((Resep) -> Unit)? = null

    private val firebaseDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference // Firebase reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResepViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_resep, parent, false)
        return ResepViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResepViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)

        // Set listener untuk item klik
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(recipe)
        }

        // Set listener untuk ikon hati (love icon) klik
        holder.loveIcon.setOnClickListener {
            // Toggle status favorit
            recipe.isFavorited = !recipe.isFavorited
            // Update status favorit di Firebase dan SharedPreferences
            saveFavoriteStatus(holder.itemView.context, recipe)
            updateFavoriteStatusInFirebase(recipe)

            // Memperbarui status ikon hati
            notifyItemChanged(position)

            // Callback untuk aksi setelah ikon hati diklik
            onHeartClick?.invoke(recipe)
        }
    }

    override fun getItemCount(): Int = recipes.size

    // Metode untuk memperbarui dataset resep
    fun updateRecipes(newRecipes: List<Resep>) {
        recipes = newRecipes
        notifyDataSetChanged() // Memperbarui RecyclerView
    }

    // ViewHolder untuk menampilkan item
    class ResepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewFood: ImageView = itemView.findViewById(R.id.foodImage)
        private val textViewFoodName: TextView = itemView.findViewById(R.id.foodName)
        val loveIcon: ImageView = itemView.findViewById(R.id.loveIcon)  // Ikon hati

        fun bind(recipe: Resep) {
            textViewFoodName.text = recipe.namaResep

            // Decode Base64 dan set image
            if (recipe.fotoMasakan.isNotEmpty()) {
                val decodedImage = Base64.decode(recipe.fotoMasakan, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size)
                imageViewFood.setImageBitmap(bitmap)
            } else {
                imageViewFood.setImageResource(R.drawable.bg_edittext) // Ganti dengan gambar default jika kosong
            }

            // Mengambil status favorit dari SharedPreferences
            val sharedPref = itemView.context.getSharedPreferences("favorite_prefs", Context.MODE_PRIVATE)
            recipe.isFavorited = sharedPref.getBoolean(recipe.namaResep, false)

            // Mengubah status ikon hati (love icon) berdasarkan status favorit
            if (recipe.isFavorited) {
                loveIcon.setImageResource(R.drawable.heart_filled) // Hati penuh (favorit)
            } else {
                loveIcon.setImageResource(R.drawable.heart_outline) // Hati kosong (tidak favorit)
            }
        }
    }

    // Fungsi untuk menyimpan status favorit ke SharedPreferences
    private fun saveFavoriteStatus(context: Context, recipe: Resep) {
        val sharedPref = context.getSharedPreferences("favorite_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(recipe.namaResep, recipe.isFavorited)
            apply()
        }
    }

    // Fungsi untuk memperbarui status favorit di Firebase
    private fun updateFavoriteStatusInFirebase(recipe: Resep) {
        val recipeId = recipe.namaResep // Gunakan namaResep sebagai ID unik untuk masing-masing resep
        val userId = "user123" // Misalnya ID pengguna atau ID sesi pengguna (ganti sesuai kebutuhan)

        // Firebase Realtime Database path
        val favoriteRef = firebaseDatabase.child("favorites").child(userId).child(recipeId)

        // Update status favorit di Firebase
        favoriteRef.setValue(recipe.isFavorited)
    }

    // Fungsi untuk mengambil status favorit dari SharedPreferences
    fun getFavoriteRecipes(context: Context): List<Resep> {
        val sharedPref = context.getSharedPreferences("favorite_prefs", Context.MODE_PRIVATE)
        val favoriteRecipes = mutableListOf<Resep>()

        for (recipe in recipes) {
            if (sharedPref.getBoolean(recipe.namaResep, false)) {
                favoriteRecipes.add(recipe) // Menambahkan resep yang difavoritkan
            }
        }
        return favoriteRecipes
    }
}
