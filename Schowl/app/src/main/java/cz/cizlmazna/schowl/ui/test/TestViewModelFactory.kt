package cz.cizlmazna.schowl.ui.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cz.cizlmazna.schowl.database.SchowlDatabaseDao
import java.lang.IllegalArgumentException

class TestViewModelFactory(
    private val database: SchowlDatabaseDao,
    private val categoryIds: LongArray,
    private val minDifficulty: Int,
    private val maxDifficulty: Int
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TestViewModel::class.java)) {
            return TestViewModel(database, categoryIds, minDifficulty, maxDifficulty) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}