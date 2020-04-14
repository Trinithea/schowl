package cz.cizlmazna.schowl.ui.test

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import cz.cizlmazna.schowl.database.Category
import cz.cizlmazna.schowl.database.SchowlDatabase
import cz.cizlmazna.schowl.database.Subject
import kotlinx.coroutines.*
import java.security.InvalidParameterException
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.hashMapOf
import kotlin.collections.mutableListOf
import kotlin.collections.set
import kotlin.collections.toLongArray

class TestSetupViewModel(
    application: Application
) : AndroidViewModel(application) {
    companion object {
        const val DEFAULT_MIN_DIFFICULTY = 0
        const val DEFAULT_MAX_DIFFICULTY = 10
        const val DEFAULT_ALL_CATEGORIES_SELECTED = false
    }

    // -2 instead of -1 as not to conflict with possible "empty" incoming values
    private var subjectId: Long = -2L

    private var categoryId: Long = -2L

    private val database = SchowlDatabase.getInstance(application).schowlDatabaseDao

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
                selectedSubjectCategories.value = database.getCategoriesRaw(selectedSubject.value!!.id)
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

    private val allCategoriesSelected = MutableLiveData(DEFAULT_ALL_CATEGORIES_SELECTED)
    fun getAllCategoriesSelected(): LiveData<Boolean> {
        return allCategoriesSelected
    }

    val categorySelectionVisibility: LiveData<Int> = Transformations.map(allCategoriesSelected) {
        when (it) {
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

    private val categoriesChecked = MutableLiveData<MutableMap<Long, Boolean>>()
    fun getCategoriesChecked(): LiveData<MutableMap<Long, Boolean>> {
        return categoriesChecked
    }

    fun onCategoryCheckedChange(category: Category, checked: Boolean) {
        categoriesChecked.value!![category.id] = checked
    }

    private val minDifficulty = MutableLiveData(DEFAULT_MIN_DIFFICULTY)

    fun getMinDifficulty(): LiveData<Int> {
        return minDifficulty
    }

    fun onSetMinDifficulty(difficulty: Int) {
        minDifficulty.value = difficulty
    }

    private val maxDifficulty = MutableLiveData(DEFAULT_MAX_DIFFICULTY)

    fun getMaxDifficulty(): LiveData<Int> {
        return maxDifficulty
    }

    fun onSetMaxDifficulty(difficulty: Int) {
        maxDifficulty.value = difficulty
    }

    private val navigateToTest = MutableLiveData(false)
    fun getNavigateToTest(): LiveData<Boolean> {
        return navigateToTest
    }

    fun doneNavigatingToTest() {
        navigateToTest.value = false
    }

    enum class ErrorMessage {
        NO_SUBJECT_SELECTED, NO_CATEGORIES_SELECTED, NO_QUESTIONS_IN_CATEGORIES, NO_RIGHT_DIFFICULTY_QUESTIONS_IN_CATEGORIES
    }

    private val errorMessage = MutableLiveData<ErrorMessage>()
    fun getErrorMessage(): LiveData<ErrorMessage> {
        return errorMessage
    }

    fun doneShowingErrorMessage() {
        errorMessage.value = null
    }

    private val categoryIds: MutableList<Long> = mutableListOf()

    // TODO merge (or something) with other functions that also load data
    fun loadInitialData(subjectId: Long, categoryId: Long) {
        if (this.subjectId == -2L && this.categoryId == -2L) {
            this.subjectId = subjectId
            this.categoryId = categoryId

            uiScope.launch {
                selectedSubject.value = if (subjectId != -1L) {
                    database.getSubject(subjectId)
                } else {
                    null
                }
                if (selectedSubject.value == null) {
                    selectedSubject.value = database.getFirstSubject()
                }
                if (selectedSubject.value != null) {
                    selectedSubjectCategories.value = database.getCategoriesRaw(selectedSubject.value!!.id)
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
                }
            }
        } else if (subjectId != this.subjectId || categoryId != this.categoryId) {
            throw InvalidParameterException("Supplying a different id to the same ViewModel currently not supported.")
        }
    }

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
        var rightDifficultyQuestionsCount = 0
        uiScope.launch {
            for (categoryId in categoryIds) {
                questionsCount += database.getQuestionsRaw(categoryId).size
                rightDifficultyQuestionsCount += database.getQuestionsLimited(categoryId, minDifficulty.value!!.toByte(), maxDifficulty.value!!.toByte()).size
            }
            if (questionsCount == 0) {
                errorMessage.value = ErrorMessage.NO_QUESTIONS_IN_CATEGORIES
                return@launch
            }
            if (rightDifficultyQuestionsCount == 0) {
                errorMessage.value = ErrorMessage.NO_RIGHT_DIFFICULTY_QUESTIONS_IN_CATEGORIES
            }

            navigateToTest.value = true
        }
    }

    fun getCategoryIds(): LongArray {
        return categoryIds.toLongArray()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}