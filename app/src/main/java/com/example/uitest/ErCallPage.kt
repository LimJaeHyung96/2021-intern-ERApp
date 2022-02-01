package com.example.uitest


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.er_call.*
import okhttp3.*
import java.io.IOException
import java.net.URL

class ErCallPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.er_call_main)
        title = "비상연락망"

        //연락망 페이지 화면 연결
        val vp = findViewById<ViewPager>(R.id.viewpager)
        val adapter = VPAdapter(supportFragmentManager)
        vp.adapter = adapter

        //페이지와 탭 연결
        val tab = findViewById<TabLayout>(R.id.tab)
        tab.setupWithViewPager(vp)
    }
}