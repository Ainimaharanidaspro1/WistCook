package.com.example.resepfavorit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ResepFavoritActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resep_favorit)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFavoriteRecipes)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Sample data for testing
        val recipes = listOf(
            Recipe(R.drawable.ic_recipe_placeholder, "Puding Caramel", 5.0),
            Recipe(R.drawable.ic_recipe_placeholder, "Nasi Goreng Kampung", 4.9),
            // Add more recipes here
        )

        val adapter = RecipeAdapter(recipes)
        recyclerView.adapter = adapter
    }
}