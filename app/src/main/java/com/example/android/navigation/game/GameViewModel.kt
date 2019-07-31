package com.example.android.navigation.game
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.navigation.database.QuestionsEntity
import com.example.android.navigation.repositry.QuestionsReopsitry
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException

class GameViewModel(application: Application
                    , val questionsReopsitry: QuestionsReopsitry)
    : AndroidViewModel(application) {

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
   private lateinit var questions :MutableList<QuestionsEntity>
    //current question to be displayed
    private var _currentQuestion = MutableLiveData<QuestionsEntity>()
    val currentQuestion: LiveData<QuestionsEntity>
        get() = _currentQuestion

    //the answers to be displayed
    private var _answers = MutableLiveData<MutableList<String>>()
    val answers: LiveData<MutableList<String>>
        get() = _answers

    // question index that choose which question to be displayed
    private var questionIndex = -1
    // private val numQuestions = min((questions.size + 1) / 2, 3)
    /**
     * job for viewModel Coroutine so i can cancel coroutine when view model get destroyed
     */
    private val GameJob = Job()
    private val GameScope = CoroutineScope(Dispatchers.Main + GameJob)

    init {
        GameScope.launch {
            //getting question list from database
            questions = getQuestionList().toMutableList()
            //randomize the question
            questions.shuffle()
            //after getting list of question from database i set question to be displayed from list
            setQuestion()

        }
    }

    private suspend fun getQuestionList(): List<QuestionsEntity> {
        return withContext(Dispatchers.IO)
        {
            val questionsList = questionsReopsitry.getQuestion()
            questionsList
        }

    }

    // Shuffles the questions and sets the question index to the first question.
    fun setQuestion() {
        ++questionIndex
        _currentQuestion.value = questions[questionIndex]
        // randomize the answers into a copy of the array
        currentQuestion.value?.incorrectAnswer?.let {
            _answers.value = it.toMutableList()
        }
        currentQuestion.value?.correctAnswer?.let{
            _answers.value?.add(it)
        }
        // and shuffle them
        _answers.value?.shuffle()
        //(activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }
    //check if user select the correct answer or not
    fun checkCorrectAnswer(answerIndex:Int):Boolean
    {
        return _answers.value?.get(answerIndex) == _currentQuestion.value?.correctAnswer
    }
    //check if we finished all the list of question or not
    //if not return true else return false
    fun nextQuestion():Boolean
    {
        // Advance to the next question
        return questionIndex < questions.size-1
    }


    override fun onCleared() {
        super.onCleared()
        //when viewModel get cleared i cancel all the current coroutine associated with this view model
        GameJob.cancel()
    }
}

/**
 * factory class responsible for creating the the viewModel and supply it with required parameters
 */
class GameViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(application, QuestionsReopsitry(application)) as T
        }
        throw IllegalArgumentException("unknown class")
    }

}