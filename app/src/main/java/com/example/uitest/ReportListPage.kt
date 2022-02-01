package com.example.uitest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.er_call.*
import kotlinx.android.synthetic.main.report_page.*
import okhttp3.*
import java.io.IOException
import java.net.URL

class ReportListPage : AppCompatActivity() {
    private var searchStr : String = ""
    private var searchNum : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_page)

        val searchCategory = arrayOf("검색 설정","날짜","사고","건물")
        var selectedStr : String? = null
        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,searchCategory)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                selectedStr = searchCategory[position]
            }
        }

        val reportView = findViewById<RecyclerView>(R.id.reportView)
        val searchBtn = findViewById<Button>(R.id.searchBtn)
        val searchEdit = findViewById<EditText>(R.id.searchEdit)


        reportView.layoutManager = LinearLayoutManager(this)
        ASD()

        searchBtn.setOnClickListener {
            if(selectedStr == null || selectedStr == "검색 설정")
                Toast.makeText(applicationContext,"검색 설정을 하지 않았습니다.",Toast.LENGTH_SHORT).show()
            else {
                searchStr = searchEdit.text.toString()
                val intent = Intent(applicationContext, SearchListPage::class.java)
                intent.putExtra("searchStr", searchStr)
                intent.putExtra("category", selectedStr)
                startActivity(intent)
            }
        }

    }

    private fun ASD(){
        val url = URL("http://192.168.5.71/reportdata.php")
        val request = Request.Builder().url(url).build()
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
                    val reportNum2 = findViewById<TextView>(R.id.reportNum)
                    reportNum2.text = "검색 결과 : " + list.result.count().toString()
                    Log.d("사이즈2",searchNum.toString())
                    reportView.adapter = ReportListAdapter(list)
                }
            }
        })
    }
}

data class ReportJson(val result: List<ReportData>)
data class ReportData(var REP_NUMB: String, var EMP_NUMB: String, var BUD_CODE: String, var ACC_CODE: String, var REP_DATE: String, var REP_FLOR: String, var REP_CONT: String, var REP_ACTN: String, var REP_DAMG: String, var REP_CAUS: String, var REP_COND: String, var REP_FMCO: String, var PRM_CODE: String, var REP_FILE: String, var REP_APRV: String, var REP_WTER: String, var REP_WTDT: String)