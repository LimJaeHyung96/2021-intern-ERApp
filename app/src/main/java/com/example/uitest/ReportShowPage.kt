package com.example.uitest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.TextView

class ReportShowPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_show_page)

        var emp = intent.getStringExtra("EMP")

        val empNum = findViewById<TextView>(R.id.EMP_NUMB)
        empNum.text = emp
    }
}