package cz.cizlmazna.schowl.ui.subjects

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cz.cizlmazna.schowl.database.SchowlDatabaseDao

class EditQuestionViewModelFactory(
    private val application: Application,
    private val categoryId: Long,
    private val questionId: Long
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditQuestionViewModel::class.java)) {
            return EditQuestionViewModel(
                application,
                categoryId,
                questionId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}