package com.example.wistcookapp

import android.content.Intent
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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class BerandaActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var realtimeDatabase: FirebaseDatabase
    private lateinit var recyclerViewFood: RecyclerView
    private lateinit var specialAdapter: SpecialAdapter
    private var favoriteRecipes = mutableListOf<Spesial>() // Menyimpan daftar favorit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)


        // Firebase initialization
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        realtimeDatabase = FirebaseDatabase.getInstance("https://wistcook-b7705-default-rtdb.asia-southeast1.firebasedatabase.app/")

        // Set greeting message
        setGreetingMessage()

        // Set up RecyclerView for food list
        setupRecyclerView()

        // Add listeners for navigation buttons and category buttons
        setupClickListeners()

        findViewById<ImageView>(R.id.imageView).setOnClickListener {
            val intent = Intent(this, CariActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.imageFavorit).setOnClickListener {
            // Pindah ke FavoritActivity dengan mengirimkan daftar favorit
            val intent = Intent(this, FavoritActivity::class.java)
            intent.putParcelableArrayListExtra("favoriteRecipes", ArrayList(favoriteRecipes))
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.imageView11).setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setGreetingMessage() {
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
    }

    private fun fetchFromRealtimeDatabase(greetingTextView: TextView, userId: String) {
        val userRef = realtimeDatabase.getReference("users/$userId")
        userRef.get()
            .addOnSuccessListener { dataSnapshot ->
                val fetchedName = dataSnapshot.child("name").getValue(String::class.java) ?: "Pengguna"
                greetingTextView.text = "Halo, $fetchedName"
            }
            .addOnFailureListener { exception ->
                Log.e("DatabaseError", "Failed to fetch user data", exception)
                greetingTextView.text = "Halo, Pengguna"
            }
    }

    private fun setupRecyclerView() {
        recyclerViewFood = findViewById(R.id.recyclerViewSpesial)

        // Set GridLayoutManager untuk RecyclerView
        val numberOfColumns = 2 // Sesuaikan jumlah kolom
        val gridLayoutManager = GridLayoutManager(this, numberOfColumns)
        recyclerViewFood.layoutManager = gridLayoutManager

        val foodNames = resources.getStringArray(R.array.food_name)
        val foodDescriptions = resources.getStringArray(R.array.food_description)
        val foodImages = resources.obtainTypedArray(R.array.food_image)
        val foodIngredients = resources.getStringArray(R.array.food_ingredients)
        val foodSteps = resources.getStringArray(R.array.food_steps)

        val foodList = mutableListOf<Spesial>()
        for (i in foodNames.indices) {
            foodList.add(
                Spesial(
                    foodNames[i],
                    foodDescriptions[i],
                    foodImages.getResourceId(i, -1),
                    foodIngredients[i],
                    foodSteps[i]
                )
            )
        }
        foodImages.recycle()

        specialAdapter = SpecialAdapter(foodList) { recipe ->
            // Toggle favorit status
            recipe.isFavorited = !recipe.isFavorited
            if (recipe.isFavorited) {
                favoriteRecipes.add(recipe)
            } else {
                favoriteRecipes.remove(recipe)
            }
            specialAdapter.notifyDataSetChanged()
        }

        recyclerViewFood.adapter = specialAdapter
    }

    private fun setupClickListeners() {
        setNavigationListener(R.id.imageCari, CariActivity::class.java)
        setNavigationListener(R.id.imagePosting, PostingActivity::class.java)
        setNavigationListener(R.id.imageFavorit, FavoritActivity::class.java)
        setNavigationListener(R.id.imageAkun, AkunActivity::class.java)

        setCategoryListener(R.id.buttonNusantara, "Menampilkan Resep Nusantara", KatNusantaraActivity::class.java)
        setCategoryListener(R.id.buttonSarapan, "Menampilkan Resep Sarapan", KatSarapanActivity::class.java)
        setCategoryListener(R.id.buttonKue, "Menampilkan Resep Kue", KatKueActivity::class.java)
        setCategoryListener(R.id.buttonPudding, "Menampilkan Resep Pudding", KatPuddingActivity::class.java)
        setCategoryListener(R.id.buttonDiet, "Menampilkan Resep Menu Diet", KatMenuDietActivity::class.java)
    }

    private fun setNavigationListener(viewId: Int, destinationActivity: Class<*>) {
        val imageView = findViewById<ImageView>(viewId)
        imageView.setOnClickListener {
            startActivity(Intent(this, destinationActivity))
        }
    }

    private fun setCategoryListener(viewId: Int, toastMessage: String, destinationActivity: Class<*>) {
        val button = findViewById<Button>(viewId)
        button.setOnClickListener {
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, destinationActivity))
        }
    }
}
