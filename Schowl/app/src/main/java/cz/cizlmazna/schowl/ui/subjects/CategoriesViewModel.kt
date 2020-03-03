package cz.cizlmazna.schowl.ui.subjects

import androidx.lifecycle.ViewModel
import cz.cizlmazna.schowl.database.Category
import cz.cizlmazna.schowl.database.SchowlDatabaseDao
import cz.cizlmazna.schowl.database.Subject
import kotlinx.coroutines.*

class CategoriesViewModel(
    private val database: SchowlDatabaseDao,
    subjectId: Long
) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var subject: Subject

    val categories = database.getCategories(subjectId)

    init {
        uiScope.launch {
            getSubject(subjectId)
        }
    }

    private suspend fun getSubject(subjectId: Long) {
        withContext(Dispatchers.IO) {
            subject = database.getSubject(subjectId)
        }
    }

    fun addCategory(name: String) {
        uiScope.launch {
            insert(Category(name = name, subjectId = subject.id))
        }
    }

    private suspend fun insert(category: Category) {
        withContext(Dispatchers.IO) {
            database.insertCategory(category)
        }
    }

    fun editCategory(category: Category, name: String) {
        category.name = name
        uiScope.launch {
            update(category)
        }
    }

    private suspend fun update(category: Category) {
        withContext(Dispatchers.IO) {
            database.updateCategory(category)
        }
    }

    fun removeCategory(category: Category) {
        uiScope.launch {
            delete(category)
        }
    }

    private suspend fun delete(category: Category) {
        withContext(Dispatchers.IO) {
            database.deleteCategory(category)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}