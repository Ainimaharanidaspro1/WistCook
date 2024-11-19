package com.example.notifikasi

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Bundle
import android.widget.ImageView

class NotificationHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationAdapter
    private val notificationList = mutableListOf<Notification>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_history)

        val backButton: ImageView = findViewById(R.id.back_button)

        recyclerView = findViewById(R.id.recyclerViewNotifications)
        recyclerView.layoutManager = LinearLayoutManager(this)

        populateDummyNotifications()

        adapter = NotificationAdapter(notificationList)
        recyclerView.adapter = adapter

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun populateDummyNotifications() {
        notificationList.add(
            Notification(
                title = "WistCook",
                message = "Hi Anna, sepertinya kamu belum coba resep ini. Yuk coba!",
                timestamp = "34m ago"
            )
        )
        notificationList.add(
            Notification(
                title = "WistCook",
                message = "Ada resep baru untuk kamu coba hari ini!",
                timestamp = "2h ago"
            )
        )
        // Tambahkan lebih banyak data sesuai kebutuhan
    }
}
