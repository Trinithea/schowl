package cz.cizlmazna.schowl.ui.subjects

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cizlmazna.schowl.database.Category
import cz.cizlmazna.schowl.database.Question
import cz.cizlmazna.schowl.database.SchowlDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditQuestionViewModel(
    application: Application,
    val categoryId: Long,
    private val questionId: Long
) : AndroidViewModel(application) {

    private val database = SchowlDatabase.getInstance(application).schowlDatabaseDao

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var category: Category

    private var question = MutableLiveData<Question>()
    fun getQuestion(): LiveData<Question> {
        return question
    }
    private var questionSet = false

    init {
        uiScope.launch {
            category = database.getCategory(categoryId)
            question.value = if (questionId != -1L) {
                database.getQuestion(questionId)
            } else {
                null
            }
            questionSet = question.value != null
        }
    }

    fun saveQuestion(questionText: String, answerText: String, difficulty: Byte) {
        val id: Long
        val categoryId: Long
        if (questionSet) {
            id = question.value!!.id
            categoryId = question.value!!.categoryId
        } else {
            id = -1
            categoryId = -1
        }

        question.value = Question(id, questionText, answerText, difficulty, categoryId)
    }

    fun confirm(questionText: String, answerText: String, difficulty: Byte) {
        if (!questionSet) {
            uiScope.launch {
                database.insertQuestion(
                    Question(
                        questionText = questionText,
                        answerText = answerText,
                        difficulty = difficulty,
                        categoryId = category.id
                    )
                )
            }
        } else {
            question.value!!.questionText = questionText
            question.value!!.answerText = answerText
            question.value!!.difficulty = difficulty
            uiScope.launch {
                database.updateQuestion(question.value!!)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}