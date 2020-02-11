package cz.cizlmazna.schowl.ui.subjects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cz.cizlmazna.schowl.database.SchowlDatabaseDao
import java.lang.IllegalArgumentException

class SubjectsViewModelFactory(
    private val databaseDao: SchowlDatabaseDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubjectsViewModel::class.java)) {
            return SubjectsViewModel(databaseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}