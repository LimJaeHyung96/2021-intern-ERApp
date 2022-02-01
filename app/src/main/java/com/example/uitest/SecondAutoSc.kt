package com.example.uitest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.net.URL

class SecondAutoSc : AppCompatActivity() {
    var checkSpw = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_auth_page)

        val editText = findViewById<EditText>(R.id.pw_editText)
        val loginBtn = findViewById<Button>(R.id.login_button)

        ASD()
        //Notification().createNotificationChannel()

        //2차인증 버튼 클릭 시 동작
        loginBtn.setOnClickListener {
            var spwStr = editText.text.toString()

            if(spwStr == checkSpw) {
                Toast.makeText(applicationContext, "2차인증 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainPage::class.java)
                startActivity(intent)
            } else if(spwStr != checkSpw)
                Toast.makeText(applicationContext,"2차인증 실패",Toast.LENGTH_SHORT).show()
        }
    }

    //2차인증 페이지 실행 시 String 값에 2차인증 코드를 저장함
    private fun ASD(){
        val url = URL("http://192.168.5.71/spwcheck.php")
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
                val list = gson.fromJson(body, JsonObj2::class.java)

                checkSpw = list.result[0].SPW_PAWD

                Log.d("확인", "로그")
            }

        })
    }
}

data class JsonObj2(val result : List<SpwLoginData>)
data class SpwLoginData(var SPW_PAWD : String)