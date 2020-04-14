package cz.cizlmazna.schowl.ui.test

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import cz.cizlmazna.schowl.database.Question
import cz.cizlmazna.schowl.database.SchowlDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TestViewModel(
    application: Application
) : AndroidViewModel(application) {
    private var categoryIds: LongArray? = null
    private var minDifficulty: Int = -1
    private var maxDifficulty: Int = -1

    private val database = SchowlDatabase.getInstance(application).schowlDatabaseDao

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var testedQuestions = mutableListOf<Question>()

    private val currentQuestionIndex = MutableLiveData(-1)
    val numberOfQuestion = Transformations.map(currentQuestionIndex) {
        (currentQuestionIndex.value?.plus(1) ?: -1).toString() + "/" + testedQuestions.size
    }

    private val currentQuestion = MutableLiveData<Question>()
    fun getCurrentQuestionId(): Long {
        return currentQuestion.value?.id ?: -1
    }

    fun getCurrentQuestionCategoryId(): Long {
        return currentQuestion.value?.categoryId ?: -1
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
        currentQuestionIndex.value?.let {
            if (it >= testedQuestions.size - 1) {
                testOver.value = true
            } else {
                currentQuestionIndex.value = it + 1
                currentQuestion.value = testedQuestions[it + 1]
                showSolution.value = false
            }
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

    fun loadData(categoryIds: LongArray, minDifficulty: Int, maxDifficulty: Int) {
        @Suppress("ReplaceArrayEqualityOpWithArraysEquals")
        if (this.categoryIds == null && this.minDifficulty == -1 && this.maxDifficulty == -1) {
            this.categoryIds = categoryIds
            this.minDifficulty = minDifficulty
            this.maxDifficulty = maxDifficulty

            uiScope.launch {
                for (categoryId in categoryIds) {
                    testedQuestions.addAll(database.getQuestionsLimited(categoryId, minDifficulty.toByte(), maxDifficulty.toByte()))
                }
                testedQuestions.shuffle()
                nextQuestion()
            }
        } else if (categoryIds != this.categoryIds || minDifficulty != this.minDifficulty || maxDifficulty != this.maxDifficulty) {
            throw IllegalArgumentException("Supplying a different id or a set of ids to the same ViewModel currently not supported.")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}