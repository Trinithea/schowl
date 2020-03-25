package cz.cizlmazna.schowl.ui.subjects

import android.app.Application
import androidx.lifecycle.*
import cz.cizlmazna.schowl.database.Category
import cz.cizlmazna.schowl.database.Question
import cz.cizlmazna.schowl.database.SchowlDatabase
import cz.cizlmazna.schowl.database.SchowlDatabaseDao
import kotlinx.coroutines.*

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
            question.value = getData(categoryId)
            questionSet = question.value != null
        }
    }

    private suspend fun getData(categoryId: Long): Question? {
        return withContext(Dispatchers.IO) {
            category = database.getCategory(categoryId)
            if (questionId != -1L) {
                return@withContext database.getQuestion(questionId)
            }
            null
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
                insert(
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
                update(question.value!!)
            }
        }
    }

    private suspend fun insert(question: Question) {
        withContext(Dispatchers.IO) {
            database.insertQuestion(question)
        }
    }

    private suspend fun update(question: Question) {
        withContext(Dispatchers.IO) {
            database.updateQuestion(question)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}