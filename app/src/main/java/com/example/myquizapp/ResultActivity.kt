package com.example.myquizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.myquizapp.QuestionsActivity.Companion.result
import com.example.myquizapp.QuestionsActivity.Companion.totalQuestions

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        var txtCorrectAnswer = findViewById<TextView>(R.id.txtCorrectAnswer)
        var txtWrongAnswer = findViewById<TextView>(R.id.txtWrongAnswer)
        var txtFinalScore = findViewById<TextView>(R.id.txtFinalScore)
        var btnBack = findViewById<Button>(R.id.btnBack)
        txtCorrectAnswer.text = "Số câu đúng là ${result}"
        txtWrongAnswer.text = "Số câu sai là ${totalQuestions - result}"
        txtFinalScore.text = "Điểm : $result/$totalQuestions"
        btnBack.setOnClickListener {
           onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}