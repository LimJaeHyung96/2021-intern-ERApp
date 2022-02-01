package com.example.uitest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.change_pw_page.*
import kotlinx.android.synthetic.main.search_page.*
import okhttp3.*
import java.io.IOException
import java.net.URL

class PasswordChange :AppCompatActivity() {
    var pw = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_pw_page)
        title = "비밀번호 변경"

        ASD2()

        val nowPW = findViewById<EditText>(R.id.now_pw)
        val newPW = findViewById<EditText>(R.id.new_pw)
        val pwCheck = findViewById<EditText>(R.id.pw_check)
        val changeBtn = findViewById<Button>(R.id.pw_change)

        changeBtn.setOnClickListener {
            if(nowPW.text.toString() != pw)
                Toast.makeText(applicationContext,"현재 비밀번호가 틀립니다.",Toast.LENGTH_SHORT).show()
            else if(newPW.text.toString() != pwCheck.text.toString())
                Toast.makeText(applicationContext,"새 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
            else{
                Toast.makeText(applicationContext,"비밀번호가 성공적으로 변경되었습니다.",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainPage::class.java)
                startActivity(intent)
            }
        }
    }

    private fun ASD2(){
        val url = URL("http://192.168.5.71/getpw.php")

        var requestBody = FormBody.Builder().add("id",AutoLogin.loginID.nowLogin).build()

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
                val list = gson.fromJson(body, ChangeJson::class.java)

                runOnUiThread {
                    pw = list.result[0].EMP_PAWD
                }
            }
        })
    }

    private fun ASD3(){
        val url = URL("http://192.168.5.71/getpw.php")

        var requestBody = FormBody.Builder().add("pw",new_pw.text.toString()).build()

        val request = Request.Builder().url(url).post(requestBody).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            //통신에 실패했을 때
            override fun onFailure(call: Call?, e: IOException?) {
                Log.d("통신상태", "실패")
            }

            //통신에 성공했을 때
            override fun onResponse(call: Call?, response: Response?) {
                Log.d("변경","성공")
            }
        })
    }
}

data class ChangeJson(val result : List<ChangePW>)
data class ChangePW(var EMP_PAWD : String)