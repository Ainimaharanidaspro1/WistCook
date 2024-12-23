package com.example.wistcookapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wistcookapp.Feedback
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FeedbackActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val backArrow: ImageView = findViewById(R.id.backArrow)
        val inputNama: EditText = findViewById(R.id.inputNama)
        val inputEmail: EditText = findViewById(R.id.inputEmail)
        val inputDeskripsi: EditText = findViewById(R.id.inputDeskripsi)
        val buttonKirim: Button = findViewById(R.id.buttonKirim)

        // Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("Feedback")

        backArrow.setOnClickListener {
            finish() // Kembali ke aktivitas sebelumnya
        }

        buttonKirim.setOnClickListener {
            val nama = inputNama.text.toString().trim()
            val email = inputEmail.text.toString().trim()
            val deskripsi = inputDeskripsi.text.toString().trim()

            if (nama.isEmpty() || email.isEmpty() || deskripsi.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val feedbackId = database.push().key ?: ""
            val feedback = Feedback(feedbackId, nama, email, deskripsi)

            database.child(feedbackId).setValue(feedback).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Umpan balik berhasil dikirim", Toast.LENGTH_SHORT).show()
                    inputNama.text.clear()
                    inputEmail.text.clear()
                    inputDeskripsi.text.clear()
                } else {
                    Toast.makeText(this, "Gagal mengirim umpan balik", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
