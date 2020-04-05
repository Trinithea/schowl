package cz.cizlmazna.schowl.ui.questions

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

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