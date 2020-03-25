package cz.cizlmazna.schowl.ui.subjects

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import cz.cizlmazna.schowl.database.Question
import cz.cizlmazna.schowl.database.SchowlDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class QuestionsViewModel(
    application: Application
) : AndroidViewModel(application) {
    private var categoryId: Long = -1L

    private val database = SchowlDatabase.getInstance(application).schowlDatabaseDao

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val questions: LiveData<List<Question>> by lazy {
        return@lazy database.getQuestions(categoryId)
    }

    fun loadData(categoryId: Long) {
        if (this.categoryId == -1L) {
            this.categoryId = categoryId
        }
    }

    fun removeQuestion(question: Question) {
        uiScope.launch {
            database.deleteQuestion(question)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}