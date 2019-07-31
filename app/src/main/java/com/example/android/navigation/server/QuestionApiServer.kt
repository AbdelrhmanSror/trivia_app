package com.example.android.navigation.server

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://opentdb.com/"


/**
 * lib to convert from json to kotlin object

 */
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()


/**
 * creating retrofit with with moshiConverterFactory and Coroutine Adapter

 */

private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory()).build()

/**
 * interface that retrofit will provide an implementation for it
 */

interface QuestionApiServer {

    @GET("api.php")
    fun getQuestions(@Query("amount")numOfQuestion:Int=10
                      , @Query("category") catId:Int
                      , @Query("difficulty")difficulty:String
                     , @Query("type") type:String="multiple")
            : Deferred<QuestionsApiModel>
}


object QuestionsService {
    val Question: QuestionApiServer by lazy {
        retrofit.create(QuestionApiServer::class.java)
    }
}
