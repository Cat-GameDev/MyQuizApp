package com.demo.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddQuestionsActivity : AppCompatActivity() {
    var subjectID = 0;
    var sub_cat_id = 0;
    lateinit var edQuestion:EditText
    lateinit var edOption1:EditText
    lateinit var edOption2:EditText
    lateinit var edOption3:EditText
    lateinit var edOption4:EditText
    lateinit var answer:Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_questions)
        edQuestion=findViewById(R.id.edQuestion)
        edOption1=findViewById(R.id.edOption1)
        edOption2=findViewById(R.id.edOption2)
        edOption3=findViewById(R.id.edOption3)
        edOption4=findViewById(R.id.edOption4)
        answer=findViewById(R.id.txtAnswer)
       val btnAdd:Button=findViewById(R.id.btnAdd)
       val btnFinish:Button=findViewById(R.id.btnFinish)
        subjectID = intent.getIntExtra("subjectID", 0)
        sub_cat_id = intent.getIntExtra("sub_cat_id", 0)
        btnAdd.setOnClickListener{
            addQues()
        }
        btnFinish.setOnClickListener{
            var intent= Intent(this,AllQuestionsActivity::class.java)
            intent.putExtra("subjectID",subjectID)
            intent.putExtra("sub_cat_id", sub_cat_id)
            startActivity(intent)
        }
    }

    fun addQues() {
        var quesApi = RetrofitHelper.getInstance().create(QuestionsApi::class.java)
        var ans=answer.selectedItem.toString()
        if (ans.equals("Đáp án 1"))
            ans=edOption1.text.toString()
         if (ans.equals("Đáp án 2"))
            ans=edOption2.text.toString()
         if (ans.equals("Đáp án 3"))
            ans=edOption3.text.toString()
         if (ans.equals("Đáp án 4"))
            ans=edOption4.text.toString()
        GlobalScope.launch(Dispatchers.IO) {
            quesApi.addQuestions(
                RequestBody.create(MediaType.parse("text/plain"), subjectID.toString()),
                RequestBody.create(MediaType.parse("text/plain"), sub_cat_id.toString()),
                RequestBody.create(MediaType.parse("text/plain"), edQuestion.text.toString()),
                RequestBody.create(MediaType.parse("text/plain"), edOption1.text.toString()),
                RequestBody.create(MediaType.parse("text/plain"), edOption2.text.toString()),
                RequestBody.create(MediaType.parse("text/plain"), edOption3.text.toString()),
                RequestBody.create(MediaType.parse("text/plain"), edOption4.text.toString()),
                RequestBody.create(MediaType.parse("text/plain"), ans)
            ).enqueue(object :
                Callback<AddQuestionResponse> {
                override fun onResponse(
                    call: Call<AddQuestionResponse>,
                    response: Response<AddQuestionResponse>
                ) {
                    if (response.body() != null) {
                        var status= response.body()!!.status
                        Toast.makeText(this@AddQuestionsActivity, status, Toast.LENGTH_SHORT).show()
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