package com.example.wistcookapp

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailInput: EditText
    private lateinit var verifyEmailButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance()

        // Find the EditText and Button from the layout
        emailInput = findViewById(R.id.email_input)
        verifyEmailButton = findViewById(R.id.btn_verify_email)

        // Set up click listener for the "Verifikasi Email" button
        verifyEmailButton.setOnClickListener {
            val email = emailInput.text.toString().trim()

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Harap masukkan email yang valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                // Create ActionCodeSettings to customize the email link
                val actionCodeSettings = ActionCodeSettings.newBuilder()
                    .setUrl("https://www.yourapp.com/resetPassword") // URL tujuan setelah pengguna mengklik tautan
                    .setHandleCodeInApp(true) // Menangani kode di aplikasi
                    .setAndroidPackageName(
                        "com.example.wistcookapp", // Ganti dengan package name aplikasi kamu
                        true, // Apakah aplikasi diinstal?
                        null // Versi minimal aplikasi
                    )
                    .build()

                // Send password reset email
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Email untuk reset kata sandi telah dikirim", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Gagal mengirim email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }
    }
}
