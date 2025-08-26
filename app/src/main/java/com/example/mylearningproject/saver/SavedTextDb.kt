package com.example.mylearningproject.saver

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedText::class], version = 1, exportSchema = false)
abstract class SavedTextDb : RoomDatabase() {
    abstract val dao: SavedTextDao

    companion object {
        @Volatile
        private var INSTANCE: SavedTextDao? = null

        fun getDaoInstance(context: Context): SavedTextDao {
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = buildDatabase(context).dao
                    INSTANCE = instance
                }
                return instance
            }
        }

        private fun buildDatabase(context: Context): SavedTextDb =
            Room.databaseBuilder(
                context.applicationContext,
                SavedTextDb::class.java,
                "savedtext_database"
            )
                .fallbackToDestructiveMigration(true)
                .build()
    }
}
