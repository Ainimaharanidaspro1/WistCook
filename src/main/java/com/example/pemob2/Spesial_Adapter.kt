package com.example.wistcookapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SpecialAdapter(
    private val foodList: List<Spesial>,
    val onHeartClick: ((Spesial) -> Unit)? = null // Listener untuk klik ikon hati
) : RecyclerView.Adapter<SpecialAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_resep, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]

        holder.foodName.text = food.name
        holder.foodImage.setImageResource(food.imageResId)

        // Menambahkan klik listener untuk ikon hati (favorit)
        holder.heartIcon.setOnClickListener {
            food.isFavorited = !food.isFavorited

            // Menyimpan status favorit di SharedPreferences dan detail makanan
            val sharedPref = holder.itemView.context.getSharedPreferences(
                "favorite_prefs",
                Context.MODE_PRIVATE
            )
            with(sharedPref.edit()) {
                putBoolean(food.name, food.isFavorited)
                if (food.isFavorited) {
                    putString("${food.name}_description", food.description)
                    putInt("${food.name}_image", food.imageResId)
                    putString("${food.name}_ingredients", food.ingredients)
                    putString("${food.name}_steps", food.steps)
                } else {
                    remove("${food.name}_description")
                    remove("${food.name}_image")
                    remove("${food.name}_ingredients")
                    remove("${food.name}_steps")
                }
                apply()
            }

            // Memperbarui ikon hati setelah perubahan status favorit
            if (food.isFavorited) {
                holder.heartIcon.setImageResource(R.drawable.heart_filled)
            } else {
                holder.heartIcon.setImageResource(R.drawable.heart_outline)
            }

            onHeartClick?.invoke(food)
        }

        // Memperbarui ikon hati berdasarkan status favorit dari SharedPreferences
        val sharedPref = holder.itemView.context.getSharedPreferences("favorite_prefs", Context.MODE_PRIVATE)
        food.isFavorited = sharedPref.getBoolean(food.name, false)
        if (food.isFavorited) {
            holder.heartIcon.setImageResource(R.drawable.heart_filled)
        } else {
            holder.heartIcon.setImageResource(R.drawable.heart_outline)
        }

        // Menambahkan klik listener untuk melihat detail resep
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailSpesialActivity::class.java)

            // Mengirim data ke activity DetailSpesial
            intent.putExtra("food_name", food.name)
            intent.putExtra("food_description", food.description)
            intent.putExtra("food_image", food.imageResId)
            intent.putExtra("food_ingredients", food.ingredients)
            intent.putExtra("food_steps", food.steps)

            // Memulai activity DetailSpesialActivity untuk menampilkan resep
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = foodList.size

    // ViewHolder untuk item RecyclerView
    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodName: TextView = itemView.findViewById(R.id.foodName)
        val foodImage: ImageView = itemView.findViewById(R.id.foodImage)
        val heartIcon: ImageView = itemView.findViewById(R.id.loveIcon)
    }
}
