package com.example.halaman_tutorial

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecommendationAdapter(
    private val context: Context,
    private val recommendations: MutableList<Recipe>
) : RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.titleRecipe) // Sesuaikan dengan ID di XML
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionRecipe) // Sesuaikan dengan ID di XML
        val imageRecipe: ImageView = view.findViewById(R.id.imageRecipe) // Menambahkan ImageView untuk gambar
        val iconFavorite: ImageView = view.findViewById(R.id.iconFavorite) // Ikon favorit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recommendation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recommendations[position]
        holder.titleTextView.text = recipe.title
        holder.descriptionTextView.text = recipe.description

        // Menggunakan gambar untuk setiap resep
        holder.imageRecipe.setImageResource(recipe.imageResId)

        // Mengatur ikon favorit
        holder.iconFavorite.setImageResource(
            if (recipe.isFavorited) R.drawable.ic_favorite // Ikon favorit aktif
            else R.drawable.ic_favorite// Ikon favorit non-aktif
        )

        holder.iconFavorite.setOnClickListener {
            // Toggle status favorit dan update gambar
            recipe.isFavorited = !recipe.isFavorited
            holder.iconFavorite.setImageResource(
                if (recipe.isFavorited) R.drawable.ic_favorite
                else R.drawable.ic_favorite
            )
        }
    }

    override fun getItemCount(): Int {
        return recommendations.size
    }
}
