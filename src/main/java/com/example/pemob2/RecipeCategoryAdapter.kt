package com.example.halaman_research.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.halaman_research.R
import com.example.halaman_research.model.RecipeCategory

class RecipeCategoryAdapter(
    private val categories: List<RecipeCategory>,
    private val onItemClick: (RecipeCategory) -> Unit // Callback untuk klik item
) : RecyclerView.Adapter<RecipeCategoryAdapter.CategoryViewHolder>() {

    // ViewHolder class untuk menghubungkan tampilan item
    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryImage: ImageView = view.findViewById(R.id.categoryImage)
        val categoryName: TextView = view.findViewById(R.id.categoryName)

        // Menghubungkan data ke tampilan dan menangani klik
        fun bind(category: RecipeCategory) {
            categoryName.text = category.name
            categoryImage.setImageResource(category.imageResId)

            // Handle klik pada item
            itemView.setOnClickListener {
                onItemClick(category)
            }
        }
    }

    // Membuat tampilan untuk setiap item dalam RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_category, parent, false)
        return CategoryViewHolder(view)
    }

    // Menyambungkan data ke ViewHolder
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category) // Bind data ke ViewHolder
    }

    // Mengembalikan jumlah item yang ada
    override fun getItemCount(): Int = categories.size
}
