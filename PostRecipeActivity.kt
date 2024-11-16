package com.example.postingresep

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable


class PostRecipeActivity : AppCompatActivity() {

    private lateinit var ivFotoMasakan: ImageView
    private lateinit var ivVideoTutorial: ImageView

    private var imageUri: Uri? = null
    private var videoUri: Uri? = null

    private val getImageResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            ivFotoMasakan.setImageURI(imageUri)
        }
    }

    private val getVideoResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            videoUri = it

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_recipe)

        val etNamaResep: EditText = findViewById(R.id.etNamaResep)
        val etDeskripsi: EditText = findViewById(R.id.etDeskripsi)
        val btnKategori1: Button = findViewById(R.id.btnKategori1)
        val btnKategori2: Button = findViewById(R.id.btnKategori2)
        val btnKategori3: Button = findViewById(R.id.btnKategori3)
        val btnKategori4: Button = findViewById(R.id.btnKategori4)
        val btnKategori5: Button = findViewById(R.id.btnKategori5)
        val btnPosting: Button = findViewById(R.id.btnPosting)
        ivFotoMasakan = findViewById(R.id.ivFotoMasakan)
        ivVideoTutorial = findViewById(R.id.ivVideoTutorial)

        btnKategori1.setOnClickListener { toggleButtonSelected(btnKategori1) }
        btnKategori2.setOnClickListener { toggleButtonSelected(btnKategori2) }
        btnKategori3.setOnClickListener { toggleButtonSelected(btnKategori3) }
        btnKategori4.setOnClickListener { toggleButtonSelected(btnKategori4) }
        btnKategori5.setOnClickListener { toggleButtonSelected(btnKategori5) }

        ivFotoMasakan.setOnClickListener {
            openImageChooser()
        }

        ivVideoTutorial.setOnClickListener {
            openVideoChooser()
        }

        btnPosting.setOnClickListener {
            val etNamaResep: EditText = findViewById(R.id.etNamaResep)
            val etDeskripsi: EditText = findViewById(R.id.etDeskripsi)
            val isKategoriSelected = (btnKategori1.isSelected || btnKategori2.isSelected || btnKategori3.isSelected || btnKategori4.isSelected || btnKategori5.isSelected)

            if (etNamaResep.text.isEmpty() || etDeskripsi.text.isEmpty() || imageUri == null || videoUri == null || !isKategoriSelected) {
                Toast.makeText(this, "Harap isi semua data terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                showConfirmationDialog()
            }
        }
    }

    private fun openImageChooser() {
        getImageResult.launch("image/*")
    }

    private fun openVideoChooser() {
        getVideoResult.launch("video/*")
    }

    private fun toggleButtonSelected(button: Button) {

        val buttons = arrayOf(
            findViewById<Button>(R.id.btnKategori1),
            findViewById<Button>(R.id.btnKategori2),
            findViewById<Button>(R.id.btnKategori3),
            findViewById<Button>(R.id.btnKategori4),
            findViewById<Button>(R.id.btnKategori5)
        )


        buttons.forEach {
            it.isSelected = false


            val defaultDrawable = GradientDrawable()
            defaultDrawable.shape = GradientDrawable.RECTANGLE
            defaultDrawable.cornerRadius = 30f
            defaultDrawable.setColor(Color.parseColor("#6C5B7B"))
            it.background = defaultDrawable
        }

        button.isSelected = true

        val selectedDrawable = GradientDrawable()
        selectedDrawable.shape = GradientDrawable.RECTANGLE
        selectedDrawable.cornerRadius = 30f
        selectedDrawable.setColor(Color.parseColor("#FFB6C1"))

        button.background = selectedDrawable
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Posting")
            .setMessage("Anda akan memposting resep ini?")
            .setPositiveButton("Oke") { _, _ ->
                Toast.makeText(this, "Berhasil memposting resep", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}