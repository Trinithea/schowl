package cz.cizlmazna.schowl.ui.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cz.cizlmazna.schowl.database.SchowlDatabaseDao
import java.lang.IllegalArgumentException

class TestSetupViewModelFactory(
    private val database: SchowlDatabaseDao,
    private val subjectId: Long,
    private val categoryId: Long
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TestSetupViewModel::class.java)) {
            return TestSetupViewModel(database, subjectId, categoryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}