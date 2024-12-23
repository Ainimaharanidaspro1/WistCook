package com.example.wistcookapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.IOException

class AkunActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var profileImageView: ImageView
    private lateinit var profileNameTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var btnEditAccount: TextView
    private lateinit var btnResep: TextView
    private lateinit var btnUmpanBalik: TextView
    private lateinit var btnFaq: TextView
    private lateinit var btnTentang: TextView

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_akun)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://wistcook-b7705-default-rtdb.asia-southeast1.firebasedatabase.app/")

        // Initialize UI elements
        profileImageView = findViewById(R.id.profile_image)
        profileNameTextView = findViewById(R.id.profile_name)
        btnEditAccount = findViewById(R.id.btn_edit_account)
        btnResep = findViewById(R.id.btn_resep)
        btnUmpanBalik = findViewById(R.id.btn_umpanbalik)
        btnFaq = findViewById(R.id.btn_faqq)
        btnTentang = findViewById(R.id.btn_tentang)
        logoutButton = findViewById(R.id.logout_button)

        // Load user data
        loadUserData()

        // Set click listeners for navigation
        setNavigationListener(R.id.btn_edit_account, EditAkunActivity::class.java)
        setNavigationListener(R.id.btn_resep, ResepmuActivity::class.java)
        setNavigationListener(R.id.btn_umpanbalik, FeedbackActivity::class.java)
        setNavigationListener(R.id.btn_faqq, FaqsActivity::class.java)
        setNavigationListener(R.id.btn_tentang, TentangAplikasiActivity::class.java)

        // Additional navigation via ImageView
        setNavigationListener(R.id.imageCari, CariActivity::class.java)
        setNavigationListener(R.id.imageFavorit, FavoritActivity::class.java)
        setNavigationListener(R.id.imagePosting, PostingActivity::class.java)
        setNavigationListener(R.id.imageView17, BerandaActivity::class.java)

        // Click on profile image to open gallery
        profileImageView.setOnClickListener {
            openGallery()
        }

        // Logout button click listener
        logoutButton.setOnClickListener {
            performLogout()
        }
    }

    // A generic function to navigate to a specific activity
    private fun setNavigationListener(viewId: Int, destinationActivity: Class<*>) {
        val view = findViewById<View>(viewId)
        view.setOnClickListener {
            val intent = Intent(this, destinationActivity)
            startActivity(intent)
        }
    }


    // Open gallery to select photo
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            if (selectedImageUri != null) {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                    profileImageView.setImageBitmap(bitmap)

                    // Convert image to Base64 and save to Firebase
                    val base64Image = convertBitmapToBase64(bitmap)
                    savePhotoToFirebase(base64Image)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Gagal Menemukan Gambar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Convert Bitmap to Base64 string
    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // Save the photo to Firebase
    private fun savePhotoToFirebase(base64Image: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userRef = database.reference.child("users").child(userId).child("photoUrl")
            userRef.setValue(base64Image).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Foto Profil Berhasil Diunggah", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Gagal Menyimpan Foto", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Load user data from Firebase
    private fun loadUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userRef = database.reference.child("users").child(userId)
            userRef.get().addOnSuccessListener { snapshot ->
                val fetchedName = snapshot.child("name").getValue(String::class.java) ?: "User"
                profileNameTextView.text = fetchedName

                val photoUrl = snapshot.child("photoUrl").getValue(String::class.java)
                if (!photoUrl.isNullOrEmpty()) {
                    try {
                        val imageBytes = Base64.decode(photoUrl, Base64.DEFAULT)
                        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        profileImageView.setImageBitmap(decodedImage)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        profileImageView.setImageResource(R.drawable.ic_account) // Default image
                    }
                } else {
                    profileImageView.setImageResource(R.drawable.ic_account)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Gagal Menemukan Data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Perform logout from the app
    private fun performLogout() {
        auth.signOut() // Logout from Firebase
        Toast.makeText(this, "Anda Berhasil Keluar", Toast.LENGTH_SHORT).show()

        // Navigate to the login page
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
