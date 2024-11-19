package com.example.notifikasi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter(private val notificationList: List<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvNotificationTitle)
        val message: TextView = itemView.findViewById(R.id.tvNotificationMessage)
        val timestamp: TextView = itemView.findViewById(R.id.tvNotificationTime)
        val logo: ImageView = itemView.findViewById(R.id.imgAppLogo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.title.text = notification.title
        holder.message.text = notification.message
        holder.timestamp.text = notification.timestamp
        holder.logo.setImageResource(R.drawable.ic_wistcook)
    }

    override fun getItemCount(): Int = notificationList.size
}
