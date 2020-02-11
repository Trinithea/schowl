package cz.cizlmazna.schowl.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "questions_table", foreignKeys = [ForeignKey(entity = Category::class, parentColumns = ["id"], childColumns = ["category_id"], onDelete = ForeignKey.CASCADE)])
data class Question(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "question_text")
    var questionText: String,

    @ColumnInfo(name = "answer_text")
    var answerText: String,

    @ColumnInfo(name = "difficulty")
    var difficulty: Byte,

    @ColumnInfo(name = "category_id")
    var categoryId: Long
)