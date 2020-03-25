package cz.cizlmazna.schowl.ui.subjects

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cz.cizlmazna.schowl.database.SchowlDatabaseDao

class CategoriesViewModelFactory(
    private val application: Application,
    private val subjectId: Long
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            return CategoriesViewModel(
                application,
                subjectId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}