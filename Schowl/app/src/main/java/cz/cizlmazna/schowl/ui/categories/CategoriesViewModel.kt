package cz.cizlmazna.schowl.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cz.cizlmazna.schowl.database.Category
import cz.cizlmazna.schowl.database.SchowlDatabase
import cz.cizlmazna.schowl.database.Subject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CategoriesViewModel(
    application: Application,
    subjectId: Long
) : AndroidViewModel(application) {

    private val database = SchowlDatabase.getInstance(application).schowlDatabaseDao

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var subject: Subject

    val categories = database.getCategories(subjectId)

    init {
        uiScope.launch {
            subject = database.getSubject(subjectId)
        }
    }

    fun addCategory(name: String) {
        uiScope.launch {
            database.insertCategory(Category(name = name, subjectId = subject.id))
        }
    }

    fun editCategory(category: Category, name: String) {
        category.name = name
        uiScope.launch {
            database.updateCategory(category)
        }
    }

    fun removeCategory(category: Category) {
        uiScope.launch {
            database.deleteCategory(category)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}