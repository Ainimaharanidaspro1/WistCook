package com.example.pengaturanakun

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FaqsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faqs)

        // Setup FAQ Item 1
        val faqItem1Layout = findViewById<LinearLayout>(R.id.faqItem1Layout)
        val faqAnswer1 = findViewById<TextView>(R.id.faqAnswer1)
        val faqArrow1 = findViewById<ImageView>(R.id.faqArrow1)

        faqItem1Layout.setOnClickListener {
            toggleFAQ(faqAnswer1, faqArrow1)
        }

        // Setup FAQ Item 2
        val faqItem2Layout = findViewById<LinearLayout>(R.id.faqItem2Layout)
        val faqAnswer2 = findViewById<TextView>(R.id.faqAnswer2)
        val faqArrow2 = findViewById<ImageView>(R.id.faqArrow2)

        faqItem2Layout.setOnClickListener {
            toggleFAQ(faqAnswer2, faqArrow2)
        }

        // Setup FAQ Item 3
        val faqItem3Layout = findViewById<LinearLayout>(R.id.faqItem3Layout)
        val faqAnswer3 = findViewById<TextView>(R.id.faqAnswer3)
        val faqArrow3 = findViewById<ImageView>(R.id.faqArrow3)

        faqItem3Layout.setOnClickListener {
            toggleFAQ(faqAnswer3, faqArrow3)
        }

        // Back Arrow Setup
        val backArrow = findViewById<ImageView>(R.id.backArrow)
        backArrow.setOnClickListener {
            finish()
        }
    }

    private fun toggleFAQ(answer: TextView, arrow: ImageView) {
        if (answer.visibility == View.GONE) {
            answer.visibility = View.VISIBLE
            arrow.setImageResource(R.drawable.ic_arrow_down) // Change to down arrow
        } else {
            answer.visibility = View.GONE
            arrow.setImageResource(R.drawable.ic_arrow_forward) // Change back to right arrow
        }
    }
}
