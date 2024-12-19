package com.example.wistcookapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wistcookapp.R
import com.example.wistcookapp.NotificationAdapter
import com.example.wistcookapp.Notification
import com.google.firebase.database.*

class NotificationActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private val notifications = mutableListOf<Notification>()
    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish() // Menutup aktivitas saat ini dan kembali ke halaman sebelumnya
        }

        recyclerView = findViewById(R.id.recyclerViewNotifications)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = NotificationAdapter(notifications)
        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance("https://wistcook-b7705-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("notifications")

        // Baca data dari Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notifications.clear() // Hapus data lama
                for (data in snapshot.children) {
                    val notification = data.getValue(Notification::class.java)
                    if (notification != null) {
                        notifications.add(notification)
                    }
                }

                // Mengurutkan data berdasarkan timestamp (terbaru di atas)
                notifications.sortWith { notification1, notification2 ->
                    notification2.timestamp.compareTo(notification1.timestamp) // Urutkan yang terbaru di atas
                }

                adapter.notifyDataSetChanged() // Refresh RecyclerView
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read data: ${error.message}")
            }
        })
    }
}
