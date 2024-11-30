package com.example.pengaturanakun

import Resep
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ResepAdapter(private val context: Context, private val resepList: List<Resep>) :
    RecyclerView.Adapter<ResepAdapter.ResepViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResepViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_resep, parent, false)
        return ResepViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResepViewHolder, position: Int) {
        val resep = resepList[position]
        holder.namaResep.text = resep.nama
        holder.deskripsiResep.text = resep.deskripsi

        // Handle klik pada menu titik tiga
        holder.btnMenuResep.setOnClickListener { showPopupMenu(it, resep) }
    }

    override fun getItemCount(): Int = resepList.size

    private fun showPopupMenu(view: View, resep: Resep) {
        val popup = PopupMenu(context, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_resep, popup.menu)
        popup.setOnMenuItemClickListener { item -> handleMenuItemClick(item, resep) }
        popup.show()
    }

    private fun handleMenuItemClick(item: MenuItem, resep: Resep): Boolean {
        return when (item.itemId) {
            R.id.menu_hapus -> {
                Toast.makeText(context, "Resep ${resep.nama} dihapus", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_bagikan -> {
                Toast.makeText(context, "Membagikan resep ${resep.nama}", Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }

    class ResepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaResep: TextView = itemView.findViewById(R.id.textViewNamaResep)
        val deskripsiResep: TextView = itemView.findViewById(R.id.textViewDeskripsiResep)
        val btnMenuResep: ImageView = itemView.findViewById(R.id.btnMenuResep)
    }
}
