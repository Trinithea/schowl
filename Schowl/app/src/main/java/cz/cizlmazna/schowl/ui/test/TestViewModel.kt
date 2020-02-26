package cz.cizlmazna.schowl.ui.test

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import cz.cizlmazna.schowl.database.Question
import cz.cizlmazna.schowl.database.SchowlDatabaseDao
import kotlinx.coroutines.*

class TestViewModel(
    private val database: SchowlDatabaseDao,
    categoryIds: LongArray,
    minDifficulty: Int,
    maxDifficulty: Int
) : ViewModel() {

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

    private val showSolution = MutableLiveData<Boolean>(false)

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

    private val testOver = MutableLiveData<Boolean>(false)
    fun getTestOver(): LiveData<Boolean> {
        return testOver
    }

    private val questionChange = MutableLiveData<Boolean>(false)
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

    private val navigateToEditQuestion = MutableLiveData<Boolean>(false)
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
                testedQuestions.addAll(getQuestions(categoryId, minDifficulty, maxDifficulty))
            }
            testedQuestions.shuffle()
            nextQuestion()
        }
    }

    private suspend fun getQuestions(
        categoryId: Long,
        minDifficulty: Int,
        maxDifficulty: Int
    ): List<Question> {
        return withContext(Dispatchers.IO) {
            database.getQuestionsLimited(categoryId, minDifficulty.toByte(), maxDifficulty.toByte())
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}