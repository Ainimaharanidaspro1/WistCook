package com.example.wistcookapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ActionCodeSettings

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var newPasswordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var confirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        auth = FirebaseAuth.getInstance()

        val newPasswordInput = findViewById<EditText>(R.id.new_password_input)
        val confirmPasswordInput = findViewById<EditText>(R.id.confirm_password_input)
        val confirmButton = findViewById<Button>(R.id.btn_confirm_new_password)

        confirmButton.setOnClickListener {
            val newPassword = newPasswordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                // Tampilkan pesan kesalahan jika ada input yang kosong
                Toast.makeText(this, "Harap isi semua bidang", Toast.LENGTH_SHORT).show()
            } else if (newPassword != confirmPassword) {
                // Tampilkan pesan kesalahan jika kata sandi tidak cocok
                Toast.makeText(this, "Kata sandi tidak cocok", Toast.LENGTH_SHORT).show()
            } else {
                // Proses untuk mereset kata sandi
                val user = FirebaseAuth.getInstance().currentUser
                user?.updatePassword(newPassword)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Kata sandi berhasil diubah", Toast.LENGTH_SHORT)
                            .show()
                        // Arahkan pengguna ke halaman login atau halaman lain setelah berhasil
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish() // Tutup aktivitas ini
                    } else {
                        Toast.makeText(
                            this,
                            "Gagal mengubah kata sandi. Silakan coba lagi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
