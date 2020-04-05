package cz.cizlmazna.schowl.database

import androidx.room.*

@Entity(
    tableName = "questions_table",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["category_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["category_id"])]
)
data class Question(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,

    @ColumnInfo(name = "question_text")
    var questionText: String,

    @ColumnInfo(name = "answer_text")
    var answerText: String,

    @ColumnInfo(name = "difficulty")
    var difficulty: Byte,

    @ColumnInfo(name = "category_id")
    var categoryId: Long
) : ICurriculum