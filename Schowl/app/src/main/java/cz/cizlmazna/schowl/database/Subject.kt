package cz.cizlmazna.schowl.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects_table")
data class Subject(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String
) {
    override fun toString(): String {
        return name
    }
}