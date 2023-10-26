package com.example.myquizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubCategoryActivity : AppCompatActivity() {
    var subjectID = 0
    var name = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)
        val listview = findViewById<ListView>(R.id.listSubCategory)
        val textview = findViewById<TextView>(R.id.textview)
        subjectID = intent.getIntExtra("subjectID", 0)
        name = intent.getStringExtra("name").toString()
        textview.text= "$name"
        val quesApi = RetrofitHelper.getInstance().create(QuestionsApi::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            quesApi.getSubCategory(
                RequestBody.create(MediaType.parse("text/plain"), subjectID.toString())
            ).enqueue(object :
                Callback<SubCatArrayResponse> {
                override fun onResponse(
                    call: Call<SubCatArrayResponse>,
                    response: Response<SubCatArrayResponse>
                ) {
                    if (response.body() != null) {
                        var adapter = response.body()?.let {
                            SubCategoryAdapter(
                                this@SubCategoryActivity,
                                it
                            )
                        }
                        listview.adapter = adapter
                    } else {
                        // Log.e("result is null ", response.body().toString())
                        return
                    }
                    //Log.e("my api key ", response.body()!!.results.toString())
                }

                override fun onFailure(call: Call<SubCatArrayResponse>, t: Throwable) {
                    Log.e("result is null ", t.message.toString())
                    //Log.d("TAG", t.message.toString())
                }
            })


        }

    }
}