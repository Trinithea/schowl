package cz.cizlmazna.schowl.database

import androidx.room.*

@Dao
interface SchowlDatabaseDao {

    @Insert
    fun insertSubject(subject: Subject)

    @Insert
    fun insertCategory(category: Category)

    @Insert
    fun insertQuestion(question: Question)

    @Update
    fun updateSubject(subject: Subject)

    @Update
    fun updateCategory(category: Category)

    @Update
    fun updateQuestion(question: Question)

    @Query("SELECT * FROM subjects_table WHERE id = :id")
    fun getSubject(id: Long): Subject

    @Query("SELECT * FROM categories_table WHERE id = :id")
    fun getCategory(id: Long): Category

    @Query("SELECT * FROM questions_table WHERE id = :id")
    fun getQuestion(id: Long): Question

    @Delete
    fun deleteSubject(subject: Subject)

    @Delete
    fun deleteCategory(category: Category)

    @Delete
    fun deleteQuestion(question: Question)

    @Query("SELECT * FROM subjects_table")
    fun getAllSubjects(): List<Subject> // TODO make this LiveData?

    @Query("SELECT * FROM categories_table WHERE subject_id = :subjectId")
    fun getCategories(subjectId: Long): List<Category>

    @Query("SELECT * FROM questions_table WHERE category_id = :categoryId")
    fun getQuestions(categoryId: Long): List<Question>
}