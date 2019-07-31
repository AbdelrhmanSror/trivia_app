package com.example.android.navigation.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import java.util.Collections.emptyList


@Entity
data class QuestionsEntity(@PrimaryKey val question: String,
                           val correctAnswer: String,
                           val incorrectAnswer: List<String>)
class ListTypeConverters {

    @TypeConverter
    fun stringToList(incorrectAnswer: String?): List<String> {
        if (incorrectAnswer == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<String>>() {}.getType()

        return Gson().fromJson(incorrectAnswer, listType)
    }

    @TypeConverter
    fun ListToString(incorrectAnswer: List<String>): String {
        return Gson().toJson(incorrectAnswer)
    }
}
