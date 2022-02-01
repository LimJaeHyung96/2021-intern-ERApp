package com.example.uitest

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.login_page.*
import java.io.IOException
import java.net.URL
import okhttp3.*

class MainActivity : AppCompatActivity() {
    val arrListID : MutableList<String> = mutableListOf<String>()
    val arrListPWD : MutableList<String> = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        ASD() // 로그인 정보를 리스트에 받아옴
        //Notification().createNotificationChannel()

        id_editText.setText(AutoLogin.prefs1.myEditText1)       //id 자동로그인 기능
        pw_editText.setText(AutoLogin.prefs2.myEditText2)       //pw 자동로그인 기능
        auto_checkBox.setOnClickListener {
            AutoLogin.prefs1.myEditText1 = id_editText.text.toString()
            AutoLogin.prefs2.myEditText2 = pw_editText.text.toString()
        }

        var loginID = findViewById<EditText>(R.id.id_editText)
        var loginPWD = findViewById<EditText>(R.id.pw_editText)
        var login_btn = findViewById<Button>(R.id.login_button)

        var loginValue = 0          // 로그인 성공시 실패메세지 중복 팝업 제거

        //로그인 버튼 누를 시 동작
        login_btn.setOnClickListener {
            var IDStr = loginID.text.toString()
            var PWDStr = loginPWD.text.toString()

            //리스트에 저장된 모든 ID,PW 정보와 대조해서 맞을 시 페이지 넘어감
            for(i in 0 until arrListID.size){
                if(IDStr == arrListID[i]) {
                    if (PWDStr == arrListPWD[i]) {
                        Toast.makeText(applicationContext, "로그인성공", Toast.LENGTH_SHORT).show()
                        loginValue = 1
                        AutoLogin.loginID.nowLogin = IDStr
                        val intent = Intent(this, SecondAutoSc::class.java)
                        startActivity(intent)
                        break
                    }
                }
            }

            //리스트에 정보를 받아 왔으나 맞지 않을 경우
            if(loginValue == 0 && arrListID.size != 0)
                Toast.makeText(applicationContext, "로그인실패", Toast.LENGTH_SHORT).show()

            //리스트에 아예 정보를 불러오지 못 했을 경우
            if(arrListID.size == 0)
                Toast.makeText(applicationContext, "통신실패", Toast.LENGTH_SHORT).show()
        }
    }

    //웹서버에서 ID,PW 정보를 전부 가져와 배열에 저장하는 함수
    private fun ASD(){
        val url = URL("http://192.168.5.71/phpdata.php")
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
                val list = gson.fromJson(body, JsonObj::class.java)
                val arrCount = list.result.count()

                for(i in 0 until arrCount){
                    arrListID.add(list.result[i].EMP_NUMB)
                    arrListPWD.add(list.result[i].EMP_PAWD)
                }
            }
        })
    }

}
class MySharedPreferences1(context: Context){    //Id auto saving with sharedpreferences

    private val prefsFilename = "prefs"
    private val prefsKeyEdt = "myEditText"
    private val prefsKeyEdt1 = "myEditText1"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename,0)

    var myEditText1: String?
        get() = prefs.getString(prefsKeyEdt,"")
        set(value) = prefs.edit().putString(prefsKeyEdt, value).apply()

    var myEditText2: String?
        get() = prefs.getString(prefsKeyEdt1,"")
        set(value) = prefs.edit().putString(prefsKeyEdt1, value).apply()

    var nowLogin: String?
        get() = prefs.getString("loginNow","")
        set(value) = prefs.edit().putString("loginNow", value).apply()
}
class AutoLogin : Application() {
    companion object {
        lateinit var loginID : MySharedPreferences1
        lateinit var prefs1 : MySharedPreferences1
        lateinit var prefs2 : MySharedPreferences1
    }

    override fun onCreate() {
        loginID = MySharedPreferences1(applicationContext)
        prefs2 = MySharedPreferences1(applicationContext)
        prefs1 = MySharedPreferences1(applicationContext)
        super.onCreate()

    }
}

data class JsonObj(val result : List<LoginData>)
data class LoginData(var EMP_NUMB : String, var EMP_PAWD : String)