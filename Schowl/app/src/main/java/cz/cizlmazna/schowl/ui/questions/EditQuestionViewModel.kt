package cz.cizlmazna.schowl.ui.questions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cizlmazna.schowl.database.Question
import cz.cizlmazna.schowl.database.SchowlDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditQuestionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var categoryId: Long = -1L

    private var questionId: Long = -1L

    private val database = SchowlDatabase.getInstance(application).schowlDatabaseDao

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var question = MutableLiveData<Question>()
    fun getQuestion(): LiveData<Question> {
        return question
    }

    fun loadData(categoryId: Long, questionId: Long) {
        if (this.categoryId == -1L && this.questionId == -1L) {
            this.categoryId = categoryId
            this.questionId = questionId
            uiScope.launch {
                question.value = if (questionId != -1L) {
                    database.getQuestion(questionId)
                } else {
                    null
                }
            }
        } else if (categoryId != this.categoryId || questionId != this.questionId) {
            throw IllegalArgumentException("Supplying a different id to the same ViewModel currently not supported.")
        }
    }

    fun saveQuestion(questionText: String, answerText: String, difficulty: Byte) {
        val id: Long = question.value?.id ?: -1
        val categoryId: Long = question.value?.categoryId ?: -1

        question.value = Question(id, questionText, answerText, difficulty, categoryId)
    }

    fun confirm(questionText: String, answerText: String, difficulty: Byte) {
        val savedQuestion = question.value
        if (savedQuestion == null) {
            uiScope.launch {
                database.insertQuestion(
                    Question(
                        questionText = questionText,
                        answerText = answerText,
                        difficulty = difficulty,
                        categoryId = categoryId
                    )
                )
            }
        } else {
            savedQuestion.questionText = questionText
            savedQuestion.answerText = answerText
            savedQuestion.difficulty = difficulty
            uiScope.launch {
                database.updateQuestion(savedQuestion)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}