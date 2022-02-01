package com.example.uitest

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listview_item.view.*
import java.lang.Exception

class CallAdapter(var mylist: JsonObject) : RecyclerView.Adapter<CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(R.layout.listview_item, parent, false)
        return CustomViewHolder(v)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(mylist.result[position])
    }

    override fun getItemCount(): Int {
        return mylist.result.count()
    }
}

class CustomViewHolder(private val view : View) : RecyclerView.ViewHolder(view){
    fun bindItems(data: PhoneData) {
        view.item1.text = data.EMP_FUNC + " " +data.EMP_NAME
        view.item2.text = data.EMP_PHNO

        view.imageButton.setOnClickListener {
            val dialog = AlertDialog.Builder(it.context)
            dialog.setTitle("비상연락망")
            dialog.setMessage("전화를 거시겠습니까?")
            dialog.setNegativeButton("아니오"){ dialogInterface, i ->

            }
            dialog.setPositiveButton("네") { dialogInterface, i ->
                try {
                    var intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.EMP_PHNO))
                    view.context.startActivity(intent)
                }catch (e: Exception){
                    Toast.makeText(it.context, "연결 실패", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.show()
        }
    }
}