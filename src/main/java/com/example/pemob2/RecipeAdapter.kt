package com.example.pengaturanakun

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeAdapter(private val recipes: List<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImage: ImageView = itemView.findViewById(R.id.recipeImage)
        val recipeName: TextView = itemView.findViewById(R.id.recipeName)
        val ratingText: TextView = itemView.findViewById(R.id.ratingText)
        val viewRecipeButton: Button = itemView.findViewById(R.id.viewRecipeButton)

        init {
            viewRecipeButton.setOnClickListener {
                // Implement action to view the recipe details
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.recipeImage.setImageResource(recipe.imageResId)
        holder.recipeName.text = recipe.name
        holder.ratingText.text = recipe.rating.toString()
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}
