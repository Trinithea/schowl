package cz.cizlmazna.schowl.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "categories_table", foreignKeys = [ForeignKey(entity = Subject::class, parentColumns = ["id"], childColumns = ["subject_id"], onDelete = ForeignKey.CASCADE)])
data class Category(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "subject_id")
    var subjectId: Long
)