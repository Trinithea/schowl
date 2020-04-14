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
import java.security.InvalidParameterException

class CategoriesViewModel(
    application: Application
) : AndroidViewModel(application) {
    private var subjectId: Long = -1L

    private val database = SchowlDatabase.getInstance(application).schowlDatabaseDao

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val categories = database.getCategories(subjectId)

    fun loadData(subjectId: Long) {
        if (this.subjectId == -1L) {
            this.subjectId = subjectId
        } else if (subjectId != this.subjectId) {
            throw InvalidParameterException("Supplying a different id to the same ViewModel currently not supported.")
        }
    }

    fun addCategory(name: String) {
        uiScope.launch {
            database.insertCategory(Category(name = name, subjectId = subjectId))
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