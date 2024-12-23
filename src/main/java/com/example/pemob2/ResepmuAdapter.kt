package com.example.wistcookapp

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ResepmuAdapter(
    private val resepList: MutableList<Resep>,
    private val onRecipeClick: (Resep) -> Unit,
    private val onDeleteClick: (Resep) -> Unit
) : RecyclerView.Adapter<ResepmuAdapter.ResepViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResepViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_resepmu, parent, false)
        return ResepViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ResepViewHolder, position: Int) {
        val currentResep = resepList[position]

        // Bind data to views
        holder.tvRecipeName.text = currentResep.namaResep
        holder.tvRecipeDescription.text = currentResep.deskripsi

        // Menampilkan gambar
        val fotoBase64 = currentResep.fotoMasakan
        if (fotoBase64.isNotEmpty()) {
            val decodedBitmap = decodeBase64(fotoBase64)
            if (decodedBitmap != null) {
                holder.ivRecipeImage.setImageBitmap(decodedBitmap)
            } else {
                Glide.with(holder.itemView.context)
                    .load(R.drawable.ic_launcher_background) // Placeholder image
                    .into(holder.ivRecipeImage)
            }
        } else {
            // Jika fotoMasakan adalah URL atau path gambar
            Glide.with(holder.itemView.context)
                .load(currentResep.fotoMasakan)
                .placeholder(R.drawable.ic_launcher_background) // Placeholder image
                .into(holder.ivRecipeImage)
        }

        // Set click listener for the item
        holder.itemView.setOnClickListener {
            onRecipeClick(currentResep)
        }

        // Set click listener for the delete icon
        holder.ivDeleteIcon.setOnClickListener {
            onDeleteClick(currentResep)
        }
    }

    override fun getItemCount(): Int {
        return resepList.size
    }

    class ResepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivRecipeImage: ImageView = itemView.findViewById(R.id.foodImage)
        val tvRecipeName: TextView = itemView.findViewById(R.id.foodName)
        val tvRecipeDescription: TextView = itemView.findViewById(R.id.foodDescription)
        val ivDeleteIcon: ImageView = itemView.findViewById(R.id.imageViewDelete)
    }

    // Fungsi untuk mendekode gambar Base64 menjadi Bitmap
    private fun decodeBase64(base64String: String): Bitmap? {
        return try {
            val decodedString = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
            android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
