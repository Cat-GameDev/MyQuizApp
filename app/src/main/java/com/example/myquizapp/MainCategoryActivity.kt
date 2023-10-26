package com.example.myquizapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainCategoryActivity : AppCompatActivity() {
    private var subjectResponse:SubjectResponse?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        val listview = findViewById<ListView>(R.id.listview)
        val quesApi = RetrofitHelper.getInstance().create(QuestionsApi::class.java)
        GlobalScope.launch {
            val result = quesApi.getQuiz()
            if (result != null) {
                subjectResponse=result.body();
                Log.e("subjectResponse ",subjectResponse.toString())
            }
            var adapter = subjectResponse?.let { MyAdapter(this@MainCategoryActivity, it) }
            GlobalScope.launch(Dispatchers.Main) {
              listview.adapter=adapter
            }
        }
    }

    class MyAdapter(val context: Context, val subjectResponse: SubjectResponse):BaseAdapter(){
        override fun getCount(): Int {
          return  subjectResponse.size
        }
        override fun getItem(p0: Int): Any {
            return  p0
        }
        override fun getItemId(p0: Int): Long {
            return  p0.toLong()
        }

        override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
           var view=p1
            view=LayoutInflater.from(context).inflate(R.layout.list_items,p2,false)
            val textSubjectName=view.findViewById<TextView>(R.id.subjectName)
            textSubjectName.text= subjectResponse[position].name
            view.setOnClickListener(){
                Log.e("subjectID is ", subjectResponse[position].cat_id.toString())
                var intent=Intent(context,SubCategoryActivity::class.java)
                intent.putExtra("subjectID", subjectResponse[position].cat_id)
                intent.putExtra("name", subjectResponse[position].name)
                context.startActivity(intent)
            }
            return view
        }
    }
}