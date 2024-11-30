package com.example.pengaturanakun

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class FeedbackActivity : AppCompatActivity() {

    private lateinit var inputNama: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputDeskripsi: EditText
    private lateinit var buttonKirim: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val backArrow = findViewById<ImageView>(R.id.backArrow)
        backArrow.setOnClickListener {
            finish() // Ends FeedbackActivity and returns to AccountActivity
        }

        inputNama = findViewById(R.id.inputNama)
        inputEmail = findViewById(R.id.inputEmail)
        inputDeskripsi = findViewById(R.id.inputDeskripsi)
        buttonKirim = findViewById(R.id.buttonKirim)

        buttonKirim.setOnClickListener {
            val nama = inputNama.text.toString()
            val email = inputEmail.text.toString()
            val deskripsi = inputDeskripsi.text.toString()

            if (nama.isEmpty() || email.isEmpty() || deskripsi.isEmpty()) {
                Toast.makeText(this, "Harap isi semua bidang", Toast.LENGTH_SHORT).show()
            } else {
                // Tampilkan dialog setelah klik kirim
                showConfirmationDialog()
            }
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Berhasil")
        builder.setMessage("Umpan balik berhasil dikirim")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
