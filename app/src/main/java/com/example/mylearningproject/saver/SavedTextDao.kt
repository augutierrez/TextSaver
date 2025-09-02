package com.example.mylearningproject.saver

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SavedTextDao {
    @Query("SELECT * FROM texts")
    suspend fun getAll(): List<SavedText>

    @Query("SELECT * FROM texts WHERE id = :textId")
    suspend fun getById(textId:Int): SavedText

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(texts : List<SavedText>)

    @Update(entity = SavedText::class)
    suspend fun update(partialSavedText: PartialSavedText)

    @Update(entity = SavedText::class)
    suspend fun updateAll(partialSavedText: List<PartialSavedText>)

    @Query("SELECT * FROM texts WHERE is_pinned = 1")
    suspend fun getAllPinned(): List<SavedText>


}
