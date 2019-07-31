package com.example.android.navigation.repositry

import android.app.Application
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.preference.PreferenceManager
import com.example.android.navigation.R
import com.example.android.navigation.database.QuestionsDataBase
import com.example.android.navigation.database.QuestionsEntity
import com.example.android.navigation.server.QuestionsApiModel
import com.example.android.navigation.server.QuestionsService
import com.example.android.navigation.server.getQuestionsEntity

data class prefrenceData(val category:Int,val difficulty:String)


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class QuestionsReopsitry(private val application: Application) {
    private val dataBase: QuestionsDataBase by lazy {
        QuestionsDataBase.getDataBase(application)
    }

    /**
     * fetching questions based on selections of user and store it in database
     */
    @WorkerThread
    suspend fun fetchQuestion() {
        try {
            val(category,difficulty)=prefValues()
            val questions:QuestionsApiModel = QuestionsService.Question.getQuestions(catId = category, difficulty = difficulty).await()
            clear()
            insert(questions.getQuestionsEntity())
        }
        catch (throwable:Throwable)
        {
            throwable.message
            throwable.printStackTrace()
        }
    }

    /**
     * insert list of question into database
     */
    private suspend fun insert(questions:List<QuestionsEntity>)
    {
        dataBase.questionsDao.insertQuestions(questions)

    }

    /**
     * clear the database
     */
    private suspend fun clear()
    {
        dataBase.questionsDao.clear()
    }

    /**
     * get list pf questions from dataBase
     */
    @WorkerThread
    fun getQuestion():List<QuestionsEntity>
    {
        return dataBase.questionsDao.getQuestions()
    }

    /**
     * returning the value of each preference in settings
     */
    private fun prefValues():prefrenceData
    {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
        val category = sharedPreferences.getString(application.getString(R.string.category), "9").toInt()
        val difficulty = sharedPreferences.getString(application.getString(R.string.difficulty),application.getString(R.string.easy)).toLowerCase()
        return prefrenceData(category,difficulty)
    }
}