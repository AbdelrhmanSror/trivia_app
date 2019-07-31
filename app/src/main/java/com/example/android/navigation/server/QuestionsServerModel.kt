package com.example.android.navigation.server

import com.example.android.navigation.database.QuestionsEntity
import com.squareup.moshi.Json

data class QuestionsApiModel(@Json(name = "results")val questions:List<Result>)
data class Result(@Json(name = "question")val question:String,
                             @Json(name = "correct_answer")val correctAnswer:String,
                             @Json(name = "incorrect_answers")val incorrectAnswer:List<String>)
//extension function to convert api model to data base model
fun QuestionsApiModel.getQuestionsEntity():List<QuestionsEntity>
{
    return questions.map {
        QuestionsEntity(it.question,it.correctAnswer,it.incorrectAnswer)
    }
}