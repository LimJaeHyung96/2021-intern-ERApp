package com.example.uitest

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.telephony.SmsManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.report_write_page.*
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import java.net.URL

class FireReport : AppCompatActivity() {
    private var acc_case : String = ""
    private val baseColor = "#4374D9"
    private var isBtnDown = false
    var sttIntent : Intent? = null
    var mRecognizer : SpeechRecognizer? = null
    var mRecognizer2 : SpeechRecognizer? = null
    var location_text: TextView? = null
    var content_text: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val cThis : Context = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_write_page)
        title = "(비)화재 보고"

        val photo_take = findViewById<Button>(R.id.photo_take)
        val photo_upload = findViewById<Button>(R.id.photo_upload)
        val fire = findViewById<Button>(R.id.fire)
        val notFire = findViewById<Button>(R.id.notFire)
        val location_btn = findViewById<ImageButton>(R.id.location_btn)
        val content_btn = findViewById<ImageButton>(R.id.content_btn)
        val send_btn = findViewById<Button>(R.id.send_btn)

        location_text = findViewById<EditText>(R.id.location_text)
        content_text = findViewById<EditText>(R.id.content_text)

        sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        sttIntent!!.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,applicationContext.packageName)
        sttIntent!!.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR")

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(cThis)
        mRecognizer!!.setRecognitionListener(listener)

        mRecognizer2 = SpeechRecognizer.createSpeechRecognizer(cThis)
        mRecognizer2!!.setRecognitionListener(listener2)

        //사진 촬영기능
        photo_take.setOnClickListener {
            Toast.makeText(applicationContext,"아직 구현되지 않은 기능입니다.",Toast.LENGTH_SHORT).show()
        }

        //사진 업로드 기능
        photo_upload.setOnClickListener {
            Toast.makeText(applicationContext,"아직 구현되지 않은 기능입니다.",Toast.LENGTH_SHORT).show()
        }

        //화재 버튼 클릭 시 사고 내용 저장 및 버튼 색 변환
        fire.setOnClickListener {
            acc_case = "화재"
            fire.setBackgroundColor(Color.RED)
            notFire.setBackgroundColor(Color.parseColor(baseColor))
        }

        //비화재 버튼 클릭 시 사고 내용 저장 및 버튼 색 변환
        notFire.setOnClickListener {
            acc_case = "비화재"
            fire.setBackgroundColor(Color.parseColor(baseColor))
            notFire.setBackgroundColor(Color.RED)
        }

        location_btn.setOnTouchListener(onBtnTouchListener) //장소 음성인식 버튼 누르고 있을 때 동작 처리
        content_btn.setOnTouchListener(onBtnTouchListener2) //내용 음성인식 버튼 누르고 있을 때 동작 처리

        //발송 버튼 클릭시 처리
        send_btn.setOnClickListener {

            var a = location_text?.text.toString()
            Toast.makeText(applicationContext,a,Toast.LENGTH_SHORT).show()
            Log.d("a는", a)
            /*
            var smsText = ""
            InsertThread(oneStr,twoStr,nameStr).run() // 데이터 입력 부분

            //SMS 발송 부분
            try {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage("010-2066-3990", null, smsText, null, null)
                smsManager.sendTextMessage("010-9357-3570", null, smsText, null, null)
                Toast.makeText(applicationContext,"전송 성공",Toast.LENGTH_SHORT).show()
            } catch(e : Exception){
                Toast.makeText(applicationContext,"전송 실패",Toast.LENGTH_SHORT).show()
            }
             */
            //val intent = Intent(this, MainPage::class.java)
            //startActivity(intent)
        }
    }

    //버튼 누르고 있는 동안 동작하는 함수(장소)
    private fun onBtnDown(){
        val kThread : TouchThread = TouchThread()
        kThread.start()
        try {
            mRecognizer!!.startListening(sttIntent)
        }catch(e : SecurityException){
            Log.d("예외",e.toString())
        }
    }

    //버튼 누르고 있는 동안 동작하는 함수(내용)
    private fun onBtnDown2(){
        val kThread : TouchThread = TouchThread()
        kThread.start()
        try {
            mRecognizer2!!.startListening(sttIntent)
        }catch(e : SecurityException){
            Log.d("예외",e.toString())
        }
    }

    private val touchHandler : Handler = object : Handler(){
        override fun handleMessage(msg: Message) {

        }
    }

    //쓰레드에서 0.2초마다 계속해서 갱신
    private inner class TouchThread : Thread(){
        override fun run() {
            super.run()
            while(isBtnDown){
                touchHandler.sendEmptyMessage(9876)
                try{
                    sleep(200)
                }catch (e : Exception){
                    Log.d("쓰레드","예외")
                }
            }
        }
    }

    //장소 부분 음성인식 버튼 동작
    private val onBtnTouchListener = View.OnTouchListener{ v, event ->
        when(event.action){
            MotionEvent.ACTION_DOWN->{
                isBtnDown = true
                onBtnDown()
            }
            MotionEvent.ACTION_UP->{
                isBtnDown = false
                mRecognizer!!.cancel()
            }
            else -> {
            }
        }
        false
    }

    //내용 부분 음성인식 버튼 동작
    private val onBtnTouchListener2 = View.OnTouchListener{ v, event ->
        when(event.action){
            MotionEvent.ACTION_DOWN->{
                isBtnDown = true
                onBtnDown2()
            }
            MotionEvent.ACTION_UP->{
                isBtnDown = false
                mRecognizer2!!.cancel()
            }
            else -> {
            }
        }
        false
    }

    //장소 부분 음성 인식
    private val listener: RecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(bundle: Bundle) {
            val myToast = Toast.makeText(applicationContext, "인식이 완료될 때까지 꾹 눌러주세요", Toast.LENGTH_SHORT)
            myToast.show()
        }

        override fun onBeginningOfSpeech() {

        }

        override fun onRmsChanged(v: Float) {}
        override fun onBufferReceived(bytes: ByteArray) {

        }

        override fun onEndOfSpeech() {

        }

        override fun onError(i: Int) {
            if(i == 7)
                Toast.makeText(applicationContext, "인식 실패! 다시 누르고 해주세요.", Toast.LENGTH_SHORT).show()
            Log.d("에러",i.toString())
        }

        override fun onResults(results: Bundle) {
            var key = SpeechRecognizer.RESULTS_RECOGNITION
            val mResult = results.getStringArrayList(key)
            val rs = arrayOfNulls<String>(mResult!!.size)
            mResult.toArray(rs)
            location_text!!.text = rs[0].toString() + " " + location_text!!.getText()
            mRecognizer!!.startListening(sttIntent)
        }

        override fun onPartialResults(bundle: Bundle) {}
        override fun onEvent(i: Int, bundle: Bundle) {}
    }

    //내용 부분 음성인식
    private val listener2: RecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(bundle: Bundle) {
            val myToast = Toast.makeText(applicationContext, "인식이 완료될 때까지 꾹 눌러주세요", Toast.LENGTH_SHORT)
            myToast.show()
        }

        override fun onBeginningOfSpeech() {

        }

        override fun onRmsChanged(v: Float) {}
        override fun onBufferReceived(bytes: ByteArray) {

        }

        override fun onEndOfSpeech() {

        }

        override fun onError(i: Int) {
            if(i == 7)
                Toast.makeText(applicationContext, "인식 실패! 다시 누르고 해주세요.", Toast.LENGTH_SHORT).show()
            Log.d("에러",i.toString())
        }

        override fun onResults(results: Bundle) {
            var key = SpeechRecognizer.RESULTS_RECOGNITION
            val mResult = results.getStringArrayList(key)
            val rs = arrayOfNulls<String>(mResult!!.size)
            mResult.toArray(rs)
            content_text!!.text = rs[0].toString() + " " + content_text!!.getText()
            mRecognizer!!.startListening(sttIntent)
        }

        override fun onPartialResults(bundle: Bundle) {}
        override fun onEvent(i: Int, bundle: Bundle) {}
    }
}

/* 발송 버튼을 누를 시 웹서버에 데이터 전송
class InsertThread(one: String, two: String, name: String) : Thread(){
    private var ins_One = one
    private var ins_Two = two
    private var ins_Name = name
    override fun run() {
        super.run()
        val url = URL("http://192.168.5.71/insert.php")

        var requestBody = FormBody.Builder().add("INS_ONE",ins_One).add("INS_TWO",ins_Two).add("INS_NAME",ins_Name).build()

        val request = Request.Builder().url(url).post(requestBody).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            //통신에 실패했을 때
            override fun onFailure(call: Call?, e: IOException?) {
                Log.d("에러",e.toString())
            }

            //통신에 성공했을 때
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
            }
        })
    }
}*/