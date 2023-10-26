package com.demo.quizapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SubCategoryAdapter(val context: Context, val subCategoryModel: ArrayList<SubCategoryModel>): BaseAdapter(){
    override fun getCount(): Int {
        return  subCategoryModel.size
    }
    override fun getItem(p0: Int): Any {
        return  p0
    }
    override fun getItemId(p0: Int): Long {
        return  p0.toLong()
    }

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        var view=p1
        view= LayoutInflater.from(context).inflate(R.layout.list_items,p2,false)
        val textSubjectName=view.findViewById<TextView>(R.id.subjectName)
        textSubjectName.text= subCategoryModel[position].name
        view.setOnClickListener{
            Log.e("subjectID is ", subCategoryModel[position].cat_id.toString())
            var intent= Intent(context,AddQuestionsActivity::class.java)
            intent.putExtra("subjectID", subCategoryModel[position].cat_id)
            intent.putExtra("sub_cat_id", subCategoryModel[position].sub_cat_id)
            context.startActivity(intent)
        }
        return view
    }
}
