package com.example.mylearningproject.saver

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedTextDao {
    @Query("SELECT * FROM texts")
    suspend fun getAll(): List<SavedText>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(texts : List<SavedText>)
}
