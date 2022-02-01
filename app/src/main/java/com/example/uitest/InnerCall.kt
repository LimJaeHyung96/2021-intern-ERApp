package com.example.uitest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.er_call.*
import okhttp3.*
import java.io.IOException
import java.net.URL

class InnerCall : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView : ViewGroup = inflater.inflate(R.layout.er_call,container,false) as ViewGroup
        var recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(activity)
        ASD()

        return rootView
    }

    private fun ASD(){
        val url = URL("http://192.168.5.71/phpphone.php")
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
                val list = gson.fromJson(body, JsonObject::class.java)
                Log.d("통신상태", "성공")

                recyclerView.adapter = CallAdapter(list)
                Log.d("통신상태", "성공")
            }
        })
    }
}

data class JsonObject(val result : List<PhoneData>)
data class PhoneData(var EMP_NAME : String, var EMP_FUNC : String, var EMP_PHNO : String)