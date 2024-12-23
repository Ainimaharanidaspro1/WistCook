package com.example.wistcookapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class KatMenuDietActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var realtimeDatabase: FirebaseDatabase
    private lateinit var recyclerViewResep: RecyclerView
    private lateinit var resepAdapter: ResepAdapter
    private val resepList = mutableListOf<Resep>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kat_menudiet)

        findViewById<ImageView>(R.id.imageView17).setOnClickListener {
            startActivity(Intent(this, BerandaActivity::class.java)) // Navigasi ke Home
        }

        findViewById<ImageView>(R.id.imageCari).setOnClickListener {
            startActivity(Intent(this, CariActivity::class.java)) // Navigasi ke Cari
        }

        findViewById<ImageView>(R.id.imagePosting).setOnClickListener {
            startActivity(Intent(this, PostingActivity::class.java)) // Navigasi ke Posting
        }

        findViewById<ImageView>(R.id.imageFavorit).setOnClickListener {
            startActivity(Intent(this, FavoritActivity::class.java)) // Navigasi ke Favorit
        }

        findViewById<ImageView>(R.id.imageAkun).setOnClickListener {
            startActivity(Intent(this, AkunActivity::class.java)) // Navigasi ke Akun
        }

        findViewById<ImageView>(R.id.imageView11).setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        // Firebase initialization
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        realtimeDatabase = FirebaseDatabase.getInstance("https://wistcook-b7705-default-rtdb.asia-southeast1.firebasedatabase.app/")

        val greetingTextView = findViewById<TextView>(R.id.textView2)
        
        val userId = auth.currentUser?.uid

        if (userId != null) {
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val fetchedName = document.getString("nama") ?: "Pengguna"
                        greetingTextView.text = "Halo, $fetchedName"
                    } else {
                        fetchFromRealtimeDatabase(greetingTextView, userId)
                    }
                }
                .addOnFailureListener {
                    fetchFromRealtimeDatabase(greetingTextView, userId)
                }
        } else {
            greetingTextView.text = "Halo, Pengguna"
        }

        recyclerViewResep = findViewById(R.id.recyclerViewResep)
        recyclerViewResep.layoutManager = GridLayoutManager(this, 2)

        resepAdapter = ResepAdapter(resepList)
        recyclerViewResep.adapter = resepAdapter

        resepAdapter.onItemClick = { resep ->
            val intent = Intent(this, DetailResepActivity::class.java)
            intent.putExtra("recipe_name", resep.namaResep)
            intent.putExtra("recipe_description", resep.deskripsi)
            intent.putExtra("recipe_ingredients", resep.bahanResep)
            intent.putExtra("recipe_steps", resep.caraMasakResep)
            intent.putExtra("recipe_image", resep.fotoMasakan)
            startActivity(intent)
        }
        
        findViewById<Button>(R.id.buttonDiet).setOnClickListener {
            resetButtonStyles()
            val buttonDiet = findViewById<Button>(R.id.buttonDiet)
            buttonDiet.setBackgroundColor(Color.parseColor("#6E3F6D"))
            buttonDiet.setTextColor(Color.WHITE)
            loadRecipesByCategory("Menu Diet")
        }

        findViewById<Button>(R.id.buttonSarapan).setOnClickListener {
            resetButtonStyles()
            val buttonSarapan = findViewById<Button>(R.id.buttonSarapan)
            buttonSarapan.setBackgroundColor(Color.parseColor("#6E3F6D"))
            buttonSarapan.setTextColor(Color.WHITE)
            loadRecipesByCategory("Sarapan")
        }

        findViewById<Button>(R.id.buttonNusantara).setOnClickListener {
            resetButtonStyles()
            val buttonNusantara = findViewById<Button>(R.id.buttonNusantara)
            buttonNusantara.setBackgroundColor(Color.parseColor("#6E3F6D"))
            buttonNusantara.setTextColor(Color.WHITE)
            loadRecipesByCategory("Nusantara")
        }

        findViewById<Button>(R.id.buttonKue).setOnClickListener {
            resetButtonStyles()
            val buttonKue = findViewById<Button>(R.id.buttonKue)
            buttonKue.setBackgroundColor(Color.parseColor("#6E3F6D"))
            buttonKue.setTextColor(Color.WHITE)
            loadRecipesByCategory("Kue")
        }

        findViewById<Button>(R.id.buttonPudding).setOnClickListener {
            resetButtonStyles()
            val buttonPudding = findViewById<Button>(R.id.buttonPudding)
            buttonPudding.setBackgroundColor(Color.parseColor("#6E3F6D"))
            buttonPudding.setTextColor(Color.WHITE)
            loadRecipesByCategory("Pudding")
        }

        findViewById<ImageView>(R.id.imageView).setOnClickListener {
            val intent = Intent(this, CariActivity::class.java)
            startActivity(intent)
        }

        // Load recipes by default category
        loadRecipesByCategory("Diet")
    }
    
    private fun fetchFromRealtimeDatabase(greetingTextView: TextView, userId: String) {
        val userRef = realtimeDatabase.getReference("users/$userId")
        userRef.get()
            .addOnSuccessListener { dataSnapshot ->
                val fetchedName = dataSnapshot.child("name").getValue(String::class.java) ?: "Pengguna"
                greetingTextView.text = "Halo, $fetchedName"
            }
            .addOnFailureListener { exception ->
                Log.e("DatabaseError", "Gagal Mengambil Data Pengguna", exception)
                greetingTextView.text = "Halo, Pengguna"
            }
    }

    private fun resetButtonStyles() {
        val buttonSarapan = findViewById<Button>(R.id.buttonSarapan)
        val buttonNusantara = findViewById<Button>(R.id.buttonNusantara)
        val buttonKue = findViewById<Button>(R.id.buttonKue)
        val buttonPudding = findViewById<Button>(R.id.buttonPudding)
        val buttonDiet = findViewById<Button>(R.id.buttonDiet)

        buttonSarapan.setBackgroundColor(Color.TRANSPARENT)
        buttonSarapan.setTextColor(Color.BLACK)
        buttonNusantara.setBackgroundColor(Color.TRANSPARENT)
        buttonNusantara.setTextColor(Color.BLACK)
        buttonKue.setBackgroundColor(Color.TRANSPARENT)
        buttonKue.setTextColor(Color.BLACK)
        buttonPudding.setBackgroundColor(Color.TRANSPARENT)
        buttonPudding.setTextColor(Color.BLACK)
        buttonDiet.setBackgroundColor(Color.TRANSPARENT)
        buttonDiet.setTextColor(Color.BLACK)
    }

    private fun loadRecipesByCategory(category: String) {
        val database = FirebaseDatabase.getInstance("https://wistcook-b7705-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val recipesRef = database.reference.child("resep")

        recipesRef.orderByChild("kategori").equalTo(category).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    resepList.clear()  
                    for (data in snapshot.children) {
                        val recipe = data.getValue(Resep::class.java)
                        recipe?.let {
                            val ingredientsList = it.bahanResep.split(",")  
                            val stepsList = it.caraMasakResep.split("\n")  

                            val updatedRecipe = it.copy(
                                bahanResep = ingredientsList.joinToString("\n"),
                                caraMasakResep = stepsList.joinToString("\n")
                            )

                            resepList.add(updatedRecipe)
                        }
                    }
                    resepAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@KatMenuDietActivity, "Resep Tidak Ditemukan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@KatMenuDietActivity, "Gagal Memuat Resep", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
