package com.example.uitest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.from


class MainPage : AppCompatActivity() {
    private var notificationId = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
        title = "메인페이지"

        //Notification().createNotificationChannel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel("Test", "테스트", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "descriptionText"
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


        val btn1 = findViewById<Button>(R.id.btn1)
        val btn2 = findViewById<Button>(R.id.btn2)
        val btn3 = findViewById<Button>(R.id.btn3)
        val btn4 = findViewById<Button>(R.id.btn4)
        val btn5 = findViewById<Button>(R.id.btn5)

        //(비)화재 비상 보고 버튼 클릭 시 동작
        btn1.setOnClickListener {
            val intent = Intent(this, FireReport::class.java)
            startActivity(intent)
        }

        //일반 비상 보고 버튼 클릭 시 동작
        btn2.setOnClickListener {
            Toast.makeText(applicationContext, "아직 구현되지 않은 페이지 입니다!", Toast.LENGTH_SHORT).show()
        }

        //비상 연락망 버튼 클릭 시 동작
        btn3.setOnClickListener {
            val intent = Intent(this, ErCallPage::class.java)
            startActivity(intent)
        }

        //공지사항 버튼 클릭 시 동작
        btn4.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


            val builder = NotificationCompat.Builder(this, "Test")
                    .setSmallIcon(R.drawable.kfns)
                    .setContentTitle("알림 테스트")
                    .setContentText("알림 테스트")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
            with(from(this)) {
                notify(notificationId, builder.build())
            }
        }

        //일반 사항 버튼 클릭 시 동작
        btn5.setOnClickListener {
            Toast.makeText(applicationContext, AutoLogin.loginID.nowLogin, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.menu1, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItem1 -> {                // 항목1을 선택했을때 실행할 코드
                val intent = Intent(this, ReportListPage::class.java)
                startActivity(intent)
            }
            R.id.menuItem2 ->                 // 항목2을 선택했을때 실행할 코드
                Toast.makeText(applicationContext, "아직 구현되지 않은 페이지 입니다!", Toast.LENGTH_SHORT).show()
            R.id.menuItem3 ->                 // 항목4을 선택했을때 실행할 코드
                Toast.makeText(applicationContext, "아직 구현되지 않은 페이지 입니다!", Toast.LENGTH_SHORT).show()
            R.id.menuItem4 ->                 // 항목5을 선택했을때 실행할 코드
                Toast.makeText(applicationContext, "아직 구현되지 않은 페이지 입니다!", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

}