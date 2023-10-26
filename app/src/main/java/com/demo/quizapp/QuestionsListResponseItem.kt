package com.demo.quizapp

data class QuestionsListResponseItem(
    val correct_option: String,
    val subject_id: String,
    val id: Int,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val question: String
)