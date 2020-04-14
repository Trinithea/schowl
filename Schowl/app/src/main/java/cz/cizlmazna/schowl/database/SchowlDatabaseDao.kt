package cz.cizlmazna.schowl.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SchowlDatabaseDao {

    @Insert
    suspend fun insertSubject(subject: Subject)

    @Insert
    suspend fun insertCategory(category: Category)

    @Insert
    suspend fun insertQuestion(question: Question)

    @Update
    suspend fun updateSubject(subject: Subject)

    @Update
    suspend fun updateCategory(category: Category)

    @Update
    suspend fun updateQuestion(question: Question)

    @Query("SELECT * FROM subjects_table WHERE id = :id")
    suspend fun getSubject(id: Long): Subject

    @Query("SELECT * FROM categories_table WHERE id = :id")
    suspend fun getCategory(id: Long): Category

    @Query("SELECT * FROM questions_table WHERE id = :id")
    suspend fun getQuestion(id: Long): Question

    @Delete
    suspend fun deleteSubject(subject: Subject)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Delete
    suspend fun deleteQuestion(question: Question)

    @Query("SELECT * FROM subjects_table")
    fun getAllSubjects(): LiveData<List<Subject>>

    @Query("SELECT * FROM categories_table WHERE subject_id = :subjectId")
    fun getCategories(subjectId: Long): LiveData<List<Category>>

    @Query("SELECT * FROM questions_table WHERE category_id = :categoryId")
    fun getQuestions(categoryId: Long): LiveData<List<Question>>

    @Query("SELECT * FROM categories_table WHERE subject_id = :subjectId")
    suspend fun getCategoriesRaw(subjectId: Long): List<Category>

    @Query("SELECT * FROM questions_table WHERE category_id = :categoryId AND difficulty >= :minDifficulty AND difficulty <= :maxDifficulty")
    suspend fun getQuestionsLimited(
        categoryId: Long,
        minDifficulty: Byte,
        maxDifficulty: Byte
    ): List<Question>

    @Query("SELECT * FROM subjects_table LIMIT 1")
    suspend fun getFirstSubject(): Subject?

    @Query("SELECT * FROM questions_table WHERE category_id = :categoryId")
    suspend fun getQuestionsRaw(categoryId: Long): List<Question>
}