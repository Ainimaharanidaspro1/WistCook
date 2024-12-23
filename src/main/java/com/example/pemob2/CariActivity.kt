package com.example.wistcookapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner

class CariActivity : AppCompatActivity() {

    private val resepList = mutableListOf<Resep>()
    private lateinit var database: DatabaseReference
    private lateinit var recyclerViewResep: RecyclerView
    private lateinit var textViewNoResult: TextView
    private lateinit var searchEditText: EditText
    private lateinit var resepAdapter: ResepAdapter
    private lateinit var kategoriAdapter: RecipeCategoryAdapter
    private lateinit var recyclerViewKategori: RecyclerView
    private val categories = listOf(
        RecipeCategory("Nusantara", R.drawable.sarapan_2),
        RecipeCategory("Sarapan", R.drawable.sarapan_1),
        RecipeCategory("Kue", R.drawable.kue),
        RecipeCategory("Puding", R.drawable.pudding),
        RecipeCategory("Diet", R.drawable.ic_diet)
    )

    private var selectedCategory = ""
    private var searchHistory: MutableList<String> = mutableListOf()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var keywordContainer: LinearLayout
    private lateinit var textViewRiwayatPencarian: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cari)

        // Initialize UI components
        recyclerViewResep = findViewById(R.id.recyclerViewResep)
        textViewNoResult = findViewById(R.id.textViewNoResult)
        searchEditText = findViewById(R.id.searchEditText)
        recyclerViewKategori = findViewById(R.id.recyclerViewKategori)
        keywordContainer = findViewById(R.id.keywordContainer)
        textViewRiwayatPencarian = findViewById(R.id.RiwayatPencarian) // Initialize search history text

        // Set up RecyclerView for recipes
        recyclerViewResep.layoutManager = GridLayoutManager(this, 2)
        resepAdapter = ResepAdapter(resepList)
        recyclerViewResep.adapter = resepAdapter

        // Set item click listener for each recipe
        resepAdapter.onItemClick = { resep ->
            val intent = Intent(this, DetailResepActivity::class.java).apply {
                putExtra("recipe_name", resep.namaResep)
                putExtra("recipe_description", resep.deskripsi)
                putExtra("recipe_ingredients", resep.bahanResep)
                putExtra("recipe_steps", resep.caraMasakResep)
                putExtra("recipe_image", resep.fotoMasakan)
            }
            startActivity(intent)
        }

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
        loadSearchHistory()

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("resep")

        // Setup Search functionality
        setupSearchListener()

        // Setup Search button functionality
        setupSearchFunctionality()

        // Initialize RecyclerView for categories
        recyclerViewKategori.layoutManager = GridLayoutManager(this, 3)
        kategoriAdapter = RecipeCategoryAdapter(categories) { category ->
            handleCategoryClick(category)
        }
        recyclerViewKategori.adapter = kategoriAdapter
    }

    private fun handleCategoryClick(category: RecipeCategory) {
        when (category.name) {
            "Nusantara" -> startActivity(Intent(this, KatNusantaraActivity::class.java))
            "Sarapan" -> startActivity(Intent(this, KatSarapanActivity::class.java))
            "Kue" -> startActivity(Intent(this, KatKueActivity::class.java))
            "Puding" -> startActivity(Intent(this, KatPuddingActivity::class.java))
            "Diet" -> startActivity(Intent(this, KatMenuDietActivity::class.java))
            else -> Toast.makeText(this, "Kategori tidak dikenali", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadSearchHistory() {
        val history = sharedPreferences.getStringSet("history", setOf())
        history?.let {
            searchHistory.clear()
            searchHistory.addAll(it)
            displaySearchHistory()
        }
    }

    private fun saveSearchHistory(query: String) {
        if (!searchHistory.contains(query)) {
            if (searchHistory.size == 5) {
                searchHistory.removeAt(0)
            }
            searchHistory.add(query)
            sharedPreferences.edit().putStringSet("history", searchHistory.toSet()).apply()
            displaySearchHistory()
        }
    }

    private fun displaySearchHistory() {
        keywordContainer.removeAllViews()
        val historyToDisplay = searchHistory.takeLast(5)
        for (keyword in historyToDisplay) {
            val keywordButton = Button(this)
            keywordButton.text = keyword
            keywordButton.setOnClickListener {
                searchEditText.setText(keyword)
                filterRecipes(keyword)
            }
            keywordContainer.addView(keywordButton)
        }
    }

    private fun setupSearchListener() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchEditText.text.toString().trim()
                if (query.isNotEmpty()) {
                    filterRecipes(query)
                    addKeywordToHorizontalScrollView(query)
                }
                true
            } else {
                false
            }
        }
    }

    private fun filterRecipes(query: String) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val allRecipes = mutableListOf<Resep>()
                for (snapshot in dataSnapshot.children) {
                    val recipe = snapshot.getValue(Resep::class.java)
                    recipe?.let {
                        if (selectedCategory.isEmpty() || it.kategori == selectedCategory) {
                            allRecipes.add(it)
                        }
                    }
                }

                val filteredRecipes = allRecipes.filter { it.namaResep.contains(query, ignoreCase = true) }

                if (filteredRecipes.isNotEmpty()) {
                    textViewNoResult.visibility = View.GONE
                    recyclerViewResep.visibility = View.VISIBLE
                    recyclerViewKategori.visibility = View.GONE
                    resepAdapter.updateRecipes(filteredRecipes)

                    // Hide search history and category text
                    findViewById<TextView>(R.id.RiwayatPencarian).visibility = View.GONE
                    findViewById<TextView>(R.id.textKategoriResep).visibility = View.GONE
                } else {
                    textViewNoResult.visibility = View.VISIBLE
                    recyclerViewResep.visibility = View.GONE
                    recyclerViewKategori.visibility = View.GONE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@CariActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupSearchFunctionality() {
        val searchIcon = findViewById<ImageView>(R.id.searchIcon)
        searchIcon.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                findViewById<TextView>(R.id.RiwayatPencarian).visibility = View.GONE
                keywordContainer.visibility = View.GONE

                filterRecipes(query)
                addKeywordToHorizontalScrollView(query)

                keywordContainer.visibility = View.GONE
            }
        }
    }

    private fun addKeywordToHorizontalScrollView(keyword: String) {
        if (!searchHistory.contains(keyword)) {
            saveSearchHistory(keyword)
        }
        keywordContainer.visibility = View.GONE
    }
}
