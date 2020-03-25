package cz.cizlmazna.schowl.ui.test

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TestViewModelFactory(
    private val application: Application,
    private val categoryIds: LongArray,
    private val minDifficulty: Int,
    private val maxDifficulty: Int
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TestViewModel::class.java)) {
            return TestViewModel(application, categoryIds, minDifficulty, maxDifficulty) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}