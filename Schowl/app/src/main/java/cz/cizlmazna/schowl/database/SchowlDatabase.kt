package cz.cizlmazna.schowl.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subject::class, Category::class, Question::class], version = 1, exportSchema = false)
abstract class SchowlDatabase : RoomDatabase(){

    abstract val schowlDatabaseDao: SchowlDatabaseDao

    companion object {

        @Volatile
        private var instance: SchowlDatabase? = null

        fun getInstance(context: Context): SchowlDatabase {
            synchronized(this){
                var instance = this.instance

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SchowlDatabase::class.java,
                        "schowl_database"
                    ).fallbackToDestructiveMigration().build()
                    this.instance = instance
                }

                return instance
            }
        }
    }
}