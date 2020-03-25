package cz.cizlmazna.schowl.ui.test

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TestSetupViewModelFactory(
    private val application: Application,
    private val subjectId: Long,
    private val categoryId: Long
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TestSetupViewModel::class.java)) {
            return TestSetupViewModel(application, subjectId, categoryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}