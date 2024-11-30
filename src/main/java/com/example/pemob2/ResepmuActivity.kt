package com.example.pengaturanakun

import Resep
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ResepmuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resepmu)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewResep)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Contoh data resep
        val resepList = listOf(
            Resep("Es Krim Coklat", "Dengan tekstur yang lembut dan rasa coklat yang kaya..."),
            Resep("Puding Stroberi", "Dengan tekstur yang lembut dan creamy...")
        )
        // Back Arrow Setup
        val backArrow = findViewById<ImageView>(R.id.backArrow)
        backArrow.setOnClickListener {
            finish()
        }

        val adapter = ResepAdapter(this, resepList)
        recyclerView.adapter = adapter
    }
}
