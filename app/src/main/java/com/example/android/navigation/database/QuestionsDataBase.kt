package com.example.android.navigation.database

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface QuestionsDao {
    @Query("SELECT * FROM questionsentity ")
    fun getQuestions():List<QuestionsEntity>

    @Insert
    suspend fun insertQuestions(question: List<QuestionsEntity>)

    @Query("DELETE FROM questionsentity")
   suspend fun clear()
}
@Database(entities = [QuestionsEntity::class],version = 3,exportSchema = false)
@TypeConverters(ListTypeConverters::class)
abstract class QuestionsDataBase : RoomDatabase() {
    abstract val questionsDao: QuestionsDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: QuestionsDataBase

        fun getDataBase(context: Context): QuestionsDataBase {
            synchronized(QuestionsDataBase::class.java)
            {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context, QuestionsDataBase::class.java, "quiz").fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE

        }

    }
}