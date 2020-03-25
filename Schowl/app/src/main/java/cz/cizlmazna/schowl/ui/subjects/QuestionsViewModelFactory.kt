package cz.cizlmazna.schowl.ui.subjects

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class QuestionsViewModelFactory(
    private val application: Application,
    private val categoryId: Long
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionsViewModel::class.java)) {
            return QuestionsViewModel(
                application,
                categoryId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}