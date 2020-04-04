package cz.cizlmazna.schowl.database

import androidx.room.*

@Entity(
    tableName = "categories_table",
    foreignKeys = [ForeignKey(
        entity = Subject::class,
        parentColumns = ["id"],
        childColumns = ["subject_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["subject_id"])]
)
data class Category(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "subject_id")
    var subjectId: Long
) : ICurriculum