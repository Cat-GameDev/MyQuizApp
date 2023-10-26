package com.example.myquizapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllQuestionsActivity : AppCompatActivity() {
    companion object{
        var subjectID = 0;
        var sub_cat_id = 0;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_questions)
        subjectID = intent.getIntExtra("subjectID", 0)
        sub_cat_id = intent.getIntExtra("sub_cat_id", 0)
        val listview: ListView = findViewById(R.id.listSubCategory)
        val btnFinish: Button = findViewById(R.id.btnFinish)
        btnFinish.setOnClickListener {
            var intent = Intent(this, QuestionsActivity::class.java)
            intent.putExtra("subjectID", subjectID)
            intent.putExtra("sub_cat_id", sub_cat_id)
            startActivity(intent)
        }
        val quesApi = RetrofitHelper.getInstance().create(QuestionsApi::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            quesApi.getQuestions(
                RequestBody.create(MediaType.parse("text/plain"), subjectID.toString()),
                RequestBody.create(MediaType.parse("text/plain"), sub_cat_id.toString())
            ).enqueue(object :
                Callback<QuestionsListResponse> {
                override fun onResponse(
                    call: Call<QuestionsListResponse>,
                    response: Response<QuestionsListResponse>
                ) {
                    if (response.body() != null) {
                        response.body()?.let {
                            var adapter = response.body()?.let {
                                MyAdapter(
                                    this@AllQuestionsActivity,
                                    it
                                )
                            }
                            listview.adapter = adapter
                        }
                    } else {
                        // Log.e("result is null ", response.body().toString())
                        return
                    }
                    //Log.e("my api key ", response.body()!!.results.toString())
                }

                override fun onFailure(call: Call<QuestionsListResponse>, t: Throwable) {
                    Log.e("result is null ", t.message.toString())
                    //Log.d("TAG", t.message.toString())
                }
            })


        }


    }

    class MyAdapter(val context: Context, val subjectResponse: QuestionsListResponse) :
        BaseAdapter() {
        override fun getCount(): Int {
            return subjectResponse.size
        }

        override fun getItem(p0: Int): Any {
            return p0
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
            var view = p1
            view = LayoutInflater.from(context).inflate(R.layout.list_items_questions, p2, false)
            val textSubjectName = view.findViewById<TextView>(R.id.subjectName)
            val imgDelete = view.findViewById<ImageView>(R.id.imgDelete)
            imgDelete.setOnClickListener {
                deleteQues(context, subjectResponse[position].id)
            }
            textSubjectName.text = "${position+1}. ${subjectResponse[position].question} \n " +
                    "A. ${subjectResponse[position].option1}\n" +
                    "B. ${subjectResponse[position].option2}\n" +
                    "C. ${subjectResponse[position].option3}\n" +
                    "D. ${subjectResponse[position].option4}" +
                    ""
            return view
        }

        fun deleteQues(context: Context, id: Int) {
            val quesApi = RetrofitHelper.getInstance().create(QuestionsApi::class.java)
            GlobalScope.launch(Dispatchers.IO) {
                quesApi.deleteQuestion(
                    RequestBody.create(MediaType.parse("text/plain"), id.toString())
                ).enqueue(object :
                    Callback<AddQuestionResponse> {
                    override fun onResponse(
                        call: Call<AddQuestionResponse>,
                        response: Response<AddQuestionResponse>
                    ) {
                        if (response.body() != null) {
                            Toast.makeText(
                                context,
                                response.body()!!.status.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            var intent= Intent(context,AllQuestionsActivity::class.java)
                            intent.putExtra("subjectID",subjectID)
                            intent.putExtra("sub_cat_id", sub_cat_id)
                            context.startActivity(intent)
                        } else {
                            // Log.e("result is null ", response.body().toString())
                            return
                        }
                        //Log.e("my api key ", response.body()!!.results.toString())
                    }

                    override fun onFailure(call: Call<AddQuestionResponse>, t: Throwable) {
                        Log.e("result is null ", t.message.toString())
                        //Log.d("TAG", t.message.toString())
                    }
                })
            }
        }
    }
}