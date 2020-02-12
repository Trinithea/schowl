package cz.cizlmazna.schowl.ui.test

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import cz.cizlmazna.schowl.database.SchowlDatabaseDao
import cz.cizlmazna.schowl.database.Subject
import kotlinx.coroutines.*

const val DEFAULT_MIN_DIFFICULTY = 0
const val DEFAULT_MAX_DIFFICULTY = 10
const val DEFAULT_ALL_CATEGORIES_SELECTED = false

class TestSetupViewModel(
    private val database: SchowlDatabaseDao,
    subjectId: Long,
    categoryId: Long
) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var selectedSubject = MutableLiveData<Subject?>() // TODO make not nullable or check or sth
    fun getSelectedSubject(): LiveData<Subject?> {
        return selectedSubject
    }

    fun onSubjectSelected(subject: Subject?) {
        selectedSubject.value = subject
        // TODO update some livedata with categories
    }

    private val subjects = database.getAllSubjects()
    fun getSubjects(): LiveData<List<Subject>> {
        return subjects
    }

    private val allCategoriesSelected = MutableLiveData<Boolean>(DEFAULT_ALL_CATEGORIES_SELECTED)
    fun getAllCategoriesSelected(): LiveData<Boolean> {
        return allCategoriesSelected
    }

    val categorySelectionVisibility: LiveData<Int> = Transformations.map(allCategoriesSelected) {
        when(it) {
            false -> View.VISIBLE
            true -> View.GONE
        }
    }

    fun onAllCategoriesSelectedChange(selected: Boolean) {
        allCategoriesSelected.value = selected
    }

    private val minDifficulty = MutableLiveData<Int>(DEFAULT_MIN_DIFFICULTY)
    fun getMinDifficulty(): LiveData<Int> {
        return minDifficulty
    }

    fun onSetMinDifficulty(difficulty: Int) {
        minDifficulty.value = difficulty
    }

    private val maxDifficulty = MutableLiveData<Int>(DEFAULT_MAX_DIFFICULTY)
    fun getMaxDifficulty(): LiveData<Int> {
        return maxDifficulty
    }

    fun onSetMaxDifficulty(difficulty: Int) {
        maxDifficulty.value = difficulty
    }

    private val navigateToTest = MutableLiveData<Boolean>(false)
    fun getNavigateToTest(): LiveData<Boolean> {
        return navigateToTest
    }

    fun doneNavigatingToTest() {
        navigateToTest.value = false
    }

    fun onConfirm() {
        // TODO magic of selecting the appropriate data
    }

    init {
        uiScope.launch {
            selectedSubject.value = getSubject(subjectId)
        }
        if (selectedSubject.value != null) {
            if (categoryId == -1L) {
                allCategoriesSelected.value = true
            } else {
                // TODO handle category
            }
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}