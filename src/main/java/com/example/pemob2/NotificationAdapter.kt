package com.example.wistcookapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(private val notificationList: List<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivNotificationIcon: ImageView = view.findViewById(R.id.ivNotificationIcon)
        val tvAppName: TextView = view.findViewById(R.id.tvAppName)
        val tvNotificationTimestamp: TextView = view.findViewById(R.id.tvNotificationTimestamp)
        val tvNotificationMessage: TextView = view.findViewById(R.id.tvNotificationMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]

        // Mengatur ikon notifikasi
        holder.ivNotificationIcon.setImageResource(R.drawable.ic_wistcook)

        // Nama aplikasi
        holder.tvAppName.text = "WistCook"

        // Mengonversi timestamp ke format yang sesuai
        val timestampFormatted = formatTimestamp(notification.timestamp)
        holder.tvNotificationTimestamp.text = timestampFormatted

        // Pesan notifikasi
        holder.tvNotificationMessage.text = notification.message
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    private fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("GMT+7")
        return format.format(date)
    }
}
