package cz.cizlmazna.schowl.ui.subjects.categories.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import cz.cizlmazna.schowl.database.Category
import cz.cizlmazna.schowl.database.Question
import cz.cizlmazna.schowl.database.SchowlDatabaseDao
import kotlinx.coroutines.*

class EditQuestionViewModel(
    private val database: SchowlDatabaseDao,
    val categoryId: Long,
    private val questionId: Long
) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var category: Category

    private var question = MutableLiveData<Question?>()

    val questionText: LiveData<String> = Transformations.map(question) {
        it?.questionText
    }

    val answerText: LiveData<String> = Transformations.map(question) {
        it?.answerText
    }

    val difficulty: LiveData<Int> = Transformations.map(question) {
        it?.difficulty?.toInt()
    }

    val questions = database.getQuestions(categoryId)

    init {
        uiScope.launch {
            question.value = getData(categoryId)
        }
    }

    private suspend fun getData(categoryId: Long): Question?{
        return withContext(Dispatchers.IO) {
            category = database.getCategory(categoryId)
            if (questionId != -1L) {
                 return@withContext database.getQuestion(questionId)
            }
            null
        }
    }

    fun confirm(questionText: String, answerText: String, difficulty: Byte) {
        if (question.value == null) {
            uiScope.launch {
                insert(Question(questionText = questionText, answerText = answerText, difficulty = difficulty, categoryId = category.id))
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