package com.example.wistcookapp

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.graphics.BitmapFactory
import android.widget.ImageView

class ResepmuActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var resepAdapter: ResepmuAdapter
    private val resepList = mutableListOf<Resep>()
    private val dbRef = FirebaseDatabase.getInstance().getReference("resep")

    // Mendapatkan ID pengguna yang sedang login
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val currentUserId = currentUser?.uid // Mengambil ID pengguna yang login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resepmu)

        val backArrow = findViewById<ImageView>(R.id.backArrow)

        backArrow.setOnClickListener {
            onBackPressed() // Memanggil metode yang kembali ke halaman sebelumnya
            // atau gunakan finish() jika Anda hanya ingin menutup aktivitas
            // finish()
        }

        recyclerView = findViewById(R.id.recyclerViewResepmu)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Menambahkan onDeleteClick untuk menangani aksi delete
        resepAdapter = ResepmuAdapter(resepList, ::onRecipeClick, ::onDeleteClick)
        recyclerView.adapter = resepAdapter

        if (currentUserId != null) {
            fetchDataFromRealtimeDatabase(currentUserId)
        } else {
            Toast.makeText(this, "Gagal mengambil data pengguna. Pastikan Anda sudah login.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchDataFromRealtimeDatabase(userId: String) {
        // Menambahkan filter untuk hanya menampilkan resep yang diposting oleh pengguna yang sedang login
        dbRef.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                resepList.clear() // Clear the list to avoid duplicates
                if (snapshot.exists()) {
                    for (resepSnapshot in snapshot.children) {
                        val resep = resepSnapshot.getValue(Resep::class.java)
                        if (resep != null) {
                            resepList.add(resep)
                        }
                    }
                    resepAdapter.notifyDataSetChanged()
                    Log.d("ResepmuActivity", "Data diterima: ${resepList.size}")
                } else {
                    Toast.makeText(this@ResepmuActivity, "Tidak ada resep yang diposting oleh Anda", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ResepmuActivity, "Gagal mengambil data: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e("ResepmuActivity", "Database error: ${error.message}")
            }
        })
    }

    private fun onRecipeClick(resep: Resep) {
        val intent = Intent(this, DetailResepActivity::class.java)
        intent.putExtra("recipe_name", resep.namaResep)
        intent.putExtra("recipe_description", resep.deskripsi)
        intent.putExtra("recipe_ingredients", resep.bahanResep)
        intent.putExtra("recipe_steps", resep.caraMasakResep)
        intent.putExtra("recipe_image", resep.fotoMasakan) // Jika menggunakan URL atau path gambar
        startActivity(intent)
    }

    private fun onDeleteClick(resep: Resep) {
        dbRef.orderByChild("namaResep").equalTo(resep.namaResep).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (resepSnapshot in snapshot.children) {
                        resepSnapshot.ref.removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@ResepmuActivity, "Resep berhasil dihapus", Toast.LENGTH_SHORT).show()
                                // Setelah data dihapus, ambil ulang data dari database untuk memperbarui UI
                                if (currentUserId != null) {
                                    fetchDataFromRealtimeDatabase(currentUserId)
                                }
                            } else {
                                Toast.makeText(this@ResepmuActivity, "Gagal menghapus resep", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@ResepmuActivity, "Resep tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ResepmuActivity, "Gagal mengambil data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Fungsi untuk mendekode gambar Base64 menjadi Bitmap
    fun decodeBase64(base64String: String): Bitmap? {
        return try {
            val decodedString = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
