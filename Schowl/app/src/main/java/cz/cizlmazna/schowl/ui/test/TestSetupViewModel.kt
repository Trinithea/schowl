package cz.cizlmazna.schowl.ui.test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.cizlmazna.schowl.database.SchowlDatabase
import cz.cizlmazna.schowl.database.SchowlDatabaseDao
import cz.cizlmazna.schowl.database.Subject
import kotlinx.coroutines.*

class TestSetupViewModel(
    private val database: SchowlDatabaseDao,
    subjectId: Long,
    categoryId: Long
) : ViewModel() {

    private val subject = MutableLiveData<Subject?>()

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        uiScope.launch {
            subject.value = getSubject(subjectId)
        }
    }

    private suspend fun getSubject(subjectId: Long): Subject? {
        return withContext(Dispatchers.IO) {
            if (subjectId != -1L) {
                return@withContext database.getSubject(subjectId)
            }
            null
        }
    }
}