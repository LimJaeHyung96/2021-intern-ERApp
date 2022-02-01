package com.example.uitest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.report_page.*
import kotlinx.android.synthetic.main.search_page.*
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import java.net.URL

class SearchListPage : AppCompatActivity() {
    private var searchStr :String = ""
    private var category :String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_page)

        val searchView = findViewById<RecyclerView>(R.id.searchView)
        val searchText = findViewById<TextView>(R.id.searchText)

        searchView.layoutManager = LinearLayoutManager(this)
        searchStr = intent.getStringExtra("searchStr").toString()
        category = intent.getStringExtra("category").toString()

        searchText.text = "검색 : " + searchStr
        ASD2()
    }

    override fun onDestroy() {
        super.onDestroy()
        val searchView = findViewById<RecyclerView>(R.id.searchView)
        searchView.removeAllViewsInLayout()
    }

    private fun ASD2(){
        val url = URL("http://192.168.5.71/search.php")

        var requestBody = FormBody.Builder().add("category",category).add("search",searchStr).build()

        val request = Request.Builder().url(url).post(requestBody).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            //통신에 실패했을 때
            override fun onFailure(call: Call?, e: IOException?) {
                Log.d("통신상태", "실패")
            }

            //통신에 성공했을 때
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                val gson = GsonBuilder().create()
                val list = gson.fromJson(body, ReportJson::class.java)

                runOnUiThread {
                    val searchNum = findViewById<TextView>(R.id.searchNum)

                    searchView.adapter = SearchListAdapter(list)

                    if(list.result[0].REP_NUMB == "x")
                        searchNum.text = "검색 결과 : 0"
                    else
                        searchNum.text = "검색 결과 : " + list.result.count().toString()

                    Log.d("테스트",list.result[0].REP_NUMB)
                }
            }
        })
    }
}