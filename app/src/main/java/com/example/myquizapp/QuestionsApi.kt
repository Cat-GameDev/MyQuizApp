package com.example.myquizapp

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface QuestionsApi {
    @GET("getCategory.php")
    suspend fun getQuiz():Response<SubjectResponse>

    @Multipart
    @POST("getSubCategory.php")
    fun getSubCategory(
        @Part("cat_id") cat_id: RequestBody,
    ): Call<SubCatArrayResponse>

    @Multipart
    @POST("getQuestions.php")
    fun getQuestions(
        @Part("cat_id") cat_id: RequestBody,
        @Part("sub_cat_id") sub_cat_id: RequestBody,
    ): Call<QuestionsListResponse>

      @Multipart
    @POST("deleteQuestion.php")
    fun deleteQuestion(
        @Part("id") id: RequestBody,
    ): Call<AddQuestionResponse>

    @Multipart
    @POST("add_questions.php")
    fun addQuestions(
        @Part("cat_id") cat_id: RequestBody,
        @Part("sub_cat_id") sub_cat_id: RequestBody,
        @Part("question") question: RequestBody,
        @Part("option1") option1: RequestBody,
        @Part("option2") option2: RequestBody,
        @Part("option3") option3: RequestBody,
        @Part("option4") option4: RequestBody,
        @Part("correct_option") correct_option: RequestBody,
    ): Call<AddQuestionResponse>
}