package com.example.pengaturanakun

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class EditAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account)

        // Initialize buttons
        val saveButton = findViewById<Button>(R.id.saveButton)
        val backButton = findViewById<ImageView>(R.id.backButton)

        // Handle back button click to return to the previous activity
        backButton.setOnClickListener {
            finish() // Close this activity and go back
        }

        // Show success dialog on save button click
        saveButton.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Berhasil")
            dialog.setMessage("Berhasil menyimpan")
            dialog.setPositiveButton("OK") { _, _ ->
                // Return to AccountActivity after clicking OK
                finish()
            }
            dialog.show()
        }
    }

    // Function to navigate to EditProfilActivity
    fun goToEditProfile(view: View) {
        val intent = Intent(this, EditProfilActivity::class.java)
        startActivity(intent)
    }
}
