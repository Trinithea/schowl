package cz.cizlmazna.schowl.ui.subjects

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cz.cizlmazna.schowl.database.Category
import cz.cizlmazna.schowl.database.Question
import cz.cizlmazna.schowl.database.SchowlDatabase
import kotlinx.coroutines.*

class QuestionsViewModel(
    application: Application,
    categoryId: Long
) : AndroidViewModel(application) {

    private val database = SchowlDatabase.getInstance(application).schowlDatabaseDao

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var category: Category

    val questions = database.getQuestions(categoryId)

    init {
        uiScope.launch {
            getCategory(categoryId)
        }
    }

    private suspend fun getCategory(categoryId: Long) {
        withContext(Dispatchers.IO) {
            category = database.getCategory(categoryId)
        }
    }

    fun removeQuestion(question: Question) {
        uiScope.launch {
            delete(question)
        }
    }

    private suspend fun delete(question: Question) {
        withContext(Dispatchers.IO) {
            database.deleteQuestion(question)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}