package cz.cizlmazna.schowl.ui.subjects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cz.cizlmazna.schowl.database.SchowlDatabaseDao

class QuestionsViewModelFactory(
    private val databaseDao: SchowlDatabaseDao,
    private val categoryId: Long
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionsViewModel::class.java)) {
            return QuestionsViewModel(
                databaseDao,
                categoryId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}