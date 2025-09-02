package com.example.mylearningproject.saver

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
class PartialSavedText(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "is_pinned")
    val isPinned: Boolean
)
