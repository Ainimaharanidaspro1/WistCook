package com.example.wistcookapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class EditProfilActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var profileImage: ImageView
    private lateinit var profileName: TextView
    private lateinit var btnGaleriFoto: Button
    private lateinit var btnAmbilFoto: Button
    private lateinit var btnSimpanProfil: Button

    private var selectedImageUri: Uri? = null
    private var selectedImageBitmap: Bitmap? = null

    private val REQUEST_IMAGE_GALLERY = 1
    private val REQUEST_IMAGE_CAMERA = 2
    private val REQUEST_PERMISSION = 100

    private val databaseReference by lazy {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        FirebaseDatabase.getInstance().getReference("users").child(userId ?: "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)

        backButton = findViewById(R.id.backButton)
        profileImage = findViewById(R.id.profile_image)
        profileName = findViewById(R.id.profileName)
        btnGaleriFoto = findViewById(R.id.btnGaleriFoto)
        btnAmbilFoto = findViewById(R.id.btnAmbilFoto)
        btnSimpanProfil = findViewById(R.id.btnSimpanProfil)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Pengguna tidak terdaftar atau belum login!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        requestPermissions()
        loadProfileData()

        backButton.setOnClickListener { finish() }
        btnGaleriFoto.setOnClickListener { openGallery() }
        btnAmbilFoto.setOnClickListener { openCamera() }
        btnSimpanProfil.setOnClickListener { saveProfile() }
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                REQUEST_PERMISSION
            )
        }
    }

    private fun loadProfileData() {
        databaseReference.get().addOnSuccessListener { snapshot ->
            val user = snapshot.getValue(User::class.java)
            user?.let {
                profileName.text = it.name.ifEmpty { "Nama Pengguna" }

                if (!it.photoUrl.isNullOrEmpty()) {
                    try {
                        val imageBytes = Base64.decode(it.photoUrl, Base64.DEFAULT)
                        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        profileImage.setImageBitmap(decodedImage)
                    } catch (e: Exception) {
                        profileImage.setImageResource(R.drawable.ic_account)
                    }
                } else {
                    profileImage.setImageResource(R.drawable.ic_account)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal memuat data pengguna", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_IMAGE_GALLERY -> {
                    selectedImageUri = data.data
                    profileImage.setImageURI(selectedImageUri)
                }
                REQUEST_IMAGE_CAMERA -> {
                    selectedImageBitmap = data.extras?.get("data") as? Bitmap
                    selectedImageBitmap?.let {
                        profileImage.setImageBitmap(it)
                    }
                }
            }
        } else {
            Toast.makeText(this, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAMERA)
    }

    private fun saveProfile() {
        if (selectedImageUri != null || selectedImageBitmap != null) {
            val base64Image = encodeImageToBase64()
            if (base64Image != null) {
                saveToDatabase(base64Image)
            } else {
                Toast.makeText(this, "Gagal mengonversi gambar", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Pilih foto profil terlebih dahulu!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun encodeImageToBase64(): String? {
        return try {
            val bitmap = when {
                selectedImageBitmap != null -> selectedImageBitmap
                selectedImageUri != null -> MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                else -> null
            }
            val outputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            val imageBytes = outputStream.toByteArray()
            Base64.encodeToString(imageBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            Log.e("ImageEncoding", "Error encoding image to Base64: ${e.message}", e)
            null
        }
    }

    private fun saveToDatabase(base64Image: String) {
        val updates = mapOf("photoUrl" to base64Image)

        databaseReference.updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan ke database!", Toast.LENGTH_SHORT).show()
            }
    }
}
