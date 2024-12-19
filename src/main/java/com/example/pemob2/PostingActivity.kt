package com.example.wistcookapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ivProfilePicture: ImageView
    private lateinit var tvUsername: TextView
    private var imageUri: Uri? = null

    private val getImageResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            findViewById<ImageView>(R.id.ivFotoMasakan).setImageURI(imageUri)
            Toast.makeText(this, "Gambar dipilih", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)

        // Buat Notification Channel
        createNotificationChannel()

        // Inisialisasi Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://wistcook-b7705-default-rtdb.asia-southeast1.firebasedatabase.app/")

        // Inisialisasi elemen UI
        ivProfilePicture = findViewById(R.id.ivProfilePicture)
        tvUsername = findViewById(R.id.tvUsername)
        val etNamaResep: EditText = findViewById(R.id.etNamaResep)
        val etDeskripsi: EditText = findViewById(R.id.etDeskripsi)
        val etBahan: EditText = findViewById(R.id.etBahanMasakan)
        val etLangkah: EditText = findViewById(R.id.etLangkahMemasak)
        val btnKategori1: Button = findViewById(R.id.btnKategori1)
        val btnKategori2: Button = findViewById(R.id.btnKategori2)
        val btnKategori3: Button = findViewById(R.id.btnKategori3)
        val btnKategori4: Button = findViewById(R.id.btnKategori4)
        val btnKategori5: Button = findViewById(R.id.btnKategori5)
        val btnPosting: Button = findViewById(R.id.btnPosting)
        val ivFotoMasakan: ImageView = findViewById(R.id.ivFotoMasakan)

        // Ambil data pengguna berdasarkan UID
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = database.reference.child("users").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        tvUsername.text = user.name

                        // Tampilkan foto pengguna jika tersedia
                        if (!user.photoUrl.isNullOrEmpty()) {
                            try {
                                val imageBytes = Base64.decode(user.photoUrl, Base64.DEFAULT)
                                val decodedImage = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                ivProfilePicture.setImageBitmap(decodedImage)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                ivProfilePicture.setImageResource(R.drawable.ic_account) // Gambar default
                            }
                        } else {
                            ivProfilePicture.setImageResource(R.drawable.ic_account) // Gambar default
                        }
                    } else {
                        Toast.makeText(this@PostingActivity, "Gagal memuat data pengguna", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@PostingActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Set listener untuk tombol kategori
        btnKategori1.setOnClickListener { toggleButtonSelected(btnKategori1) }
        btnKategori2.setOnClickListener { toggleButtonSelected(btnKategori2) }
        btnKategori3.setOnClickListener { toggleButtonSelected(btnKategori3) }
        btnKategori4.setOnClickListener { toggleButtonSelected(btnKategori4) }
        btnKategori5.setOnClickListener { toggleButtonSelected(btnKategori5) }


        // Set listener untuk memilih gambar
        ivFotoMasakan.setOnClickListener {
            getImageResult.launch("image/*")
        }

        // Set listener untuk tombol posting
        btnPosting.setOnClickListener {
            val isKategoriSelected = (btnKategori1.isSelected || btnKategori2.isSelected || btnKategori3.isSelected || btnKategori4.isSelected || btnKategori5.isSelected)

            if (etNamaResep.text.isEmpty() || etDeskripsi.text.isEmpty() || etBahan.text.isEmpty() || etLangkah.text.isEmpty() || imageUri == null || !isKategoriSelected) {
                Toast.makeText(this, "Harap isi semua data terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                val namaResep = etNamaResep.text.toString()
                val deskripsi = etDeskripsi.text.toString()
                val bahan = etBahan.text.toString()
                val langkah = etLangkah.text.toString()
                showConfirmationDialog(namaResep, deskripsi, bahan, langkah)
            }
        }
    }

    private fun toggleButtonSelected(selectedButton: Button) {
        val buttons = arrayOf(
            findViewById<Button>(R.id.btnKategori1),
            findViewById<Button>(R.id.btnKategori2),
            findViewById<Button>(R.id.btnKategori3),
            findViewById<Button>(R.id.btnKategori4),
            findViewById<Button>(R.id.btnKategori5)
        )

        buttons.forEach { button ->
            button.isSelected = (button == selectedButton)
            button.setTextColor(if (button.isSelected) Color.WHITE else Color.YELLOW)
        }
    }


    private fun showConfirmationDialog(namaResep: String, deskripsi: String, bahan: String, langkah: String) {
        val selectedCategory = getSelectedCategory()
        if (selectedCategory == null) {
            Toast.makeText(this, "Pilih kategori terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Posting")
            .setMessage("Anda akan memposting resep ini?")
            .setPositiveButton("Oke") { _, _ ->
                uploadRecipeToFirebase(namaResep, deskripsi, bahan, langkah, selectedCategory)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun getSelectedCategory(): String? {
        val selectedCategory = when {
            findViewById<Button>(R.id.btnKategori1).isSelected -> "Nusantara"
            findViewById<Button>(R.id.btnKategori2).isSelected -> "Sarapan"
            findViewById<Button>(R.id.btnKategori3).isSelected -> "Kue"
            findViewById<Button>(R.id.btnKategori4).isSelected -> "Puding"
            findViewById<Button>(R.id.btnKategori5).isSelected -> "Diet"
            else -> null
        }
        Log.d("SelectedCategory", "Kategori Terpilih: $selectedCategory")
        return selectedCategory
    }


    private fun uploadRecipeToFirebase(namaResep: String, deskripsi: String, bahan: String, langkah: String, kategori: String) {
        val database = FirebaseDatabase.getInstance("https://wistcook-b7705-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val recipeRef = database.reference.child("resep").push()

        val imageBase64 = imageUri?.let { uriToBase64(it) }

        val newRecipe = mapOf(
            "userId" to auth.currentUser?.uid,
            "userName" to tvUsername.text.toString(),
            "namaResep" to namaResep,
            "deskripsi" to deskripsi,
            "kategori" to kategori,
            "fotoMasakan" to imageBase64,
            "bahanResep" to bahan,
            "caraMasakResep" to langkah
        )

        recipeRef.setValue(newRecipe)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Resep berhasil diposting", Toast.LENGTH_SHORT).show()
                    saveNotificationData(namaResep, deskripsi)
                    sendNotification()
                    // Kembali ke halaman beranda
                    val intent = Intent(this, BerandaActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Gagal menyimpan data: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun uriToBase64(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            bytes?.let { Base64.encodeToString(it, Base64.DEFAULT) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }



// Function to save notification data to Firebase
private fun saveNotificationData(namaResep: String, deskripsi: String) {
    val database = FirebaseDatabase.getInstance("https://wistcook-b7705-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val notificationRef = database.reference.child("notifications").push()

    val notificationData = NotificationData(
        title = "Resep Sudah Berhasil Diposting!",
        message = "Selamat, Anda telah berhasil memposting resep $namaResep anda!",
        timestamp = System.currentTimeMillis()
    )

    notificationRef.setValue(notificationData)
}
    // Create notification channel for posting
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "posting_resep_channel",
                "Posting Resep",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Notifikasi untuk posting resep berhasil."
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    // Send notification upon successful post
    private fun sendNotification() {
        val intent = Intent(this, PostingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        val builder = NotificationCompat.Builder(this, "posting_resep_channel")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.heart_filled)
            .setContentTitle("Wistcook")
            .setContentText("Resep Anda sudah berhasil diposting!")

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@PostingActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request permission if not granted
                return
            }
            notify(1, builder.build())
        }
    }
}

// Data class for notification
data class NotificationData(
    val title: String,
    val message: String,
    val timestamp: Long
)
