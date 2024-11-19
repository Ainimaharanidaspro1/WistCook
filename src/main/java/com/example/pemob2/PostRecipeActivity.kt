package com.example.postingresep

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.io.InputStream

class PostRecipeActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private var videoUri: Uri? = null

    private val getImageResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            findViewById<ImageView>(R.id.ivFotoMasakan).setImageURI(imageUri)
            Toast.makeText(this, "Gambar dipilih", Toast.LENGTH_SHORT).show()
        }
    }

    private val getVideoResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            videoUri = it
            val thumbnail = generateVideoThumbnail(it)
            val ivVideoTutorial = findViewById<ImageView>(R.id.ivVideoTutorial)
            if (thumbnail != null) {
                ivVideoTutorial.setImageBitmap(thumbnail)
                Toast.makeText(this, "Video dipilih", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal membuat thumbnail video", Toast.LENGTH_SHORT).show()
            }
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
        val ivFotoMasakan: ImageView = findViewById(R.id.ivFotoMasakan)
        val ivVideoTutorial: ImageView = findViewById(R.id.ivVideoTutorial)

        btnKategori1.setOnClickListener { toggleButtonSelected(btnKategori1) }
        btnKategori2.setOnClickListener { toggleButtonSelected(btnKategori2) }
        btnKategori3.setOnClickListener { toggleButtonSelected(btnKategori3) }
        btnKategori4.setOnClickListener { toggleButtonSelected(btnKategori4) }
        btnKategori5.setOnClickListener { toggleButtonSelected(btnKategori5) }

        ivFotoMasakan.setOnClickListener {
            getImageResult.launch("image/*")
        }

        ivVideoTutorial.setOnClickListener {
            getVideoResult.launch("video/*")
        }

        btnPosting.setOnClickListener {
            val isKategoriSelected = (btnKategori1.isSelected || btnKategori2.isSelected || btnKategori3.isSelected || btnKategori4.isSelected || btnKategori5.isSelected)

            if (etNamaResep.text.isEmpty() || etDeskripsi.text.isEmpty() || imageUri == null || videoUri == null || !isKategoriSelected) {
                Toast.makeText(this, "Harap isi semua data terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                val namaResep = etNamaResep.text.toString()
                val deskripsi = etDeskripsi.text.toString()
                showConfirmationDialog(namaResep, deskripsi)
            }
        }
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

    private fun showConfirmationDialog(namaResep: String, deskripsi: String) {
        val selectedCategory = getSelectedCategory()
        if (selectedCategory == null) {
            Toast.makeText(this, "Pilih kategori terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Posting")
            .setMessage("Anda akan memposting resep ini?")
            .setPositiveButton("Oke") { _, _ ->
                uploadRecipeToFirebase(namaResep, deskripsi, selectedCategory)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun getSelectedCategory(): String? {
        return when {
            findViewById<Button>(R.id.btnKategori1).isSelected -> "Nusantara"
            findViewById<Button>(R.id.btnKategori2).isSelected -> "Sarapan"
            findViewById<Button>(R.id.btnKategori3).isSelected -> "Kue"
            findViewById<Button>(R.id.btnKategori4).isSelected -> "Pudding"
            findViewById<Button>(R.id.btnKategori5).isSelected -> "Menu Diet"
            else -> null
        }
    }

    private fun uploadRecipeToFirebase(namaResep: String, deskripsi: String, kategori: String) {
        val database = FirebaseDatabase.getInstance("https://postingresep-7f77a-default-rtdb.asia-southeast1.firebasedatabase.app")
        val recipeRef = database.reference.child("recipes").push()

        val imageBase64 = imageUri?.let { uriToBase64(it) }
        val videoBase64 = videoUri?.let { uriToBase64(it) }

        val recipeData = listOf(
            mapOf("key" to "Nama Resep", "value" to namaResep),
            mapOf("key" to "Deskripsi", "value" to deskripsi),
            mapOf("key" to "Image", "value" to imageBase64),
            mapOf("key" to "Video", "value" to videoBase64),
            mapOf("key" to "Kategori", "value" to kategori)
        )

        recipeRef.setValue(recipeData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Resep berhasil diposting", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Gagal menyimpan data: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun uriToBase64(uri: Uri): String? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            bytes?.let { Base64.encodeToString(it, Base64.DEFAULT) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun generateVideoThumbnail(uri: Uri): Bitmap? {
        return try {
            val filePathColumn = arrayOf(MediaStore.Video.Media.DATA)
            val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
            cursor?.moveToFirst()
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val videoPath = columnIndex?.let { cursor.getString(it) }
            cursor?.close()

            videoPath?.let {
                ThumbnailUtils.createVideoThumbnail(it, MediaStore.Images.Thumbnails.MINI_KIND)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}


