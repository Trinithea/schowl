package cz.cizlmazna.schowl.ui.test

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import cz.cizlmazna.schowl.database.Question
import cz.cizlmazna.schowl.database.SchowlDatabase
import kotlinx.coroutines.*

class TestViewModel(
    application: Application,
    categoryIds: LongArray,
    minDifficulty: Int,
    maxDifficulty: Int
) : AndroidViewModel(application) {

    private val database = SchowlDatabase.getInstance(application).schowlDatabaseDao

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var testedQuestions = mutableListOf<Question>()

    private val currentQuestionIndex = MutableLiveData(-1)
    val numberOfQuestion = Transformations.map(currentQuestionIndex) {
        currentQuestionIndex.value!!.plus(1).toString() + "/" + testedQuestions.size
    }

    private val currentQuestion = MutableLiveData<Question>()
    fun getCurrentQuestionId(): Long {
        return currentQuestion.value!!.id
    }

    fun getCurrentQuestionCategoryId(): Long {
        return currentQuestion.value!!.categoryId
    }

    val currentQuestionText = Transformations.map(currentQuestion) {
        it.questionText
    }

    val currentSolutionText = Transformations.map(currentQuestion) {
        it.answerText
    }

    private val showSolution = MutableLiveData(false)
    fun getShowSolution(): LiveData<Boolean> {
        return showSolution
    }

    val showSolutionButtonVisible = Transformations.map(showSolution) {
        when (it) {
            true -> View.GONE
            false -> View.VISIBLE
        }
    }

    val solutionPartsVisible = Transformations.map(showSolution) {
        when (it) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }

    fun onShowSolution() {
        showSolution.value = true
    }

    private val testOver = MutableLiveData(false)
    fun getTestOver(): LiveData<Boolean> {
        return testOver
    }

    private val questionChange = MutableLiveData(false)
    fun getQuestionChange(): LiveData<Boolean> {
        return questionChange
    }

    fun doneChangingQuestion() {
        questionChange.value = false
    }

    fun nextQuestion() {
        questionChange.value = true
        if (currentQuestionIndex.value!! >= testedQuestions.size - 1) {
            testOver.value = true
        } else {
            currentQuestionIndex.value = currentQuestionIndex.value!! + 1
            currentQuestion.value = testedQuestions[currentQuestionIndex.value!!]
            showSolution.value = false
        }
    }

    private val navigateToEditQuestion = MutableLiveData(false)
    fun getNavigateToEditQuestion(): LiveData<Boolean> {
        return navigateToEditQuestion
    }

    fun editQuestion() {
        navigateToEditQuestion.value = true
    }

    fun doneNavigatingToEditQuestion() {
        if (navigateToEditQuestion.value == true) {
            navigateToEditQuestion.value = false
            nextQuestion()
        }
    }

    init {
        uiScope.launch {
            for (categoryId in categoryIds) {
                testedQuestions.addAll(database.getQuestionsLimited(categoryId, minDifficulty.toByte(), maxDifficulty.toByte()))
            }
            testedQuestions.shuffle()
            nextQuestion()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}