package cz.cizlmazna.schowl.ui.test

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.database.Category
import cz.cizlmazna.schowl.database.SchowlDatabaseDao
import cz.cizlmazna.schowl.database.Subject
import kotlinx.coroutines.*

class TestSetupViewModel(
    private val database: SchowlDatabaseDao,
    subjectId: Long,
    categoryId: Long
) : ViewModel() {
    companion object {
        const val DEFAULT_MIN_DIFFICULTY = 0
        const val DEFAULT_MAX_DIFFICULTY = 10
        const val DEFAULT_ALL_CATEGORIES_SELECTED = false
    }

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * value == null => no subject selected
     */
    private var selectedSubject = MutableLiveData<Subject>()
    fun getSelectedSubject(): LiveData<Subject> {
        return selectedSubject
    }

    // Careful, sometimes this is run with null even though a subject appears to be selected
    fun onSubjectSelected(subject: Subject?) {
        if (subject != null && subject != selectedSubject.value) {
            selectedSubject.value = subject
            uiScope.launch {
                selectedSubjectCategories.value = setSelectedSubjectCategories()
                val categoriesCheckedTemp = hashMapOf<Long, Boolean>()
                for (category in selectedSubjectCategories.value!!) {
                    categoriesCheckedTemp[category.id] = false
                }
                categoriesChecked.value = categoriesCheckedTemp
            }
        }
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

    private val selectedSubjectCategories = MutableLiveData<List<Category>>()

    fun getSelectedSubjectCategories(): LiveData<List<Category>> {
        return selectedSubjectCategories
    }

    private suspend fun setSelectedSubjectCategories(): List<Category> {
        return withContext(Dispatchers.IO) {
            database.getCategoriesRaw(selectedSubject.value!!.id)
        }
    }

    private val categoriesChecked = MutableLiveData<MutableMap<Long, Boolean>>()
    fun getCategoriesChecked(): LiveData<MutableMap<Long, Boolean>> {
        return categoriesChecked
    }

    fun onCategoryCheckedChange(category: Category, checked: Boolean) {
        categoriesChecked.value!![category.id] = checked
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

    enum class ErrorMessage {
        NO_SUBJECT_SELECTED, NO_CATEGORIES_SELECTED, NO_QUESTIONS_IN_CATEGORIES
    }

    private val errorMessage = MutableLiveData<ErrorMessage>()
    fun getErrorMessage(): LiveData<ErrorMessage> {
        return errorMessage
    }

    fun doneShowingErrorMessage() {
        errorMessage.value = null
    }

    private val categoryIds: MutableList<Long> = mutableListOf()

    fun onConfirm() {
        if (selectedSubject.value == null) {
            errorMessage.value = ErrorMessage.NO_SUBJECT_SELECTED
            return
        }
        categoryIds.clear()
        if (allCategoriesSelected.value!!) {
            for (category in selectedSubjectCategories.value!!) {
                categoryIds.add(category.id)
            }
        } else {
            for (categoryIdCheck in categoriesChecked.value!!.entries) {
                if (categoryIdCheck.value) {
                    categoryIds.add(categoryIdCheck.key)
                }
            }
        }
        if (categoryIds.size == 0) {
            errorMessage.value = ErrorMessage.NO_CATEGORIES_SELECTED
            return
        }

        var questionsCount = 0
        uiScope.launch {
            for (categoryId in categoryIds) {
                questionsCount += getQuestionsCount(categoryId)
            }
            if (questionsCount == 0) {
                errorMessage.value = ErrorMessage.NO_QUESTIONS_IN_CATEGORIES
                return@launch
            }

            navigateToTest.value = true
        }
    }

    private suspend fun getQuestionsCount(categoryId: Long): Int {
        return withContext(Dispatchers.IO) {
            database.getQuestionsRaw(categoryId).size
        }
    }

    fun getCategoryIds(): LongArray {
        return categoryIds.toLongArray()
    }

    init {
        uiScope.launch {
            selectedSubject.value = getSubject(subjectId)
            if (selectedSubject.value != null) {
                selectedSubjectCategories.value = setSelectedSubjectCategories()
                val categoriesCheckedTemp = hashMapOf<Long, Boolean>()
                for (category in selectedSubjectCategories.value!!) {
                    categoriesCheckedTemp[category.id] = false
                }
                if (categoryId == -1L) {
                    allCategoriesSelected.value = true
                } else {
                    categoriesCheckedTemp[categoryId] = true
                }
                categoriesChecked.value = categoriesCheckedTemp
            } else {
                // TODO This has to be cleaned up
                selectedSubject.value = getFirstSubject()
                selectedSubjectCategories.value = setSelectedSubjectCategories()
                val categoriesCheckedTemp = hashMapOf<Long, Boolean>()
                for (category in selectedSubjectCategories.value!!) {
                    categoriesCheckedTemp[category.id] = false
                }
                categoriesChecked.value = categoriesCheckedTemp
            }
        }
    }

    private suspend fun getFirstSubject(): Subject {
        return withContext(Dispatchers.IO) {
            database.getFirstSubject()
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