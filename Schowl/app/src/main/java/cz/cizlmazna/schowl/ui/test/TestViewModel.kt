package cz.cizlmazna.schowl.ui.test

import androidx.lifecycle.ViewModel
import cz.cizlmazna.schowl.database.SchowlDatabaseDao
import kotlinx.coroutines.*

class TestViewModel(
    private val database: SchowlDatabaseDao,
    categoryIds: LongArray
) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        uiScope.launch {

        }
    }

    private suspend fun getCategory(categoryId: Long){
        withContext(Dispatchers.IO) {

        }
    }
}