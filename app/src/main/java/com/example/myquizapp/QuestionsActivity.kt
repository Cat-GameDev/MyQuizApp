package com.example.myquizapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class QuestionsActivity : AppCompatActivity() {
    var txtQuestion: TextView? = null
    lateinit var DispName: TextView
    var txtResult: TextView? = null
    var radioGroup: RadioGroup? = null
    var btnNext: Button? = null
    var rbOption1: RadioButton? = null
    var rbOption2: RadioButton? = null
    var rbOption3: RadioButton? = null
    var rbOption4: RadioButton? = null
    var subjectID = 0;
    var sub_cat_id = 0;
    private var mCountDown: CountDownTimer? = null
    private var questionsList: QuestionsListResponse? = null
    private var ans = ""

    companion object {
        var result = 0
        var totalQuestions = 0
        var i = 1

    }

    override fun onResume() {
        super.onResume()
        i = 1
        result = 0
        totalQuestions = 0
    }

    val numbers: MutableList<Int> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)
        subjectID = intent.getIntExtra("subjectID", 0)
        sub_cat_id = intent.getIntExtra("sub_cat_id", 0)
        txtQuestion = findViewById(R.id.txtQuestion)
        radioGroup = findViewById(R.id.radioGroup)
        DispName = findViewById(R.id.DispName)
        btnNext = findViewById(R.id.btnNext)
        rbOption1 = findViewById(R.id.radio1)
        rbOption2 = findViewById(R.id.radio2)
        rbOption3 = findViewById(R.id.radio3)
        rbOption4 = findViewById(R.id.radio4)
        txtResult = findViewById(R.id.txtResult)

        radioGroup?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton = group?.findViewById<View>(checkedId) as? RadioButton
            val isChecked = checkedRadioButton?.isChecked
            if (isChecked == true) {
                ans = checkedRadioButton?.text.toString()
            }
        })
        btnNext?.setOnClickListener(View.OnClickListener {
            questionsList.let {
                if (i < it?.size!!) {
                    mCountDown?.start()
                    totalQuestions = it.size
                    Log.e(ans,it[numbers[i-1]].correct_option)
                    if (ans == it[numbers[i-1]].correct_option) {
                        result++
                        txtResult?.text = "Số câu đúng : $result"
                    }
                    txtQuestion?.text = "${i + 1}. " + questionsList!![numbers[i]].question
                    rbOption1?.text = it[numbers[i]].option1
                    rbOption2?.text = it[numbers[i]].option2
                    rbOption3?.text = it[numbers[i]].option3
                    rbOption4?.text = it[numbers[i]].option4
                    if (i == it?.size.minus(1)) {
                        btnNext?.text = "Finish"
                    }
                    ans=""
                    radioGroup?.clearCheck()
                    i++
                } else {
                    mCountDown?.cancel()
                    if (ans == it[numbers[i-1]].correct_option) {
                        result++
                        txtResult?.text = "Số câu đúng : $result"
                    }
                    val intent = Intent(this@QuestionsActivity, ResultActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                // timer(it)
            }
//            } else {
//                Toast.makeText(
//                    this@QuestionsActivity,
//                    "Please select one option",
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }
        })

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
                            for (i in 0 until it.size) {
                                numbers.add(i)
                            }
                            numbers.shuffle()
                            questionsList = it
                            GlobalScope.launch(Dispatchers.Main) {
                                Log.e("total questions are ", questionsList?.size.toString())
                                txtQuestion?.text =
                                    "1. " + questionsList!![numbers[0]].question
                                rbOption1?.text = questionsList!![numbers[0]].option1
                                rbOption2?.text = questionsList!![numbers[0]].option2
                                rbOption3?.text = questionsList!![numbers[0]].option3
                                rbOption4?.text = questionsList!![numbers[0]].option4
                                timer(it)
                            }
                        }
                    } else {
                        return
                    }
                }

                override fun onFailure(call: Call<QuestionsListResponse>, t: Throwable) {
                    Log.e("result is null ", t.message.toString())
                }
            })


        }

    }

    fun timer(questionList: QuestionsListResponse) {
        mCountDown = object : CountDownTimer(5000, 1000) {
            override fun onFinish() {
                DispName.setText("Kết thúc!")
                btnNext?.performClick()
            }

            override fun onTick(millisUntilFinished: Long) {
                DispName.setText(
                    "Thời gian: "
                            + (millisUntilFinished / 1000).toString()
                )
            }
        }.start()
    }
}