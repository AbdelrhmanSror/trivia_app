package com.example.android.navigation.title

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.navigation.repositry.QuestionsReopsitry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class TitleViewModel(application: Application, val questionsReopsitry: QuestionsReopsitry) : AndroidViewModel(application) {
    /**
     * job for viewModel Coroutine so i can cancel coroutine when view model get destroyed
     */
    val TitleJob = Job()
    val TitelScope = CoroutineScope(Dispatchers.IO + TitleJob)


    /**
     * start fetching question from server and then store it in data base
     * using coroutine
     */
    fun startFetchingQuestion() {
        TitelScope.launch {
            questionsReopsitry.fetchQuestion()
        }
    }

    override fun onCleared() {
        super.onCleared()
        //when viewModel get cleared i cancel all the current coroutine associated with this view model
        TitleJob.cancel()
    }
}

/**
 * factory class responsible for creating the the viewModel and supply it with required parameters
 */
class TitleViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TitleViewModel::class.java)) {
            return TitleViewModel(application, QuestionsReopsitry(application)) as T
        }
        throw IllegalArgumentException("unknown class")
    }

}