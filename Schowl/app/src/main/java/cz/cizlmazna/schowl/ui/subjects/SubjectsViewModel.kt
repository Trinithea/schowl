package cz.cizlmazna.schowl.ui.subjects

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cz.cizlmazna.schowl.database.SchowlDatabase
import cz.cizlmazna.schowl.database.Subject
import kotlinx.coroutines.*

class SubjectsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = SchowlDatabase.getInstance(application).schowlDatabaseDao

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val subjects = database.getAllSubjects()

    fun addSubject(name: String) {
        uiScope.launch {
            insert(Subject(name = name))
        }
    }

    private suspend fun insert(subject: Subject) {
        withContext(Dispatchers.IO) {
            database.insertSubject(subject)
        }
    }

    fun editSubject(subject: Subject, name: String) {
        subject.name = name
        uiScope.launch {
            update(subject)
        }
    }

    private suspend fun update(subject: Subject) {
        withContext(Dispatchers.IO) {
            database.updateSubject(subject)
        }
    }

    fun removeSubject(subject: Subject) {
        uiScope.launch {
            delete(subject)
        }
    }

    private suspend fun delete(subject: Subject) {
        withContext(Dispatchers.IO) {
            database.deleteSubject(subject)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}