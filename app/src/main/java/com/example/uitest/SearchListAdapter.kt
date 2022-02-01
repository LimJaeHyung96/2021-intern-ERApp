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
import kotlinx.android.synthetic.main.report_item.view.*
import java.lang.Exception

class SearchListAdapter(var mylist : ReportJson) : RecyclerView.Adapter<CustomViewHolder3>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder3 {
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(R.layout.report_item, parent, false)
        return CustomViewHolder3(v)
    }

    override fun onBindViewHolder(holder: CustomViewHolder3, position: Int) {
        holder.bindItems(mylist.result[position])
    }

    override fun getItemCount(): Int {
        return mylist.result.count()
    }
}

class CustomViewHolder3(private val view : View) : RecyclerView.ViewHolder(view){
    fun bindItems(data: ReportData) {

        if(data.REP_NUMB == "x") {
            view.repName.text = "검색 결과가 없습니다."
            view.repEMP.text = ""
            view.repDate.text = ""
        }else{
            view.repName.text = data.BUD_CODE + " " + data.ACC_CODE + "발생보고"
            view.repEMP.text = data.REP_WTER
            view.repDate.text = data.REP_DATE
        }

        view.setOnClickListener {
            val intent = Intent(it.context, ReportShowPage::class.java)
            intent.putExtra("EMP",data.EMP_NUMB)
            view.context.startActivity(intent)
        }
    }
}